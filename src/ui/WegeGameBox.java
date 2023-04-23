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
import java.util.Objects;

/**
 * The view of a Wege game and UI interactions for the game.
 */
public class WegeGameBox extends VBox {

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
        /* The bottom pane of this box */
        WegeBottomPane bottomPane = new WegeBottomPane(startingDeck);
        bottomPane.setEndGameEvent(event -> wegeGameMaster.endGame());
        /* The top playing board of this box */
        WegePlayingBoardPane playingBoard = new WegePlayingBoardPane(rows, cols);
        // UI Interactions when a player click a button on the playing board.
        playingBoard.setBoardButtonClickedHandler(getBoardButtonClickedHandler(bottomPane));
        ObservableList<Node> children = getChildren();
        children.add(playingBoard);
        children.add(bottomPane);
        bottomPane.displayPlayerScore(0);
    }

    private EventHandler<MouseEvent> getBoardButtonClickedHandler(WegeBottomPane bottomPane) {
        return mouseClickedEvent -> {
            if (wegeGameMaster.isGameEnd()) {
                wegeGameMaster.endGame();
                return;
            }
            WegeBoardButton boardButton = (WegeBoardButton) mouseClickedEvent.getSource();
            if (bottomPane.getNextCard() == null) return;
            WegeCard nextCard = bottomPane.getNextCard();
            WegeGameMaster.Move currentMove = wegeGameMaster.nextMove(
                    nextCard, boardButton.getRow(), boardButton.getCol());
            if (Objects.requireNonNull(currentMove) == WegeGameMaster.Move.PLACE) {
                placeCard(boardButton, bottomPane);
            } else if (currentMove == WegeGameMaster.Move.SWAP) {
                swapCard(boardButton, bottomPane);
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
    }

}
