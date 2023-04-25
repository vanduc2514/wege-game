package game;

/**
 * The intersection model of the game Wege. Create a type Intersection
 * to keep track of each intersection on the board. An intersection where
 * the corners of the tiles connect. (Include the board corners as "intersections"
 * even though there is only one tile there at that corner.)
 */
public class Intersection {

    /* The x coordinate of this intersection on the game board */
    private final int x;

    /* The y coordinate of this intersection on the game board */
    private final int y;

    /* Whether this card is visited while travelling the intersections */
    private boolean visited;

    /* Where this card is completed so that it does not need to be travelled again */
    private boolean completed;

    /* Get the number of gnome facing together */
    private int facingGnomeCount;

    /* if this intersection connect lands */
    private boolean connectLand;

    /**
     * Create a new type of Intersection.
     *
     * @param x The x-coordinate on the intersection grid.
     * @param y The y-coordinate on the intersection grid.
     */
    public Intersection(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Return the x-coordinate of this intersection on the game grid.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Return the y-coordinate of this intersection on the game grid.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Set to <code>true</code> if this intersection connect lands.
     */
    public void connectLand(boolean connectLand) {
        this.connectLand = connectLand;
    }

    /**
     * Return <code>true</code> if this intersection connect lands.
     */
    public boolean isConnectLand() {
        return connectLand;
    }

    /**
     * Return <code>true</code> if this intersection connect water.
     */
    public boolean isWater() {
        return !connectLand;
    }

    /**
     * Mark this intersection for avoiding re-visit this intersection again.
     *
     * @param visited <code>true</code> if this intersection is visited before.
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Return <code>true</code> if this intersection is visited before
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Mark this intersection for not starting from it again.
     *
     * @param completed Set to <code>true</code> if this intersection is completed.
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Return <code>true</code> if this intersection is completed
     * and don't need to start travel again.
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Return the number of gnome facing together and
     * form a group at this intersection
     */
    public int getFacingGnomeCount() {
        return facingGnomeCount;
    }

    /**
     * Increase the number of gnome facing together at this intersection.
     */
    public void increaseFacingGnomeCount() {
        facingGnomeCount++;
    }

}
