package csds132.hw4.ui;

import csds132.hw4.game.*;
import csds132.hw4.model.WegeCard;
import csds132.hw4.model.WegeDeck;
import csds132.hw4.model.WegePlayer;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.LinkedList;

/**
 * The view of Wege Game.
 */
public class WegeGame extends VBox {

    /* The next card button in this view. */
    private WegeButton nextCardButton;

    // TODO: bind label with Next Card Button to automatically update when a new card is set.
    private final Label label = new Label();

    /* Track players of a Wege Game. */
    private WegePlayerMonitor wegePlayerMonitor;

    /**
     * Create a new Wege Game.
     *
     * @param rows the number of row for the playing board of the Wege Game
     * @param cols the number of column for the playing board of the Wege Game
     */
    public WegeGame(int rows, int cols) {
        wegePlayerMonitor = createPlayerMonitor(rows, cols);
        createView(rows, cols);
    }

    /**
     * Create a player monitor to track the players of this game.
     *
     * @param rows the number of row for the playing board of the Wege Game
     * @param cols the number of column for the playing board of the Wege Game
     * @return a {@link WegePlayerMonitor}
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
     * @param rows the number of row for the playing board of the Wege Game
     * @param cols the number of column for the playing board of the Wege Game
     */
    private void createView(int rows, int cols) {
        GridPane playingBoard = createPlayingBoard(rows, cols);
        FlowPane flowPane = createBottomPane();
        ObservableList<Node> children = getChildren();
        children.add(playingBoard);
        children.add(flowPane);
    }

    /**
     * Create a playing board for this Wege Game.
     *
     * @param rows the number of row for the playing board of the Wege Game
     * @param cols the number of column for the playing board of the Wege Game
     * @return a {@link GridPane} contains a 2 dimension array of {@link WegeButton}
     * to let a player place a card on the playing board.
     */
    private GridPane createPlayingBoard(int rows, int cols) {
        GridPane gridPane = new GridPane();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                WegeButton boardButton = createBoardButton(row, col);
                gridPane.add(boardButton, col, row);
            }
        }
        return gridPane;
    }

    /**
     * Create the bottom pane for this view.
     *
     * @return a {@link FlowPane} contains a {@link WegeButton} to let a player
     * choose a card to play on the playing board.
     */
    private FlowPane createBottomPane() {
        FlowPane flowPane = new FlowPane();
        nextCardButton = createNextCardButton();
        flowPane.getChildren().add(nextCardButton);
        return flowPane;
    }

    /**
     * Create the next card button. This button can display the next card
     * to be played and can rotate that card.
     *
     * @return a {@link WegeButton} initially display the first card from the deck.
     */
    private WegeButton createNextCardButton() {
        final WegeDeck wegeDeck = WegeDeck.createDefaultDeck();
        final WegeButton nextCardButton = new WegeButton(100, 100);
        WegeCard firstCard = wegeDeck.drawFromFront();
        nextCardButton.setCard(firstCard);
        nextCardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (nextCardButton.getCard() == null) {
                WegeCard nextCard = wegeDeck.drawFromFront();
                nextCardButton.setCard(nextCard);
                label.setText(nextCard.getCardType().name());
            } else {
                nextCardButton.rotate();
            }
        });
        return nextCardButton;
    }

    /**
     * Create the button on the playing board. This button display the card
     * that a player play.
     *
     * @param rows the number of row for the playing board of the Wege Game
     * @param cols the number of column for the playing board of the Wege Game
     * @return a {@link WegeButton} with no card inside.
     */
    private WegeButton createBoardButton(int rows, int cols) {
        WegeButton boardButton = new WegeButton(100, 100);
        boardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (nextCardButton.getCard() == null) return;
            WegePlayer currentPlayer = wegePlayerMonitor.getNextPlayer();
            WegeCard nextCard = nextCardButton.getCard();
            WegePlayer.PlayerMove currentPlayerMove = currentPlayer.playCard(nextCard, rows, cols);
            if (currentPlayerMove == null) return;
            switch (currentPlayerMove) {
                case SWAP -> swapCard(boardButton);
                case PLACE -> placeCard(boardButton);
            }
        });
        return boardButton;
    }

    /**
     * Place a card to a board button then clear the card in the next card button.
     *
     * @param boardButton the button on the playing board to place a card.
     */
    private void placeCard(WegeButton boardButton) {
        boardButton.setCard(nextCardButton.getCard());
        nextCardButton.setCard(null);
    }

    /**
     * Swap a card from the board button with the card in next card button.
     *
     * @param boardButton the button on the playing board to swap a card
     */
    private void swapCard(WegeButton boardButton) {
        WegeCard currentCardOnBoard = boardButton.getCard();
        boardButton.setCard(nextCardButton.getCard());
        nextCardButton.setCard(currentCardOnBoard);
    }

}
