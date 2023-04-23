package ui;

import game.WegeCard;
import game.WegeDeck;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

/**
 * The pane to display at the bottom, contains next card button and other label.
 */
public class WegeBottomPane extends FlowPane {

    /* The button display the next card for the player */
    private WegeNextCardButton nextCardButton;

    /* The label to display the type of the current player. */
    private Label playerTypeLabel;

    /* The label to display the score of the current player. */
    private Label playerScoreLabel;

    /**
     * Create a new bottom pane for the game Wege.
     *
     * @param startingDeck the starting deck of the game Wege.
     */
    public WegeBottomPane(WegeDeck startingDeck) {
        createView(startingDeck);
    }

    /**
     * Set the card for the next button.
     *
     * @param wegeCard the card to set.
     */
    public void setNextCard(WegeCard wegeCard) {
        nextCardButton.setCard(wegeCard);
    }

    /**
     * Get next card of the next button.
     *
     * @return the next card from the next button.
     */
    public WegeCard getNextCard() {
        return nextCardButton.getCard();
    }

    /**
     * Display the player score.
     *
     * @param score the score to be displayed.
     */
    public void displayPlayerScore(int score) {
        playerScoreLabel.setText(buildPlayerScoreText(score));
    }

    private EventHandler<MouseEvent> handler;

    Button endGame = new Button("End Game");

    public void setEndGameEvent(EventHandler<MouseEvent> handler) {
        endGame.setOnMouseClicked(handler);
    }

    /**
     * Create the view for this pane.
     *
     * @param statingDeck the starting deck.
     */
    private void createView(WegeDeck statingDeck) {
        VBox gameInfoBox = new VBox();
        Insets labelPadding = new Insets(0, 0, 10, 0);
        playerTypeLabel = new Label();
        playerTypeLabel.setPadding(labelPadding);
        playerScoreLabel = new Label();
        playerScoreLabel.setPadding(labelPadding);
        Label cardLabel = new Label();
        cardLabel.setPadding(labelPadding);
        gameInfoBox.getChildren().addAll(playerTypeLabel, playerScoreLabel, cardLabel);
        nextCardButton = createNextCardButton(statingDeck, cardLabel);
        getChildren().addAll(nextCardButton, gameInfoBox, endGame);
    }

    /**
     * Create the next card button. This button can display the next card
     * to be played and can rotate that card.
     *
     * @return a button display the next card to play.
     */
    private WegeNextCardButton createNextCardButton(WegeDeck startingDeck,
                                                    Label cardLabel) {
        WegeNextCardButton nextCardButton = new WegeNextCardButton(100, 100);
        nextCardButton.addCardChangedListener((observable, oldCard, newCard) -> {
            if (newCard != null) {
                cardLabel.setText(buildWegeCardText(newCard));
            } else {
                cardLabel.setText(null);
            }
        });
        nextCardButton.addMouseClickedListener(mouseClickedEvent -> {
            if (nextCardButton.getCard() == null) {
                WegeCard nextCard = startingDeck.drawFromFront();
                nextCardButton.setCard(nextCard);
            } else {
                nextCardButton.rotate();
                cardLabel.setText(buildCardInfo(nextCardButton.getCard()));
            }
        });
        WegeCard initialCard = startingDeck.drawFromFront();
        nextCardButton.setCard(initialCard);
        return nextCardButton;
    }
    /**
     * Build the text for display the player score.
     */
    private String buildPlayerScoreText(int score) {
        return "Player Score: " + String.valueOf(score);
    }

    /**
     * Build the text for display the card in the next button.
     */
    private String buildWegeCardText(WegeCard wegeCard) {
        return "Wege Card: " + buildCardInfo(wegeCard);
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
                .append(" card")
                .append("Orientation: ")
                .append(wegeCard.getOrientation());
        if (wegeCard.hasGnome()) {
            builder.append(" with a Gnome in the corner");
        } else if (wegeCard.isPathGnome()){
            builder.append(" with a Gnome is the path");
        }
        return builder.toString();
    }

}
