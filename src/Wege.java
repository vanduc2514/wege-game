import game.WegeGameSetting;
import ui.WegeGameBox;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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

    /**
     * Validate each of arguments given to this application. Each of them should be
     * a realistic number.
     *
     * @param applicationArguments arguments given to this application.
     * @return true if arguments are valid. Otherwise, return false.
     */
    private boolean validateApplicationArguments(List<String> applicationArguments) {
        if (applicationArguments.isEmpty()) return true;
        for (String argument : applicationArguments) {
            if (!WegeGameSetting.VALID_POSITIVE_INTEGER_REGEX.matcher(argument).matches()) {
                String errorMessageFormat = "The application arguments %s is not valid!. " +
                        "Please use a realistic number.";
                System.out.printf(errorMessageFormat + "%n", Arrays.toString(applicationArguments.toArray()));
                return false;
            }
        }
        return true;
    }

    /**
     * Parse the application arguments to the game arguments.
     *
     * @param applicationArguments the argument given to this application.
     * @return the game arguments.
     */
    private List<Integer> parseApplicationArguments(List<String> applicationArguments) {
        List<Integer> gameArguments = new ArrayList<>();
        for (String applicationArgument : applicationArguments) {
            gameArguments.add(Integer.parseInt(applicationArgument));
        }
        return gameArguments;
    }

    /**
     * Parse the game arguments to a {@link WegeGameSetting}.
     *
     * @param gameArguments the arguments given to this game.
     * @return a Wege game setting.
     */
    private static WegeGameSetting parseGameArguments(List<Integer> gameArguments) {
        return switch (gameArguments.size()) {
            case 0 -> WegeGameSetting.createStandardGame();
            case 1 -> WegeGameSetting.createSpecialGame(gameArguments.get(0));
            default -> WegeGameSetting.createGame(gameArguments);
        };
    }

}
