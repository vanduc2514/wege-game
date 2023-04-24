package game;

import javafx.geometry.Pos;

// TODO: ultimate refactor to remove this sub-class
public class WegePlayingCard extends WegeCard {

    private int row;

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

    public void setRow(int row) {
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getCol() {
        return col;
    }

    public boolean isLand(Intersection intersection) {
        Pos relativePosition = findRelativePosition(intersection);
        return isLand(relativePosition);
    }

    public boolean isWater(Intersection intersection) {
        return !isLand(intersection);
    }

    public boolean isGnome(Intersection intersection) {
        Pos relativePosition = findRelativePosition(intersection);
        return hasGnome() && getGnomePosition() == relativePosition;
    }

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
     */
    private Pos findRelativePosition(Intersection intersection) {
        int x = intersection.getX(), y = intersection.getY();
        if (row == x && col == y) return Pos.TOP_LEFT;
        if (row == x && col + 1 == y) return Pos.TOP_RIGHT;
        if (row + 1 == x && col + 1 == y) return Pos.BOTTOM_RIGHT;
        if (row + 1 == x && col == y) return Pos.BOTTOM_LEFT;
        return null;
    }

}
