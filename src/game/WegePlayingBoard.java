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
    private final WegePlayingCard[][] cardsOnBoard;

    private final int maxX;

    private final int maxY;

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
        this.cardsOnBoard = new WegePlayingCard[rows][cols];
        this.intersectionGrid = new Intersection[rows + 1][cols + 1];
        this.maxX = intersectionGrid.length - 1;
        this.maxY = intersectionGrid[0].length - 1;
    }

    /**
     * Place a card on the game board at the given location. This update
     * the internal state of the board.
     * 
     * @param card the Wege card to be placed.
     */
    public void placeCardOnBoard(WegePlayingCard card) {
        int row = card.getRow();
        int col = card.getCol();
        cardsOnBoard[row][col] = card;
        List<Intersection> surroundIntersections = getSurroundIntersections(row, col);
        for (Intersection intersection : surroundIntersections) {
            intersection.setLand(card.isLand(intersection));
            if (card.isGnome(intersection)) {
                intersection.increaseFacingGnomeCount();
            }
        }
        cardsPlayed++;
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
     * Check if the playing board is filled up with Wege cards.
     *
     * @return <code>true</code> if the board is filled up.
     * Otherwise, return <code>false</code>.
     */
    public boolean isFilledUp() {
        return cardsPlayed == maximumCards;
    }

    public Intersection findFirstIntersection(int row, int col) {
        for (int x = row; x <= row + 1; x++) {
            for (int y = col; y <= col + 1; y++) {
                Intersection intersection = intersectionGrid[x][y];
                if (intersection != null) {
                    return intersection;
                }
            }
        }
        return null;
    }

    // Return null if all is completed.
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
     * intersections that presented on the intersection grid from
     * a location on the playing board.
     *
     * @param x the x of the card on the playing board.
     * @param y the column of the card on the playing board.
     * @return intersections surrounding that location or an empty list
     * if the intersections have not been played.
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

    public Intersection findOppositePoint(Intersection startPoint, Pos direction) {
        int x = startPoint.getX();
        int y = startPoint.getY();
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

    private List<Intersection> getSurroundIntersections(int row, int col) {
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
    private static int inBoundary(int coordinate, int boundary) {
        // If the given coordinate is out of boundary,
        // return the nearest coordinate.
        if (boundary == 0) return Math.max(coordinate, boundary);
        return Math.min(coordinate, boundary);
    }

}
