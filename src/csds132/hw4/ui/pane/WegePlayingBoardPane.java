package csds132.hw4.ui.pane;

import csds132.hw4.ui.button.WegeBoardButton;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * The top pane to display the Wege Playing Board.
 */
public class WegePlayingBoardPane extends GridPane {

    /**
     * Create a new playing board for the game Wege.
     *
     * @param rows the number of row for the playing board of the Wege Game
     * @param cols the number of column for the playing board of the Wege Game
     * @param mouseClickedEventHandler the event handler for a button when a mouse click to it.
     */
    public WegePlayingBoardPane(int rows,
                                int cols,
                                EventHandler<MouseEvent> mouseClickedEventHandler) {
        createPlayingBoard(rows, cols, mouseClickedEventHandler);
    }

    /**
     * Create a playing board for this Wege Game.
     *
     * @param rows the number of row for the playing board of the Wege Game
     * @param cols the number of column for the playing board of the Wege Game
     * @param mouseClickedEventHandler the event handler for a button when a mouse click to it.
     */
    private void createPlayingBoard(int rows,
                                    int cols,
                                    EventHandler<MouseEvent> mouseClickedEventHandler) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                WegeBoardButton boardButton = createBoardButton(row, col, mouseClickedEventHandler);
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
     * @param mouseClickedEventHandler the event handler for this button when a mouse click to it.
     * @return a button display on the playing board.
     */
    private WegeBoardButton createBoardButton(int row,
                                         int col,
                                         EventHandler<MouseEvent> mouseClickedEventHandler) {
        WegeBoardButton boardButton = new WegeBoardButton(100, 100, row, col);
        boardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedEventHandler);
        return boardButton;
    }

}
