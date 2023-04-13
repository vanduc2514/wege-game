import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class WegeGame {

    private final WegeButton[][] wegeBoard;

    private final WegeDeck wegeDeck;

    private final WegeButton nextCardButton;

    private final Label label = new Label();

    private int firstLandPlaced;

    public WegeGame(int row, int col) {
        wegeDeck = WegeDeck.createDefaultDeck();
        nextCardButton = new WegeButton(100, 100);
        nextCardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (nextCardButton.getCard() == null) {
                WegeCard nextCard = wegeDeck.drawFromFront();
                nextCardButton.setCard(nextCard);
                label.setText(nextCard.getCardType().name());
            } else {
                nextCardButton.rotate();
            }
        });
        wegeBoard = createWegeLand(row, col);
    }

    public GridPane drawBoard() {
        GridPane gridPane = new GridPane();
        FlowPane flowPane = new FlowPane(nextCardButton, label);
        for (int row = 0; row < wegeBoard.length; row++) {
            for (int col = 0; col < wegeBoard[row].length; col++) {
                WegeButton wegeBoardButton = this.wegeBoard[row][col];
                int finalRow = row;
                int finalCol = col;
                wegeBoardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    if (firstLandPlaced != 2) {
                        firstLandPlaced++;
                        placeCard(wegeBoardButton);
                        return;
                    };
                    WegeCard currentCard = wegeBoardButton.getCard();
                    WegeCard nextCard = nextCardButton.getCard();
                    WegeCard.CardType nextCardType = nextCard.getCardType();
                    if (nextCardType == WegeCard.CardType.BRIDGE) {
                        if (currentCard != null && currentCard.getCardType() != WegeCard.CardType.COSSACK) {
                            nextCardButton.setCard(currentCard);
                            wegeBoardButton.setCard(nextCard);
                            return;
                        }
                    }
                    List<WegeCard> adjacentCards = getAdjacentCards(finalRow, finalCol);
                    if (!adjacentCards.isEmpty()) {
                        switch (nextCardType) {
                            case COSSACK, BRIDGE -> placeCard(wegeBoardButton);
                            case WATER, LAND -> {
                                for (WegeCard adjacent : adjacentCards) {
                                    if (nextCardType == adjacent.getCardType()
                                            || adjacent.getCardType() == WegeCard.CardType.BRIDGE
                                            || adjacent.getCardType() == WegeCard.CardType.COSSACK) placeCard(wegeBoardButton);
                                }
                            }
                        }
                    }
                });
                gridPane.add(wegeBoardButton, col, row);
            }
        }
        gridPane.add(flowPane, 0, wegeBoard.length + 1, wegeBoard.length, 1);
        return gridPane;
    }

    private List<WegeCard> getAdjacentCards(int finalRow, int finalCol) {
        List<WegeCard> adjacentButtons = new ArrayList<>();
        BiConsumer<Integer, Integer> func = addAdjacentButtons(adjacentButtons);
        func.accept(finalRow, finalCol - 1);
        func.accept(finalRow, finalCol + 1);
        func.accept(finalRow + 1, finalCol);
        func.accept(finalRow - 1, finalCol);
        return adjacentButtons;
    }

    private BiConsumer<Integer, Integer> addAdjacentButtons(List<WegeCard> buttonContainer) {
        return (row, col) -> {
            try {
                WegeButton wegeButton = wegeBoard[row][col];
                WegeCard card = wegeButton.getCard();
                if (card != null) buttonContainer.add(card);
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
        };
    }

    private WegeButton[][] createWegeLand(int row, int col) {
        WegeButton[][] wegeLand = new WegeButton[row][col];
        for (int rowIdx = 0; rowIdx < wegeLand.length; rowIdx++) {
            for (int colIdx = 0; colIdx < wegeLand[rowIdx].length; colIdx++) {
                WegeButton wegeBoardButton = new WegeButton(100, 100);
                wegeLand[rowIdx][colIdx] = wegeBoardButton;
            }
        }
        return wegeLand;
    }

    private void placeCard(WegeButton wegeBoardButton) {
        if (nextCardButton.getCard() != null) {
            wegeBoardButton.setCard(nextCardButton.getCard());
            nextCardButton.setCard(null);
            label.setText(null);
        }
    }

}
