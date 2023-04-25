package game;

import javafx.geometry.Pos;

import java.util.*;

/**
 * The data structure (model) for the playing board of the game Wege.
 */
public class WegePlayingBoard {

    /* 2 dimension array that represent the current cards played on the game board. */
    private final WegePlayingCard[][] cardsOnBoard;

    /* 2 dimension array that represent intersections where cards connect on the game board. */
    private final Intersection[][] intersectionGrid;

    /* The maximum x of the intersection grid on the game board. */
    private final int maxX;

    /* The maximum y of the intersection grid on the game board. */
    private final int maxY;

    /**
     * Create a new game board for the game Wege.
     *
     * @param rows the number of rows for this board.
     * @param cols the number of columns for this board.
     */
    public WegePlayingBoard(int rows, int cols) {
        this.cardsOnBoard = new WegePlayingCard[rows][cols];
        this.intersectionGrid = new Intersection[rows + 1][cols + 1];
        this.maxX = intersectionGrid.length - 1;
        this.maxY = intersectionGrid[0].length - 1;
    }

    /**
     * Place a card on the game board at the given location and create
     * the intersection surrounds it, used for checking the connection point
     * of adjacent cards.
     * 
     * @param card the Wege card to be placed.
     */
    public void placeCardOnBoard(WegePlayingCard card) {
        int row = card.getRow();
        int col = card.getCol();
        cardsOnBoard[row][col] = card;
        List<Intersection> surroundIntersections = getAssociateIntersections(card);
        for (Intersection intersection : surroundIntersections) {
            intersection.connectLand(card.isLand(intersection));
            if (card.isGnome(intersection)) {
                intersection.increaseFacingGnomeCount();
            }
        }
    }

    /**
     * Find a card from the playing board at a given location.
     *
     * @param row the row on the board.
     * @param col the column on the board.
     * @return a card on the playing board or <code>null</code>
     * if there is no card at the given location.
     */
    public WegePlayingCard findPlayedCard(int row, int col) {
        try {
            return cardsOnBoard[row][col];
        } catch (IndexOutOfBoundsException ignored) {
            return null;
        }
    }

    /**
     * Find the first intersection which can be used for card connection
     * around a location on the game board.
     *
     * @param row the row on the playing board.
     * @param col the column on the playing board.
     * @return the first intersection which connect to an existing card on
     * the game board. If the location does not have any adjacent cards,
     * return <code>null</code>
     * @see #placeCardOnBoard(WegePlayingCard) associate intersection to a card
     * when it's placed on the game board.
     */
    public Intersection findFirstConnection(int row, int col) {
        int pointContact = 0;
        Intersection firstFound = null;
        for (int x = row; x <= row + 1; x++) {
            for (int y = col; y <= col + 1; y++) {
                Intersection intersection = intersectionGrid[x][y];
                if (intersection != null) {
                    firstFound = intersection;
                    pointContact++;
                }
            }
        }
        // At least two points need to connect
        if (pointContact >= 2) return firstFound;
        return null;
    }

    /**
     * Find the first intersection in the grid which is not completed for collecting
     * player statistic.
     *
     * @return the first not completed intersection or <code>null</code> if all
     * intersections are completed.
     */
    public Intersection findFirstNotCompletedIntersection() {
        for (int x = 0; x < intersectionGrid.length; x++) {
            for (int y = 0; y < intersectionGrid[x].length; y++) {
                Intersection intersection = intersectionGrid[x][y];
                if (intersection != null && !intersection.isCompleted()) {
                    return intersection;
                }
            }
        }
        return null;
    }

    /**
     * Find {@link Pos#TOP_LEFT}, {@link Pos#TOP_RIGHT},
     * {@link Pos#BOTTOM_LEFT}, {@link Pos#BOTTOM_RIGHT}
     * intersections which surround a card on the game board.
     *
     * @param card the card played on the game board.
     * @return intersections surrounding that location or an empty list
     * if the card is not being played before.
     */
    public List<Intersection> findSurroundIntersections(WegePlayingCard card) {
        List<Intersection> intersections = new ArrayList<>();
        for (int x = 0; x <= card.getRow() + 1; x++) {
            for (int y = 0; y <= card.getCol() + 1; y++) {
                Intersection intersection = intersectionGrid[x][y];
                if (intersection != null) intersections.add(intersection);
            }
        }
        return intersections;
    }

