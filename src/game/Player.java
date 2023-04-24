package game;

public class Player {

    private boolean played;

    private boolean isLand;

    public Player(boolean isLand) {
        this.isLand = isLand;
    }

    public boolean isLand() {
        return isLand;
    }

    public boolean isPlayed() {
        boolean status = this.played;
        if (!this.played) {
            this.played = true;
        }
        return status;
    }

}
