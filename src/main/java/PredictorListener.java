import java.util.EventListener;

/**
 * @author Tor Gammelgard
 * @version 2015-11-30
 */
public interface PredictorListener extends EventListener {

    /**
     * Called everytime the <code>Predictor</code> wants to notify its listeners that a prediction change has been made
     *
     * @param predictionEvent an event which stores information about the prediction change
     */
    void predictionChanged(PredictionEvent predictionEvent);
}
