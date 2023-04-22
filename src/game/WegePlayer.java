package game;

import java.util.Deque;
import java.util.LinkedList;

/**
 * The player of the game Wege. He can decide what is the next move and
 * keep track all of his played cards.
 */
public class WegePlayer {

    /* The possible moves of a player. */
    public enum Move {PLACE, SWAP}

    /* The possible types player of the Wege Game. */
    public enum PlayerType {LAND, WATER}

    /* Wege Game Master help the player checking the game rules. */
    private static WegeGameMaster wegeGameMaster;

    /* The type of this player. */
    private final PlayerType playerType;

    /* The list of cards played so far. */
    private final Deque<WegeCard> cardPlayed;

    /* The current score of this player */
    private int score;

    /**
     * This constructor is hidden to make sure a player must
     * share the same {@link #wegeGameMaster}. Use {@link WegePlayerBuilder}
     * to initialize a builder to build a Wege Player.
     */
    private WegePlayer(PlayerType playerType) {
        this.playerType = playerType;
        cardPlayed = new LinkedList<>();
    }

    /**
     * Get the type of this player.
     *
     * @return the type of this player.
     */
    public PlayerType getPlayerType() {
        return playerType;
    }

    /**
     * The current score of this player.
     */
    public int getScore() {
        return this.score;
    }

    public void increaseGnome(int score) {
        this.score += score;
    }

    /**
     * Decide the next move and keep track all the cards played so far.
     *
     * @param nextCard the card that this player receive from the deck.
     * @param row the row on the playing board to place the next card.
     * @param col the col on the playing board to place the next card.
     * @return {@link Move} after a decision is made. or return null
     * @throws IllegalMoveException if this player move is illegal.
     */
    public Move playCard(WegeCard nextCard, int row, int col) throws IllegalMoveException {
        Move move;
        // If there is no card played yet, place the card.
        if (cardPlayed.isEmpty()) {
            move = Move.PLACE;
        } else {
            // If not, ask the game master what should be the next move
            move = wegeGameMaster.nextMove(nextCard, row, col);
        };
        wegeGameMaster.trackPlayedCard(nextCard, this, row, col);
        cardPlayed.add(nextCard);
        return move;
    }

    /**
     * The builder that build a {@link WegePlayer}.
     */
    public static class WegePlayerBuilder {

        /**
         * Build a player from a type.
         *
         * @param playerType the type of the player.
         * @return a new instance of {@link WegePlayer}.
         */
        public WegePlayer buildPlayer(PlayerType playerType) {
            return new WegePlayer(playerType);
        }

        /**
         * Create a Builder from a {@link WegeGameMaster}. This builder make
         * sure to share the game master to all of the {@link WegePlayer} that it builds.
         *
         * @param wegeGameMaster the {@link WegeGameMaster} which helps track the game rules.
         */
        public WegePlayerBuilder(WegeGameMaster wegeGameMaster) {
            WegePlayer.wegeGameMaster = wegeGameMaster;
        }

    }

}
