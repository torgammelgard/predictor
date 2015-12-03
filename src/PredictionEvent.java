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

    public void setPrediction(String prediction){
        this.prediction = prediction;
    }

    public String getPrediction() {
        return prediction;
    }
}
