package ui;

import game.WegeCard;
import game.WegePlayingCard;
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
    public void setNextCard(WegePlayingCard wegeCard) {
        nextCardButton.setCard(wegeCard);
    }

    /**
     * Get next card of the next button.
     *
     * @return the next card from the next button.
     */
    public WegePlayingCard getNextCard() {
        return (WegePlayingCard) nextCardButton.getCard();
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
        Label cardLabel = new Label();
        cardLabel.setPadding(labelPadding);
        gameInfoBox.getChildren().addAll(cardLabel);
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
        WegeNextCardButton button = new WegeNextCardButton(100, 100);
        button.addCardChangedListener((observable, oldCard, newCard) -> {
            if (newCard != null) {
                cardLabel.setText(buildCardInfo(newCard));
            } else {
                cardLabel.setText(null);
            }
        });
        button.addMouseClickedListener(mouseClickedEvent -> {
            if (button.getCard() == null) {
                WegePlayingCard nextCard = startingDeck.drawFromFront();
                button.setCard(nextCard);
            } else {
                button.rotate();
                cardLabel.setText(buildCardInfo(button.getCard()));
            }
        });
        WegePlayingCard initialCard = startingDeck.drawFromFront();
        button.setCard(initialCard);
        return button;
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
