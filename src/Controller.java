import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author Tor Gammelgard
 * @version 2015-11-30
 */
public class Controller implements KeyListener {

    /** Model */
    Predictor predictor;

    /** View */
    View view;

    public Controller(Predictor predictor, View view) {
        this.predictor = predictor;
        this.view = view;

        // setup the listeners
        view.getInputPanel().addKeyListenerOnTextPane(this);
        predictor.setPredictorListener(view);
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
