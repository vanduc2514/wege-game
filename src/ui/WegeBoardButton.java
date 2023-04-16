package ui;

public class WegeBoardButton extends WegeButton {

    /* The row of this button on the playing board. */
    private final int row;

    /* The column of this button on the playing board. */
    private final int col;

    /**
     * Create a Wege game button
     *
     * @param width  the desired with of the button
     * @param height the desired height of the button
     * @param row the row of this button on the playing board.
     * @param col the column of this button on the playing board.
     */
    public WegeBoardButton(int width,
                           int height,
                           int row,
                           int col) {
        super(width, height);
        this.row = row;
        this.col = col;
    }

    /**
     * Return the row of this button on the playing board.
     */
    public int getRow() {
        return row;
    }

    /**
     * Return the column of this button on the playing board.
     */
    public int getCol() {
        return col;
    }
}
