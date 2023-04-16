package csds132.hw4.game;

/* Exception for the move is not illegal */
public class IllegalMoveException extends Exception {

    /**
     * Create an exception with a message.
     */
    public IllegalMoveException(String message) {
        super(message);
    }

}
