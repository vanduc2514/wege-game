package csds132.hw4.ui.pane;

import csds132.hw4.game.WegeCard;
import csds132.hw4.game.WegeDeck;
import csds132.hw4.ui.WegeButton;
import csds132.hw4.ui.button.WegeNextCardButton;
import csds132.hw4.ui.label.WegeCardLabel;
import csds132.hw4.ui.label.WegePlayerScoreLabel;
import csds132.hw4.ui.label.WegePlayerTypeLabel;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class WegeBottomPane extends FlowPane {

    private WegeButton nextCardButton;

    private WegePlayerTypeLabel playerTypeLabel;

    private WegePlayerScoreLabel playerScoreLabel;

    private WegeDeck wegeDeck;

    public WegeBottomPane(WegeDeck wegeDeck) {
        this.wegeDeck = wegeDeck;
        createView();
    }

    private void createView() {
        VBox gameInfoBox = new VBox();
        playerTypeLabel = new WegePlayerTypeLabel();
        playerScoreLabel = new WegePlayerScoreLabel();
        WegeCardLabel cardLabel = new WegeCardLabel();
        gameInfoBox.getChildren().addAll(playerTypeLabel, cardLabel, playerScoreLabel);
        nextCardButton = createNextCardButton(cardLabel);
        getChildren().addAll(nextCardButton, gameInfoBox);
    }

    /**
     * Create the next card button. This button can display the next card
     * to be played and can rotate that card.
     *
     * @return a {@link WegeButton} initially display the first card from the deck.
     */
    private WegeButton createNextCardButton(WegeCardLabel cardLabel) {
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
                WegeCard nextCard = wegeDeck.drawFromFront();
                nextCardButton.setCard(nextCard);
            } else {
                nextCardButton.rotate();
            }
        });
        WegeCard initialCard = wegeDeck.drawFromFront();
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
                .append("card");
        if (wegeCard.hasGnome()) {
            builder.append(" with a Gnome in the corner");
        } else if (wegeCard.isPathGnome()){
            builder.append(" with a Gnome is the path");
        }
        return builder.toString();
    }

}
