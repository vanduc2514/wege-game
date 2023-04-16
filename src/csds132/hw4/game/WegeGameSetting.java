package csds132.hw4.game;

import java.util.List;
import java.util.regex.Pattern;

/**
 * A record hold settings for a Wege Game
 *
 * @param rows the number of row for the playing board for this game.
 * @param cols the number of column for the playing board for this game.
 * @param deck the deck contains wege cards for this game.
 */
public record WegeGameSetting(int rows, int cols, WegeDeck deck) {

    /* Only accept maximum two-digit positive number start from 1. */
    public static final Pattern VALID_POSITIVE_INTEGER_REGEX = Pattern.compile("^[1-9]$");

    /* The number of row for the standard playing board of the Wege game. */
    public static final int STANDARD_BOARD_ROWS = 6;

    /* The number of column for the standard playing board of the Wege game. */
    public static final int STANDARD_BOARD_COLS = 6;

    /**
     * Create a Wege game with {@link #STANDARD_BOARD_ROWS} x {@link #STANDARD_BOARD_COLS} board
     * and the {@link WegeDeck#createStandardDeck()}.
     *
     * @return setting of this game.
     */
    public static WegeGameSetting createStandardGame() {
        return createStandardBoardWithDeck(WegeDeck.createStandardDeck());
    }

    /**
     * Create a Wege game with {@link #STANDARD_BOARD_ROWS} x {@link #STANDARD_BOARD_COLS} board
     * and a deck.
     *
     * @param wegeDeck the deck used for this game.
     * @return setting of this game.
     */
    public static WegeGameSetting createStandardBoardWithDeck(WegeDeck wegeDeck) {
        return new WegeGameSetting(STANDARD_BOARD_ROWS, STANDARD_BOARD_COLS, wegeDeck);
    }

    /**
     * Create a Wege game with {@link #STANDARD_BOARD_ROWS} x {@link #STANDARD_BOARD_COLS} board
     * and a special deck.
     *
     * @param numberOfEachSpecialCard the amount of each special card.
     * @see WegeDeck#createSpecialDeck(int)
     * @return setting of this game.
     */
    public static WegeGameSetting createSpecialGame(int numberOfEachSpecialCard) {
        WegeGameSetting wegeGameSetting;
        WegeDeck specialDeck = WegeDeck.createSpecialDeck(numberOfEachSpecialCard);
        wegeGameSetting = WegeGameSetting.createStandardBoardWithDeck(specialDeck);
        return wegeGameSetting;
    }

    /**
     * Create a Wege game from the arguments.
     *
     * @param arguments the arguments given of this wege game.
     * @return setting of this game.
     */
    public static WegeGameSetting createGame(List<Integer> arguments) {
        WegeGameSetting wegeGameSetting;
        int rowsOfPlayingBoard = arguments.get(0);
        int colsOfPlayingBoard = arguments.get(1);
        WegeDeck wegeDeck;
        if (arguments.size() == 3) {
            int numberOfEachSpecialCard = arguments.get(2);
            wegeDeck = WegeDeck.createSpecialDeck(numberOfEachSpecialCard);
        } else {
            wegeDeck = WegeDeck.createWegeDeck(rowsOfPlayingBoard, colsOfPlayingBoard);
        }
        wegeGameSetting = new WegeGameSetting(rowsOfPlayingBoard, colsOfPlayingBoard, wegeDeck);
        return wegeGameSetting;
    }

}
