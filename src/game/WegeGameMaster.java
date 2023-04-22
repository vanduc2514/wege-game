package game;

import javafx.geometry.Pos;

import java.util.*;

/**
 * The Game Master of the game Wege. This master keeps a {@link #playingBoard} to help
 * maintain the game and track the {@link WegeCard} that are being played on the game board.
 * Only him knows the game rule and the scoring of it.
 */
public class WegeGameMaster {

    /* The 2 dimension Wege playing board. */
    private final WegeCard[][] playingBoard;

    private final Intersection[][] boardIntersections;

    private LinkedList<Intersection> allIntersections = new LinkedList<>();

    /**
     * The location (row, col) of Wege Cards on {@link #playingBoard}. This map is used
     * to find the location of a {@link WegeCard} without looping through the playing board.
     */
    private final Map<WegeCard, Location> cardLocations;

    /**
     * Create a new master for the game Wege.
     *
     * @param rows number of rows for the playing board.
     * @param cols number of columns for the playing board.
     */
    public WegeGameMaster(int rows, int cols) {
        this.playingBoard = new WegeCard[rows][cols];
        this.cardLocations = new HashMap<>(playingBoard.length);
        // The number of intersections is plus 1 to cover all the board,
        this.boardIntersections = new Intersection[rows + 1][cols + 1];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Intersection intersection = new Intersection(new Location(row, col));
                this.allIntersections.add(intersection);
                boardIntersections[row][col] = intersection;
            }
        }
    }

    /**
     * Place given {@link WegeCard} on the playing board.
     *
     * @param wegeCard   the {@link WegeCard} to be placed.
     * @param wegePlayer
     * @param row        the row of this card on the playing board.
     * @param col        the column of this card on the playing board.
     */
    public void trackPlayedCard(WegeCard wegeCard, WegePlayer wegePlayer, int row, int col) {
        playingBoard[row][col] = wegeCard;
        // Use WegePlayerCard
        cardLocations.put(wegeCard, new Location(row, col));
//        List<Intersection> intersection = findAllIntersection(row, col);
//        intersection.forEach(i -> i.getCards().add(wegeCard));
    }

    /**
     * Find {@link Pos#TOP_LEFT}, {@link Pos#TOP_RIGHT}, {@link Pos#BOTTOM_RIGHT}, {@link Pos#BOTTOM_LEFT}
     * Intersections of a given location of a wege card.
     *
     * @param row the row of the card on the playing board.
     * @param col the column of the card on the playing board.
     * @return all Intersections relate to this card.
     */
    private List<Intersection> findAllIntersection(int row, int col) {
        Location topLeftLoc = new Location(row + 1, col);
        Location topRightLoc = new Location(row + 1, col + 1);
        Location bottomRightLoc = new Location(row, col + 1);
        Location bottomLeftLoc = new Location(row, col);
        return List.of(
                boardIntersections[topLeftLoc.row()][topLeftLoc.col()],
                boardIntersections[topRightLoc.row()][topRightLoc.col()],
                boardIntersections[bottomRightLoc.row()][bottomRightLoc.col()],
                boardIntersections[bottomRightLoc.row()][bottomLeftLoc.col()]
        );
    }

    /**
     * Find the relative Position of an Intersection in a card.
     *
     * @param intersectionLocation the location of the Intersection.
     * @param cardLocation the location of the card
     * @return the relative Position or return null if this Intersection
     * is not belong to the card.
     */
    private Pos getPositionOnCard(Location intersectionLocation, Location cardLocation) {
        int cardRow = cardLocation.row();
        int cardCol = cardLocation.col();
        if (intersectionLocation.row() == cardRow
                && intersectionLocation.col() == cardCol + 1)
            return Pos.TOP_LEFT;
        if (intersectionLocation.row() == cardRow + 1
                && intersectionLocation.col() == cardCol + 1)
            return Pos.TOP_RIGHT;
        if (intersectionLocation.row() == cardRow + 1
                && intersectionLocation.col() == cardCol)
            return Pos.BOTTOM_RIGHT;
        if (intersectionLocation.row() == cardRow
                && intersectionLocation.col() == cardCol)
            return Pos.BOTTOM_LEFT;
        return null;
    }

    public WegePlayer.Move nextMove(WegeCard nextCard, int row, int col) throws IllegalMoveException {
        WegeCard wegeCard = findWegeCard(row, col);
        // If there is no card, it's a placement
        // otherwise, it's a swap
        if (wegeCard == null && isLegalRegularCardMove(nextCard, row, col)) {
            return WegePlayer.Move.PLACE;
        } else if (wegeCard != null
                && isLegalBridgeCardMove(nextCard, row, col)) {
            return WegePlayer.Move.SWAP;
        }
        throw new IllegalMoveException();
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
     * @param nextCard the next card in the deck.
     * @param row the location (row) to place on the playing board.
     * @param col the location (col) to place on the playing board.
     * @return true if the next card can be played on the target location.
     * Otherwise, return false.
     */
    public boolean isLegalRegularCardMove(WegeCard nextCard, int row, int col) {
        WegeCard adjacentCard;
        Pos adjacentCardContactPos;
        Pos nextCardContactPos;
            if ((adjacentCard = findLeftCard(row, col)) != null) {
            adjacentCardContactPos = Pos.TOP_RIGHT;
            nextCardContactPos = Pos.TOP_LEFT;
        } else if ((adjacentCard = findBottomCard(row, col)) != null) {
            adjacentCardContactPos = Pos.TOP_LEFT;
            nextCardContactPos = Pos.BOTTOM_LEFT;
        } else if ((adjacentCard = findRightCard(row, col)) != null) {
            adjacentCardContactPos = Pos.TOP_LEFT;
            nextCardContactPos = Pos.TOP_RIGHT;
        } else if ((adjacentCard = findTopCard(row, col)) != null) {
            adjacentCardContactPos = Pos.TOP_LEFT;
            nextCardContactPos = Pos.BOTTOM_LEFT;
        } else {
            return false;
        }
        return adjacentCard.isWater(adjacentCardContactPos) == nextCard.isWater(nextCardContactPos);
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
     * @param row the location (row) to place on the playing board.
     * @param col the location (col) to place on the playing board.
     * @return true if the next card can be played on the target location.
     */
    // Redo this to have specific logic for bridge card.
    public boolean isLegalBridgeCardMove(WegeCard wegeCard, int row, int col) {
        WegeCard cardOnBoard = findWegeCard(row, col);
        WegeCard.CardType cardOnBoardType;
        if (cardOnBoard == null
                || (cardOnBoardType = cardOnBoard.getCardType()) == WegeCard.CardType.COSSACK)
            return false;
        boolean legalSwap = isLegalRegularCardMove(wegeCard, row, col);
        if (!legalSwap) {
            return false;
        }
        if (cardOnBoardType == WegeCard.CardType.WATER
                || cardOnBoardType == WegeCard.CardType.LAND) {
            if (!cardOnBoard.hasGnome()) return true;
            return findGnomeGroupMembers(row, col).isEmpty();
        }
        return false;
    }

    /**
     * Find all card that is possible to form a Gnome group for the given card.
     * Look for the direction where the Gnome is faced at.
     *
     * @param row the row on the playing board.
     * @param col the column on the playing board.
     * @return all members of the Gnome Group. If no member is found, return
     * an empty list.
     */
    private List<WegeCard> findGnomeGroupMembers(int row, int col) {
        List<WegeCard> groupMembers = new ArrayList<>();
        WegeCard wegeCard = findWegeCard(row, col);
        if (wegeCard == null) return groupMembers;
        switch (wegeCard.getGnomePosition()) {
            case TOP_LEFT -> {
                WegeCard topCard = findBottomCard(row, col);
                if (isInGnomeGroup(topCard, Pos.BOTTOM_LEFT)) groupMembers.add(topCard);
                WegeCard leftCard = findLeftCard(row, col);
                if (isInGnomeGroup(leftCard, Pos.TOP_RIGHT)) groupMembers.add(leftCard);
            }
            case TOP_RIGHT -> {
                WegeCard topCard = findBottomCard(row, col);
                if (isInGnomeGroup(topCard, Pos.BOTTOM_RIGHT)) groupMembers.add(topCard);
                WegeCard rightCard = findRightCard(row, col);
                if (isInGnomeGroup(rightCard, Pos.TOP_LEFT)) groupMembers.add(rightCard);
            }
            case BOTTOM_RIGHT -> {
                WegeCard bottomCard = findTopCard(row, col);
                if (isInGnomeGroup(bottomCard, Pos.TOP_RIGHT)) groupMembers.add(bottomCard);
                WegeCard rightCard = findRightCard(row, col);
                if (isInGnomeGroup(rightCard, Pos.BOTTOM_LEFT)) groupMembers.add(rightCard);
            }
            case BOTTOM_LEFT -> {
                WegeCard bottomCard = findTopCard(row, col);
                if (isInGnomeGroup(bottomCard, Pos.TOP_LEFT)) groupMembers.add(bottomCard);
                WegeCard leftCard = findLeftCard(row, col);
                if (isInGnomeGroup(leftCard, Pos.BOTTOM_RIGHT)) groupMembers.add(leftCard);
            }
        }
        // all null needs to be removed to avoid NullPointerException.
        groupMembers.removeAll(Collections.singletonList(null));
        return groupMembers;
    }

    /**
     * Check if the relative card has is in a Gnome group.
     *
     * @param wegeCard the relative card.
     * @param relativePosition the Gnome position to form a group.
     * @return true if this card has a gnome, and it faces toward each other.
     * Otherwise, return false.
     */
    private boolean isInGnomeGroup(WegeCard wegeCard, Pos relativePosition) {
        if (wegeCard == null) return false;
        if (!wegeCard.hasGnome()) return false;
        return wegeCard.getGnomePosition() == relativePosition;
    }

    /**
     * Find the Left card of the current card.
     *
     * @param row the row on the playing board.
     * @param col the column on the playing board.
     * @return a card on the playing board or null if the card can not be found.
     */
    private WegeCard findLeftCard(int row, int col) {
        return findWegeCard(row, col - 1);
    }

    /**
     * Find the Top card of the current card.
     *
     * @param row the row on the playing board.
     * @param col the column on the playing board.
     * @return a card on the playing board or null if the card can not be found.
     */
    private WegeCard findTopCard(int row, int col) {
        return findWegeCard(row - 1, col);
    }

    /**
     * Find the Right card of the current card.
     *
     * @param row the row on the playing board.
     * @param col the column on the playing board.
     * @return a card on the playing board or null if the card can not be found.
     */
    private WegeCard findRightCard(int row, int col) {
        return findWegeCard(row, col + 1);
    }

    /**
     * Find the Bottom card of the current card.
     *
     * @param row the row on the playing board.
     * @param col the column on the playing board.
     * @return a card on the playing board or null if the card can not be found.
     */
    private WegeCard findBottomCard(int row, int col) {
        return findWegeCard(row + 1, col);
    }

    /**
     * Find the {@link WegeCard} from the playing board for the given locations.
     *
     * @param row the row in the playing board.
     * @param col the column in the playing board.
     * @return a card on the playing board or null if the card can not be found.
     */
    private WegeCard findWegeCard(int row, int col) {
        try {
            return playingBoard[row][col];
        } catch (IndexOutOfBoundsException ignored) {
            return null;
        }
    }

    public class Score {
        // Make circular list to check infinity of elements
        // Tail point to Head.
        private LinkedList<Intersection> intersection = new LinkedList<>();

        LinkedList<Intersection> temp = new LinkedList<>();
        public void endGame() {
            Iterator<Intersection> intersectionIterator = intersection.iterator();
            Intersection boardIntersection;
            while (!(boardIntersection = intersectionIterator.next()).isCompleted()) {
                // Not completed intersection.
                Intersection notCompleted = boardIntersection;
                // Add to the front of temp linked list
                temp.add(notCompleted);
                List<Intersection> visited = new ArrayList<>();
                while (!temp.isEmpty()) {
                    // remove front of linked list
                    Intersection current = temp.pop();
                    // Set to true
                    current.setVisited(true);
                    // Collect all visited.
                    visited.add(current);
                    // For each of the cards, follow the path and count gnome
                    List<WegePlayerCard> gnomeCards = new ArrayList<>();
                    for (WegePlayerCard card : current.getCards()) {
                        // If there is a gnome at this intersection
                        if (card.hasGnome()) {
                            gnomeCards.add(card);
                        }
                        if (card.isPlayedBySameType()) {
                            Intersection otherSide = findTheOtherSide(card);
                            if (!otherSide.isVisited()) {
                                temp.add(otherSide);
                            }
                        }
                    }
                }
                // 3. Once the list is empty, look for all the intersections with visited
                // set to true but completed set to false.
                for (Intersection intersection1 : visited) {
                    intersection1.setCompleted(true);
                    if (intersection1.touchEdge()) {
                        // COunt all the edges
                    }
                }
            }
        }

        private Intersection findTheOtherSide(WegePlayerCard card) {
            throw new IllegalStateException("Not yet implemented!");
        }
    }
}
