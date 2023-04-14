/**
 * The button to use in a Wege playing board.
 */
public class WegeBoardButton extends WegeButton {

    /**
     * The row of this button on the playing board.
     */
    public final int row;

    /**
     * The column of this button on the playing board.
     */
    public final int col;

    /**
     * Create a new button with the location on the playing board
     *
     * @param row the row on the playing board
     * @param col the column on the playing board
     */
    public WegeBoardButton(int row, int col) {
        super(100 , 100);
        this.row = row;
        this.col = col;
    }

}
