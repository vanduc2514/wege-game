package csds132.hw4.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.function.Supplier;

/**
 * A Deck with different cards of the Wege Game.
 */
public class WegeDeck {

    /* The position of Gnome in a card. */
    private enum GnomePos {PATH, CORNER}

    /* Holder to store all playing cards of this desk. */
    private final LinkedList<WegeCard> cards;

    /**
     * Create a new deck with no playing cards.
     */
    public WegeDeck() {
        this.cards = new LinkedList<>();
    }

    /**
     * Add multiple cards with the same type to the deck.
     *
     * @param cards the amount of cards adds to the deck.
     * @param cardSupplier a {@link Supplier} to supply a type of wege card.
     */
    public void addCardsToDeck(
            int cards,
            Supplier<WegeCard> cardSupplier) {
        for (int i = 0; i < cards; i++) {
            this.cards.add(cardSupplier.get());
        }
    }

    /**
     * Shuffle the deck.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Draw the first card from the deck.
     */
    public WegeCard drawFromFront() {
        return cards.pop();
    }

    /**
     * Create a deck contains 40 cards of the following types.
     *
     * <ul><li>12 land cards without gnomes</li>
     *     <li>12 water cards without gnomes</li>
     *     <li>3 land cards with a land gnome on the path</li>
     *     <li>2 land cards with a water gnome on one of the water corners</li>
     *     <li>3 water cards with a water gnome on the stream</li>
     *     <li>2 water cards with a land gnome on one of the land corners</li>
     *     <li>3 cossack cards </li>
     *     <li>3 bridge cards</li>
     * </ul>
     *
     * @return a {@link WegeDeck} for the playing board.
     */
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

    /**
     * Create a special deck contains only cossack cards, bridge cards, each
     * variation of a card with a gnome. Each of them has the same amount of card.
     *
     * @param numberOfEachCard the number of each special card.
     * @return a {@link WegeDeck} for the playing board.
     */
    public static WegeDeck createSpecialDeck(int numberOfEachCard) {
        WegeDeck playingDeck = new WegeDeck();
        playingDeck.addCardsToDeck(numberOfEachCard, cardSupplier(WegeCard.CardType.LAND, GnomePos.PATH));
        playingDeck.addCardsToDeck(numberOfEachCard, cardSupplier(WegeCard.CardType.LAND, GnomePos.CORNER));
        playingDeck.addCardsToDeck(numberOfEachCard, cardSupplier(WegeCard.CardType.WATER, GnomePos.PATH));
        playingDeck.addCardsToDeck(numberOfEachCard, cardSupplier(WegeCard.CardType.LAND, GnomePos.CORNER));
        playingDeck.addCardsToDeck(numberOfEachCard, cardSupplier(WegeCard.CardType.COSSACK, null));
        playingDeck.addCardsToDeck(numberOfEachCard, cardSupplier(WegeCard.CardType.BRIDGE, null));
        playingDeck.shuffle();
        return playingDeck;
    }

    // TODO: implement
    public static WegeDeck createCustomDeck(int rowsOfPlayingBoard, int colsOfPlayingBoard) {
        return null;
    }

    /**
     * Create a supplier to supply a specific card for the Wege Game.
     *
     * @param cardType the type of the wege card.
     * @param gnomePos the position of Gnome on the card.
     * @return a {@link Supplier} which supply the card based on cardType and gnomePos
     */
    public static Supplier<WegeCard> cardSupplier(WegeCard.CardType cardType, GnomePos gnomePos) {
        if (gnomePos == null) return () -> new WegeCard(cardType, false, false);
        return switch (gnomePos) {
            case CORNER -> () -> new WegeCard(cardType, true, false);
            case PATH -> () -> new WegeCard(cardType, true, true);
        };
    }

}
