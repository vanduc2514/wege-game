package game;

import javafx.geometry.Pos;

import java.util.*;

public class Intersection {

    private final Location location;

    private boolean visited;

    private boolean completed;

    private WegeCard.CardType intersectionType;

    private Map<WegeCard, Pos> cardsPos;

    private Set<WegeCard> cards;

    public void setIntersectionType(WegeCard.CardType intersectionType) {
        this.intersectionType = intersectionType;
    }

    public WegeCard.CardType getIntersectionType() {
        return intersectionType;
    }

    public Intersection(Location location) {
        this.location = location;
        cardsPos = new HashMap<>();
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

    public void updatePosMap(WegeCard wegeCard, Pos position) {
        cardsPos.put(wegeCard, position);
    }

    public boolean touchEdge() {
        throw new IllegalStateException("Not yet implemented!");
    }
}
