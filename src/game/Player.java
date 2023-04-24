package game;

import java.util.HashMap;
import java.util.Map;

public class Player {

    private final boolean isLand;

    private int cossackCardsPlayed;

    private int centralGround;

    private int edgeTouched;

    private final Map<Integer, Integer> facingGnomeGroup = new HashMap<>(4);

    public Player(boolean isLand) {
        this.isLand = isLand;
    }

    public boolean isLand() {
        return isLand;
    }

    public int getCossackCardsPlayed() {
        return cossackCardsPlayed;
    }

    public void increaseCossackCardPlayed() {
        cossackCardsPlayed++;
    }

    public void increaseFacingGnomeGroup(int gnomeAtThisIntersection) {
        Integer groupCount = facingGnomeGroup.putIfAbsent(gnomeAtThisIntersection, 0);
        if (groupCount != null) {
            facingGnomeGroup.put(gnomeAtThisIntersection, ++groupCount);
        }
    }

    public void increaseCentralGround() {
        centralGround++;
    }

    public void setMaximumEdgeTouched(int edgeCount) {
        this.edgeTouched = Math.max(this.edgeTouched, edgeCount);
    }

    @Override
    public String toString() {
        return "{\"Player\":{"
                + "\"isLand\":\"" + isLand + "\""
                + ", \"cossackCardsPlayed\":\"" + cossackCardsPlayed + "\""
                + ", \"centralGround\":\"" + centralGround + "\""
                + ", \"edgeTouched\":\"" + edgeTouched + "\""
                + ", \"facingGnomeGroup\":" + facingGnomeGroup
                + "}}";
    }
}
