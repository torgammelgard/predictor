import java.util.EventObject;

/**
 * @author Tor Gammelgard
 * @version 2015-11-30
 */
public class PredictionEvent extends EventObject {

    private String prediction;

    public PredictionEvent(Predictor predictor) {
        super(predictor);
    }

    /**
     * Setter
     *
     * @param prediction new prediction string
     */
    public void setPrediction(String prediction){
        this.prediction = prediction;
    }

    /**
     * Getter
     *
     * @return the stored prediction
     */
    public String getPrediction() {
        return prediction;
    }
}
