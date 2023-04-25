package game;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class TestUtil {

    private TestUtil() {}

    /**
     * Create the Wege Card base on a given type
     *
     * @param cardType the type of this card.
     * @return a new Wege card.
     */
    public static WegePlayingCard createWegeCard(WegeCard.CardType cardType) {
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

}
