package ui;

import game.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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
    private final WegeGameMaster wegeGameMaster;

    /**
     * Create a new Wege Game.
     *
     * @param rows     the number of row for the playing board for this game.
     * @param cols     the number of column for the playing board for this game.
     * @param wegeDeck the dek contains wege cards for this game.
     */
    public WegeGameBox(int rows, int cols, WegeDeck wegeDeck) {
        wegeGameMaster = new WegeGameMaster(rows, cols);
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
        bottomPane = new WegeBottomPane(startingDeck);
        bottomPane.setEndGameEvent(event -> wegeGameMaster.endGame());
        playingBoard = new WegePlayingBoardPane(rows, cols);
        // UI Interactions when a player click a button on the playing board.
        playingBoard.setBoardButtonClickedHandler(getBoardButtonClickedHandler(bottomPane));
        ObservableList<Node> children = getChildren();
        children.add(playingBoard);
        children.add(bottomPane);
        bottomPane.displayPlayerScore(0);
    }

    /**
     * Get the listener when mouse click to the button on the playing board.
     *
     * @param bottomPane the bottom pane to set the place or swap cards.
     * @return a listener which knows the button being clicked on the playing board
     * and handle that event.
     */
    private ChangeListener<WegeBoardButton> getBoardButtonClickedListener(WegeBottomPane bottomPane) {
        return (observable, previousButtonClicked, nextButtonClicked) -> {
            if (bottomPane.getNextCard() == null) return;
            WegePlayer currentPlayer = wegeGameMaster.getCurrentPlayer();
            WegeCard nextCard = bottomPane.getNextCard();
            try {
                WegePlayer.Move currentMove = currentPlayer.playCard(
                        nextCard, nextButtonClicked.getRow(), nextButtonClicked.getCol());
                switch (currentMove) {
                    case PLACE -> placeCard(nextButtonClicked, bottomPane);
                    case SWAP -> swapCard(nextButtonClicked, bottomPane);
                }
            } catch (IllegalMoveException ignored) {
                // TODO: Should display explanation why this player made the wrong move ?
            }
        };
    }


    private EventHandler<MouseEvent> getBoardButtonClickedHandler(WegeBottomPane bottomPane) {
        return mouseClickedEvent -> {
            if (wegeGameMaster.isGameEnd()) {
                wegeGameMaster.endGame();
            }
            WegeBoardButton boardButton = (WegeBoardButton) mouseClickedEvent.getSource();
            if (bottomPane.getNextCard() == null) return;
            WegePlayer currentPlayer = wegeGameMaster.getCurrentPlayer();
            WegeCard nextCard = bottomPane.getNextCard();
            try {
                WegePlayer.Move currentMove = currentPlayer.playCard(
                        nextCard, boardButton.getRow(), boardButton.getCol());
                switch (currentMove) {
                    case PLACE -> placeCard(boardButton, bottomPane);
                    case SWAP -> swapCard(boardButton, bottomPane);
                }
            } catch (IllegalMoveException ignored) {
                // TODO: Should display explanation why this player made the wrong move ?
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
//        bottomPane.displayPlayerType(wegeGameMaster.getQueuePlayer().getPlayerType());
    }

}
