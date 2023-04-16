package csds132.hw4.ui.pane;

import csds132.hw4.game.WegeCard;
import csds132.hw4.game.WegeDeck;
import csds132.hw4.game.WegePlayer;
import csds132.hw4.ui.WegeButton;
import csds132.hw4.ui.button.WegeNextCardButton;
import csds132.hw4.ui.label.WegeCardLabel;
import csds132.hw4.ui.label.WegePlayerScoreLabel;
import csds132.hw4.ui.label.WegePlayerTypeLabel;
import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class WegeBottomPane extends FlowPane {

    /* The button display the next card for the player */
    private WegeButton nextCardButton;

    /* The label to display the type of the current player. */
    private WegePlayerTypeLabel playerTypeLabel;

    /* The label to display the score of the current player. */
    private WegePlayerScoreLabel playerScoreLabel;

    /**
     * Create a new bottom pane for the game Wege.
     *
     * @param startingDeck the starting deck of the game Wege.
     */
    public WegeBottomPane(WegeDeck startingDeck) {
        createView(startingDeck);
    }

    /**
     * Display the player type.
     *
     * @param playerType the player type to be displayed.
     */
    public void displayPlayerType(WegePlayer.PlayerType playerType) {
        playerTypeLabel.setValue(playerType.name());
    }

    /**
     * Display the player score.
     *
     * @param score the score to be displayed.
     */
    public void displayPlayerScore(int score) {
        playerScoreLabel.setValue(String.valueOf(score));
    }

    /**
     * Create the view for this pane.
     *
     * @param statingDeck the starting deck.
     */
    private void createView(WegeDeck statingDeck) {
        VBox gameInfoBox = new VBox();
        Insets labelPadding = new Insets(0, 0, 10, 0);
        playerTypeLabel = new WegePlayerTypeLabel();
        playerTypeLabel.setPadding(labelPadding);
        playerScoreLabel = new WegePlayerScoreLabel();
        playerScoreLabel.setValue("0");
        playerScoreLabel.setPadding(labelPadding);
        WegeCardLabel cardLabel = new WegeCardLabel();
        cardLabel.setPadding(labelPadding);
        gameInfoBox.getChildren().addAll(playerTypeLabel, playerScoreLabel, cardLabel);
        nextCardButton = createNextCardButton(statingDeck, cardLabel);
        getChildren().addAll(nextCardButton, gameInfoBox);
    }

    /**
     * Create the next card button. This button can display the next card
     * to be played and can rotate that card.
     *
     * @return a {@link WegeButton} initially display the first card from the deck.
     */
    private WegeButton createNextCardButton(WegeDeck startingDeck,
                                            WegeCardLabel cardLabel) {
        WegeNextCardButton nextCardButton = new WegeNextCardButton(100, 100);
        nextCardButton.addCardChangedListener((observable, oldCard, newCard) -> {
            if (newCard != null) {
                cardLabel.setValue(buildCardInfo(newCard));
            } else {
                cardLabel.clearText();
            }
        });
        nextCardButton.addMouseClickedListener(mouseClickedEvent -> {
            if (nextCardButton.getCard() == null) {
                WegeCard nextCard = startingDeck.drawFromFront();
                nextCardButton.setCard(nextCard);
            } else {
                nextCardButton.rotate();
            }
        });
        WegeCard initialCard = startingDeck.drawFromFront();
        nextCardButton.setCard(initialCard);
        return nextCardButton;
    }

    /**
     * Return the Player Type label from this pane.
     */
    public WegePlayerTypeLabel getPlayerTypeLabel() {
        return playerTypeLabel;
    }

    /**
     * Return the next card button from this pane.
     */
    public WegeButton getNextCardButton() {
        return nextCardButton;
    }

    /**
     * Build the card information
     *
     * @param wegeCard a {@link WegeCard}
     * @return the String contains information about the given card.
     */
    private String buildCardInfo(WegeCard wegeCard) {
        StringBuilder builder = new StringBuilder();
        builder.append(wegeCard.getCardType().name())
                .append(" card");
        if (wegeCard.hasGnome()) {
            builder.append(" with a Gnome in the corner");
        } else if (wegeCard.isPathGnome()){
            builder.append(" with a Gnome is the path");
        }
        return builder.toString();
    }

}
