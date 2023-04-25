package ui;

import game.*;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * The view of a Wege game and UI interactions for the game.
 */
public class WegeGameBox extends VBox {

    /* Maximum cards that can be placed on the game board. */
    private final int maximumCards;

    /* The game master for the Game Wege. He helps checking the game rule and collect player statistic */
    private final WegeGameMaster wegeGameMaster;

    /* The number of cards had been played so far. */
    private int cardsPlayed;

    /**
     * Create a new Wege Game.
     *
     * @param rows     the number of row for the playing board for this game.
     * @param cols     the number of column for the playing board for this game.
     * @param wegeDeck the dek contains wege cards for this game.
     */
    public WegeGameBox(int rows, int cols, WegeDeck wegeDeck) {
        this.maximumCards = rows * cols;
        WegePlayingBoard wegePlayingBoard = new WegePlayingBoard(rows, cols);
        wegeGameMaster = new WegeGameMaster(wegePlayingBoard);
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
     * Create a view for the Wege Game.
     *
     * @param rows         the number of row for the playing board of the Wege Game
     * @param cols         the number of column for the playing board of the Wege Game
     * @param startingDeck the Wege deck to start the game.
     */
    private void createView(int rows, int cols, WegeDeck startingDeck) {
        /* The bottom pane of this box */
        WegeBottomPane bottomPane = new WegeBottomPane(startingDeck);
        bottomPane.setEndGameEvent(event -> {
            wegeGameMaster.collectPlayerStatistic().forEach(System.out::println);
        });
        /* The top playing board of this box */
        WegePlayingBoardPane playingBoard = new WegePlayingBoardPane(rows, cols);
        // UI Interactions when a player click a button on the playing board.
        playingBoard.setBoardButtonClickedHandler(getBoardButtonClickedHandler(bottomPane));
        ObservableList<Node> children = getChildren();
        children.add(playingBoard);
        children.add(bottomPane);
    }

    private EventHandler<MouseEvent> getBoardButtonClickedHandler(WegeBottomPane bottomPane) {
        return mouseClickedEvent -> {
            if (isGameEnded()) {
                wegeGameMaster.collectPlayerStatistic()
                        .forEach(System.out::println);
                return;
            }
            if (bottomPane.getNextCard() == null) return;
            WegeBoardButton boardButton = (WegeBoardButton) mouseClickedEvent.getSource();
            WegePlayingCard nextCard = bottomPane.getNextCard();
            int row = boardButton.getRow();
            int col = boardButton.getCol();
            nextCard.setRow(row);
            nextCard.setCol(col);
            if (boardButton.getCard() == null
                    && wegeGameMaster.tryPlaceCard(nextCard)) {
                placeCard(boardButton, bottomPane);
            } else if (boardButton.getCard() != null
                    && wegeGameMaster.trySwapCard(nextCard)){
                swapCard(boardButton, bottomPane);
            }
        };
    }

    /**
     * Check if the game is ended when all cards have been played on the board.
     *
     * @return <code>true</code> if the board is filled up with all Wege card.
     * Otherwise, return <code>false</code>.
     */
    private boolean isGameEnded() {
        return cardsPlayed == maximumCards;
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
        cardsPlayed++;
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
        bottomPane.setNextCard((WegePlayingCard) currentCardOnBoard);
    }

    /**
     * Set a card to the button on the playing board.
     *
     * @param boardButton button on the playing board.
     * @param wegeCard    the card to set to this button.
     */
    private void setCardOnBoard(WegeButton boardButton, WegeCard wegeCard) {
        boardButton.setCard(wegeCard);
    }

}
