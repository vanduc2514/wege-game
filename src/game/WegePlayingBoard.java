package game;

import javafx.geometry.Pos;

import java.util.*;
import java.util.function.Function;

/**
 * The data structure (model) for the playing board of the game Wege.
 */
public class WegePlayingBoard {

    /* Maximum cards that can be placed on the game board. */
    private final int maximumCards;

    /* 2 dimension array that represent the current cards played on the game board. */
    private final WegeCard[][] cardsOnBoard;

    /* 2 dimension array that represent intersections where cards connect on the game board. */
    private final Intersection[][] intersectionGrid;

    /* The number of cards had been played so far. */
    private int cardsPlayed;

    /**
     * Create a new game board for the game Wege.
     *
     * @param rows the number of rows for this board.
     * @param cols the number of columns for this board.
     */
    public WegePlayingBoard(int rows, int cols) {
        this.maximumCards = rows * cols;
        this.cardsOnBoard = new WegeCard[rows][cols];
        this.intersectionGrid = createIntersectionGrid(this.cardsOnBoard);
    }

    /**
     * Place a card on the game board at the given location. This update
     * the internal state of the board.
     * 
     * @param card the Wege card to be placed.
     * @param row the row to be placed on the board.
     * @param col the column to be placed on the board.
     */
    public void placeCardOnBoard(WegeCard card, int row, int col) {
        cardsOnBoard[row][col] = card;
        card.setRow(row);
        card.setCol(col);
        cardsPlayed++;
    }

    /**
     * Swap a card on the game board at the given location. This update
     * the internal state of the board.
     *
     * @param card the Wege card to swap with another card on the board.
     * @param row the row to be swapped on the board.
     * @param col the column to be swapped on the board.
     */
    public void swapCardOnBoard(WegeCard card, int row, int col) {
        cardsOnBoard[row][col] = card;
        card.setRow(row);
        card.setCol(col);
    }

    /**
     * Find the top card to a location on the playing board.
     *
     * @param row the row on the board.
     * @param col the column on the board.
     * @return a card on the playing board or <code>null</code>
     * if there is no card at the given location.
     */
    public WegeCard findTopCard(int row, int col) {
        return findWegeCard(row - 1, col);
    }

    /**
     * Find the right card to a location on the playing board.
     *
     * @param row the row on the board.
     * @param col the column on the board.
     * @return a card on the playing board or <code>null</code>
     * if there is no card at the given location.
     */
    public WegeCard findRightCard(int row, int col) {
        return findWegeCard(row, col + 1);
    }

    /**
     * Find the bottom card to a location on the playing board.
     *
     * @param row the row on the board.
     * @param col the column on the board.
     * @return a card on the playing board or <code>null</code>
     * if there is no card at the given location.
     */
    public WegeCard findBottomCard(int row, int col) {
        return findWegeCard(row + 1, col);
    }

    /**
     * Find the left card to a location on the playing board.
     *
     * @param row the row on the board.
     * @param col the column on the board.
     * @return a card on the playing board or <code>null</code>
     * if there is no card at the given location.
     */
    public WegeCard findLeftCard(int row, int col) {
        return findWegeCard(row, col - 1);
    }

    /**
     * Find a card from the playing board at a given location.
     *
     * @param row the row on the board.
     * @param col the column on the board.
     * @return a card on the playing board or <code>null</code>
     * if there is no card at the given location.
     */
    public WegeCard findWegeCard(int row, int col) {
        try {
            return cardsOnBoard[row][col];
        } catch (IndexOutOfBoundsException ignored) {
            return null;
        }
    }

    /**
     * Check if the playing board is filled up with Wege cards.
     *
     * @return <code>true</code> if the board is filled up.
     * Otherwise, return <code>false</code>.
     */
    public boolean isFilledUp() {
        return cardsPlayed == maximumCards;
    }

