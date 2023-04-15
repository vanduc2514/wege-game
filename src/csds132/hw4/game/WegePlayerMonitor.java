package csds132.hw4.game;

import csds132.hw4.model.WegePlayer;

import java.util.Deque;

public class WegePlayerMonitor {

    private Deque<WegePlayer> playersQueue;

    public WegePlayerMonitor(Deque<WegePlayer> playersQueue) {
        this.playersQueue = playersQueue;
    }

    public WegePlayer getCurrentPlayer() {
        return playersQueue.peek();
    }

    public WegePlayer getNextPlayer() {
        WegePlayer nextPlayer = playersQueue.pop();
        // Queue for next turn
        playersQueue.add(nextPlayer);
        return nextPlayer;
    }

}
