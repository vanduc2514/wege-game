import javafx.geometry.Pos;
import javafx.util.Pair;

import java.util.*;

/**
 * The playing Board of the game Wege.
 */
public class WegeGame {

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
     * The number of row on the playing board
     */
    private final int rows;

    /**
     * The number of column on the playing board
     */
    private final int cols;

    /**
     * Create a new state of the Wege game board.
     *
     * @param rows number of rows of this board.
     * @param cols number of columns of this board.
     */
    public WegeGame(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.playingBoard = new WegeCard[rows][cols];
        this.cardLocations = new HashMap<>(playingBoard.length);
    }

    /**
     * Get the height of the playing board.
     * 
     * @return the number of rows in the playing board.
     */
    public int getHeight() {
        return rows;
    }

    /**
     * Get the width of the playing board.
     *
     * @return the number of columns in the playing board.
     */
    public int getWidth() {
        return cols;
    }

    /**
     * Place given {@link WegeCard} on the playing board.
     *
     * @param wegeCard the {@link WegeCard} to be placed.
     * @param row      the row of this card on the playing board.
     * @param col      the column of this card on the playing board.
     */
    public void placeCard(WegeCard wegeCard, int row, int col) {
        playingBoard[row][col] = wegeCard;
        Pair<Integer, Integer> location = new Pair<>(row, col);
        cardLocations.put(wegeCard, location);
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
     * @param cardOnBoard the card to be swapped
     * @return true if the card on the playing board can be swapped. Otherwise, return false.
     */
    public boolean isLegalSwap(WegeCard cardOnBoard) {
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
        return findAdjacentCards(currentCardLocation.getKey(), currentCardLocation.getValue());
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
        Pair<Integer, Integer> currentCardLocation = cardLocations.get(wegeCard);
        if (currentCardLocation == null) {
            throw new IllegalArgumentException("The given card is not played yet!");
        }
        if (!wegeCard.hasGnome()) {
            throw new IllegalArgumentException("The given card does not have a Gnome!");
        }
        return findGnomeGroupMembers(currentCardLocation.getKey(), currentCardLocation.getValue());
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
                if (topCard.hasGnome() && topCard.getGnomePosition() == Pos.BOTTOM_LEFT) {
                    groupMembers.add(topCard);
                }
                WegeCard leftCard = findLeftCard(row, col);
                if (leftCard.hasGnome() && topCard.getGnomePosition() == Pos.TOP_RIGHT) {
                    groupMembers.add(leftCard);
                }
            }
            case TOP_RIGHT -> {
                WegeCard topCard = findTopCard(row, col);
                if (topCard.hasGnome() && topCard.getGnomePosition() == Pos.BOTTOM_RIGHT) {
                    groupMembers.add(topCard);
                }
                WegeCard rightCard = findRightCard(row, col);
                if (rightCard.hasGnome() && topCard.getGnomePosition() == Pos.TOP_LEFT) {
                    groupMembers.add(rightCard);
                }
            }
            case BOTTOM_RIGHT -> {
                WegeCard bottomCard = findBottomCard(row, col);
                if (bottomCard.hasGnome() && bottomCard.getGnomePosition() == Pos.TOP_RIGHT) {
                    groupMembers.add(bottomCard);
                }
                WegeCard rightCard = findRightCard(row, col);
                if (rightCard.hasGnome() && bottomCard.getGnomePosition() == Pos.BOTTOM_LEFT) {
                    groupMembers.add(rightCard);
                }
            }
            case BOTTOM_LEFT -> {
                WegeCard bottomCard = findBottomCard(row, col);
                if (bottomCard.hasGnome() && bottomCard.getGnomePosition() == Pos.TOP_LEFT) {
                    groupMembers.add(bottomCard);
                }
                WegeCard leftCard = findLeftCard(row, col);
                if (leftCard.hasGnome() && bottomCard.getGnomePosition() == Pos.BOTTOM_RIGHT) {
                    groupMembers.add(leftCard);
                }
            }
        }
        // all null needs to be removed to avoid NullPointerException.
        groupMembers.removeAll(Collections.singletonList(null));
        return groupMembers;
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

}