    /**
     * Find all cards that made up this intersection. Card at corner and edge
     * of the game board is still considered as valid.
     *
     * @param intersection the intersection.
     * @return all surrounds card.
     */
    public List<WegePlayingCard> findSurroundCards(Intersection intersection) {
        Set<WegePlayingCard> cards = new HashSet<>();
        int x = intersection.getX();
        int y = intersection.getY();
        for (int row = x - 1; row < inBoundary(x + 1, maxX); row++) {
            for (int col = y - 1; col < inBoundary(y + 1, maxY); col++) {
                WegePlayingCard card = cardsOnBoard[inBoundary(row, 0)][inBoundary(col, 0)];
                if (card != null) cards.add(card);
            }
        }
        return new ArrayList<>(cards);
    }

    /**
     * Find the opposite intersection of an intersection based on a direction.
     *
     * @param intersection an intersection
     * @param direction the direction to find the opposite intersection.
     * @return the opposite diagonal intersection.
     */
    public Intersection findOppositeIntersection(Intersection intersection, Pos direction) {
        int x = intersection.getX();
        int y = intersection.getY();
        return switch (direction) {
            case TOP_LEFT -> intersectionGrid
                    [inBoundary(x - 1, 0)]
                    [inBoundary(y - 1, 0)];
            case TOP_RIGHT -> intersectionGrid
                    [inBoundary(x - 1, 0)]
                    [inBoundary(y + 1, maxY)];
            case BOTTOM_RIGHT -> intersectionGrid
                    [inBoundary(x + 1, maxX)]
                    [inBoundary(y + 1, maxY)];
            case BOTTOM_LEFT -> intersectionGrid
                    [inBoundary(x + 1, maxX)]
                    [inBoundary(y - 1, 0)];
            default -> throw new IllegalArgumentException("Position is not valid!");
        };
    }

    /**
     * Count how many edges that an intersection touch on the game board.
     * It could be Left corner, Right corner, Bottom corner and Top corner.
     *
     * @param intersections a list of intersection which forms a trail
     *                      among cards.
     * @return the number edges touched from the list of intersection.
     */
    public int countEdgeTouch(List<Intersection> intersections) {
        Set<Pos> edges = new HashSet<>();
        for (Intersection intersection : intersections) {
            int x = intersection.getX();
            int y = intersection.getY();
            if (x == 0) edges.add(Pos.TOP_CENTER);
            else if (x == maxX) edges.add(Pos.BOTTOM_CENTER);
            else if (y == 0) edges.add(Pos.CENTER_LEFT);
            else if (y == maxY) edges.add(Pos.CENTER_RIGHT);
        }
        return edges.size();
    }

    /**
     * Get a snapshot of the internal game board structure.
     * Use only for unit test.
     *
     * @return a copy array of {@link #cardsOnBoard}
     */
    WegePlayingCard[][] getCardsOnBoard() {
        return copy2DArray(cardsOnBoard);
    }

    /**
     * Get a snapshot of the internal intersection grid structure.
     * Use only for unit test.
     *
     * @return a copy array of {@link #intersectionGrid}
     */
    Intersection[][] getIntersectionGrid() {
        return copy2DArray(intersectionGrid);
    }

    /**
     * Create an intersection at the given coordinate.
     * Use only for unit test.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return the created intersection.
     */
    Intersection createIntersection(int x, int y) {
        Intersection intersection = new Intersection(x, y);
        intersectionGrid[x][y] = intersection;
        return intersection;
    }

    /**
     * Get intersections that associate with a card on the game board.
     * If there is no intersection associated before, initialize a new intersection
     * and add it to the result list.
     *
     * @param card the card on the game board.
     * @return the surround intersections that associate to a card.
     */
    private List<Intersection> getAssociateIntersections(WegePlayingCard card) {
        int row = card.getRow();
        int col = card.getCol();
        List<Intersection> intersections = new ArrayList<>();
        for (int x = row; x <= row + 1; x++) {
            for (int y = col; y <= col + 1; y++) {
                Intersection intersection = intersectionGrid[x][y];
                if (intersection == null) {
                    intersection = new Intersection(x, y);
                    intersectionGrid[x][y] = intersection;
                }
                intersections.add(intersection);
            }
        }
        return intersections;
    }

    /**
     * Get the coordinate within the game board boundary.
     */
    private int inBoundary(int coordinate, int boundary) {
        // If the given coordinate is out of boundary,
        // return the nearest coordinate.
        if (boundary == 0) return Math.max(coordinate, boundary);
        return Math.min(coordinate, boundary);
    }

    /**
     * Copy a 2 dimension array.
     *
     * @param source the source array.
     * @return the copy array.
     * @param <T> type of the source array.
     */
    private <T> T[][] copy2DArray(T[][] source) {
        return Arrays.stream(source)
                .map(row -> Arrays.stream(row).toArray(size -> row.clone()))
                .toArray(size -> source.clone());
    }
}
