import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.util.LinkedList;

public class WegeGame {

    private final WegeButton nextCardButton;

    // TODO: bind label with Next Card Button to automatically update when a new card is set.
    private final Label label = new Label();

    private final WegePlayerMonitor wegePlayerMonitor;

    /**
     * The rows of the playing board.
     */
    private final int rows;

    /**
     * The cols of the playing board.
     */
    private final int cols;

    public WegeGame(int rows, int cols) {
        nextCardButton = createNextCardButton();
        WegeGameMaster wegeGameMaster = new WegeGameMaster(rows, cols);
        WegePlayer.WegePlayerBuilder wegePlayerBuilder = new WegePlayer.WegePlayerBuilder(wegeGameMaster);
        LinkedList<WegePlayer> players = new LinkedList<>();
        players.add(wegePlayerBuilder.buildPlayer(WegePlayer.PlayerType.LAND));
        players.add(wegePlayerBuilder.buildPlayer(WegePlayer.PlayerType.WATER));
        wegePlayerMonitor = new WegePlayerMonitor(players);
        this.rows = rows;
        this.cols = cols;
    }

    public GridPane drawBoard() {
        GridPane gridPane = new GridPane();
        FlowPane flowPane = new FlowPane(nextCardButton, label);
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                WegeBoardButton boardButton = createBoardButton(row, col);
                gridPane.add(boardButton, col, row);
            }
        }
        gridPane.add(flowPane, 0, rows + 1, rows, 1);
        return gridPane;
    }

    private WegeButton createNextCardButton() {
        final WegeDeck wegeDeck = WegeDeck.createDefaultDeck();
        final WegeButton nextCardButton = new WegeButton(100, 100);
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

    private WegeBoardButton createBoardButton(int row, int col) {
        WegeBoardButton boardButton = new WegeBoardButton(row, col);
        boardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (nextCardButton.getCard() == null) return;
            WegePlayer currentPlayer = wegePlayerMonitor.getNextPlayer();
            WegeCard nextCard = nextCardButton.getCard();
            WegePlayer.PlayerMove currentPlayerMove = currentPlayer.playCard(nextCard, row, col);
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
    private void placeCard(WegeBoardButton boardButton) {
        setCardOnBoard(boardButton);
        nextCardButton.setCard(null);
    }

    /**
     * Swap a card from the board button with the next card button.
     *
     * @param boardButton the button on the playing board to swap a card
     */
    private void swapCard(WegeBoardButton boardButton) {
        WegeCard currentCardOnBoard = boardButton.getCard();
        setCardOnBoard(boardButton);
        nextCardButton.setCard(currentCardOnBoard);
    }

    /**
     * Set the next card to the playing board.
     *
     * @param boardButton the board button to set the next card.
     */
    private void setCardOnBoard(WegeBoardButton boardButton) {
        WegeCard nextCard = nextCardButton.getCard();
        boardButton.setCard(nextCard);
    }

}
