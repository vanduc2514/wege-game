package homework.wege.game;

import homework.wege.model.WegeCard;

import java.util.Collections;
import java.util.LinkedList;
import java.util.function.Supplier;

public class WegeDeck {

    private final LinkedList<WegeCard> cards;

    private WegeDeck(LinkedList<WegeCard> cards) {
        this.cards = cards;
    }

    public WegeDeck addCardToDeck(
            int numberOfCards,
            Supplier<WegeCard> wegeCardSupplier) {
        for (int i = 0; i < numberOfCards; i++) {
            cards.add(wegeCardSupplier.get());
        }
        return this;
    }

    public WegeDeck shuffle() {
        Collections.shuffle(cards);
        return this;
    }

    public WegeCard drawFromFront() {
        return cards.pop();
    }

    public static WegeDeck createEmptyDeck() {
        return new WegeDeck(new LinkedList<>());
    }

    public static WegeDeck createDefaultDeck() {
        return createEmptyDeck()
                .addCardToDeck(12, cardSupplier(WegeCard.CardType.LAND, null))
                .addCardToDeck(12, cardSupplier(WegeCard.CardType.WATER, null))
                .addCardToDeck(3, cardSupplier(WegeCard.CardType.LAND, GnomePos.PATH))
                .addCardToDeck(2, cardSupplier(WegeCard.CardType.LAND, GnomePos.CORNER))
                .addCardToDeck(3, cardSupplier(WegeCard.CardType.WATER, GnomePos.PATH))
                .addCardToDeck(2, cardSupplier(WegeCard.CardType.LAND, GnomePos.CORNER))
                .addCardToDeck(3, cardSupplier(WegeCard.CardType.COSSACK, null))
                .addCardToDeck(3, cardSupplier(WegeCard.CardType.BRIDGE, null))
                .shuffle();
    }

    public static Supplier<WegeCard> cardSupplier(WegeCard.CardType cardType, GnomePos gnomePos) {
        if (gnomePos == null) return () -> new WegeCard(cardType, false, false);
        switch (gnomePos) {
            case CORNER:
                return () -> new WegeCard(cardType, true, false);
            case PATH:
                return () -> new WegeCard(cardType, true, true);
            default:
                throw new IllegalArgumentException("A Gnome does have Pos");
        }
    }

    private enum GnomePos {
        PATH, CORNER
    }

}
