package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Helper class to help with handling arguments.
 */
public class WegeCLI {

    /* Only accept maximum two-digit positive number start from 1. */
    private static final Pattern VALID_POSITIVE_INTEGER_REGEX = Pattern.compile("^[1-9]$");

    /**
     * Validate each of arguments given to this application. Each of them should be
     * a realistic number.
     *
     * @param applicationArguments arguments given to this application.
     * @return true if arguments are valid. Otherwise, return false.
     */
    public static boolean validateApplicationArguments(List<String> applicationArguments) {
        if (applicationArguments.isEmpty()) return true;
        for (String argument : applicationArguments) {
            if (!VALID_POSITIVE_INTEGER_REGEX.matcher(argument).matches()) {
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
    public static List<Integer> parseApplicationArguments(List<String> applicationArguments) {
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
    public static WegeGameSetting parseGameArguments(List<Integer> gameArguments) {
        return switch (gameArguments.size()) {
            case 0 -> WegeGameSetting.createStandardGame();
            case 1 -> WegeGameSetting.createSpecialGame(gameArguments.get(0));
            default -> WegeGameSetting.createGame(gameArguments);
        };
    }

}
