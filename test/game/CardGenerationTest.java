package game;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static game.TestUtil.countCardWithCondition;
import static game.TestUtil.matchWegeCard;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Parameterized Test cases for card generation of the Deck")
class CardGenerationTest {

    private static final int SPECIAL_DECK_SIZE = 6;

    private static WegeDeck standardDeck;

    private static WegeDeck specialDeck;

    private static WegeDeck smallDeck;

    private static WegeDeck largeDeck;

    private static WegeDeck significantSmallDeck;

    private static WegeDeck significantLargeDeck;

    @BeforeAll
    static void init() {
        standardDeck = WegeDeck.createStandardDeck();
        specialDeck = WegeDeck.createSpecialDeck(SPECIAL_DECK_SIZE);
        smallDeck = WegeDeck.createWegeDeck(7, 5);
        significantSmallDeck = WegeDeck.createWegeDeck(8, 4);
        largeDeck = WegeDeck.createWegeDeck(9, 5);
        significantLargeDeck = WegeDeck.createWegeDeck(9, 8);
    }

    @ParameterizedTest(name = "{0}, amount: {1}")
    @MethodSource("provideArgumentsForStandardDeckTest")
    @DisplayName("Test generate standard deck (6 x 6)")
    void shouldGenerateStandardDeck(Predicate<WegePlayingCard> condition, int expectedAmountOfCards) {
        List<WegePlayingCard> actualCardsInDeck = standardDeck.getAllCards();
        var accumulator = countCardWithCondition(actualCardsInDeck);
        assertEquals(expectedAmountOfCards, accumulator.apply(condition));
    }

    @ParameterizedTest(name = "{0}, amount: " + SPECIAL_DECK_SIZE)
    @MethodSource("provideArgumentsForSpecialDeckTest")
    @DisplayName("Test generate special deck (size = 6)")
    void shouldGenerateSpecialDeck(Predicate<WegePlayingCard> condition) {
        List<WegePlayingCard> actualCardsInDeck = specialDeck.getAllCards();
        var accumulator = countCardWithCondition(actualCardsInDeck);
        assertEquals(SPECIAL_DECK_SIZE, accumulator.apply(condition));
    }

    @ParameterizedTest(name = "{0}, amount: {1}")
    @MethodSource("provideArgumentsForSmallDeckTest")
    @DisplayName("Test generate small deck (7 x 5)")
    void shouldGenerateSmallDeck(Predicate<WegePlayingCard> condition, int expectedAmountOfCards) {
        List<WegePlayingCard> actualCardsInDeck = smallDeck.getAllCards();
        var accumulator = countCardWithCondition(actualCardsInDeck);
        assertEquals(expectedAmountOfCards, accumulator.apply(condition));
    }

    @ParameterizedTest(name = "{0}, amount: {1}")
    @MethodSource("provideArgumentsForSignificantSmallDeckTest")
    @DisplayName("Test generate significant small deck (8 x 4)")
    void shouldGenerateSignificantSmallDeck(Predicate<WegePlayingCard> condition, int expectedAmountOfCards) {
        List<WegePlayingCard> actualCardsInDeck = significantSmallDeck.getAllCards();
        var accumulator = countCardWithCondition(actualCardsInDeck);
        assertEquals(expectedAmountOfCards, accumulator.apply(condition));
    }

    @ParameterizedTest(name = "{0}, amount: {1}")
    @MethodSource("provideArgumentsForLargeDeckTest")
    @DisplayName("Test generate large deck (9 x 5)")
    void shouldGenerateLargeDeck(Predicate<WegePlayingCard> condition, int expectedAmountOfCards) {
        List<WegePlayingCard> actualCardsInDeck = largeDeck.getAllCards();
        var accumulator = countCardWithCondition(actualCardsInDeck);
        assertEquals(expectedAmountOfCards, accumulator.apply(condition));
    }

    @ParameterizedTest(name = "{0}, amount: {1}")
    @MethodSource("provideArgumentsForSignificantLargeDeckTest")
    @DisplayName("Test generate significant large deck (9 x 8)")
    void shouldGenerateSignificantLargeDeck(Predicate<WegePlayingCard> condition, int expectedAmountOfCards) {
        List<WegePlayingCard> actualCardsInDeck = significantLargeDeck.getAllCards();
        var accumulator = countCardWithCondition(actualCardsInDeck);
        assertEquals(expectedAmountOfCards, accumulator.apply(condition));
    }

    //////////////////////////// Test case Arguments. //////////////////////////////////

    /**
     * Provide a list of argument contains predicate to count cards in the deck
     * and the expected amount of each card. For large deck, it means
     * the different tiles in the game board with the standard titles can not be cut in
     * half equally.
     *
     * @return a list of arguments used for parameterized test.
     * @see WegeDeck#createWegeDeck(int, int)
     */
    private static List<Arguments> provideArgumentsForLargeDeckTest() {
        return provideArgumentsForNormalDeckTest(4);
    }

    /**
     * Provide a list of argument contains predicate to count cards in the deck
     * and the expected amount of each card. For significant large deck, it means
     * the different tiles in the game board with the standard titles can be cut in
     * half equally.
     *
     * @return a list of arguments used for parameterized test.
     * @see WegeDeck#createWegeDeck(int, int)
     */
    private static List<Arguments> provideArgumentsForSignificantLargeDeckTest() {
        return provideArgumentsForNormalDeckTest(18);
    }

