package ui;

import game.WegeCard;
import game.WegeDeck;
import game.WegePlayingCard;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

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

    /**
     * Create the view for this pane.
     *
     * @param statingDeck the starting deck.
     */
    private void createView(WegeDeck statingDeck) {
        VBox gameInfoBox = new VBox();
        Insets labelPadding = new Insets(0, 0, 10, 0);
        Font font = Font.font("Arial", 14);
        Label cardLabel = new Label();
        cardLabel.setFont(font);
        cardLabel.setPadding(labelPadding);
        Label descriptionLabel = new Label(
                "Wege, or Landlock, is a game for two players. " +
                        "If you are \"Land\", your goal is to have a connected land path " +
                        "touch as many sides of the park as possible. " +
                        "If you are \"Water\", your goal is to create a connected stream of " +
                        "water that connects as many sides of the park as possible"
        );
        descriptionLabel.setPrefWidth(500);
        descriptionLabel.setWrapText(true);
        descriptionLabel.setFont(font);
        gameInfoBox.getChildren().addAll(cardLabel, descriptionLabel);
        nextCardButton = createNextCardButton(statingDeck, cardLabel);
        Separator separator = new Separator(Orientation.VERTICAL);
        getChildren().addAll(nextCardButton, separator, gameInfoBox);
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
                .append(" card");
        if (wegeCard.hasGnome()) {
            builder.append(" with a Gnome in the corner");
        } else if (wegeCard.isPathGnome()){
            builder.append(" with a Gnome is the path");
        }
        return builder.toString();
    }

}
