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
    private final Map<WegeCard, CardLocation> cardLocations;

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
        CardLocation location = new CardLocation(row, col);
        cardLocations.put(wegeCard, location);
        List<Intersection> intersection = findAllIntersection(row, col);
        intersection.forEach(i -> i.getCards().add((WegePlayerCard) wegeCard));
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

    /**
     * Check if the next card in the deck is able to place at a location (row, col) on the playing board.
     * The rules are:
     *
     * <ul>
     *     <li>
     *         If the next card is a {@link WegeCard.CardType#BRIDGE}
     *         or a {@link WegeCard.CardType#COSSACK}, it can be placed
     *         next to any cards.
     *     </li>
     *     <li>
     *         If the next card is a {@link WegeCard.CardType#LAND}
     *         or a {@link WegeCard.CardType#WATER}, it can only placed
     *         next to a card with the same type, or next to a
     *         {@link WegeCard.CardType#BRIDGE}
     *     </li>
     * </ul>
     *
     * @param nextCard the next card in the deck.
     * @param row the location (row) to place on the playing board.
     * @param col the location (col) to place on the playing board.
     * @return true if the next card can be placed on the target location. Otherwise, return false.
     */
    public boolean isLegalPlacement(WegeCard nextCard, int row, int col) {
        List<WegeCard> adjacentCards = findAdjacentCards(row, col);
        switch (nextCard.getCardType()) {
            case COSSACK, BRIDGE -> {
                if (!adjacentCards.isEmpty()) return true;
            }
            case LAND, WATER -> {
                for (WegeCard adjacentCard : adjacentCards) {
                    WegeCard.CardType adjacentCardType = adjacentCard.getCardType();
                    if (nextCard.getCardType() == adjacentCardType
                            || adjacentCardType == WegeCard.CardType.BRIDGE)
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if the card {@link WegeCard.CardType#BRIDGE} can be swapped with a card on the playing board.
     * The rules are:
     *
     * <ul>
     *     <li>
     *         If the card on the playing board is a {@link WegeCard.CardType#LAND}
     *         or a {@link WegeCard.CardType#WATER}, it can be swapped as long as it does
     *         not have a gnome or the gnome does not belong to a facing group.
     *     </li>
     *     <li>
     *         If the card on the playing board is the same kind, it can be swapped.
     *     </li>
     *     <li>
     *         If the card on the playing board is {@link WegeCard.CardType#COSSACK},
     *         it cannot be swapped.
     *     </li>
     *     <li>
     *         If there is no card existed in the playing board, it cannot be swapped.
     *     </li>
     * </ul>
     *
     * @param row the row of the card to be swapped on the playing board
     * @param col the column of the card to be swapped on the playing board
     * @return true if the card on the playing board can be swapped. Otherwise, return false.
     */
    public boolean isLegalSwap(int row, int col) {
        WegeCard cardOnBoard = findWegeCard(row, col);
        if (cardOnBoard == null) return false;
        switch (cardOnBoard.getCardType()) {
            case LAND, WATER -> {
                if (!cardOnBoard.hasGnome() || findGnomeGroupMembers(cardOnBoard).isEmpty())
                    return true;
            }
            case BRIDGE -> {
                return true;
            }
        }
        return false;
    }

    /**
     * Find all adjacent cards for a given location (row, col).
     *
     * @param row the row on the playing board.
     * @param col the column on the playing board.
     * @return all of {@link WegeCard} that is placed next to the given location.
     * If there is no card available, return an empty list.
     */
    private List<WegeCard> findAdjacentCards(int row, int col) {
        List<WegeCard> adjacentCards = new ArrayList<>();
        adjacentCards.add(findTopCard(row, col));
        adjacentCards.add(findRightCard(row, col));
        adjacentCards.add(findBottomCard(row, col));
        adjacentCards.add(findLeftCard(row, col));
        // all null needs to be removed to avoid NullPointerException.
        adjacentCards.removeAll(Collections.singletonList(null));
        return adjacentCards;
    }

    /**
     * Find all card that is possible to form a Gnome group for the given card.
     * Look for the direction where the Gnome is faced at.
     *
     * @param wegeCard the {@link WegeCard} that contains a Gnome
     * @return all members of the Gnome Group. If no member is found, return
     * an empty list.
     * @throws IllegalArgumentException if the given card does not have a Gnome
     *                                  or the card is not played on the board.
     */
    public List<WegeCard> findGnomeGroupMembers(WegeCard wegeCard) {
        CardLocation currentCardLocation = cardLocations.get(wegeCard);
        if (currentCardLocation == null) {
            throw new IllegalArgumentException("The given card is not played yet!");
        }
        if (!wegeCard.hasGnome()) {
            throw new IllegalArgumentException("The given card does not have a Gnome!");
        }
        return findGnomeGroupMembers(currentCardLocation.row(), currentCardLocation.col());
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
                WegeCard topCard = findTopCard(row, col);
                if (isInGnomeGroup(topCard, Pos.BOTTOM_LEFT)) groupMembers.add(topCard);
                WegeCard leftCard = findLeftCard(row, col);
                if (isInGnomeGroup(leftCard, Pos.TOP_RIGHT)) groupMembers.add(leftCard);
            }
            case TOP_RIGHT -> {
                WegeCard topCard = findTopCard(row, col);
                if (isInGnomeGroup(topCard, Pos.BOTTOM_RIGHT)) groupMembers.add(topCard);
                WegeCard rightCard = findRightCard(row, col);
                if (isInGnomeGroup(rightCard, Pos.TOP_LEFT)) groupMembers.add(rightCard);
            }
            case BOTTOM_RIGHT -> {
                WegeCard bottomCard = findBottomCard(row, col);
                if (isInGnomeGroup(bottomCard, Pos.TOP_RIGHT)) groupMembers.add(bottomCard);
                WegeCard rightCard = findRightCard(row, col);
                if (isInGnomeGroup(rightCard, Pos.BOTTOM_LEFT)) groupMembers.add(rightCard);
            }
            case BOTTOM_LEFT -> {
                WegeCard bottomCard = findBottomCard(row, col);
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
     * Find the Top card of the current card.
     *
     * @param row the row on the playing board.
     * @param col the column on the playing board.
     * @return a card on the playing board or null if the card can not be found.
     */
    private WegeCard findTopCard(int row, int col) {
        return findWegeCard(row + 1, col);
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
        return findWegeCard(row - 1, col);
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

    /**
     * Record class to represent location of a card in the playing board (row, col).
     *
     * @param row the row of this location
     * @param col the column of this location
     */
    private record CardLocation(int row, int col) {}

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
