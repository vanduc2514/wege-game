import csds132.hw4.model.WegeDeck;
import csds132.hw4.model.WegeGameSetting;
import csds132.hw4.ui.WegeGame;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class Wege extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        WegeGameSetting wegeGameSetting = null;
        List<String> wegeParams = getParameters().getUnnamed();
        switch (wegeParams.size()) {
            case 0 -> wegeGameSetting = WegeGameSetting.createDefaultGame();
            case 1 -> {
                int numberOfEachSpecialCard = Integer.parseInt(wegeParams.get(0));
                WegeDeck specialDeck = WegeDeck.createSpecialDeck(numberOfEachSpecialCard);
                wegeGameSetting = WegeGameSetting.createDefaultBoardWithDeck(specialDeck);
            }
            default -> {
                wegeGameSetting = createCustomSetting(wegeParams);
            }
        }
        Scene scene = new Scene(new WegeGame(wegeGameSetting));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Create a custom setting for the Wege Application.
     *
     * @param wegeParams the parameters given to this application.
     * @return a {@link WegeGameSetting} for a Wege game.
     */
    private static WegeGameSetting createCustomSetting(List<String> wegeParams) {
        WegeGameSetting wegeGameSetting;
        int rowsOfPlayingBoard = Integer.parseInt(wegeParams.get(0));
        int colsOfPlayingBoard = Integer.parseInt(wegeParams.get(1));
        WegeDeck wegeDeck;
        if (wegeParams.size() == 3) {
            int numberOfEachSpecialCard = Integer.parseInt(wegeParams.get(2));
            wegeDeck = WegeDeck.createSpecialDeck(numberOfEachSpecialCard);
        } else {
            wegeDeck = WegeDeck.createCustomDeck(rowsOfPlayingBoard, colsOfPlayingBoard);
        }
        wegeGameSetting = new WegeGameSetting(rowsOfPlayingBoard, colsOfPlayingBoard, wegeDeck);
        return wegeGameSetting;
    }

}
