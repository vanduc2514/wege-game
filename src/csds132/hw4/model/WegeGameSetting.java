package csds132.hw4.model;

import java.util.regex.Pattern;

/**
 * A record hold settings for a Wege Game
 *
 * @param rows the number of row for the playing board for this game.
 * @param cols the number of column for the playing board for this game.
 * @param deck the deck contains wege cards for this game.
 */
public record WegeGameSetting(int rows, int cols, WegeDeck deck) {

    // Only accept maximum two-digit positive number start from 1
    public static final Pattern VALID_POSITIVE_INTEGER_REGEX = Pattern.compile("^[1-9]$");

    /**
     * Create a game with a 6 x 6 board and a default deck.
     */
    public static WegeGameSetting createDefaultGame() {
        return createDefaultBoardWithDeck(WegeDeck.createDefaultDeck());
    }

    /**
     * Create a game with a 6 x 6 board with a given deck.
     */
    public static WegeGameSetting createDefaultBoardWithDeck(WegeDeck wegeDeck) {
        return new WegeGameSetting(6, 6, wegeDeck);
    }

}
