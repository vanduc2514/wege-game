package game;

import java.util.HashSet;
import java.util.Set;

public class Intersection {

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
        cards = new HashSet<>();
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

}
