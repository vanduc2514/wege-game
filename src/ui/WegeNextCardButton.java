package ui;

import game.WegeCard;
import game.WegePlayingCard;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class WegeNextCardButton extends WegeButton {

    /* Dynamic property which has the ability of listen to changes. */
    private ObjectProperty<WegePlayingCard> cardProperty;

    /**
     * Create a Wege game button
     *
     * @param width  the desired with of the button
     * @param height the desired height of the button
     */
    public WegeNextCardButton(int width, int height) {
        super(width, height);
        cardProperty = new SimpleObjectProperty<>();

    }

    @Override
    public void setCard(WegeCard card) {
        // Draw the card to the button.
        super.setCard(card);
        // Fire event which change the card.
        cardProperty.set((WegePlayingCard) card);
    }

    /**
     * Add a listener which handle the Wege card change event.
     *
     * @param changedWegePlayingCardListener the lister for Wege card change event.
     */
    public void addCardChangedListener(ChangeListener<WegePlayingCard> changedWegePlayingCardListener) {
        cardProperty.addListener(changedWegePlayingCardListener);
    }

    /**
     * Add a listener which handle the event when mouse click to this button.
     *
     * @param mouseClickedEventHandler the handler when mouse click to this button
     */
    public void addMouseClickedListener(EventHandler<MouseEvent> mouseClickedEventHandler) {
        addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedEventHandler);
    }

}
