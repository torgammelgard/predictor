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

        // setup the listeners
        v.getInputPanel().addKeyListenerOnTextPane(this);
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
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            predictor.addWord(view.getInputPanel().getInput());
            view.getInputPanel().reset();
        } else if (e.getKeyChar() == KeyEvent.VK_TAB) {
            view.getInputPanel().tabAction();
        }
        else {
            predictor.update(view.getInputPanel().getInput().toCharArray());
        }

    }
}
