package game;

import java.util.HashSet;
import java.util.Set;

public class Intersection {

    private final int x;

    private final int y;

    private final Location location;

    private boolean visited;

    private boolean completed;

    private int facingGnomeCount;

    private WegePlayingCard.CardType intersectionType;

    private Set<WegePlayingCard> cards;

    private boolean isLand;

    public void setIntersectionType(WegePlayingCard.CardType intersectionType) {
        this.intersectionType = intersectionType;
    }

    public WegePlayingCard.CardType getIntersectionType() {
        return intersectionType;
    }

    public Intersection(Location location) {
        this.location = location;
        this.x = 0;
        this.y = 0;
        cards = new HashSet<>();
    }

    public Intersection(int x, int y) {
        this.location = null;
        this.x = x;
        this.y = y;
    }

    public void setLand(boolean land) {
        isLand = land;
    }

    public boolean isLand() {
        return isLand;
    }

    public boolean isWater() {
        return !isLand;
    }

    public Location getLocation() {
        return location;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isVisited() {
        return visited;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Set<WegePlayingCard> getCards() {
        return cards;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getFacingGnomeCount() {
        return facingGnomeCount;
    }

    public void increaseFacingGnomeCount() {
        facingGnomeCount++;
    }

}
