package game;

import javafx.geometry.Pos;

import java.util.*;

/**
 * The master of the game Wege. Only he knows the game rule and the game score well.
 * He also needs a game board to function properly.
 */
public class WegeGameMaster {

    /* The Land player of the game Wege. */
    private final Player landPlayer = new Player(true);

    /* The Water player of the game Wege. */
    private final Player waterPlayer = new Player(false);

    /* The playing board of the game Wege. */
    private final WegePlayingBoard gameBoard;

    /* The player who takes next turn. */
    private Player nextPlayer;

    /* flag to indicate whether the game started by a player placing a card on the game board. */
    private boolean gameStarted;

    /**
     * Create a game master to control the game Wege by the rule of the game.
     *
     * @param gameBoard the playing board of the game Wege.
     */
    public WegeGameMaster(WegePlayingBoard gameBoard) {
        this.gameBoard = gameBoard;
        // Land player always go first.
        this.nextPlayer = landPlayer;
    }

    /**
     * Attempt to place a Wege card on the game board.
     *
     * @param card the card to place
     * @return <code>true</code> if the card is placed successfully.
     * @see #isLegalPlace(WegePlayingCard) the rule for valid placement.
     */
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
            gameBoard.placeCardOnBoard(card);
        }
        return legalPlace;
    }

    /**
     * Attempt to swap a Wege card on the game board. If the swap card
     * is not align with the existing card on the game board, don't swap it.
     *
     * @param card the card to swap
     * @return <code>true</code> if the card is swap successfully.
     * @see #isLegalSwap(WegePlayingCard) the rule for valid swap.
     * @see #isLegalPlace(WegePlayingCard) the rule for valid place.
     */
    public boolean trySwapCard(WegePlayingCard card) {
        boolean legalSwap = isLegalSwap(card) && isLegalPlace(card);
        if (legalSwap) {
            setNextPlayer();
            gameBoard.placeCardOnBoard(card);
        }
        return legalSwap;
    }

    /**
     * Collect player statistic from cards played so far on the game board.
     *
     * @return the statistic for all players of the game Wege.
     */
    public List<Player> collectPlayerStatistic() {
        // Travel the board.
        while (true) {
            Intersection firstNotCompleted = gameBoard.findFirstNotCompletedIntersection();
            if (firstNotCompleted == null) {
                break;
            }
            Player player = getAssociatePlayer(firstNotCompleted);
            travelAndUpdate(firstNotCompleted, player);
        }
        gameBoard.resetIntersectionGrid();
        return List.of(landPlayer, waterPlayer);
    }

    /**
     * The rules are:
     *
     * <ul>
     *     <li>
     *         The first card played can go anywhere.
     *     </li>
     *     <li>
     *         Each additional card played must be placed adjacent to
     *         an existing card, either horizontally or vertically.
     *     </li>
     *     <li>
     *         The land and water on the card must match with the land/water
     *         of the card it is placed next to. Water cannot connect to land
     *         and land cannot connect to water.
     *     </li>
     *     <li>
     *
     *     </li>
     * </ul>
     *
     * @param card the card to be placed on the game board.
     * @return true if the next card can be placed on the game board.
     * Otherwise, return false.
     */
    private boolean isLegalPlace(WegePlayingCard card) {
        Intersection firstContactPoint = gameBoard.findFirstConnection(card.getRow(), card.getCol());
        // If no intersection played
        if (firstContactPoint == null) return false;
        return firstContactPoint.isConnectLand() && card.isLand(firstContactPoint) || firstContactPoint.isWater() && card.isWater(firstContactPoint);
    }

    /**
     * The rules are:
     *
     * <ul>
     *     <li>
     *         There are special "bridge" cards. With a bridge card, you can either
     *         play it like a regular card, or you can use it to replace an existing
     *         water or land card. But:
     *         <ul>
     *             <li>
     *                 A bridge card cannot replace a cossack card.
     *             </li>
     *             <li>
     *               A bridge card cannot replace a land or water card
     *               if that card has a gnome AND that gnome is part of a group of "facing" gnomes.
     *             </li>
     *         </ul>
     *     </li>
     * </ul>
     *
     * @param card the card to be swapped on the game board.
     * @return true if the next card can be swapped on the game board.
     * Otherwise, return false.
     */
    private boolean isLegalSwap(WegePlayingCard card) {
        // Only bridge card is legal for swap
        if (card.getCardType() != WegeCard.CardType.BRIDGE) return false;
        WegePlayingCard cardPlayedOnBoard = gameBoard.findPlayedCard(card.getRow(), card.getCol());
        // Cannot swap a not played card.
        if (cardPlayedOnBoard == null) return false;
        WegeCard.CardType cardPlayedType = cardPlayedOnBoard.getCardType();
        // Cannot swap a cossack card.
        if (cardPlayedType == WegeCard.CardType.COSSACK) return false;
        // Can swap if water and land card does not have a gnome.
        if (!cardPlayedOnBoard.hasGnome()) return true;
        List<Intersection> surroundIntersections = gameBoard.findSurroundIntersections(cardPlayedOnBoard);
        for (Intersection intersection : surroundIntersections) {
            // Cannot swap a card in the group of facing gnome.
            if (intersection.getFacingGnomeCount() > 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the game is started yet. If not, return the current state
     * then set the flag {@link #gameStarted} to true.
     *
     * @return <code>true</code> if the game is started by a player place the
     * first card on the game board.
     */
    private boolean isGameStarted() {
        try {
            return gameStarted;
        } finally {
            gameStarted = true;
        }
    }

    /**
     * Set the player queue for the next turn.
     */
    private void setNextPlayer() {
        if (nextPlayer != landPlayer) {
            nextPlayer = landPlayer;
        } else {
            nextPlayer = waterPlayer;
        }
    }

    /**
     * Get the other player of the game. If the current player
     * is land then the other must be water.
     *
     * @param player the current player.
     * @return the other player.
     */
    private Player getOtherPlayer(Player player) {
        if (player.isLandPlayer()) return waterPlayer;
        return landPlayer;
    }

    ////////////////////////// Player Score evaluation //////////////////////

    /**
     * Get the player associate to an intersection. If the intersection is land
     * then the player is land player. Otherwise, the player is water player.
     *
     * @param intersection the intersection on the game board.
     * @return the player associate to this intersection.
     */
    private Player getAssociatePlayer(Intersection intersection) {
        Player player;
        if (intersection.isConnectLand()) {
            player = landPlayer;
        } else {
            player = waterPlayer;
        }
        return player;
    }

    /**
     * From the first intersection, follow the trail by go to the opposite direction based
     * on cards have been played so far on the game board. Only consider cards with the
     * same type. While traveling, collect the statistic for the given player associate to
     * the intersection.
     *
     * @param startIntersection the intersection on the game board to start travel.
     * @param player the player associate to this intersection.
     */
    private void travelAndUpdate(Intersection startIntersection, Player player) {
        Deque<Intersection> intersectionStack = new LinkedList<>();
        intersectionStack.add(startIntersection);
        List<Intersection> visited = new ArrayList<>();
        while (!intersectionStack.isEmpty()) {
            Intersection nextPoint = intersectionStack.pop();
            // Collect facing gnome.
            player.increaseFacingGnomeGroup(nextPoint.getFacingGnomeCount());
            nextPoint.setVisited(true);
            visited.add(nextPoint);
            for (WegePlayingCard card : gameBoard.findSurroundCards(nextPoint)) {
                if (isValidTrail(nextPoint, card)) {
                    // Go to the opposite.
                    Intersection oppositePoint = findOppositeIntersection(nextPoint, card);
                    if (!oppositePoint.isVisited()) intersectionStack.addFirst(oppositePoint);
                }
            }
        }
        // Collect edges
        if (!visited.isEmpty()) {
            int edgeCount = gameBoard.countEdgeTouch(visited);
            if (edgeCount == 0) getOtherPlayer(player).increaseCentralGround();
            else player.setMaximumEdgeTouched(edgeCount);
        }
        visited.forEach(i -> i.setCompleted(true));
    }

    /**
     * Find the opposite intersection for a given intersection on a game card.
     *
     * @param intersection the intersection.
     * @param card the card to find the opposite intersection
     * @return the opposite intersection.
     * @throws RuntimeException if the intersection is not on the game card.
     */
    private Intersection findOppositeIntersection(Intersection intersection, WegePlayingCard card) {
        Pos opposite = card.findOppositePosition(intersection);
        return gameBoard.findOppositeIntersection(intersection, opposite);
    }

    /**
     * Check if the given card is valid for traveling on the trail (path / stream)
     * belong to it. If it's a Cossack card, then it cannot be traveled.
     *
     * @param startIntersection the intersection to start travel.
     * @param card the card surrounds this intersection.
     * @return <code>true</code> if this card can be traveled further. Otherwise,
     * return <code>false</code>.
     * @throws RuntimeException if the in start intersection is far away from the given card.
     */
    private boolean isValidTrail(Intersection startIntersection, WegePlayingCard card) {
        // Cossack means it's a block.
        if (card.getCardType() == WegeCard.CardType.COSSACK) return false;
        // Bridge can be travel.
        if (card.getCardType() == WegeCard.CardType.BRIDGE) return true;
        return startIntersection.isConnectLand() && card.getCardType() == WegeCard.CardType.LAND
                || startIntersection.isWater() && card.getCardType() == WegeCard.CardType.WATER;
    }

}
