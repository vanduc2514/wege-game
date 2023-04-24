package game;

public class Player {

    private int cossackCardsPlayed;

    private final boolean isLand;

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
}
