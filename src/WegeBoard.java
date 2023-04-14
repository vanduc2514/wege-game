import javafx.geometry.Pos;
import javafx.util.Pair;

import java.util.*;

/**
 * The playing Board of the game Wege.
 */
public class WegeBoard {

    /**
     * The 2 dimension Wege playing board.
     */
    private final WegeCard[][] playingBoard;

    /**
     * The location (row, col) of Wege Cards on {@link #playingBoard}. This map is used
     * to find the location of a {@link WegeCard} without looping through the playing board.
     */
    private final Map<WegeCard, Pair<Integer, Integer>> cardLocations;

    /**
     * Create a new state of the Wege game board.
     *
     * @param numberOfRows number of rows of this board.
     * @param numberOfCols number of columns of this board.
     */
    public WegeBoard(int numberOfRows, int numberOfCols) {
        this.playingBoard = new WegeCard[numberOfRows][numberOfCols];
        this.cardLocations = new HashMap<>(playingBoard.length);
    }

    /**
     * Track the location of a given {@link WegeCard} on the playing board.
     *
     * @param wegeCard the {@link WegeCard} to be tracked.
     * @param row      the row of this card on the playing board.
     * @param col      the column of this card on the playing board.
     */
    public void trackWegeCardLocation(WegeCard wegeCard, int row, int col) {
        playingBoard[row][col] = wegeCard;
        Pair<Integer, Integer> location = new Pair<>(row, col);
        cardLocations.put(wegeCard, location);
    }

    /**
     * Find all adjacent cards for a given {@link WegeCard}.
     *
     * @param wegeCard a {@link WegeCard} in the playing board.
     * @return all of {@link WegeCard} that is placed next to the given wege card.
     * If there is no card available, return an empty list.
     * @throws IllegalArgumentException if the wege card is not played on the board.
     */
    public List<WegeCard> findAdjacentCards(WegeCard wegeCard) {
        Pair<Integer, Integer> currentCardLocation = cardLocations.get(wegeCard);
        if (currentCardLocation == null)
            throw new IllegalArgumentException("The given card is not played yet!");
        List<WegeCard> adjacentCards = new ArrayList<>();
        adjacentCards.add(findTopCard(currentCardLocation));
        adjacentCards.add(findRightCard(currentCardLocation));
        adjacentCards.add(findBottomCard(currentCardLocation));
        adjacentCards.add(findLeftCard(currentCardLocation));
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
        Pair<Integer, Integer> currentCardLocation = cardLocations.get(wegeCard);
        if (currentCardLocation == null)
            throw new IllegalArgumentException("The given card is not played yet!");
        try {
            List<WegeCard> groupMembers = new ArrayList<>();
            switch (wegeCard.getGnomePosition()) {
                case TOP_LEFT -> {
                    WegeCard topCard = findTopCard(currentCardLocation);
                    if (topCard.hasGnome() && topCard.getGnomePosition() == Pos.BOTTOM_LEFT) {
                        groupMembers.add(topCard);
                    }
                    WegeCard leftCard = findLeftCard(currentCardLocation);
                    if (leftCard.hasGnome() && topCard.getGnomePosition() == Pos.TOP_RIGHT) {
                        groupMembers.add(leftCard);
                    }
                }
                case TOP_RIGHT -> {
                    WegeCard topCard = findTopCard(currentCardLocation);
                    if (topCard.hasGnome() && topCard.getGnomePosition() == Pos.BOTTOM_RIGHT) {
                        groupMembers.add(topCard);
                    }
                    WegeCard rightCard = findRightCard(currentCardLocation);
                    if (rightCard.hasGnome() && topCard.getGnomePosition() == Pos.TOP_LEFT) {
                        groupMembers.add(rightCard);
                    }
                }
                case BOTTOM_RIGHT -> {
                    WegeCard bottomCard = findBottomCard(currentCardLocation);
                    if (bottomCard.hasGnome() && bottomCard.getGnomePosition() == Pos.TOP_RIGHT) {
                        groupMembers.add(bottomCard);
                    }
                    WegeCard rightCard = findRightCard(currentCardLocation);
                    if (rightCard.hasGnome() && bottomCard.getGnomePosition() == Pos.BOTTOM_LEFT) {
                        groupMembers.add(rightCard);
                    }
                }
                case BOTTOM_LEFT -> {
                    WegeCard bottomCard = findBottomCard(currentCardLocation);
                    if (bottomCard.hasGnome() && bottomCard.getGnomePosition() == Pos.TOP_LEFT) {
                        groupMembers.add(bottomCard);
                    }
                    WegeCard leftCard = findLeftCard(currentCardLocation);
                    if (leftCard.hasGnome() && bottomCard.getGnomePosition() == Pos.BOTTOM_RIGHT) {
                        groupMembers.add(leftCard);
                    }
                }
            }
            ;
            // all null needs to be removed to avoid NullPointerException.
            groupMembers.removeAll(Collections.singletonList(null));
            return groupMembers;
        } catch (NoSuchElementException ignored) {
            throw new IllegalArgumentException("The given card does not have a Gnome!");
        }
    }

    /**
     * Find the Top card of the current card.
     *
     * @param currentCardLocation the location of the current card. Key is the row
     *                            and Value is the col
     * @return a card on the playing board or null if the card can not be found.
     */
    private WegeCard findTopCard(Pair<Integer, Integer> currentCardLocation) {
        return findWegeCard(currentCardLocation.getKey() + 1, currentCardLocation.getValue());
    }

    /**
     * Find the Right card of the current card.
     *
     * @param currentCardLocation the location of the current card. Key is the row
     *                            and Value is the col
     * @return a card on the playing board or null if the card can not be found.
     */
    private WegeCard findRightCard(Pair<Integer, Integer> currentCardLocation) {
        return findWegeCard(currentCardLocation.getKey(), currentCardLocation.getValue() + 1);
    }

    /**
     * Find the Bottom card of the current card.
     *
     * @param currentCardLocation the location of the current card. Key is the row
     *                            and Value is the col
     * @return a card on the playing board or null if the card can not be found.
     */
    private WegeCard findBottomCard(Pair<Integer, Integer> currentCardLocation) {
        return findWegeCard(currentCardLocation.getKey() - 1, currentCardLocation.getValue());
    }

    /**
     * Find the Left card of the current card.
     *
     * @param currentCardLocation the location of the current card. Key is the row
     *                            and Value is the col
     * @return a card on the playing board or null if the card can not be found.
     */
    private WegeCard findLeftCard(Pair<Integer, Integer> currentCardLocation) {
        return findWegeCard(currentCardLocation.getKey(), currentCardLocation.getValue() - 1);
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

}
