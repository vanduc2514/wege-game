package game;

import javafx.geometry.Pos;

/**
 * The model for a card of the game Wege, which contains useful methods to interact with.
 */
public class WegePlayingCard extends WegeCard {

    /* The row of this card played on the game board. */
    private int row;

    /* The column of this card played on the game board. */
    private int col;

    /**
     * Create a Wege game card
     *
     * @param type        the type of game card
     * @param hasGnome    whether there is a gnome on this card
     * @param isPathGnome sets whether the gnome (if there is one) is on the land/water "path" or on a dead end
     */
    public WegePlayingCard(WegeCard.CardType type, boolean hasGnome, boolean isPathGnome) {
        super(type, hasGnome, isPathGnome);
    }

    /**
     * Set the row of this card on the game board.
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Get the row of this card on the game board.
     */
    public int getRow() {
        return row;
    }

    /**
     * Set the column of this card on the game board.
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Get the column of this card on the game board.
     */
    public int getCol() {
        return col;
    }

    /**
     * Check if an intersection associate to this card is have multiple lands connect.
     *
     * @param intersection the intersection.
     * @return <code>true</code> if this intersection connect multiple lands.
     */
    public boolean isLand(Intersection intersection) {
        Pos relativePosition = findRelativePosition(intersection);
        return isLand(relativePosition);
    }

    /**
     * Check if an intersection associate to this card is have multiple water connect.
     *
     * @param intersection the intersection.
     * @return <code>true</code> if this intersection connect multiple water.
     */
    public boolean isWater(Intersection intersection) {
        return !isLand(intersection);
    }

    /**
     * Check if an intersection associate to this card has a Gnome.
     *
     * @param intersection the intersection.
     * @return <code>true</code> if this intersection has a Gnome on the card.
     * @throws IllegalArgumentException if the intersection is not associated with this card.
     */
    public boolean isGnome(Intersection intersection) {
        Pos relativePosition = findRelativePosition(intersection);
        return hasGnome() && getGnomePosition() == relativePosition;
    }

    /**
     * Find the diagonal position of an intersection on this card.
     *
     * @param intersection the intersection.
     * @return the opposite diagonal position.
     * @throws IllegalArgumentException if the intersection is not associated with this card.
     */
    public Pos findOppositePosition(Intersection intersection) {
        return switch (findRelativePosition(intersection)) {
            case TOP_LEFT -> Pos.BOTTOM_RIGHT;
            case TOP_RIGHT -> Pos.BOTTOM_LEFT;
            case BOTTOM_RIGHT -> Pos.TOP_LEFT;
            case BOTTOM_LEFT -> Pos.TOP_RIGHT;
            default -> throw new IllegalArgumentException("Intersection is not on this card!");
        };
    }

    /**
     * find the relative position of an intersection coordinate
     * to a card location.
     *
     * @param intersection an intersection
     * @return the relative position or null if the intersection is not belong to a card.
     * @throws IllegalArgumentException if the intersection is not associated with this card.
     */
    private Pos findRelativePosition(Intersection intersection) {
        int x = intersection.getX(), y = intersection.getY();
        if (row == x && col == y) return Pos.TOP_LEFT;
        if (row == x && col + 1 == y) return Pos.TOP_RIGHT;
        if (row + 1 == x && col + 1 == y) return Pos.BOTTOM_RIGHT;
        if (row + 1 == x && col == y) return Pos.BOTTOM_LEFT;
        throw new IllegalArgumentException("Intersection is not on this card!");
    }

}
