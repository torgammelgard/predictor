import java.util.EventListener;

/**
 * @author Tor Gammelgard
 * @version 2015-11-30
 */
public interface PredictorListener extends EventListener {

    void predictionChanged(PredictionEvent predictionEvent);
}
