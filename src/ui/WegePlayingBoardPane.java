package ui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * The top pane to display the Wege Playing Board.
 */
public class WegePlayingBoardPane extends GridPane {

    /* Handler for mouse click to a board button. */
    private EventHandler<MouseEvent> boardButtonHandler;

    /**
     * Create a new playing board for the game Wege.
     *
     * @param rows the number of row for the playing board of the Wege Game
     * @param cols the number of column for the playing board of the Wege Game
     */
    public WegePlayingBoardPane(int rows, int cols) {
        createPlayingBoard(rows, cols);
    }

    public void setBoardButtonClickedHandler(EventHandler<MouseEvent> boardButtonHandler) {
        this.boardButtonHandler = boardButtonHandler;
    }

    /**
     * Create a playing board for this Wege Game.
     *
     * @param rows the number of row for the playing board of the Wege Game
     * @param cols the number of column for the playing board of the Wege Game
     */
    private void createPlayingBoard(int rows, int cols) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                WegeBoardButton boardButton = createBoardButton(row, col);
                add(boardButton, col, row);
            }
        }
    }

    /**
     * Create the button on the playing board. This button display the card
     * that a player play.
     *
     * @param row the row of this button on the playing board.
     * @param col the column of this button on the playing board.
     * @return a button display on the playing board.
     */
    private WegeBoardButton createBoardButton(int row, int col) {
        WegeBoardButton boardButton = new WegeBoardButton(100, 100, row, col);
        boardButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                mouseClickedEvent -> {
                    if (boardButtonHandler != null)
                        boardButtonHandler.handle(mouseClickedEvent);
                });
        return boardButton;
    }

}
