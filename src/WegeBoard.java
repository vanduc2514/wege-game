import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

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
//        wegeBoard = createWegeBoard(row, col);
        wegeGame = new WegeGame(row, col);
    }

    public GridPane drawBoard() {
        GridPane gridPane = new GridPane();
        FlowPane flowPane = new FlowPane(nextCardButton, label, gnomePos);

        for (int row = 0; row < wegeGame.getHeight(); row++) {
            for (int col = 0; col < wegeGame.getWidth(); col++) {
                final Pair<Integer, Integer> boardLocation = new Pair<>(row, col);
                WegeButton boardButton = new WegeButton(100, 100);
                boardButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                        new WegeButtonBoardHandler(new Pair<>(row, col)));
//                wegeBoard.trackWegeButtonPosition(boardButton, row, col);
//                boardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

//                    WegeCard currentCard = boardButton.getCard();
//                    WegeCard nextCard = nextCardButton.getCard();
//                    WegeCard.CardType nextCardType = nextCard.getCardType();
//                    if (nextCardType == WegeCard.CardType.BRIDGE) {
//                        if (currentCard != null && currentCard.getCardType() != WegeCard.CardType.COSSACK) {
//                            swapCard(boardButton, nextCardButton);
//                            return;
//                        }
//                    }
//                    List<WegeCard> adjacentCards = wegeBoard.findAdjacentCards(boardButton);
//                    if (!adjacentCards.isEmpty()) {
//                        switch (nextCardType) {
//                            case COSSACK, BRIDGE -> placeCard(boardButton, nextCardButton);
//                            case WATER, LAND -> {
//                                for (WegeCard adjacent : adjacentCards) {
//                                    if (nextCardType == adjacent.getCardType()
//                                            || adjacent.getCardType() == WegeCard.CardType.BRIDGE
//                                            || adjacent.getCardType() == WegeCard.CardType.COSSACK) placeCard(boardButton, nextCardButton);
//                                }
//                            }
//                        }
//                    }
//                });


                gridPane.add(boardButton, col, row);
            }
        }
        gridPane.add(flowPane, 0, wegeGame.getHeight() + 1, wegeGame.getWidth(), 1);
        return gridPane;
    }

    /**
     * Create the playing board without the wege card
     *
     * @param rows number of rows for the playing board.
     * @param cols number of columns for the playing board.
     * @return a 2 dimension array which contains the wege button for the playing board.
     */
    private WegeButton[][] createWegeBoard(int rows, int cols) {
        WegeButton[][] wegeLand = new WegeButton[rows][cols];
        for (int rowIdx = 0; rowIdx < wegeLand.length; rowIdx++) {
            for (int colIdx = 0; colIdx < wegeLand[rowIdx].length; colIdx++) {
                WegeButton wegeBoardButton = new WegeButton(100, 100);
                wegeLand[rowIdx][colIdx] = wegeBoardButton;
            }
        }
        return wegeLand;
    }

    /**
     * Place a card to a board button then clear the card in the next card button.
     *
     * @param boardButton the button on the playing board to place a card.
     * @param nextCardButton the next card button.
     */
    private void placeCard(WegeButton boardButton, WegeButton nextCardButton) {
        WegeCard nextCard = nextCardButton.getCard();
        if (nextCard != null) {
            boardButton.setCard(nextCard);
            nextCardButton.setCard(null);
        }
    }

    /**
     * Swap a card from the board button with the next card button.
     *
     * @param boardButton the button on the playing board to swap a card
     * @param nextCardButton the next card button.
     */
    private void swapCard(WegeButton boardButton, WegeButton nextCardButton) {
        WegeCard nextCard = nextCardButton.getCard();
        if (nextCard != null) {
            nextCardButton.setCard(boardButton.getCard());
            boardButton.setCard(nextCard);
        }
    }

}
