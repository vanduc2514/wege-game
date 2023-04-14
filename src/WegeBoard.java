import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

public class WegeBoard {

    private final WegeDeck wegeDeck;

    private final WegeButton nextCardButton;

    // TODO: bind label with Next Card Button to automatically update when a new card is set.
    private final Label label = new Label();

    private final Label gnomePos = new Label();

    // TODO: Create player, remove temporary solution
    private int firstLandPlaced;

    private WegeGame wegeGame;

    public WegeBoard(int row, int col) {
        wegeDeck = WegeDeck.createDefaultDeck();
        nextCardButton = new WegeButton(100, 100);
        nextCardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (nextCardButton.getCard() == null) {
                WegeCard nextCard = wegeDeck.drawFromFront();
                nextCardButton.setCard(nextCard);
                label.setText(nextCard.getCardType().name());
                if (nextCard.hasGnome()) {
                    label.setText(nextCard.getGnomePosition().name());
                }
            } else {
                nextCardButton.rotate();
            }
        });
        wegeGame = new WegeGame(row, col);
    }

    public GridPane drawBoard() {
        GridPane gridPane = new GridPane();
        FlowPane flowPane = new FlowPane(nextCardButton, label, gnomePos);
        for (int row = 0; row < wegeGame.getHeight(); row++) {
            for (int col = 0; col < wegeGame.getWidth(); col++) {
                WegeBoardButton boardButton = createBoardButton(row, col);
                gridPane.add(boardButton, col, row);
            }
        }
        gridPane.add(flowPane, 0, wegeGame.getHeight() + 1, wegeGame.getWidth(), 1);
        return gridPane;
    }

    private WegeBoardButton createBoardButton(int row, int col) {
        WegeBoardButton boardButton = new WegeBoardButton(row, col);
        boardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (nextCardButton.getCard() == null) return;
            if (firstLandPlaced != 2) {
                ++firstLandPlaced;
                placeCard(boardButton);
                return;
            }
            WegeCard nextCard = nextCardButton.getCard();
            WegeCard cardOnBoard = boardButton.getCard();
            if (nextCard.getCardType() == WegeCard.CardType.BRIDGE
                    && wegeGame.isLegalSwap(cardOnBoard)) {
                swapCard(boardButton);
            } else if (wegeGame.isLegalPlacement(nextCard, boardButton.row, boardButton.col)) {
                placeCard(boardButton);
            }
        });
        return boardButton;
    }

    /**
     * Place a card to a board button then clear the card in the next card button.
     *
     * @param boardButton the button on the playing board to place a card.
     */
    private void placeCard(WegeBoardButton boardButton) {
        setCardOnBoard(boardButton);
        nextCardButton.setCard(null);
    }

    /**
     * Swap a card from the board button with the next card button.
     *
     * @param boardButton the button on the playing board to swap a card
     */
    private void swapCard(WegeBoardButton boardButton) {
        setCardOnBoard(boardButton);
        nextCardButton.setCard(boardButton.getCard());
    }

    /**
     * Set the next card to the playing board.
     *
     * @param boardButton the board button to set the next card.
     */
    private void setCardOnBoard(WegeBoardButton boardButton) {
        WegeCard nextCard = nextCardButton.getCard();
        boardButton.setCard(nextCard);
        wegeGame.placeCard(nextCard, boardButton.row, boardButton.col);
    }

}
