package game;

import java.util.ArrayList;
import java.util.List;

public class Intersection {

    private final Location location;

    private boolean visited;

    private boolean completed;

    private List<WegePlayerCard> cards;

    public Intersection(Location location) {
        this.location = location;
        cards = new ArrayList<>();
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

    public List<WegePlayerCard> getCards() {
        return cards;
    }

    public boolean touchEdge() {
        throw new IllegalStateException("Not yet implemented!");
    }
}
