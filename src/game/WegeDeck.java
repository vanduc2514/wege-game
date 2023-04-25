package game;

import java.util.*;
import java.util.function.Supplier;

/**
 * A Deck with different cards of the Wege Game.
 */
public class WegeDeck {

    public static final int STANDARD_BOARD_TILES = 36;

    /* The position of Gnome in a card. */
    public enum GnomePos {PATH, CORNER}

    /* Holder to store all playing cards of this desk. */
    private final LinkedList<WegePlayingCard> cards;

    /**
     * Create a new deck with no playing cards.
     */
    private WegeDeck() {
        this.cards = new LinkedList<>();
    }

    /**
     * Prefilled the card with the given list of cards.
     * Constructor used for Unit Test.
     *
     * @param playingCards the cards to be added to the Wege Deck.
     */
    WegeDeck(LinkedList<WegePlayingCard> playingCards) {
        this.cards = playingCards;
    }

    /**
     * Draw the first card from the deck.
     */
    public WegePlayingCard drawFromFront() {
        return cards.pop();
    }

    /**
     * Get the size of the deck.
     */
    public int size() {
        return cards.size();
    }

    /**
     * Add multiple cards with the same type to the deck.
     *
     * @param cards the amount of cards adds to the deck.
     * @param cardSupplier a {@link Supplier} to supply a type of wege card.
     */
    void addCardsToDeck(
            int cards,
            Supplier<WegePlayingCard> cardSupplier) {
        for (int i = 0; i < cards; i++) {
            this.cards.add(cardSupplier.get());
        }
    }

    /**
     * Shuffle the deck.
     */
    void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Return all the cards available in the deck. Used for unit test.
     *
     * @return a copy list of all cards in this deck to avoid
     * mutation.
     */
    List<WegePlayingCard> getAllCards() {
        return List.copyOf(cards);
    }

    /**
     * Create a deck contains 40 cards of the following types.
     *
     * <ul>
     *     <li>12 land cards without gnomes</li>
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
    public static WegeDeck createStandardDeck() {
        WegeDeck playingDeck = new WegeDeck();
        insertStandardCards(playingDeck);
        playingDeck.shuffle();
        return playingDeck;
    }

    /**
     * Create a special deck contains only cossack cards, bridge cards, each
     * variation of a card with a gnome. Each of them has the same amount of cards.
     *
     * @param numberOfEachCard the number of each special card.
     * @return a {@link WegeDeck} for the playing board.
     */
    public static WegeDeck createSpecialDeck(int numberOfEachCard) {
        WegeDeck playingDeck = new WegeDeck();
        playingDeck.addCardsToDeck(numberOfEachCard, cardSupplier(WegeCard.CardType.LAND, GnomePos.PATH));
        playingDeck.addCardsToDeck(numberOfEachCard, cardSupplier(WegeCard.CardType.LAND, GnomePos.CORNER));
        playingDeck.addCardsToDeck(numberOfEachCard, cardSupplier(WegeCard.CardType.WATER, GnomePos.PATH));
        playingDeck.addCardsToDeck(numberOfEachCard, cardSupplier(WegeCard.CardType.WATER, GnomePos.CORNER));
        playingDeck.addCardsToDeck(numberOfEachCard, cardSupplier(WegeCard.CardType.COSSACK, null));
        playingDeck.addCardsToDeck(numberOfEachCard, cardSupplier(WegeCard.CardType.BRIDGE, null));
        playingDeck.shuffle();
        return playingDeck;
    }

    /**
     * Create a Wege deck base on the board dimension. Only significant different
     * of tiles to the standard tiles {@link #STANDARD_BOARD_TILES} are taken
     * into consideration.
     *
     * @param rows the number of row for the playing board of the Wege Game
     * @param cols the number of column for the playing board of the Wege Game
     * @return a deck contains cards for the game Wege.
     */
    public static WegeDeck createWegeDeck(int rows, int cols) {
        WegeDeck wegeDeck = new WegeDeck();
        insertStandardCards(wegeDeck);
        int difference = rows * cols - STANDARD_BOARD_TILES;
        // Both Land and Water cards need to be removed, hence the divide to 2.
        final int numberOfCards = Math.abs(difference / 2);
        if (difference < 0) {
            // If significant smaller than the standard size.
            removeWaterAndLandCardsFromDeck(wegeDeck, numberOfCards);
        } else if (difference > 0) {
            // If significant larger than the standard size.
            addWaterAndLandCardsToDeck(wegeDeck, numberOfCards);
        }
        wegeDeck.shuffle();
        return wegeDeck;
    }

