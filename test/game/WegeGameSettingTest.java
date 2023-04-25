package game;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test cases for creating game setting for Wege.")
class WegeGameSettingTest {

    private static final int SPECIAL_CARDS_AMOUNT = 6;

    private static final int STANDARD_CARD_SIZE = 36;

    @Test
    @DisplayName("Test create standard game")
    void shouldCreateStandardGame() {
        WegeGameSetting actual = WegeGameSetting.createStandardGame();
        assertEquals(6, actual.rows());
        assertEquals(6, actual.cols());
        assertEquals(40, actual.deck().size());
    }

    @Test
    @DisplayName("Test create special game")
    void shouldCreateSpecialGame() {
        int fakeEachCardAmount = 5;
        WegeGameSetting actual = WegeGameSetting.createSpecialGame(fakeEachCardAmount);
        assertEquals(6, actual.rows());
        assertEquals(6, actual.cols());
        assertEquals(fakeEachCardAmount * SPECIAL_CARDS_AMOUNT, actual.deck().size());
    }

    @Test
    @DisplayName("Test create game with special deck")
    void shouldCreateStandardGameWithSpecialDeck() {
        int fakeRow = 7, fakeCol = 8, fakeEachCardAmount = 8;
        List<Integer> fakeGameArguments = List.of(fakeRow, fakeCol, fakeEachCardAmount);
        WegeGameSetting actual = WegeGameSetting.createGame(fakeGameArguments);
        assertEquals(fakeRow, actual.rows());
        assertEquals(fakeCol, actual.cols());
        assertEquals(fakeEachCardAmount * SPECIAL_CARDS_AMOUNT, actual.deck().size());
    }

    @Test
    @DisplayName("Test create game From custom size")
    void shouldCreateGameFromCustomSize() {
        int fakeRow = 8, fakeCol = 9;
        List<Integer> fakeGameArguments = List.of(fakeRow, fakeCol);
        WegeGameSetting actual = WegeGameSetting.createGame(fakeGameArguments);
        assertEquals(fakeRow, actual.rows());
        assertEquals(fakeCol, actual.cols());
        assertEquals(40 + STANDARD_CARD_SIZE, actual.deck().size());
    }
}