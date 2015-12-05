import javax.swing.*;
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

    InputPanel inputPanel;
    String prediction;

    public View() {

        inputPanel = new InputPanel();

        setLayout(new FlowLayout());
        add(inputPanel);
        add(inputPanel);

        setTitle("Predictor");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public InputPanel getInputPanel() {
        return inputPanel;
    }

    private void setPrediction(String prediction) {
        this.prediction = prediction;
        inputPanel.appendPrediction(prediction);
    }

    @Override
    public void predictionChanged(PredictionEvent event) {
        String prediction = event.getPrediction();
        setPrediction(prediction);
    }


    class InputPanel extends JPanel {
        protected JTextPane textPane;
        StyledDocument doc;
        String appendedPrediction = "";
        int predictionOffset = 0;

        // help from java2s.com
        InputPanel() {
            doc = new DefaultStyledDocument() {
                @Override
                public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                    // making the document contain ONE line only
                    if (str.equals("\n"))
                        return;
                    super.insertString(offs, str, a);
                }
            };

            doc.addDocumentListener(new MyDocumentListener());
            textPane = new JTextPane(doc);
            textPane.setEditable(true);
            textPane.setPreferredSize(new Dimension(400, 100));
            textPane.setFont(new Font("Serif", Font.PLAIN, 48));
            JScrollPane scrollPane = new JScrollPane(textPane);
            add(scrollPane);
        }

        public void addKeyListenerOnTextPane(KeyListener listener) {
            textPane.addKeyListener(listener);
        }

        boolean editingPrediction = false;

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

        public String getInput() {
            return textPane.getText().replace(appendedPrediction, "");
        }

        public void reset() {
            textPane.setText("");
        }

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
