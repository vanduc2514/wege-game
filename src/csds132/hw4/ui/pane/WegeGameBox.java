package csds132.hw4.ui.pane;

import csds132.hw4.game.*;
import csds132.hw4.game.WegeCard;
import csds132.hw4.game.WegeDeck;
import csds132.hw4.game.WegeGameSetting;
import csds132.hw4.game.WegePlayer;
import csds132.hw4.ui.button.WegeBoardButton;
import csds132.hw4.ui.button.WegeButton;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.LinkedList;

/**
 * The view of a Wege game and UI interactions for the game.
 */
public class WegeGameBox extends VBox {

    /* The top playing board of this box */
    private WegePlayingBoardPane playingBoard;

    /* The bottom pane of this box */
    private WegeBottomPane bottomPane;

    /* Track players of a Wege Game. */
    private final WegePlayerMonitor wegePlayerMonitor;

    /**
     * Create a new Wege Game.
     *
     * @param rows     the number of row for the playing board for this game.
     * @param cols     the number of column for the playing board for this game.
     * @param wegeDeck the dek contains wege cards for this game.
     */
    public WegeGameBox(int rows, int cols, WegeDeck wegeDeck) {
        wegePlayerMonitor = createPlayerMonitor(rows, cols);
        createView(rows, cols, wegeDeck);
    }

    /**
     * Create a new box to display the ui of the Wege game.
     *
     * @param wegeGameSetting The setting of Wege game.
     */
    public WegeGameBox(WegeGameSetting wegeGameSetting) {
        this(wegeGameSetting.rows(), wegeGameSetting.cols(), wegeGameSetting.deck());
    }

    /**
     * Create a player monitor to track the players of this game.
     *
     * @param rows the number of row for the playing board of the Wege Game
     * @param cols the number of column for the playing board of the Wege Game
     * @return a monitor to track the turn of the player.
     */
    private WegePlayerMonitor createPlayerMonitor(int rows, int cols) {
        final WegePlayerMonitor wegePlayerMonitor;
        WegeGameMaster wegeGameMaster = new WegeGameMaster(rows, cols);
        WegePlayer.WegePlayerBuilder wegePlayerBuilder = new WegePlayer.WegePlayerBuilder(wegeGameMaster);
        LinkedList<WegePlayer> players = new LinkedList<>();
        players.add(wegePlayerBuilder.buildPlayer(WegePlayer.PlayerType.LAND));
        players.add(wegePlayerBuilder.buildPlayer(WegePlayer.PlayerType.WATER));
        wegePlayerMonitor = new WegePlayerMonitor(players);
        return wegePlayerMonitor;
    }

    /**
     * Create a view for the Wege Game.
     *
     * @param rows         the number of row for the playing board of the Wege Game
     * @param cols         the number of column for the playing board of the Wege Game
     * @param startingDeck the Wege deck to start the game.
     */
    private void createView(int rows, int cols, WegeDeck startingDeck) {
        bottomPane = new WegeBottomPane(startingDeck);
        EventHandler<MouseEvent> clickToBoardHandler = getClickToBoardHandler(bottomPane);
        playingBoard = new WegePlayingBoardPane(rows, cols, clickToBoardHandler);
        ObservableList<Node> children = getChildren();
        children.add(playingBoard);
        children.add(bottomPane);
        bottomPane.displayPlayerScore(0);
        bottomPane.displayPlayerType(wegePlayerMonitor.getQueuePlayer().getPlayerType());
    }

    /**
     * Get the handler when mouse click to the button on the playing board
     *
     * @param bottomPane the bottom pane to set the place or swap cards.
     */
    private EventHandler<MouseEvent> getClickToBoardHandler(WegeBottomPane bottomPane) {
        return mouseClickedEvent -> {
            if (bottomPane.getNextCard() == null) return;
            WegeBoardButton boardButton = (WegeBoardButton) mouseClickedEvent.getSource();
            WegePlayer currentPlayer = wegePlayerMonitor.getCurrentPlayer();
            WegeCard nextCard = bottomPane.getNextCard();
            WegePlayer.PlayerMove currentPlayerMove = currentPlayer.playCard
                    (nextCard, boardButton.getRow(), boardButton.getCol());
            if (currentPlayerMove == null) return;
            switch (currentPlayerMove) {
                case PLACE -> placeCard(boardButton, bottomPane);
                case SWAP -> swapCard(boardButton, bottomPane);
            }
        };
    }

    /**
     * Place a card to a board button then clear the card in the next card button.
     *
     * @param boardButton the button on the playing board to place a card.
     * @param bottomPane  contains next card button.
     */
    private void placeCard(WegeButton boardButton, WegeBottomPane bottomPane) {
        setCardOnBoard(boardButton, bottomPane.getNextCard());
        bottomPane.setNextCard(null);
    }

    /**
     * Swap a card from the board button with the card in next card button.
     *
     * @param boardButton the button on the playing board to swap a card
     * @param bottomPane  contains next card button.
     */
    private void swapCard(WegeButton boardButton, WegeBottomPane bottomPane) {
        WegeCard currentCardOnBoard = boardButton.getCard();
        setCardOnBoard(boardButton, bottomPane.getNextCard());
        bottomPane.setNextCard(currentCardOnBoard);
    }

    /**
     * Set a card to the button on the playing board.
     *
     * @param boardButton button on the playing board.
     * @param wegeCard    the card to set to this button.
     */
    private void setCardOnBoard(WegeButton boardButton, WegeCard wegeCard) {
        boardButton.setCard(wegeCard);
        bottomPane.displayPlayerType(wegePlayerMonitor.getQueuePlayer().getPlayerType());
    }

}
