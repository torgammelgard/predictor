/**
 * @author Tor Gammelgard
 * @version 2015-11-30
 */
public class App {

    public static void main(String... args) {
        Predictor predictor = new Predictor();
        View view = new View();
        Controller controller = new Controller(predictor, view);
    }
}
