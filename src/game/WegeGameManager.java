package game;

import javafx.geometry.Pos;

import java.util.*;

public class WegeGameManager {

    private final Player landPlayer = new Player(true);

    private final Player waterPlayer = new Player(false);

    private final WegePlayingBoard playingBoard;

    private Player nextPlayer;

    private boolean gameStarted;

    public WegeGameManager(WegePlayingBoard playingBoard) {
        this.playingBoard = playingBoard;
        this.nextPlayer = landPlayer;
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
            if (card.getCardType() == WegeCard.CardType.COSSACK) {
                nextPlayer.increaseCossackCardPlayed();
            }
            setNextPlayer();
            playingBoard.placeCardOnBoard(card);
        }
        return legalPlace;
    }

    public boolean trySwapCard(WegePlayingCard card) {
        boolean legalSwap = isLegalSwap(card) && isLegalPlace(card);
        if (legalSwap) {
            setNextPlayer();
            playingBoard.placeCardOnBoard(card);
        }
        return legalSwap;
    }

    public boolean isGameEnded() {
        return playingBoard.isFilledUp();
    }

    public List<Player> collectPlayerStatistic() {
        if (!isGameEnded())
            throw new IllegalStateException("Game is not ended yet!");
        // Travel the board.
        while (true) {
            Intersection firstNotCompleted = playingBoard.findFirstNotCompletedIntersection();
            if (firstNotCompleted == null) {
                break;
            }
            Player player = getPlayerOfThisPoint(firstNotCompleted);
            traverseAndUpdate(firstNotCompleted, player);
        }
        return List.of(landPlayer, waterPlayer);
    }

    private boolean isLegalPlace(WegePlayingCard card) {
        Intersection firstContactPoint = playingBoard.findFirstIntersection(card.getRow(), card.getCol());
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
        List<Intersection> surroundIntersections = playingBoard.findSurroundIntersections(cardPlayedOnBoard);
        for (Intersection intersection: surroundIntersections) {
            // Cannot swap a card in the group of facing gnome.
            if (intersection.getFacingGnomeCount() > 1) {
                return false;
            }
        }
        return true;
    }

    private boolean isGameStarted() {
        try {
            return gameStarted;
        } finally {
            gameStarted = true;
        }
    }

    private void setNextPlayer() {
        if (nextPlayer != landPlayer) {
            nextPlayer = landPlayer;
        } else {
            nextPlayer = waterPlayer;
        }
    }

    private Player getOtherPlayer(Player player) {
        if (player == landPlayer) return player;
        return waterPlayer;
    }

    ////////////////////////// Player Score evaluation //////////////////////

    private Player getPlayerOfThisPoint(Intersection firstNotCompleted) {
        Player player;
        if (firstNotCompleted.isLand()) {
            player = landPlayer;
        } else {
            player = waterPlayer;
        }
        return player;
    }

    private void traverseAndUpdate(Intersection firstPoint, Player player) {
        Deque<Intersection> intersectionStack = new LinkedList<>();
        intersectionStack.add(firstPoint);
        List<Intersection> visited = new ArrayList<>();
        while (!intersectionStack.isEmpty()) {
            Intersection nextPoint = intersectionStack.pop();
            // Collect facing gnome.
            player.increaseFacingGnomeGroup(nextPoint.getFacingGnomeCount());
            nextPoint.setVisited(true);
            visited.add(nextPoint);
            for (WegePlayingCard card : playingBoard.findSurroundCards(nextPoint)) {
                if (isValidPath(nextPoint, card)) {
                    // Go to the opposite.
                    Intersection oppositePoint = findOppositePoint(nextPoint, card);
                    if (!oppositePoint.isVisited()) intersectionStack.addFirst(oppositePoint);
                }
            }
        }
        // Collect edges
        if (!visited.isEmpty()) {
            int edgeCount = playingBoard.countEdgeTouch(visited);
            if (edgeCount == 0) getOtherPlayer(player).increaseCentralGround();
            else player.setMaximumEdgeTouched(edgeCount);
        }
        visited.forEach(i -> i.setCompleted(true));
    }

    private Intersection findOppositePoint(Intersection startPoint, WegePlayingCard card) {
        Pos opposite = card.findOppositePosition(startPoint);
        return playingBoard.findOppositePoint(startPoint, opposite);
    }

    private static boolean isValidPath(Intersection startPoint, WegePlayingCard card) {
        // Cossack means it's a block.
        if (card.getCardType() == WegeCard.CardType.COSSACK) return false;
        // Bridge can be travel.
        if (card.getCardType() == WegeCard.CardType.BRIDGE) return true;
        return startPoint.isLand() && card.getCardType() == WegeCard.CardType.LAND
                || startPoint.isWater() && card.getCardType() == WegeCard.CardType.WATER;
    }

}
