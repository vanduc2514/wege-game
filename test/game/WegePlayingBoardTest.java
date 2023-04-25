package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static game.TestUtil.createCardWithoutGnome;
import static game.TestUtil.createLandCard;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test cases of the functionality for the game board")
class WegePlayingBoardTest {

    /* the game board to test */
    private WegePlayingBoard gameBoard;

    @BeforeEach
    void initStandardBoard() {
        gameBoard = new WegePlayingBoard(
                WegeGameSetting.STANDARD_BOARD_ROWS,
                WegeGameSetting.STANDARD_BOARD_COLS);
    }

    @ParameterizedTest(name = "Test place card at location {0}-{1}")
    @CsvSource({
            "0,0", "0,2", "0,5",
            "2,0", "2,2", "2,5",
            "5,0", "5,2", "5,5"
    })
    @DisplayName("Test place card on game board")
    void shouldPlaceCardOnGameBoard(int row, int col) {
        WegePlayingCard card = createLandCard(row, col);
        gameBoard.placeCardOnBoard(card);
        // Verify the location and intersection of the card.
        assertNotNull(gameBoard.getCardsOnBoard()[row][col]);
        assertNotNull(gameBoard.getIntersectionGrid()[row][col]);
        assertNotNull(gameBoard.getIntersectionGrid()[row][col + 1]);
        assertNotNull(gameBoard.getIntersectionGrid()[row + 1][col + 1]);
        assertNotNull(gameBoard.getIntersectionGrid()[row + 1][col]);
    }

    @ParameterizedTest(name = "Test place card at location {0}-{1}")
    @DisplayName("Test place card outside of game board")
    @CsvSource({
            "-1,0", "-1,2", "-1,5", "-1,6",
            "0,6", "2,6", "5,6", "6,6",
            "6,5", "6,2", "6,0", "6,-1",
            "5,-1", "2,-1", "0,-1", "-1,-1"
    })
    void shouldNotPlaceCardOutsideOfGameBoard(int row, int col) {
        WegePlayingCard card = createLandCard(row, col);
        assertThrows(Exception.class, () -> {
            gameBoard.placeCardOnBoard(card);
        });
    }

    @ParameterizedTest(name =
            "Intersections of {0} card, land location. " +
                    "Top left-{1}, " +
                    "top right-{2}, " +
                    "bottom right-{3}, " +
                    "bottom left-{4}")
    @MethodSource("provideIntersectionTypeTestArguments")
    @DisplayName("Test intersection of cards")
    void shouldMatchLandAndWater(WegeCard.CardType cardType,
                                 boolean isLandAtTopLeft,
                                 boolean isLandAtTopRight,
                                 boolean isLandAtBottomRight,
                                 boolean isLandAtBottomLeft) {
        WegePlayingCard card = createCardWithoutGnome(cardType, 0, 0);
        gameBoard.placeCardOnBoard(card);
        Intersection[][] intersections = gameBoard.getIntersectionGrid();
        assertEquals(isLandAtTopLeft, intersections[0][0].isConnectLand());
        assertEquals(isLandAtTopRight, intersections[0][1].isConnectLand());
        assertEquals(isLandAtBottomRight, intersections[1][1].isConnectLand());
        assertEquals(isLandAtBottomLeft, intersections[1][0].isConnectLand());
    }

    @Test
    @DisplayName("Test count Gnome at intersection")
    void shouldCountFacingGnome() {
        Intersection stubIntersection = gameBoard.createIntersection(1, 1);
        stubIntersection.increaseFacingGnomeCount();
        WegePlayingCard dummyCard = new WegePlayingCard(WegeCard.CardType.LAND, true, true);
        // collapse with intersection 1,1
        dummyCard.setRow(1);
        dummyCard.setCol(1);
        gameBoard.placeCardOnBoard(dummyCard);
        // Gnome is increased.
        assertEquals(2, stubIntersection.getFacingGnomeCount());
    }

    @Test
    @DisplayName("Test not count Gnome at intersection")
    void shouldNotCountFacingGnome() {
        Intersection stubIntersection = gameBoard.createIntersection(1, 1);
        stubIntersection.increaseFacingGnomeCount();
        WegePlayingCard dummyCard = new WegePlayingCard(WegeCard.CardType.LAND, true, true);
        // Far away from the intersection 1,1
        dummyCard.setRow(2);
        dummyCard.setCol(2);
        gameBoard.placeCardOnBoard(dummyCard);
        // Gnome not increased
        assertEquals(1, stubIntersection.getFacingGnomeCount());
    }

    @Test
    @DisplayName("Test find played card at location")
    void shouldFindPlayedCard() {
        gameBoard.placeCardOnBoard(createLandCard(1, 1));
        assertNotNull(gameBoard.findPlayedCard(1, 1));
    }

    @Test
    @DisplayName("Test find not played card at location")
    void shouldNotFindNotPlayedCard() {
        assertNull(gameBoard.findPlayedCard(1, 1));
    }

