package game;

import java.util.HashSet;
import java.util.Set;

public class Intersection {

    private final int x;

    private final int y;

    private boolean visited;

    private boolean completed;

    private int facingGnomeCount;

    private boolean isLand;

    public Intersection(int x, int y) {
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
