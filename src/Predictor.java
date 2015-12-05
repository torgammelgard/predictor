import java.util.HashMap;
import java.util.Map;

/**
 * @author Tor Gammelgard
 * @version 2015-11-30
 */
public class Predictor {

    public static final int WORD_DEPTH = 3;

    Map<String, Map<String, Integer>> database;

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

    /**
     * Adds a word to the database.
     *
     * @param word a string where the first word will be added to the database
     */
    public void addWord(String word) {
        word = word.trim().split(" ")[0];

        for (int i = 1; i <= WORD_DEPTH && i <= word.length(); i++) {
            String subStr = word.substring(0, i);

            Map<String, Integer> predictionMap = new HashMap<>();

            if (database.get(subStr) != null) {
                predictionMap = database.get(subStr);
                if (predictionMap.get(word) != null)
                    predictionMap.put(word, predictionMap.get(word) + 1);
                else
                    predictionMap.put(word, 1);
            } else {
                predictionMap.put(word, 1);
                database.put(subStr, predictionMap);
            }
        }
    }

    public void update(char[] characters) {
        if (characters.length > WORD_DEPTH) {
            firePrediction("");
            return;
        }
        String inString = String.valueOf(characters);
        String startStr = "";

        for (int i = 1; i <= inString.length(); i++) {
            if (database.containsKey(inString.substring(0, i)))
                startStr = inString.substring(0, i);
            else
                break;
        }

        if (!startStr.equals("") && database.containsKey(inString))
            firePrediction(findMostLikelyCandidate(database.get(startStr)));
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
