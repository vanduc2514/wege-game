package game;

public class WegePlayerCard extends WegeCard {

    private WegePlayer playedBy;

    /**
     * Create a Wege game button
     *
     * @param type        the type of game card
     * @param hasGnome    whether there is a gnome on this card
     * @param isPathGnome sets whether the gnome (if there is one) is on the land/water "path" or on a dead end
     */
    public WegePlayerCard(CardType type, boolean hasGnome, boolean isPathGnome) {
        super(type, hasGnome, isPathGnome);
    }

    public void setPlayedBy(WegePlayer playedBy) {
        this.playedBy = playedBy;
    }

    public WegePlayer getPlayedBy() {
        return playedBy;
    }

}