    /**
     * Provide a list of argument contains predicate to count cards in the deck
     * and the expected amount of each card. For small deck, it means
     * the different tiles in the game board with the standard titles cannot be cut in
     * half equally.
     *
     * @return a list of arguments used for parameterized test.
     * @see WegeDeck#createWegeDeck(int, int)
     */
    private static List<Arguments> provideArgumentsForSmallDeckTest() {
        return provideArgumentsForNormalDeckTest(0);
    }

    /**
     * Provide a list of argument contains predicate to count cards in the deck
     * and the expected amount of each card. For significant small deck, it means
     * the different tiles in the game board with the standard titles can be cut in
     * half equally.
     *
     * @return a list of arguments used for parameterized test.
     * @see WegeDeck#createWegeDeck(int, int)
     */
    private static List<Arguments> provideArgumentsForSignificantSmallDeckTest() {
        return provideArgumentsForNormalDeckTest(-2);
    }

    /**
     * Provide a list of argument contains predicate to count cards in the deck
     * and the expected amount of each card. For normal deck, it means the water
     * and land card without gnome can be adjusted base on the size of the game board
     * (total tiles in the game board).
     *
     * @param adjustment the amount to increase or decrease the card.
     * @return a list of arguments used for parameterized test.
     * @see WegeDeck#createWegeDeck(int, int)
     */
    private static List<Arguments> provideArgumentsForNormalDeckTest(int adjustment) {
        List<Arguments> standardDeckArguments = provideArgumentsForStandardDeckTest();
        List<Arguments> significantSmallDeckArguments = new ArrayList<>(standardDeckArguments.subList(0, 6));
        for (int i = 6; i < standardDeckArguments.size(); i++) {
            Object[] arguments = standardDeckArguments.get(i).get();
            significantSmallDeckArguments.add(Arguments.of(arguments[0], (int) arguments[1] + adjustment));
        }
        return significantSmallDeckArguments;
    }

    /**
     * Provide a list of argument contains predicate to count cards in the deck
     * and the expected amount of each card.
     *
     * @see WegeDeck#createStandardDeck()
     * @return a list of arguments used for parameterized test.
     */
    private static List<Arguments> provideArgumentsForStandardDeckTest() {
        List<Arguments> basicDeckArguments = provideCardsCondition();
        return List.of(
            Arguments.of(basicDeckArguments.get(0).get()[0], 3),
            Arguments.of(basicDeckArguments.get(1).get()[0], 2),
            Arguments.of(basicDeckArguments.get(2).get()[0], 3),
            Arguments.of(basicDeckArguments.get(3).get()[0], 2),
            Arguments.of(basicDeckArguments.get(4).get()[0], 3),
            Arguments.of(basicDeckArguments.get(5).get()[0], 3),
            Arguments.of(basicDeckArguments.get(6).get()[0], 12),
            Arguments.of(basicDeckArguments.get(7).get()[0], 12));
    }

    /**
     * Provide a list of cards to test the amount of each special card in the deck.
     *
     * @see WegeDeck#createSpecialDeck(int)
     * @return a list of arguments used for parameterized test.
     */
    private static List<Arguments> provideArgumentsForSpecialDeckTest() {
        return provideCardsCondition().subList(0, 6);
    }

    /**
     * Provide a List of Predicate to help counting cards only if the given
     * predicate return <code>true</code>
     *
     * @return a list of arguments used for parameterized test.
     */
    private static List<Arguments> provideCardsCondition() {
        return List.of(
            Arguments.of(Named.of("Land card with a Gnome in Path", (Predicate<WegePlayingCard>)
                    card -> matchWegeCard(card, WegeCard.CardType.LAND, WegeDeck.GnomePos.PATH))),
            Arguments.of(Named.of("Land card with a Gnome in Corner", (Predicate<WegePlayingCard>)
                    card -> matchWegeCard(card, WegeCard.CardType.LAND, WegeDeck.GnomePos.CORNER))),
            Arguments.of(Named.of("Water card with a Gnome in Path", (Predicate<WegePlayingCard>)
                    card -> matchWegeCard(card, WegeCard.CardType.WATER, WegeDeck.GnomePos.PATH))),
            Arguments.of(Named.of("Water card with a Gnome in Corner", (Predicate<WegePlayingCard>)
                    card -> matchWegeCard(card, WegeCard.CardType.WATER, WegeDeck.GnomePos.CORNER))),
            Arguments.of(Named.of("Bridge Card", (Predicate<WegePlayingCard>)
                    card -> matchWegeCard(card, WegeCard.CardType.BRIDGE, null))),
            Arguments.of(Named.of("Cossack Card", (Predicate<WegePlayingCard>)
                    card -> matchWegeCard(card, WegeCard.CardType.COSSACK, null))),
            Arguments.of(Named.of("Land card without Gnome", (Predicate<WegePlayingCard>)
                    card -> matchWegeCard(card, WegeCard.CardType.LAND, null))),
            Arguments.of(Named.of("Water card without Gnome", (Predicate<WegePlayingCard>)
                    card -> matchWegeCard(card, WegeCard.CardType.WATER, null))));
    }

}