    //TODO: Heavy refactor
    public List<PlayerStatistic> collectPlayerStatistics() {
        if (!isFilledUp()) {
            throw new IllegalStateException("Playing board is not filled up with all of the cards");
        }
        PlayerStatistic landStatistic = new PlayerStatistic(true);
        PlayerStatistic waterStatistic = new PlayerStatistic(false);
        LinkedList<Intersection> visitQueue = new LinkedList<>();
        Iterator<Intersection> intersectionIterator = createIntersectionGridIterator(intersectionGrid);
        Function<Intersection, PlayerStatistic> selectStatisticFunc = createStatisticFunction(landStatistic, waterStatistic);
        Function<PlayerStatistic, PlayerStatistic> getOtherStatisticFunc = createOtherStatisticFunction(landStatistic, waterStatistic);
        
        //////////////////////////////////////////////// Start travelling.
        Intersection nextIntersection;
        while (!(nextIntersection = intersectionIterator.next()).isCompleted()) {
            PlayerStatistic currentStatistic = selectStatisticFunc.apply(nextIntersection);
            visitQueue.addFirst(nextIntersection);
            List<Intersection> visitedIntersections = new ArrayList<>();
            int gnomeGroup = 0;
            while (!visitQueue.isEmpty()) {
                // remove front of linked list
                Intersection current = visitQueue.pop();
                // Set to true
                current.setVisited(true);
                visitedIntersections.add(current);
                // For each of the cards, follow the path and count gnomeGroup
                for (WegeCard card : findCardsAtIntersection(current.getX(), current.getY())) {
                    Pos startPos = findPosOnCard(card, current);
                    // Count up Gnome at the intersection.
                    if (card.hasGnome() && card.getGnomePosition() == startPos) {
                        gnomeGroup++;
                    }
                    if (sameKind(card, currentStatistic) || card.getCardType() == WegeCard.CardType.BRIDGE) {
                        Pos endPos = getDiagonalOppositePosition(startPos);
                        Intersection intersection = findIntersectionOnCard(card, endPos);
                        if (!intersection.isVisited()) visitQueue.addFirst(intersection);
                    }
                }
            }

            Set<Pos> edges = new HashSet<>();
            for (Intersection visited : visitedIntersections) {
                Pos edgePosition = findEdgePosition(visited, intersectionGrid[0].length - 1, intersectionGrid.length - 1);
                if (edgePosition != null) edges.add(edgePosition);
                visited.setCompleted(true);
            }
            if (edges.isEmpty()) {

            } else {

            }
            
        }
        return null;
    }

    private Intersection findIntersectionOnCard(WegeCard card, Pos endPos) {
        return null;
    }

    /**
     * Find the edge position on the grid of an intersection if it's on and edge.
     *
     * @param intersection the intersection to find.
     * @param endX the end coordinate of x-axis on the grid.
     * @param endY the end coordinate of y-axis on the grid.
     * @return the edge position on the grid, or <code>null</code>
     * if the intersection is not on an edge.
     */
    private Pos findEdgePosition(Intersection intersection, int endX, int endY) {
        if (intersection.getX() > 0 && intersection.getY() == 0) {
            return Pos.CENTER_LEFT;
        } else if (intersection.getX() == 0 && intersection.getY() > 0) {
            return Pos.TOP_CENTER;
        } else if (intersection.getX() > 0 && intersection.getY() == endY) {
            return Pos.CENTER_RIGHT;
        } else if (intersection.getX() == endX) {
            return Pos.BOTTOM_CENTER;
        }
        return null;
    }

    /**
     * Create a function which return the statistic of the other player.
     *
     * @param landStatistic the statistic for land player.
     * @param waterStatistic the statistic for water player.
     * @return a function if apply to the statistic of the land player, return the statistic
     * of the water player and vice-versa.
     */
    private Function<PlayerStatistic, PlayerStatistic> createOtherStatisticFunction(
            PlayerStatistic landStatistic,
            PlayerStatistic waterStatistic) {
        return statistic -> {
            if (statistic == landStatistic) return waterStatistic;
            return landStatistic;
        };
    }

    /**
     * Create a function to determine which is the statistic base on the
     * type of the current intersection. If it's a land, return statistic belong to 
     * the land player. Otherwise, return statistic belong to the water player.
     * 
     * @param landStatistic the statistic for land player.
     * @param waterStatistic the statistic for water player.
     * @return a function which can apply to an intersection and determine which 
     * is the statistic needed.
     */
    private Function<Intersection, PlayerStatistic> createStatisticFunction(
            PlayerStatistic landStatistic,
            PlayerStatistic waterStatistic) {
        return intersection -> {
            WegeCard topLeftCard = findTopLeftCard(intersection.getX(), intersection.getY());
            Pos locationOnCard = findPosOnCard(topLeftCard, intersection);
            if (topLeftCard.isLand(locationOnCard)) return landStatistic;
            else return waterStatistic;
        };
    }

    /**
     * Find all the cards that made up this intersection in clockwise manner.
     *
     * @param x the x coordinate of this intersection on the grid.
     * @param y the y coordinate of this intersection on the grid.
     * @return all cards that made up this intersection.
     */
    private Set<WegeCard> findCardsAtIntersection(int x, int y) {
        // Since the corner and edge intersections can have duplicate cards.
        Set<WegeCard> cards = new HashSet<>();
        // top left
        cards.add(findTopLeftCard(x, y));
        // top right
        cards.add(cardsOnBoard[getCoordinateInBoard(x - 1)][y]);
        // bottom right
        cards.add(cardsOnBoard[x][y]);
        // bottom left
        cards.add(cardsOnBoard[x][getCoordinateInBoard(y - 1)]);
        return cards;
    }

