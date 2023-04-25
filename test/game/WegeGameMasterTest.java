package game;

import javafx.geometry.Pos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static game.TestUtil.createCardWithoutGnome;
import static game.TestUtil.createLandCard;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Test cases for the game master of the game Wege")
class WegeGameMasterTest {

    /* The test subject */
    private WegeGameMaster gameMaster;

    @BeforeEach
    void initGameMaster() {
        WegePlayingBoard gameBoard = new WegePlayingBoard(
                WegeGameSetting.STANDARD_BOARD_ROWS,
                WegeGameSetting.STANDARD_BOARD_COLS);
        gameMaster = new WegeGameMaster(gameBoard);
        gameMaster.tryPlaceCard(createLandCard(0, 0));
    }

    @Test
    @DisplayName("Test place card at game start")
    void shouldPlaceCardAtGameStart() {
        assertTrue(createNewGameMaster().tryPlaceCard(createLandCard(0,0)));
    }

    @Test
    @DisplayName("Test place card match same land and water")
    void shouldPlaceCardMatchLandAndWater() {
        WegePlayingCard card = createCardWithoutGnome(WegeCard.CardType.WATER);
        card.setRow(0); card.setCol(1);
        assertTrue(gameMaster.tryPlaceCard(card));
    }

    @Test
    @DisplayName("Test place card not match same land and water")
    void shouldNotPlaceCardNotMatchLandAndWater() {
        WegePlayingCard card = createCardWithoutGnome(WegeCard.CardType.LAND);
        card.setRow(0); card.setCol(1);
        assertFalse(gameMaster.tryPlaceCard(card));
    }

    @Test
    @DisplayName("Test place card not adjacent")
    void shouldNotPlaceCardWithoutAdjacent() {
        assertFalse(gameMaster.tryPlaceCard(createLandCard(2,2)));
    }

    @Test
    @DisplayName("Test swap card with land card not has Gnome")
    void shouldSwapCardIfLandWithoutGnome() {
        assertTrue(gameMaster.trySwapCard(createCardWithoutGnome(WegeCard.CardType.BRIDGE)));
    }

    @Test
    @DisplayName("Test swap card with water card not has Gnome")
    void shouldSwapCardIfWaterWithoutGnome() {
        gameMaster.tryPlaceCard(createCardWithoutGnome(WegeCard.CardType.WATER));
        assertTrue(gameMaster.trySwapCard(createCardWithoutGnome(WegeCard.CardType.BRIDGE)));
    }

    @Test
    @DisplayName("Test swap card with land not has gnome group")
    void shouldSwapCardIfLandNotHasGnomeGroup() {
        gameMaster.tryPlaceCard(new WegePlayingCard(WegeCard.CardType.LAND, true, true));
        assertTrue(gameMaster.trySwapCard(createCardWithoutGnome(WegeCard.CardType.BRIDGE)));
    }

    @Test
    @DisplayName("Test swap card with water not has gnome group")
    void shouldSwapCardIfWaterNotHasGnomeGroup() {
        gameMaster.tryPlaceCard(new WegePlayingCard(WegeCard.CardType.WATER, true, true));
        assertTrue(gameMaster.trySwapCard(createCardWithoutGnome(WegeCard.CardType.BRIDGE)));
    }

    @Test
    @DisplayName("Test swap not Bridge card")
    void shouldNotSwapCardIfNotBridge() {
        for (WegePlayingCard card : List.of(
            createCardWithoutGnome(WegeCard.CardType.LAND),
            createCardWithoutGnome(WegeCard.CardType.WATER),
            createCardWithoutGnome(WegeCard.CardType.COSSACK)
        )) {
            assertFalse(gameMaster.trySwapCard(card));
        }
    }

    @Test
    @DisplayName("Test swap card with not played card")
    void shouldNotSwapCardIfNotPlayed() {
        WegePlayingCard bridgeCard = createCardWithoutGnome(WegeCard.CardType.BRIDGE);
        bridgeCard.setRow(1); bridgeCard.setCol(1);
        assertFalse(gameMaster.trySwapCard(bridgeCard));
    }

    @Test
    @DisplayName("Test swap card with Cossack card")
    void shouldNotSwapCardIfCossack() {
        gameMaster.tryPlaceCard(createCardWithoutGnome(WegeCard.CardType.COSSACK));
        assertFalse(gameMaster.trySwapCard(createCardWithoutGnome(WegeCard.CardType.BRIDGE)));
    }

    @Test
    @DisplayName("Test swap card with land has gnome group")
    void shouldNotSwapCardIfLandHasGnomeGroup() {
        WegeGameMaster thisTestMaster = createNewGameMaster();
        generateCardsInGnomeGroup(WegeCard.CardType.LAND).forEach(thisTestMaster::tryPlaceCard);
        assertFalse(thisTestMaster.trySwapCard(createCardWithoutGnome(WegeCard.CardType.BRIDGE)));
    }

    @Test
    @DisplayName("Test swap card with water has gnome group")
    void shouldNotSwapCardIfWaterHasGnomeGroup() {
        WegeGameMaster thisTestMaster = createNewGameMaster();
        generateCardsInGnomeGroup(WegeCard.CardType.WATER).forEach(thisTestMaster::tryPlaceCard);
        assertFalse(thisTestMaster.trySwapCard(createCardWithoutGnome(WegeCard.CardType.BRIDGE)));
    }

    /**
     * Generate a list of cards in the gnome group.
     *
     * @param cardType the type of the card.
     * @return cards that has gnome group.
     */
    private List<WegePlayingCard> generateCardsInGnomeGroup(WegeCard.CardType cardType) {
        WegePlayingCard topLeft = new WegePlayingCard(cardType, true, true);
        topLeft.setOrientation(Pos.BOTTOM_LEFT);
        WegePlayingCard card = new WegePlayingCard(cardType, true, true);
        card.setRow(1); card.setCol(0);
        return List.of(topLeft, card);
    }

    /**
     * Create new game master with a 3 x 3 board.
     *
     * @return a new game master.
     */
    private WegeGameMaster createNewGameMaster() {
        return new WegeGameMaster(new WegePlayingBoard(3, 3));
    }

}