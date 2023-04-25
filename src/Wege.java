import game.WegeGameSetting;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.WegeGameBox;

import java.util.List;
import java.util.Scanner;

import static game.WegeCLI.*;

/**
 * Wege game application
 */
public class Wege extends Application {

    /**
     * Start the game with the given arguments
     *
     * @param args the argument of the game Wege.
     *             <ul>
     *                  <li>
     *                      If no arguments is provided, start the game with
     *                      {@link WegeGameSetting#createStandardGame()}
     *                  </li>
     *                  <li>
     *                      If one argument is provided, start the game with
     *                      {@link WegeGameSetting#createSpecialGame(int)}
     *                  </li>
     *                  <li>
     *                      If two or more arguments is provided, start the game with
     *                      {@link WegeGameSetting#createGame(List)}
     *                  </li>
     *             </ul>
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        WegeGameSetting wegeGameSetting = retrieveSettingFromCLI();
        System.out.printf("Start the game with %d x %d playing board and %d cards%n",
                wegeGameSetting.rows(), wegeGameSetting.cols(), wegeGameSetting.deck().size());
        Scene scene = new Scene(new WegeGameBox(wegeGameSetting));
        primaryStage.setTitle("Wege Game v1.0");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Retrieve the application setting given from the command line.
     * If a user enter an invalid number, show message and let the user
     * enter again.
     *
     * @return a wege game setting.
     * @throws IllegalStateException if the user force quit by enter q key.
     */
    private WegeGameSetting retrieveSettingFromCLI() {
        List<String> applicationArguments = getParameters().getUnnamed();
        while (!validateApplicationArguments(applicationArguments)) {
            System.out.println("Enter the Wege game arguments to start");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            applicationArguments = List.of(input.split(" "));
        }
        List<Integer> gameArguments = parseApplicationArguments(applicationArguments);
        return parseGameArguments(gameArguments);
    }
}
