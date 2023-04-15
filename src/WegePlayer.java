import java.util.Deque;
import java.util.LinkedList;

/**
 * The player of the game Wege. He can decide what is the next move and
 * keep track all of his played cards.
 */
public class WegePlayer {

    /**
     * Wege Game Master help the player checking the game rules.
     */
    private static WegeGameMaster wegeGameMaster;

    /**
     * Each player needs to have a type.
     */
    private final PlayerType playerType;

    /**
     * The list of cards played so far.
     */
    private final Deque<WegeCard> cardPlayed;

    /**
     * This constructor is hidden to make sure a player must
     * share the same {@link #wegeGameMaster}. Use
     * {@link WegePlayerBuilder#createWegePlayerBuilder(WegeGameMaster)}
     * to initialize a builder to build a Wege Player.
     */
    private WegePlayer(PlayerType playerType) {
        this.playerType = playerType;
        cardPlayed = new LinkedList<>();
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

    /**
     * Decide the next move and keep track all the cards played so far.
     *
     * @param nextCard the card that this player receive from the deck.
     * @param row the row on the playing board to play a card.
     * @param col the col on the playing board to play a card.
     * @return {@link PlayerMove} after a decision is made.
     */
    public PlayerMove playCard(WegeCard nextCard, int row, int col) {
        PlayerMove playerMove = null;
        if (cardPlayed.isEmpty()) {
            playerMove = PlayerMove.PLACE;
        } else if (nextCard.getCardType() == WegeCard.CardType.BRIDGE
                // ask the game master if this player
                // can swap with the card on the board ?
                && wegeGameMaster.isLegalSwap(nextCard)) {
            playerMove = PlayerMove.SWAP;
            // ask the game master if this player
            // can place the card on the board ?
        } else if (wegeGameMaster.isLegalPlacement(nextCard, row, col)){
            playerMove = PlayerMove.PLACE;
        }
        wegeGameMaster.trackCard(nextCard, row, col);
        cardPlayed.add(nextCard);
        return playerMove;
    }

    public enum PlayerMove {
        PLACE, SWAP
    }

    public enum PlayerType {
        LAND, WATER
    }
}
