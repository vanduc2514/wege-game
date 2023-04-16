package csds132.hw4.ui.label;

import javafx.scene.control.Label;

/**
 * A Label for the game Wege. This label display the text in
 * a key-value format {@link #KEY_VALUE_FORMAT}
 */
public abstract class WegeLabel extends Label {

    private static final String KEY_VALUE_FORMAT = "%s: %s";

    /**
     * Set the value for this label.
     */
    public void setValue(String value) {
        String text = String.format(KEY_VALUE_FORMAT, getKeyName(), value);
        setText(text);
    }

    /**
     * Clear all text in this label.
     */
    public void clearText() {
        setText(null);
    }

    /**
     * Get the key for this label. Subclasses of this class
     * need to specify the key for this label.
     */
    protected abstract String getKeyName();

}
