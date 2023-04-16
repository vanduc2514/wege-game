package csds132.hw4.game;

import csds132.hw4.model.WegePlayer;

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
     * Get the current player to take this turn.
     *
     * @return the current player.
     */
    public WegePlayer getCurrentPlayer() {
        WegePlayer currentPlayer = playersQueue.pop();
        // Queue for next turn
        playersQueue.add(currentPlayer);
        return currentPlayer;
    }

}
