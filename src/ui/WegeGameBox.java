package ui;

import game.*;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

/**
 * The view of a Wege game and UI interactions for the game.
 */
public class WegeGameBox extends VBox {

    /* Maximum cards that can be placed on the game board. */
    private final int maximumCards;

    /* The game master for the Game Wege. He helps checking the game rule and collect player statistic */
    private final WegeGameMaster wegeGameMaster;

    /* The number of cards had been played so far. */
    private int cardsPlayed;

    private Dialog<String> gameScoreDialog;

    /**
     * Create a new Wege Game.
     *
     * @param rows     the number of row for the playing board for this game.
     * @param cols     the number of column for the playing board for this game.
     * @param wegeDeck the dek contains wege cards for this game.
     */
    public WegeGameBox(int rows, int cols, WegeDeck wegeDeck) {
        this.maximumCards = rows * cols;
        WegePlayingBoard wegePlayingBoard = new WegePlayingBoard(rows, cols);
        wegeGameMaster = new WegeGameMaster(wegePlayingBoard);
        createView(rows, cols, wegeDeck);
    }

    /**
     * Create a new box to display the ui of the Wege game.
     *
     * @param wegeGameSetting The setting of Wege game.
     */
    public WegeGameBox(WegeGameSetting wegeGameSetting) {
        this(wegeGameSetting.rows(), wegeGameSetting.cols(), wegeGameSetting.deck());
    }

    /**
     * Create a view for the Wege Game.
     *
     * @param rows         the number of row for the playing board of the Wege Game
     * @param cols         the number of column for the playing board of the Wege Game
     * @param startingDeck the Wege deck to start the game.
     */
    private void createView(int rows, int cols, WegeDeck startingDeck) {
        getChildren().clear();
        /* The bottom pane of this box */
        WegeBottomPane bottomPane = new WegeBottomPane(startingDeck);
        /* The top playing board of this box */
        WegePlayingBoardPane playingBoard = new WegePlayingBoardPane(rows, cols);
        // UI Interactions when a player click a button on the playing board.
        playingBoard.setBoardButtonClickedHandler(getBoardButtonClickedHandler(bottomPane));
        ObservableList<Node> children = getChildren();
        children.add(playingBoard);
        children.add(bottomPane);
        // Create the game score dialog
        createGameScoreDialog();
    }

    private void createGameScoreDialog() {
        gameScoreDialog = new Dialog<>();
        gameScoreDialog.setTitle("Game Statistic");
        ButtonType okButton = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        DialogPane dialogPane = gameScoreDialog.getDialogPane();
        dialogPane.setPrefHeight(200);
        dialogPane.setPrefWidth(300);
        //Adding buttons to the dialog pane
        dialogPane.getButtonTypes().add(okButton);
    }

    private EventHandler<MouseEvent> getBoardButtonClickedHandler(WegeBottomPane bottomPane) {
        return mouseClickedEvent -> {
            if (isGameEnded()) {
                List<Player> playerStatistics = wegeGameMaster.collectPlayerStatistic();
                playerStatistics.forEach(System.out::println);
                showGameScore(playerStatistics);
                return;
            }
            if (bottomPane.getNextCard() == null) return;
            WegeBoardButton boardButton = (WegeBoardButton) mouseClickedEvent.getSource();
            WegePlayingCard nextCard = bottomPane.getNextCard();
            int row = boardButton.getRow();
            int col = boardButton.getCol();
            nextCard.setRow(row);
            nextCard.setCol(col);
            if (boardButton.getCard() == null
                    && wegeGameMaster.tryPlaceCard(nextCard)) {
                placeCard(boardButton, bottomPane);
            } else if (boardButton.getCard() != null
                    && wegeGameMaster.trySwapCard(nextCard)){
                swapCard(boardButton, bottomPane);
            }
        };
    }

    private void showGameScore(List<Player> playerStatistics) {
        VBox statistic = new VBox();
        playerStatistics.forEach(p -> statistic.getChildren().add(createPlayerStatistic(p)));
        Button newGameButton = new Button("New game");
        statistic.getChildren().add(newGameButton);
        newGameButton.setOnAction(event -> {
            WegeGameSetting defaultGame = WegeGameSetting.createStandardGame();
            createView(defaultGame.rows(), defaultGame.cols(), defaultGame.deck());
            gameScoreDialog.close();
        });
        gameScoreDialog.getDialogPane().setContent(statistic);
        gameScoreDialog.show();
    }

