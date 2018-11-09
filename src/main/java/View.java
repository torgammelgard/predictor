import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyListener;

/**
 * @author Tor Gammelgard
 * @version 2015-11-30
 */
public class View extends JFrame implements PredictorListener {

    private InputPanel inputPanel;
    private JButton learnBtn;
    private boolean editingPrediction = false;

    public View() {

        inputPanel = new InputPanel();
        setLayout(new FlowLayout());
        add(inputPanel);

        getContentPane().setBackground(new Color(75, 75, 75));
        Border outsideBorder = BorderFactory.createEmptyBorder(15, 15, 15, 15);
        Border insideBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        Border border = BorderFactory.createCompoundBorder(outsideBorder, insideBorder);
        inputPanel.setBorder(border);

        learnBtn = new JButton("Learn from text file");
        add(learnBtn);

        setTitle("Predictor");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Getter
     *
     * @return the input panel
     */
    public InputPanel getInputPanel() {
        return inputPanel;
    }

    /**
     * Getter
     *
     * @return the learning button
     */
    public JButton getLearnBtn() {
        return learnBtn;
    }

    private void setPrediction(String prediction) {
        inputPanel.appendPrediction(prediction);
    }

    /**
     * Implementation of the <code>PredictionListener</code>.
     *
     * @param event the prediction event sent from the Predictor
     */
    @Override
    public void predictionChanged(PredictionEvent event) {
        String prediction = event.getPrediction();
        setPrediction(prediction);
    }


    /**
     * A panel for displaying input and predictions
     */
    public class InputPanel extends JPanel {

        protected JTextPane textPane;
        private StyledDocument doc;
        private String appendedPrediction = "";
        private int predictionOffset = 0;

        public InputPanel() {
            doc = new DefaultStyledDocument() {
                @Override
                public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                    // making the document contain ONE line only
                    if (str.equals("\n") || str.equals("\t"))
                        return;
                    super.insertString(offs, str, a);
                }
            };

            doc.addDocumentListener(new MyDocumentListener());
            setBackground(new Color(138, 138, 138));
            textPane = new JTextPane(doc);
            textPane.setEditable(true);
            textPane.setPreferredSize(new Dimension(600, 100));
            textPane.setFont(new Font("Serif", Font.BOLD, 72));
            textPane.setBackground(Color.LIGHT_GRAY);
            JScrollPane scrollPane = new JScrollPane(textPane);
            add(scrollPane);
        }

        /**
         * Adds a key listener to the text pane.
         *
         * @param listener a key listener
         */
        public void addKeyListenerOnTextPane(KeyListener listener) {
            textPane.addKeyListener(listener);
        }

        /**
         * Appends the prediction to the input string
         *
         * @param prediction the prediction string
         */
        public void appendPrediction(String prediction) {
            if (appendedPrediction.length() > 0) {
                try {
                    doc.remove(predictionOffset, appendedPrediction.length());
                    appendedPrediction = "";
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }

            if (!prediction.equals("")) {
                editingPrediction = true;
                try {
                    MutableAttributeSet attributeSet = textPane.getInputAttributes();
                    StyleConstants.setForeground(attributeSet, Color.RED);

                    if (doc.getLength() < prediction.length()) {
                        appendedPrediction = prediction.substring(getInput().length(), prediction.length());
                        if (appendedPrediction.length() > 0) {
                            predictionOffset = doc.getLength();
                            doc.insertString(predictionOffset, appendedPrediction, attributeSet);
                            textPane.setCaretPosition(getInput().length());
                        }
                    }
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                editingPrediction = false;
            }
            MutableAttributeSet attributeSet = textPane.getInputAttributes();
            StyleConstants.setForeground(attributeSet, Color.BLACK);

        }

        /**
         * Getter
         *
         * @return the input string
         */
        public String getInput() {
            String str = textPane.getText();
            return (appendedPrediction.equals("")) ? str : str.substring(0, predictionOffset);
        }

        /**
         * Action associated with the TAB button
         */
        public void tabAction() {
            try {
                doc.insertString(predictionOffset, appendedPrediction, textPane.getInputAttributes());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            setPrediction("");
        }

        /**
         * Resets the text pane.
         */
        public void reset() {
            textPane.setText("");
            appendedPrediction = "";
            predictionOffset = 0;
        }

        /**
         * A listener that keeps track of where the appended prediction is
         * and skips updating the offset value if we are in editing prediction mode.
         * (These methods are called everytime any sort of inserting or removing is made on the document)
         */
        private class MyDocumentListener implements DocumentListener {

            @Override
            public void insertUpdate(DocumentEvent e) {
                if (editingPrediction) {
                    return;
                }
                predictionOffset += e.getLength();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (editingPrediction) {
                    return;
                }
                predictionOffset -= e.getLength();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        }
    }

}
