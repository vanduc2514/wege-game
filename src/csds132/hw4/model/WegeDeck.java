package csds132.hw4.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.function.Supplier;

public class WegeDeck {

    private final LinkedList<WegeCard> cards;

    public WegeDeck() {
        this.cards = new LinkedList<>();
    }

    private void addCardsToDeck(
            int numberOfCards,
            Supplier<WegeCard> wegeCardSupplier) {
        for (int i = 0; i < numberOfCards; i++) {
            cards.add(wegeCardSupplier.get());
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public WegeCard drawFromFront() {
        return cards.pop();
    }

    public static WegeDeck createDefaultDeck() {
        WegeDeck playingDeck = new WegeDeck();
        playingDeck.addCardsToDeck(12, cardSupplier(WegeCard.CardType.LAND, null));
        playingDeck.addCardsToDeck(12, cardSupplier(WegeCard.CardType.WATER, null));
        playingDeck.addCardsToDeck(3, cardSupplier(WegeCard.CardType.LAND, GnomePos.PATH));
        playingDeck.addCardsToDeck(2, cardSupplier(WegeCard.CardType.LAND, GnomePos.CORNER));
        playingDeck.addCardsToDeck(3, cardSupplier(WegeCard.CardType.WATER, GnomePos.PATH));
        playingDeck.addCardsToDeck(2, cardSupplier(WegeCard.CardType.LAND, GnomePos.CORNER));
        playingDeck.addCardsToDeck(3, cardSupplier(WegeCard.CardType.COSSACK, null));
        playingDeck.addCardsToDeck(3, cardSupplier(WegeCard.CardType.BRIDGE, null));
        playingDeck.shuffle();
        return playingDeck;
    }

    private static Supplier<WegeCard> cardSupplier(WegeCard.CardType cardType, GnomePos gnomePos) {
        if (gnomePos == null) return () -> new WegeCard(cardType, false, false);
        return switch (gnomePos) {
            case CORNER -> () -> new WegeCard(cardType, true, false);
            case PATH -> () -> new WegeCard(cardType, true, true);
        };
    }

    private enum GnomePos {
        PATH, CORNER
    }

}