    private VBox createPlayerStatistic(Player player) {
        VBox statisticBox = new VBox();
        Font titleFont = Font.font("Arial", FontWeight.EXTRA_BOLD, 16);
        Font bodyFont = Font.font("Arial", FontWeight.NORMAL, 14);
        Insets labelPadding = new Insets(0, 0, 20, 0);

        String playerLabelTxt;
        if (player.isLandPlayer()) {
            playerLabelTxt = "LAND EARNED:";
        } else {
            playerLabelTxt = "WATER EARNED:";
        }
        Label playerLabel = new Label(playerLabelTxt);
        playerLabel.setFont(titleFont);
        statisticBox.getChildren().addAll(playerLabel, new Separator());

        String sideConnectedTxt = "%d sides connected = %d points";
        int maximumEdgesTouched = player.getMaximumEdgesTouched();
        int sideConnectedScore = player.getGameScore().getSideConnectedScore();
        Label sideConnectedLabel = new Label(
                String.format(sideConnectedTxt, maximumEdgesTouched, sideConnectedScore));
        sideConnectedLabel.setFont(bodyFont);
        statisticBox.getChildren().add(sideConnectedLabel);

        String centralGroundTxt = "%d %s created = %d points";
        int centralGround = player.getCentralGround();
        int scoreCentralGround = player.getGameScore().getCentralGroundScore();
        if (player.isLandPlayer()) {
            centralGroundTxt = String.format(centralGroundTxt, centralGround, "islands", scoreCentralGround);
        } else {
            centralGroundTxt = String.format(centralGroundTxt, centralGround, "ponds", scoreCentralGround);
        }
        Label centralGroundLabel = new Label(centralGroundTxt);
        centralGroundLabel.setFont(bodyFont);
        statisticBox.getChildren().add(centralGroundLabel);

        final String[] gnomeFacingTxt = {"%d intersections of %d gnomes = %d points"};
        player.getGameScore().getIntersectionOfGnomeScore().forEach((group, pair) -> {
            gnomeFacingTxt[0] = String.format(gnomeFacingTxt[0], pair.getKey(), group, pair.getValue());
            Label gnomeFacingLabel = new Label(gnomeFacingTxt[0]);
            gnomeFacingLabel.setFont(bodyFont);
            statisticBox.getChildren().add(gnomeFacingLabel);
        });

        String cossackPlayedTxt = String.format("%d cossack played = %d points",
                player.getCossackCardsPlayed(), player.getGameScore().getCossackScore());
        Label cossackPlayedLabel = new Label(cossackPlayedTxt);
        cossackPlayedLabel.setFont(bodyFont);
        statisticBox.getChildren().add(cossackPlayedLabel);

        String totalTxt = String.format("TOTAL POINTS: %d",
                player.getGameScore().total());
        Label totalLabel = new Label(totalTxt);
        totalLabel.setFont(bodyFont);
        totalLabel.setPadding(labelPadding);
        statisticBox.getChildren().add(totalLabel);

        return statisticBox;
    }

    /**
     * Check if the game is ended when all cards have been played on the board.
     *
     * @return <code>true</code> if the board is filled up with all Wege card.
     * Otherwise, return <code>false</code>.
     */
    private boolean isGameEnded() {
        return cardsPlayed == maximumCards;
    }

    /**
     * Place a card to a board button then clear the card in the next card button.
     *
     * @param boardButton the button on the playing board to place a card.
     * @param bottomPane  contains next card button.
     */
    private void placeCard(WegeButton boardButton, WegeBottomPane bottomPane) {
        setCardOnBoard(boardButton, bottomPane.getNextCard());
        bottomPane.setNextCard(null);
        cardsPlayed++;
    }

    /**
     * Swap a card from the board button with the card in next card button.
     *
     * @param boardButton the button on the playing board to swap a card
     * @param bottomPane  contains next card button.
     */
    private void swapCard(WegeButton boardButton, WegeBottomPane bottomPane) {
        WegeCard currentCardOnBoard = boardButton.getCard();
        setCardOnBoard(boardButton, bottomPane.getNextCard());
        bottomPane.setNextCard((WegePlayingCard) currentCardOnBoard);
    }

    /**
     * Set a card to the button on the playing board.
     *
     * @param boardButton button on the playing board.
     * @param wegeCard    the card to set to this button.
     */
    private void setCardOnBoard(WegeButton boardButton, WegeCard wegeCard) {
        boardButton.setCard(wegeCard);
    }

}