    /**
     * Insert a collection of 40 standard cards to the given deck.
     * List of standard cards:
     *
     * <ul>
     *     <li>12 land cards without gnomes</li>
     *     <li>12 water cards without gnomes</li>
     *     <li>3 land cards with a land gnome on the path</li>
     *     <li>2 land cards with a water gnome on one of the water corners</li>
     *     <li>3 water cards with a water gnome on the stream</li>
     *     <li>2 water cards with a land gnome on one of the land corners</li>
     *     <li>3 cossack cards </li>
     *     <li>3 bridge cards</li>
     * </ul>
     *
     * @param wegeDeck the deck to add cards to.
     */
    private static void insertStandardCards(WegeDeck wegeDeck) {
        // Gnome cards needs to be at the top for fast removal.
        wegeDeck.addCardsToDeck(3, cardSupplier(WegeCard.CardType.LAND, GnomePos.PATH));
        wegeDeck.addCardsToDeck(2, cardSupplier(WegeCard.CardType.LAND, GnomePos.CORNER));
        wegeDeck.addCardsToDeck(3, cardSupplier(WegeCard.CardType.WATER, GnomePos.PATH));
        wegeDeck.addCardsToDeck(2, cardSupplier(WegeCard.CardType.WATER, GnomePos.CORNER));
        wegeDeck.addCardsToDeck(12, cardSupplier(WegeCard.CardType.WATER, null));
        wegeDeck.addCardsToDeck(12, cardSupplier(WegeCard.CardType.LAND, null));
        wegeDeck.addCardsToDeck(3, cardSupplier(WegeCard.CardType.COSSACK, null));
        wegeDeck.addCardsToDeck(3, cardSupplier(WegeCard.CardType.BRIDGE, null));
    }

    /**
     * Remove both Water and Land cards from the deck. If the deck has no cards left
     * to be removed, stops removing.
     *
     * @param wegeDeck the Wege deck contains Land and Water cards.
     * @param numberOfCards the number of each Land and Water cards.
     */
    private static void removeWaterAndLandCardsFromDeck(WegeDeck wegeDeck, int numberOfCards) {
        int landCardsRemoved = 0;
        int waterCardsRemoved = 0;
        Iterator<WegePlayingCard> deckIterator = wegeDeck.cards.iterator();
        while (deckIterator.hasNext()) {
            // If there are enough cards to be removed, stop.
            if (landCardsRemoved == numberOfCards
                    && waterCardsRemoved == numberOfCards) {
                break;
            }
            WegePlayingCard cardInDeck = deckIterator.next();
            if (!cardInDeck.hasGnome()) {
                if (landCardsRemoved != numberOfCards && cardInDeck.getCardType() == WegeCard.CardType.LAND) {
                    landCardsRemoved++;
                    deckIterator.remove();
                } else if (waterCardsRemoved != numberOfCards && cardInDeck.getCardType() == WegeCard.CardType.WATER) {
                    waterCardsRemoved++;
                    deckIterator.remove();
                }
            }
        }
    }

    /**
     * Add both Water and Land cards to the deck.
     *
     * @param wegeDeck a Wege deck.
     * @param numberOfCards the number of each Land and Water cards.
     */
    private static void addWaterAndLandCardsToDeck(WegeDeck wegeDeck, int numberOfCards) {
        wegeDeck.addCardsToDeck(numberOfCards, cardSupplier(WegeCard.CardType.WATER, null));
        wegeDeck.addCardsToDeck(numberOfCards, cardSupplier(WegeCard.CardType.LAND, null));
    }

    /**
     * Create a supplier to supply a specific card for the Wege Game.
     *
     * @param cardType the type of the wege card.
     * @param gnomePos the position of Gnome on the card.
     * @return a {@link Supplier} which supply the card based on cardType and gnomePos
     */
    private static Supplier<WegePlayingCard> cardSupplier(WegeCard.CardType cardType, GnomePos gnomePos) {
        if (gnomePos == null) return () -> new WegePlayingCard(cardType, false, false);
        return switch (gnomePos) {
            case CORNER -> () -> new WegePlayingCard(cardType, true, false);
            case PATH -> () -> new WegePlayingCard(cardType, true, true);
        };
    }

}