    @ParameterizedTest(name = "Contact Point at row={0},col={1}")
    @CsvSource({
            "0,1,1,1", //Top of 1-1
            "1,2,1,2", //Right of 1-1
            "2,1,2,1", //Bottom of 1-1
            "1,0,1,1" // Left of 1-1
    })
    @DisplayName("Test find first contact point")
    void shouldFindFirstContactPoint(int row, int col, int x, int y) {
        // Given a card played before
        WegePlayingCard dummyPlayedCard = createLandCard(1, 1);
        gameBoard.placeCardOnBoard(dummyPlayedCard);
        Intersection intersection = gameBoard.findFirstConnection(row, col);
        assertNotNull(intersection);
        assertEquals(x, intersection.getX());
        assertEquals(y, intersection.getY());
    }

    @ParameterizedTest(name = "Contact Point at row={0},col={1}")
    @CsvSource({
            // Close to 1-1 but only have one contact point
            "1,1", "1,3", "3,3", "3,1",
            // Far from 1-1 and do not have any contact point
            "0,0", "0,2", "0,5", "3,5", "5,5", "5,2", "5,0", "2,0"
    })
    @DisplayName("Test not found contact point")
    void shouldNotFindContactPoint(int row, int col) {
        // Given a card played before
        WegePlayingCard dummyPlayedCard = createLandCard(2, 2);
        gameBoard.placeCardOnBoard(dummyPlayedCard);
        Intersection intersection = gameBoard.findFirstConnection(row, col);
        assertNull(intersection);
    }

    @ParameterizedTest(name = "Not competed at {0}, {1}")
    @CsvSource({
            "0,0", "0,5", "5,5", "5,0", //corner
            "0,2", "2,5", "5,2", "2,0", //edge
            "1,1", "2,2", "3,3", "4,4", // middle
            "1,4", "2,3", "3,4", "2,4" // random
    })
    @DisplayName("Test find first completed intersection")
    void shouldFindFirstNotCompleted(int notCompletedX, int notCompletedY) {
        // Prepare all completed
        for (int x = 0; x < gameBoard.getIntersectionGrid().length; x++) {
            for (int y = 0; y < gameBoard.getIntersectionGrid()[x].length; y++) {
                gameBoard.createIntersection(x, y).setCompleted(true);
            }
        }
        // Set the not completed at
        gameBoard.getIntersectionGrid()[notCompletedX][notCompletedY].setCompleted(false);
        // Find first not completed
        Intersection actual = gameBoard.findFirstNotCompletedIntersection();
        assertNotNull(actual);
        assertEquals(notCompletedX, actual.getX());
        assertEquals(notCompletedY, actual.getY());
    }

    @Test
    @DisplayName("Test not found not completed intersection")
    void shouldNotFoundNotCompletedIntersection() {
        // Prepare all completed
        for (int x = 0; x < gameBoard.getIntersectionGrid().length; x++) {
            for (int y = 0; y < gameBoard.getIntersectionGrid()[x].length; y++) {
                gameBoard.createIntersection(x, y).setCompleted(true);
            }
        }
        // Find first not completed
        Intersection actual = gameBoard.findFirstNotCompletedIntersection();
        assertNull(actual);
    }

    @ParameterizedTest(name = "Intersections at row={0}, col={1}")
    @CsvSource({
            "0,0", "0,5", "5,5", "5,0", //corner
            "0,2", "2,5", "5,2", "2,0", //edge
            "1,1", "2,2", "3,3", "4,4", // middle
            "1,4", "2,3", "3,4", "2,4" // random
    })
    @DisplayName("Test find surround intersections of card on game board")
    void shouldFindSurroundIntersections(int row, int col) {
        // Setup
        WegePlayingCard card = createLandCard(row, col);
        gameBoard.placeCardOnBoard(card);
        // Find
        List<Intersection> surroundIntersections = gameBoard.findSurroundIntersections(card);
        assertEquals(4, surroundIntersections.size());
    }

    @ParameterizedTest(name = "Intersections at row={0}, col={1}")
    @CsvSource({
            "0,0", "0,5", "5,5", "5,0", //corner
            "0,2", "2,5", "5,2", "2,0", //edge
            "1,1", "2,2", "3,3", "4,4", // middle
            "1,4", "2,3", "3,4", "2,4" // random
    })
    @DisplayName("Test not find surround intersections of card on game board")
    void shouldNotFoundIntersectionsOfNotPlayedCard(int row, int col) {
        WegePlayingCard card = createLandCard(row, col);
        List<Intersection> surroundIntersections = gameBoard.findSurroundIntersections(card);
        assertTrue(surroundIntersections.isEmpty());
    }

    static List<Arguments> provideIntersectionTypeTestArguments() {
        return List.of(
                Arguments.of(WegeCard.CardType.LAND, true, false, true, false),
                Arguments.of(WegeCard.CardType.WATER, false, true, false, true),
                Arguments.of(WegeCard.CardType.BRIDGE, true, false, true, false),
                Arguments.of(WegeCard.CardType.COSSACK, true, false, true, false)
        );
    }
}