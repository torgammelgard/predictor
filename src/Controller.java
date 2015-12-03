import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author Tor Gammelgard
 * @version 2015-11-30
 */
public class Controller implements KeyListener {
    Predictor predictor;

    View view;

    public Controller(Predictor m, View v) {
        predictor = m;
        view = v;

        v.getInputPanel().addKeyListenerOnTextField(this);
        m.setPredictorListener(v);
    }


    // KeyListener implementation

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        String inputString = view.getInputPanel().getInput();
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            predictor.update(view.getInputPanel().getInput());
            view.getInputPanel().reset();
        }
        else
            predictor.update(inputString.toCharArray());

    }
}
