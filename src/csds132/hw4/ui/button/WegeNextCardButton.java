package csds132.hw4.ui.button;

import csds132.hw4.game.WegeCard;
import csds132.hw4.ui.WegeButton;
import csds132.hw4.ui.label.WegeCardLabel;
import csds132.hw4.ui.label.WegePlayerScoreLabel;
import csds132.hw4.ui.label.WegePlayerTypeLabel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class WegeNextCardButton extends WegeButton {

    /**
     * Dynamic property which has the ability of listen to changes.
     */
    ObjectProperty<WegeCard> cardProperty;

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
        cardProperty.set(card);
    }

    /**
     * Add a listener which handle the Wege card change event.
     *
     * @param changedWegeCardListener the lister for Wege card change event.
     */
    public void addCardChangedListener(ChangeListener<WegeCard> changedWegeCardListener) {
        cardProperty.addListener(changedWegeCardListener);
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
