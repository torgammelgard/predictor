import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tor Gammelgard
 * @version 2015-11-30
 */
public class Predictor {

    /** The predictor stores added words in a map, where the maximum length of the key is WORD_DEPTH. */
    public static final int WORD_DEPTH = 3;

    private Map<String, Map<String, Integer>> database;
    private PredictorListener listener;

    public Predictor() {
        database = new HashMap<>();
    }

    /**
     * Setter
     *
     * @param listener a prediction listener
     */
    public void setPredictorListener(PredictorListener listener) {
        this.listener = listener;
    }

    /**
     * Notifies the predictor listener everytime a prediction change is made.
     *
     * @param prediction the prediction to be broadcasted
     */
    private void firePrediction(String prediction) {
        if (listener != null) {
            PredictionEvent me = new PredictionEvent(this);
            me.setPrediction(prediction);
            listener.predictionChanged(me);
        }
    }

    public void learnFromFile(String filename) {
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] words;
                words = line.split(" ");
                for (String word : words)
                    addWord(word);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (IOException e) {
            System.out.println("Couldn't read file.");
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

    /**
     * Updates the characters on which the predictor makes its prediction.
     *
     * @param characters an array of input characters
     */
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

    /**
     * Finds a prediction based on the database.
     *
     * @param predictionMap a prediction map from the database
     * @return a prediction
     */
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
