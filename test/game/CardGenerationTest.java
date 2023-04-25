package game;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.function.Predicate;

import static game.TestUtil.countCardWithCondition;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Test cases for card generation of the Deck")
class CardGenerationTest {

    private static final int SPECIAL_DECK_SIZE = 6;

    static WegeDeck standardDeck;

    static WegeDeck specialDeck;

    @BeforeAll
    static void init() {
        standardDeck = WegeDeck.createStandardDeck();
        specialDeck = WegeDeck.createSpecialDeck(SPECIAL_DECK_SIZE);
    }

    @ParameterizedTest(name = "{0}, amount: {1}")
    @MethodSource("provideInputsForStandardDeckTest")
    @DisplayName("Test generate standard deck")
    void shouldGenerateStandardDeck(Predicate<WegePlayingCard> condition, int expectedAmountOfCards) {
        // When
        List<WegePlayingCard> actualCardsInDeck = standardDeck.getAllCards();
        // Then the amount of standard cards are correct.
        var accumulator = countCardWithCondition(actualCardsInDeck);
        assertEquals(expectedAmountOfCards, accumulator.apply(condition));
    }

    @ParameterizedTest(name = "{0}, amount: " + SPECIAL_DECK_SIZE)
    @MethodSource("provideInputsForSpecialDeckTest")
    @DisplayName("Test generate special deck")
    void shouldGenerateSpecialDeck(Predicate<WegePlayingCard> condition) {
        // When
        List<WegePlayingCard> actualCardsInDeck = specialDeck.getAllCards();
        // Then the amount of standard cards are correct.
        var accumulator = countCardWithCondition(actualCardsInDeck);
        assertEquals(SPECIAL_DECK_SIZE, accumulator.apply(condition));
    }

    static List<Arguments> provideInputsForSpecialDeckTest() {
        return provideCardsCondition().subList(0, 6);
    }

    static List<Arguments> provideInputsForStandardDeckTest() {
        List<Arguments> basicDeckInputs = provideCardsCondition();
        return List.of(
            Arguments.of(basicDeckInputs.get(0).get()[0], 3),
            Arguments.of(basicDeckInputs.get(1).get()[0], 2),
            Arguments.of(basicDeckInputs.get(2).get()[0], 3),
            Arguments.of(basicDeckInputs.get(3).get()[0], 2),
            Arguments.of(basicDeckInputs.get(4).get()[0], 3),
            Arguments.of(basicDeckInputs.get(5).get()[0], 3),
            Arguments.of(basicDeckInputs.get(6).get()[0], 12),
            Arguments.of(basicDeckInputs.get(7).get()[0], 12));
    }

    static List<Arguments> provideCardsCondition() {
        return List.of(
            Arguments.of(Named.of("Land card with a Gnome in Path", (Predicate<WegePlayingCard>)
                    card -> testCard(card, WegeCard.CardType.LAND, WegeDeck.GnomePos.PATH))),
            Arguments.of(Named.of("Land card with a Gnome in Corner", (Predicate<WegePlayingCard>)
                    card -> testCard(card, WegeCard.CardType.LAND, WegeDeck.GnomePos.CORNER))),
            Arguments.of(Named.of("Water card with a Gnome in Path", (Predicate<WegePlayingCard>)
                    card -> testCard(card, WegeCard.CardType.WATER, WegeDeck.GnomePos.PATH))),
            Arguments.of(Named.of("Water card with a Gnome in Corner", (Predicate<WegePlayingCard>)
                    card -> testCard(card, WegeCard.CardType.WATER, WegeDeck.GnomePos.CORNER))),
            Arguments.of(Named.of("Bridge Card", (Predicate<WegePlayingCard>)
                    card -> testCard(card, WegeCard.CardType.BRIDGE, null))),
            Arguments.of(Named.of("Cossack Card", (Predicate<WegePlayingCard>)
                    card -> testCard(card, WegeCard.CardType.COSSACK, null))),
            Arguments.of(Named.of("Land card without Gnome", (Predicate<WegePlayingCard>)
                    card -> testCard(card, WegeCard.CardType.LAND, null))),
            Arguments.of(Named.of("Water card without Gnome", (Predicate<WegePlayingCard>)
                    card -> testCard(card, WegeCard.CardType.WATER, null))));
    }

    static boolean testCard(
            WegePlayingCard thisCard,
            WegeCard.CardType cardType,
            WegeDeck.GnomePos gnomePos) {
        WegeCard.CardType thisType = thisCard.getCardType();
        if (thisType != cardType) return false;
        if (gnomePos == null) return !thisCard.hasGnome();
        return switch (gnomePos) {
            case PATH -> thisCard.isPathGnome();
            case CORNER -> thisCard.hasGnome() && !thisCard.isPathGnome();
        };
    }

}
