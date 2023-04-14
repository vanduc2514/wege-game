import javafx.util.Pair;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Tracker which tracks the position of a {@link WegeButton} and find the adjacent cards
 * for a given {@link WegeButton} on the playing board.
 */
public class WegeButtonTracker {

    /**
     * The Wege playing board.
     */
    private final WegeButton[][] wegeButtonBoard;

    /**
     * The position (row, col) of Wege Buttons on the playing board {@link #wegeButtonBoard}. This map is used
     * to find the location of a {@link WegeButton} without looping through the playing board.
     */
    private final Map<WegeButton, Pair<Integer, Integer>> wegeButtonPositions;

    /**
     * Create a tracker based on a given playing board
     *
     * @param wegeButtonBoard a 2 dimension array which represents the structure of a playing board
     */
    public WegeButtonTracker(WegeButton[][] wegeButtonBoard) {
        this.wegeButtonBoard = wegeButtonBoard;
        this.wegeButtonPositions = new HashMap<>(wegeButtonBoard.length);
    }

    /**
     * Track the position of a given {@link WegeButton} on the playing board.
     *
     * @param wegeButton the {@link WegeButton} to be tracked.
     * @param rowPos     the row position of this wege button on the playing board.
     * @param colPos     the column position of this wege button on the playing board.
     */
    public void trackWegeButtonPosition(WegeButton wegeButton, int rowPos, int colPos) {
        Pair<Integer, Integer> position = new Pair<>(rowPos, colPos);
        wegeButtonPositions.put(wegeButton, position);
    }

    /**
     * Find all adjacent cards for a given {@link WegeButton}.
     *
     * @param wegeButton a {@link WegeButton} in the playing board.
     * @return a list of {@link WegeCard} placed next to the given wege button. If there is no card
     * available, return an empty list.
     * @throws IllegalArgumentException if the wege button is not present in the playing board.
     */
    public List<WegeCard> findAdjacentCards(WegeButton wegeButton) {
        Pair<Integer, Integer> wegeButtonPosition = wegeButtonPositions.get(wegeButton);
        if (wegeButtonPosition == null)
            throw new IllegalArgumentException("The given button cannot be found on the playing board!");
        final int rowPos = wegeButtonPosition.getKey();
        final int colPos = wegeButtonPosition.getValue();
        List<WegeCard> adjacentCards = new ArrayList<>();
        BiConsumer<Integer, Integer> findAndCollectCards = findAndCollectCardsFunc(adjacentCards);
        findAndCollectCards.accept(rowPos, colPos - 1); // Left
        findAndCollectCards.accept(rowPos, colPos + 1); // Right
        findAndCollectCards.accept(rowPos + 1, colPos); // Top
        findAndCollectCards.accept(rowPos - 1, colPos); // Bottom
        return adjacentCards;
    }

    /**
     * Create a function to find and collect a {@link WegeCard} to the given container.
     *
     * @param container a container to store the cards
     * @return a {@link BiConsumer} which takes the row position and column position to find the card
     * in the playing board. If the card is found, add it the given container.
     */
    private BiConsumer<Integer, Integer> findAndCollectCardsFunc(List<WegeCard> container) {
        return (rowPos, colPos) -> {
            findWegeButtonFromBoard(rowPos, colPos)
                    .map(WegeButton::getCard)
                    .ifPresent(container::add);
        };
    }

    /**
     * Find the {@link WegeButton} from the playing board for the given positions.
     *
     * @param rowPos the row position in the playing board.
     * @param colPos the column position in the playing board.
     * @return an {@link Optional} contain the button and {@link Optional#empty()} if the
     * button is not found in the board.
     * @throws IllegalArgumentException if the indices are negative value.
     */
    private Optional<WegeButton> findWegeButtonFromBoard(int rowPos, int colPos) {
        if (rowPos < 0 || colPos < 0) throw new IllegalArgumentException("Row and Col value cannot be negative number");
        if (rowPos > wegeButtonBoard.length) return Optional.empty();
        if (colPos > wegeButtonBoard[rowPos].length) return Optional.empty();
        return Optional.of(wegeButtonBoard[rowPos][colPos]);
    }

}
