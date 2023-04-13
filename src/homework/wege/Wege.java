package homework.wege;

import homework.wege.game.WegeDeck;
import homework.wege.model.WegeButton;
import homework.wege.model.WegeCard;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Wege extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane gridPane = initializeBoard(100, 6, 6);
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * TODO: Rewrite doc for better readability
     * Initialize the game board with the given parameters. Board is created with empty
     * {@link homework.wege.model.WegeCard}
     *
     * @param wegeButtonSize the size of the {@link WegeButton}. The button is created in a square frame
     * @param numberOfRow the number of rows for the game board
     * @param numberOfColumn the number of columns for the game board
     * @return the {@link GridPane} with {@link WegeButton} filled in a 2-dimension array.
     */
    private static GridPane initializeBoard(int wegeButtonSize, int numberOfRow, int numberOfColumn) {
        WegeDeck wegeDeck = WegeDeck.createDefaultDeck();
        WegeButton nextCardButton = new WegeButton(wegeButtonSize, wegeButtonSize);
        nextCardButton.setText("Next Card");
        nextCardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (nextCardButton.getCard() == null) {
                WegeCard nextCard = wegeDeck.drawFromFront();
                nextCardButton.setCard(nextCard);
            } else {
                nextCardButton.rotate();
            }
        });
        GridPane gridPane = new GridPane();
        for (int rowIdx = 0; rowIdx < numberOfRow; rowIdx++) {
            for (int colIdx = 0; colIdx < numberOfColumn; colIdx++) {
                WegeButton wegeButton = new WegeButton(wegeButtonSize, wegeButtonSize);
                wegeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    if (nextCardButton.getCard() != null) {
                        wegeButton.setCard(nextCardButton.getCard());
                        nextCardButton.setCard(null);
                    }
                });
                gridPane.add(wegeButton, colIdx, rowIdx);
            }
        }
        gridPane.add(nextCardButton, 1, numberOfRow + 1);
        return gridPane;
    }

}
