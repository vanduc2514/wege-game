package game;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class WegeGameManager {

    Player landPlayer = new Player(true);

    Player waterPlayer = new Player(false);

    Queue<Player> playerQueue = new LinkedList<>();

    private boolean gameStarted;

    private final WegePlayingBoard playingBoard;

    public WegeGameManager(WegePlayingBoard playingBoard) {
        this.playingBoard = playingBoard;
        playerQueue.add(landPlayer);
        playerQueue.add(waterPlayer);
    }

    // Get all the intersection and check the position of it
    public boolean tryPlaceCard(WegePlayingCard card) {
        boolean legalPlace;
        if (!isGameStarted()) {
            legalPlace = true;
        } else {
            legalPlace = isLegalPlace(card);
        }
        if (legalPlace) {
            playingBoard.placeCardOnBoard(card);
        }
        return legalPlace;
    }

    public boolean trySwapCard(WegePlayingCard card) {
        boolean legalSwap = isLegalSwap(card) && isLegalPlace(card);
        if (legalSwap) {
            playingBoard.placeCardOnBoard(card);
        }
        return legalSwap;
    }

    private boolean isGameStarted() {
        try {
            return gameStarted;
        } finally {
            gameStarted = true;
        }
    }

    private boolean isLegalPlace(WegePlayingCard card) {
        Intersection firstContactPoint = playingBoard.findFirstContactPoint(card.getRow(), card.getCol());
        // If no intersection played
        if (firstContactPoint == null) return false;
        return firstContactPoint.isLand() && card.isLand(firstContactPoint)
                || firstContactPoint.isWater() && card.isWater(firstContactPoint);
    }

    private boolean isLegalSwap(WegePlayingCard card) {
        // Only bridge card is legal for swap
        if (card.getCardType() != WegeCard.CardType.BRIDGE) return false;
        WegePlayingCard cardPlayedOnBoard = playingBoard.findPlayedCard(card.getRow(), card.getCol());
        // Cannot swap a not played card.
        if (cardPlayedOnBoard == null) return false;
        WegeCard.CardType cardPlayedType = cardPlayedOnBoard.getCardType();
        // Cannot swap a cossack card.
        if (cardPlayedType == WegeCard.CardType.COSSACK) return false;
        // Can swap if water and land card does not have a gnome.
        if (!cardPlayedOnBoard.hasGnome()) return true;
        List<Intersection> surroundIntersections = playingBoard.findSurroundIntersections(
                cardPlayedOnBoard.getRow(), cardPlayedOnBoard.getCol());
        for (Intersection intersection: surroundIntersections) {
            // Cannot swap a card in the group of facing gnome.
            if (intersection.getFacingGnomeCount() > 1) {
                return false;
            }
        }
        return true;
    }

}
