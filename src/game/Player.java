package game;

import java.util.HashMap;
import java.util.Map;

/**
 * Model class which represent a player for the game Wege.
 * He knows what is the interested statistic based on cards played
 * on the Wege game board
 */
public class Player {

    /* if this player is a land player, Otherwise, he is the water player. */
    private final boolean isLandPlayer;

    /* the number of cossack cards played on the board so far. */
    private int cossackCardsPlayed;

    /* water or land that does not connect by paths or streams to the edge of the game board */
    private int centralGround;

    /* the maximum number of edges on the game board by land / stream created by this player. */
    private int maximumEdgesTouched;

    /* details statistic about how many gnome group created by this player. */
    private final Map<Integer, Integer> facingGnomeGroup = new HashMap<>(4);

    /**
     * Create a new player.
     *
     * @param isLandPlayer set to <code>true</code> if this is a land player.
     */
    public Player(boolean isLandPlayer) {
        this.isLandPlayer = isLandPlayer;
    }

    /**
     * Return <code>true</code> if this player is a land player.
     */
    public boolean isLandPlayer() {
        return isLandPlayer;
    }

    /**
     * Return the number of cossack cards played by this player.
     */
    public int getCossackCardsPlayed() {
        return cossackCardsPlayed;
    }

    /**
     * Increase the number of cossack cards played by this player to 1.
     */
    public void increaseCossackCardPlayed() {
        cossackCardsPlayed++;
    }

    /**
     * Return the statistic of gnome location. Key is the combination of gnome
     * value is the amount of these combinations.
     */
    public Map<Integer, Integer> getFacingGnomeGroup() {
        return facingGnomeGroup;
    }

    /**
     * Increase the number of gnome group at this location to 1.
     *
     * @param gnomeAtThisIntersection the amount of gnome facing together at
     *                                this intersection.
     */
    public void increaseFacingGnomeGroup(int gnomeAtThisIntersection) {
        Integer groupCount = facingGnomeGroup.putIfAbsent(gnomeAtThisIntersection, 0);
        if (groupCount != null) {
            facingGnomeGroup.put(gnomeAtThisIntersection, ++groupCount);
        }
    }

    /**
     * Return water or land that does not connect by paths or streams to the edge of the game board.
     */
    public int getCentralGround() {
        return centralGround;
    }

    /**
     * Increase water or land that does not connect by paths or streams to the edge of the game board to 1.
     */
    public void increaseCentralGround() {
        centralGround++;
    }

    /**
     * Set the maximum value of edges touched by cards of this player.
     * If the given value is larger than the existing value, set it to
     * the new record.
     *
     * @param edgeCount the number of Edges.
     */
    public void setMaximumEdgeTouched(int edgeCount) {
        this.maximumEdgesTouched = Math.max(this.maximumEdgesTouched, edgeCount);
    }

    /**
     * Return the maximum edges touched by cards of this player.
     */
    public int getMaximumEdgesTouched() {
        return maximumEdgesTouched;
    }

    @Override
    public String toString() {
        return "{\"Player\":{"
                + "\"isLand\":\"" + isLandPlayer + "\""
                + ", \"cossackCardsPlayed\":\"" + cossackCardsPlayed + "\""
                + ", \"centralGround\":\"" + centralGround + "\""
                + ", \"edgeTouched\":\"" + maximumEdgesTouched + "\""
                + ", \"facingGnomeGroup\":" + facingGnomeGroup
                + "}}";
    }
}
