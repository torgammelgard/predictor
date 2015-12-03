import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

/**
 * @author Tor Gammelgard
 * @version 2015-11-30
 */
public class View extends JFrame implements PredictorListener {

    InputPanel inputPanel;
    OutputPanel outputPanel;
    String prediction;

    public View() {

        inputPanel = new InputPanel();
        outputPanel = new OutputPanel();

        setLayout(new FlowLayout());
        add(inputPanel);
        add(outputPanel);

        setTitle("MVC Extended");
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
        outputPanel.setTextFieldText(prediction);
    }

    @Override
    public void predictionChanged(PredictionEvent event) {
        String prediction = event.getPrediction();
        setPrediction(prediction);
    }

    class CustomPanel extends JPanel {
        protected JTextField textField;

        CustomPanel() {
            textField = new JTextField();
            textField.setPreferredSize(new Dimension(400, 50));
            textField.setFont(new Font("Serif", Font.BOLD, 32));
            textField.setForeground(Color.BLACK);
            add(textField);
        }

        public void setTextFieldText(String text) {
            textField.setText(text);
        }
        public void addKeyListenerOnTextField(KeyListener listener) {
            textField.addKeyListener(listener);
        }

        public String getInput() {
            return textField.getText();
        }

        public void reset() {
            textField.setText("");
        }
    }

    class InputPanel extends CustomPanel {
    }

    class OutputPanel extends CustomPanel {

    }

}
