package game;

import javafx.geometry.Pos;

import java.util.HashMap;
import java.util.Map;

public class WegePlayerCard extends WegeCard {

    private WegePlayer playedBy;

    Map<Pos, Intersection> itscRelationPos;

    /**
     * Create a Wege game button
     *
     * @param type        the type of game card
     * @param hasGnome    whether there is a gnome on this card
     * @param isPathGnome sets whether the gnome (if there is one) is on the land/water "path" or on a dead end
     */
    public WegePlayerCard(CardType type, boolean hasGnome, boolean isPathGnome) {
        super(type, hasGnome, isPathGnome);
        itscRelationPos = new HashMap<>(4);
    }

    public void setPlayedBy(WegePlayer playedBy) {
        this.playedBy = playedBy;
    }

    public WegePlayer getPlayedBy() {
        return playedBy;
    }

    public boolean isPlayedBySameType() {
        if (getCardType() == CardType.LAND
                && getPlayedBy().getPlayerType() == WegePlayer.PlayerType.LAND) {
            return true;
        }
        if (getCardType() == CardType.WATER
              && getPlayedBy().getPlayerType() == WegePlayer.PlayerType.WATER) {
            return true;
        }
        return getCardType() == CardType.BRIDGE;
    }

    public void setTopLeftIntersection(Intersection intersection) {
        itscRelationPos.put(Pos.TOP_LEFT, intersection);
    }

    public void setTopRightIntersection(Intersection intersection) {
        itscRelationPos.put(Pos.TOP_RIGHT, intersection);
    }

    public void setBottomRightIntersection(Intersection intersection) {
        itscRelationPos.put(Pos.BOTTOM_RIGHT, intersection);
    }

    public void setBottomLeftIntersection(Intersection intersection) {
        itscRelationPos.put(Pos.BOTTOM_LEFT, intersection);
    }

    public boolean isWater(Intersection intersection) {
        for (Map.Entry<Pos, Intersection> entry : itscRelationPos.entrySet()) {
            Pos pos = entry.getKey();
            Intersection itsc = entry.getValue();
            if (isWater(pos) && intersection == itsc) {
                return true;
            }
        }
        return false;
    }

}
