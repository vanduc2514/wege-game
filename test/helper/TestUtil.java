package helper;

import game.WegeCard;
import game.WegeDeck;
import game.WegePlayingCard;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class TestUtil {

    private TestUtil() {}

    /**
     * Create the card to place it at the given location on the game board.
     *
     * @param cardType the type of the card.
     * @param row the row on the game board.
     * @param col the column on the game board.
     * @return a new card to place on the game board.
     */
    public static WegePlayingCard createCardWithoutGnome(WegeCard.CardType cardType, int row, int col) {
        WegePlayingCard landCard = createCardWithoutGnome(cardType);
        landCard.setRow(row);
        landCard.setCol(col);
        return landCard;
    }

    /**
     * Create the land card to place it at the given location on the game board.
     *
     * @param row the row on the game board.
     * @param col the column on the game board.
     * @return a new land card
     */
    public static WegePlayingCard createLandCard(int row, int col) {
        WegePlayingCard landCard = createCardWithoutGnome(WegeCard.CardType.LAND);
        landCard.setRow(row);
        landCard.setCol(col);
        return landCard;
    }

    /**
     * Create the Wege Card base on a given type, this card does not have a gnome on it.
     *
     * @param cardType the type of this card.
     * @return a new Wege card.
     */
    public static WegePlayingCard createCardWithoutGnome(WegeCard.CardType cardType) {
        return new WegePlayingCard(cardType, false, false);
    }

    /**
     * Count the amount of card in the collection based on a type.
     *
     * @param cards the cards collection to count.
     * @return a Function to count the card with a given type as parameter
     */
    public static Function<WegeCard.CardType, Integer>
    countCardWithType(List<WegePlayingCard> cards) {
        var accumulateFunction = countCardWithCondition(cards);
        return cardType -> accumulateFunction.apply(card -> card.getCardType() == cardType);
    }

    /**
     * Count the amount of card in the collection based on a condition.
     *
     * @param cards the cards collection to count.
     * @return a Function to count the card based on a condition as parameter.
     */
    public static Function<Predicate<WegePlayingCard>, Integer>
    countCardWithCondition(List<WegePlayingCard> cards) {
        return condition -> {
            int accumulator = 0;
            for (WegePlayingCard card : cards) {
                if (condition.test(card)) {
                    accumulator++;
                }
            }
            return accumulator;
        };
    }

    /**
     * Match a wege card based on the given characteristic.
     *
     * @param card the card to match.
     * @param cardType the type of the card.
     * @param gnomePos the position of Gnome on the card, or
     *                 <code>null</code> if there is no Gnome on card.
     * @return true if the given card match with the given characteristic.
     */
    public static boolean matchWegeCard(
            WegePlayingCard card,
            WegeCard.CardType cardType,
            WegeDeck.GnomePos gnomePos) {
        WegeCard.CardType thisType = card.getCardType();
        if (thisType != cardType) return false;
        if (gnomePos == null) return !card.hasGnome();
        return switch (gnomePos) {
            case PATH -> card.isPathGnome();
            case CORNER -> card.hasGnome() && !card.isPathGnome();
        };
    }

}
