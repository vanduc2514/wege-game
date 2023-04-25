package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.LinkedList;
import java.util.List;

import static helper.TestUtil.countCardWithType;
import static helper.TestUtil.createWegeCard;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@DisplayName("Test cases for the Wege deck")
class WegeDeckTest {

    /* The amount of Land card for each test. */
    static final int LAND_CARD_AMOUNT = 3;

    /* The amount of Water card for each test. */
    static final int WATER_CARD_AMOUNT = 5;

    /* Test Deck for all test cases. */
    WegeDeck wegeDeck;

    /* playing cards used for verify the behavior of the deck. */
    LinkedList<WegePlayingCard> stubPlayingCards;

    /**
     * Create a deck and pre-filled it with {@link #LAND_CARD_AMOUNT}
     * and {@link #WATER_CARD_AMOUNT}. This deck is used for all test cases.
     */
    @BeforeEach
    void init() {
        stubPlayingCards = new LinkedList<>();
        for (int i = 0; i < LAND_CARD_AMOUNT; i++) {
            stubPlayingCards.add(createWegeCard(WegeCard.CardType.LAND));
        }
        for (int i = 0; i < WATER_CARD_AMOUNT; i++) {
            stubPlayingCards.add(createWegeCard(WegeCard.CardType.WATER));
        }
        wegeDeck = new WegeDeck(stubPlayingCards);
    }

    @Test
    @DisplayName("Test size of the Wege Deck")
    void shouldReturnTotalCards() {
        assertEquals(WATER_CARD_AMOUNT + LAND_CARD_AMOUNT, wegeDeck.size());
    }

    @Test
    @DisplayName("Test draw card from front of the Wege Deck")
    void shouldDrawFromFront() {
        assertSame(stubPlayingCards.get(0), wegeDeck.drawFromFront());
    }

    @ParameterizedTest(name = "Add {0} {1} card to the deck")
    @MethodSource("provideInputForAddCardTest")
    @DisplayName("Test add card to the Wege Deck")
    void shouldAddCardToTheDeck(int numberOfCardToAdd, WegeCard.CardType typeOfCardToAdd) {
        // Helper
        var counter = countCardWithType(stubPlayingCards);
        // Given
        int expectedAmount = counter.apply(typeOfCardToAdd) + numberOfCardToAdd;
        // When
        wegeDeck.addCardsToDeck(numberOfCardToAdd, () -> createWegeCard(typeOfCardToAdd));
        // Then the size should increase
        assertEquals(LAND_CARD_AMOUNT + WATER_CARD_AMOUNT + numberOfCardToAdd, wegeDeck.size());
        // Then the amount of card should increase.
        assertEquals(expectedAmount, counter.apply(typeOfCardToAdd));
    }

    static List<Arguments> provideInputForAddCardTest() {
        return List.of(
                Arguments.of(0, WegeCard.CardType.LAND),
                Arguments.of(1, WegeCard.CardType.WATER),
                Arguments.of(7, WegeCard.CardType.BRIDGE),
                Arguments.of(9, WegeCard.CardType.COSSACK)
        );
    }

}