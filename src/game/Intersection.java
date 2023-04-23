package game;

import java.util.HashSet;
import java.util.Set;

public class Intersection {

    private final int x;

    private final int y;

    private final Location location;

    private boolean visited;

    private boolean completed;

    private WegeCard.CardType intersectionType;

    private Set<WegeCard> cards;

    public void setIntersectionType(WegeCard.CardType intersectionType) {
        this.intersectionType = intersectionType;
    }

    public WegeCard.CardType getIntersectionType() {
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

    public Set<WegeCard> getCards() {
        return cards;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
