package game;

public class Intersection {

    private final Location location;

    private boolean visited;

    private boolean completed;

    public Intersection(Location location) {
        this.location = location;
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

}