    private WegeCard findTopLeftCard(int x, int y) {
        return cardsOnBoard[getCoordinateInBoard(x - 1)][getCoordinateInBoard(y - 1)];
    }

    ///////////////////////// Helper methods. ///////////////////////////

    /**
     * Get the coordinate within the game board boundary.
     */
    static int getCoordinateInBoard(int coordinate) {
        // If the given coordinate is out of boundary,
        // return the nearest coordinate.
        return Math.max(0, coordinate);
    }

    /**
     * find the relative position of an intersection on a card.
     * 
     * @param card the card contains this intersection.
     * @param intersection the intersection.
     * @return the relative position or null if the intersection is not belong to a card.
     */
    static Pos findPosOnCard(WegeCard card, Intersection intersection) {
        return findPosOnCard(
                card.getRow(), 
                card.getCol(),
                intersection.getX(),
                intersection.getY());
    }

    /**
     * find the relative position of an intersection coordinate
     * to a card location.
     * 
     * @param row the row of the card on the game board.
     * @param col the column of the card on the game board.
     * @param x the x coordinate on the intersection grid.
     * @param y the y coordinate on the intersection grid.
     * @return the relative position or null if the intersection is not belong to a card.
     */
    static Pos findPosOnCard(int row, int col, int x, int y) {
        if (row == x && col == y) return Pos.TOP_LEFT;
        if (row == x && col + 1 == y) return Pos.TOP_RIGHT;
        if (row + 1 == x && col + 1 == y) return Pos.BOTTOM_RIGHT;
        if (row + 1 == x && col == y) return Pos.BOTTOM_LEFT;
        return null;
    }

    /**
     * Check if a card is belonged to the current statistic.
     *
     * @param card a card
     * @param currentStatistic the statistic to evaluate.
     * @return <code>true</code> if the card is a land card and the statistic for land player or
     * if the card is a water card and the statistic for the water player. Otherwise, return
     * <code>false</code>
     */
    static boolean sameKind(WegeCard card, PlayerStatistic currentStatistic) {
        if (currentStatistic.isLandPlayer()
                && card.getCardType() == WegeCard.CardType.LAND) return true;
        return !currentStatistic.isLandPlayer()
                && card.getCardType() == WegeCard.CardType.WATER;
    }

    /**
     * Return the opposite position in diagonal.
     * 
     * @param pos a position to get the opposite.
     * @return the diagonal opposite position.
     */
    static Pos getDiagonalOppositePosition(Pos pos) {
        return switch (pos) {
            case TOP_LEFT -> Pos.BOTTOM_RIGHT;
            case TOP_RIGHT -> Pos.BOTTOM_LEFT;
            case BOTTOM_RIGHT -> Pos.TOP_LEFT;
            case BOTTOM_LEFT -> Pos.TOP_RIGHT;
            default -> null;
        };
    }

    /**
     * Create the grid which represent connection of cards on the game board.
     *
     * @param cardsOnBoard the 2 dimension array of cards on the game board.
     * @return an 2 dimension array of intersection. An intersection represent where the corners
     * of the cards connect. This includes the board corners as "intersections" even though there
     * is only one card there at that corner.
     */
    static Intersection[][] createIntersectionGrid(WegeCard[][] cardsOnBoard) {
        Intersection[][] grid = new Intersection[cardsOnBoard.length + 1][cardsOnBoard[0].length + 1];
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                grid[x][y] = new Intersection(x, y);
            }
        }
        return grid;
    }

    /**
     * Create a special circular iterator that travel from the x-axis of the grid to y-axis
     * of the grid. If it reaches the end of the grid, come back to the starting point and
     * travel again.
     * 
     * @param grid the grid of intersections
     * @return a circular interator.
     */
    Iterator<Intersection> createIntersectionGridIterator(Intersection[][] grid) {
        return new Iterator<>() {

            private final int endX = grid.length - 1;

            private final int endY = grid[0].length - 1;

            private int x = 0;

            private int y = 0;

            /**
             * Since it's circular, so it always has the next element. 
             */
            @Override
            public boolean hasNext() {
                return true;
            }

            /**
             * Return the intersection from the start of the x-axis 
             * then move to the next coordinate on the x-axis. If we
             * reach the end, come back to the start on the x-axis, move
             * to the next coordinate on the y-axis and travel again. If 
             * we reach the end, come back to the start on the y-axis.
             */
            @Override
            public Intersection next() {
                Intersection intersection = grid[x][y];
                if (x == endX) {
                    x = 0;
                    if (y == endY) {
                        y = 0;
                    } else {
                        y++;
                    }
                } else {
                    x++;
                }
                return intersection;
            }
        };
    }
    
}
