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

    private WegePlayerMonitor wegePlayerMonitor;

    private int cardsPlayed;

    private final int totalCards;

    /**
     * Create a new master for the game Wege.
     *
     * @param rows number of rows for the playing board.
     * @param cols number of columns for the playing board.
     */
    public WegeGameMaster(int rows, int cols) {
        this.totalCards = rows * cols;
        this.playingBoard = new WegeCard[rows][cols];
        // The number of intersections is plus 1 to cover all the board,
        this.boardIntersections = new Intersection[rows + 1][cols + 1];
        for (int row = 0; row < rows + 1; row++) {
            for (int col = 0; col < cols + 1; col++) {
                Intersection intersection = new Intersection(new Location(row, col));
                this.allIntersections.add(intersection);
                boardIntersections[row][col] = intersection;
            }
        }
        // TODO: Refactor.
        WegePlayer.WegePlayerBuilder builder = new WegePlayer.WegePlayerBuilder(this);
        WegePlayer waterPlayer = builder.buildPlayer(WegePlayer.PlayerType.WATER);
        WegePlayer landPlayer = builder.buildPlayer(WegePlayer.PlayerType.LAND);
        LinkedList<WegePlayer> players = new LinkedList<>();
        players.add(waterPlayer);
        players.add(landPlayer);
        wegePlayerMonitor = new WegePlayerMonitor(players);
    }

    public boolean isGameEnd() {
        return cardsPlayed >= totalCards;
    }



    /**
     * Place given {@link WegeCard} on the playing board.
     *
     * @param wegeCard   the {@link WegeCard} to be placed.
     * @param row        the row of this card on the playing board.
     * @param col        the column of this card on the playing board.
     */
    public void trackPlayedCard(WegeCard wegeCard, int row, int col) {
        WegeCard card = playingBoard[row][col];
        playingBoard[row][col] = wegeCard;
        cardsPlayed++;
        var intersection = findAllIntersection(row, col);
        if (card != null) {
            // TODO: If the card is bridge, remove all references.
            intersection.keySet().forEach(e -> {
                e.getCards().removeIf(c -> c == card);
            });
        }
        intersection.forEach((k , v) -> {
            if (wegeCard.isLand(v)) {
                k.setIntersectionType(WegeCard.CardType.LAND);
            } else {
                k.setIntersectionType(WegeCard.CardType.WATER);
            }
        });
        wegeCard.setIntersections(intersection);
        intersection.keySet().forEach(i -> i.getCards().add(wegeCard));
    }

    /**
     * Find {@link Pos#TOP_LEFT}, {@link Pos#TOP_RIGHT}, {@link Pos#BOTTOM_RIGHT}, {@link Pos#BOTTOM_LEFT}
     * Intersections of a given location of a wege card.
     *
     * @param row the row of the card on the playing board.
     * @param col the column of the card on the playing board.
     * @return all Intersections relate to this card.
     */
    private Map<Intersection, Pos> findAllIntersection(int row, int col) {
        return Map.ofEntries(
                Map.entry(boardIntersections[row][col], Pos.TOP_LEFT),
                Map.entry(boardIntersections[row][col + 1], Pos.TOP_RIGHT),
                Map.entry(boardIntersections[row + 1][col], Pos.BOTTOM_LEFT),
                Map.entry(boardIntersections[row + 1][col + 1], Pos.BOTTOM_RIGHT)
        );
    }

    public WegePlayer.Move nextMove(WegeCard nextCard, int row, int col) throws IllegalMoveException {
        WegeCard wegeCard = findWegeCard(row, col);
        // If there is no card, it's a placement
        // otherwise, it's a swap
        if (wegeCard == null && isLegalRegularCardMove(nextCard, row, col)) {
            return WegePlayer.Move.PLACE;
        } else if (wegeCard != null
                && wegeCard.getCardType() == WegeCard.CardType.BRIDGE
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

    public WegePlayer getCurrentPlayer() {
        return wegePlayerMonitor.getCurrentPlayer();
    }

    public WegePlayer getQueuePlayer() {
        return wegePlayerMonitor.getQueuePlayer();
    }

    public void endGame() {
        LinkedList<Intersection> temp = new LinkedList<>();
        Iterator<Intersection> intersectionIterator = allIntersections.iterator();
        Intersection boardIntersection;
        while (!(boardIntersection = intersectionIterator.next()).isCompleted()) {
            // Add not completed intersection to the front of temp linked list
            WegeCard.CardType intersectionType = boardIntersection.getIntersectionType();
            temp.add(boardIntersection);
            List<Intersection> visitedIntersections = new ArrayList<>();
            int gnomeGroup = 0;
            while (!temp.isEmpty()) {
                // remove front of linked list
                Intersection current = temp.pop();
                // Set to true
                current.setVisited(true);
                visitedIntersections.add(current);
                // For each of the cards, follow the path and count gnomeGroup
                for (WegeCard card : current.getCards()) {
                    // Count up Gnome at the intersection.
                    if (card.hasGnome() && card.getGnomePosition() == card.getPosition(current)) {
                        gnomeGroup++;
                    }
                    if (card.getCardType() == intersectionType
                            || card.getCardType() == WegeCard.CardType.BRIDGE) {
                        Pos intersectionPos = card.getPosition(current);
                        // Check if the diagonal is also water
                        Pos end = getDiagonalPos(intersectionPos);
                        Intersection intersection = card.getIntersection(end);
                        if (!intersection.isVisited()) temp.add(intersection);
                    }
                }
            }
            // 3. Once the list is empty, look for all the intersections with visitedIntersections
            // set to true but completed set to false.
            Set<Pos> edges = new HashSet<>();
            for (Intersection visited : visitedIntersections) {
                // Which edge this intersection is at ?
                Location location = visited.getLocation();
                if (location.row() > 0 && location.col() == 0) {
                    edges.add(Pos.CENTER_LEFT);
                } else if (location.row() == 0 && location.col() > 0) {
                    edges.add(Pos.TOP_CENTER);
                } else if (location.row() > 0 && location.col() == playingBoard[0].length - 1) {
                    edges.add(Pos.CENTER_RIGHT);
                } else if (location.row() == playingBoard.length - 1) {
                    edges.add(Pos.BOTTOM_CENTER);
                }
                visited.setCompleted(true);
            }

            // Calculate score base on edge touch and no edge touch
            // base on the intersection.

            if (intersectionType == WegeCard.CardType.LAND) {
                // Calculate score for land.
                // This includes sides touch for this section,
                // Number of pond / land
                // and gnomeGroup count at this section.
                System.out.printf("%s sides connected \n", edges.size());
                System.out.printf("%s ponds created \n", "0");
                System.out.printf("intersections of %d gnomes \n", gnomeGroup);
                System.out.printf("Cossack played: %d \n", 0);
            } else {
                // Calculate score for water.
            }
        }
    }

    public Pos getDiagonalPos(Pos pos) {
        return switch (pos) {
            case TOP_LEFT -> Pos.BOTTOM_RIGHT;
            case TOP_RIGHT -> Pos.BOTTOM_LEFT;
            case BOTTOM_RIGHT -> Pos.TOP_LEFT;
            case BOTTOM_LEFT -> Pos.TOP_RIGHT;
            default -> null;
        };
    }

    /**
     * Monitor the turn of the player of the game Wege.
     */
    public class WegePlayerMonitor {

        /* The queue containing order of players */
        private Deque<WegePlayer> playersQueue;

        /**
         * Create a new monitor
         *
         * @param playersQueue initial queue of players.
         */
        public WegePlayerMonitor(Deque<WegePlayer> playersQueue) {
            this.playersQueue = playersQueue;
        }

        /**
         * Get the player queue for the next turn. This player
         * is not removed from the queue.
         *
         * @return the player queue for the next turn.
         */
        public WegePlayer getQueuePlayer() {
            return playersQueue.peek();
        }

        /**
         * Get the player to take this turn. The player is then removed
         * from the top of the queue, and is queued for the next turn.
         *
         * @return the current player for this turn.
         */
        public WegePlayer getCurrentPlayer() {
            WegePlayer currentPlayer = playersQueue.pop();
            // Queue for next turn
            playersQueue.add(currentPlayer);
            return currentPlayer;
        }

    }
}
