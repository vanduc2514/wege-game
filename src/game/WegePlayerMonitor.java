package game;

import java.util.Deque;

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
