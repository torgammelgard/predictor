import java.util.HashMap;
import java.util.Map;

/**
 * @author Tor Gammelgard
 * @version 2015-11-30
 */
public class Predictor {

    Map<Character, Map<String, Integer>> database;

    private PredictorListener listener;

    public Predictor() {
        database = new HashMap<>();
    }

    public void setPredictorListener(PredictorListener listener) {
        this.listener = listener;
    }

    public void firePrediction(String prediction) {
        if (listener != null) {
            PredictionEvent me = new PredictionEvent(this);
            me.setPrediction(prediction);
            listener.predictionChanged(me);
        }
    }

    public void update(String word) {
        char firstChar = word.toCharArray()[0];
        Map<String, Integer> predictionMap = new HashMap<>();

        if (database.get(firstChar) != null) {
            predictionMap = database.get(firstChar);
            if (predictionMap.get(word) != null)
                predictionMap.put(word, predictionMap.get(word) + 1);
            else
                predictionMap.put(word, 1);
        } else {
            predictionMap.put(word, 1);
            database.put(firstChar, predictionMap);
        }
    }

    public void update(char[] character) {
        if (character.length != 0 && database.containsKey(character[0]))
            firePrediction(findMostLikelyCandidate(database.get(character[0])));
        else
            firePrediction("");
    }

    private String findMostLikelyCandidate(Map<String, Integer> predictionMap) {
        int max = 0;
        String prediction = "";
        for (String key : predictionMap.keySet()) {
            if (predictionMap.get(key) > max) {
                prediction = key;
                max = predictionMap.get(key);
            }
        }
        return prediction;
    }
}
