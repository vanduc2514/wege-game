import javafx.geometry.Pos;
import java.util.NoSuchElementException;

/**
 * A card for the Wege (LandLock) game.
 * 
 * @author Harold Connamacher
 */
public class WegeCard {
  /** The types of cards in the game */
  public enum CardType {WATER, LAND, BRIDGE, COSSACK};
  
  /** What kind of card is this? */
  private CardType cardType;
  
  /** The location of the end of a path/water, also the location of a path gnome */
  private Pos orientation = Pos.TOP_LEFT;
  
  /** Whether this piece has a gnome */
  private boolean hasGnome = false;
  
  /** Whether the gnome, if it exists, is on a path or a dead end */
  private boolean isPathGnome = false;
  
  /**
   * Create a Wege game button
   * @param type the type of game card
   * @param hasGnome whether there is a gnome on this card
   * @param isPathGnome sets whether the gnome (if there is one) is on the land/water "path" or on a dead end
   */
  public WegeCard(CardType type, boolean hasGnome, boolean isPathGnome) {
    this.cardType = type;
    this.hasGnome = hasGnome;
    this.isPathGnome = isPathGnome;
    this.orientation = Pos.TOP_LEFT;
  }
  
  /**
   * Returns the type of card.
   * @return the type of game card.
   */
  public CardType getCardType() {
    return cardType;
  }
  
  /**
   * Returns the orientation of the card.  This determines the corner that starts the land/water "path" or bridge.
   * For cossack cards, it determines the corner with land.
   * @return the orientation of the card.
   */
  public Pos getOrientation() {
    return orientation;
  }
  
  /**
   * Changes the orientation of the card.
   * @param orientation the new orientation for the card.
   */
  public void setOrientation(Pos orientation) {
    this.orientation = orientation;
  }
  
  /**
   * Returns whether there is a gnome on the card.
   */
  public boolean hasGnome() {
    return hasGnome;
  }
  
  /**
   * Returns whether the gnome on the card is on the path or on the dead end.
   * @return true if the gnome is on the path and false if it is on a dead end.
   */
  public boolean isPathGnome() {
    return isPathGnome;
  }
  
  /**
   * Returns the position of the gnome on the card.
   * @return the position of the gnome on the card, assuming there is a gnome on the card.
   */
  public Pos getGnomePosition() {
    if (!hasGnome())
      throw new NoSuchElementException();
    
    if (isPathGnome())
      return getOrientation();
    else
      return rotate(getOrientation());
  }
  
  /**
   * Rotates an orientation or location by 90 degrees clockwise.
   * @param orientation an orientation or a location
   * @return the orientation/location rotated 90 degrees clockwise.
   */
  public Pos rotate(Pos orientation) {
    switch (orientation) {
      case TOP_LEFT: return Pos.TOP_RIGHT;
      case TOP_RIGHT: return Pos.BOTTOM_RIGHT;
      case BOTTOM_RIGHT: return Pos.BOTTOM_LEFT;
      default: return Pos.TOP_LEFT;
    }
  }
    
  /**
   * Returns whether a particular corner of the card has water.
   * @param location one of the four corners of the card
   * @return true if that corner displays water and false if that corner displays land or a bridge
   */
  public boolean isWater(Pos location) {
    boolean waterOnMainDiagonal = 
       getCardType() == CardType.WATER  && (getOrientation() == Pos.TOP_LEFT  || getOrientation() == Pos.BOTTOM_RIGHT) ||
     !(getCardType() == CardType.WATER) && (getOrientation() == Pos.TOP_RIGHT || getOrientation() == Pos.BOTTOM_LEFT);
    
    if (location == Pos.TOP_LEFT || location == Pos.BOTTOM_RIGHT)
      return waterOnMainDiagonal;
    else
      return !waterOnMainDiagonal;
  }
  
  /**
   * Returns whether a particular corner of the card has land.
   * @param location one of the four corners of the card
   * @return true if that corner displays land or a bridge and false if that corner displays water
   */
  public boolean isLand(Pos location) {
    return !isWater(location);
  }
}
  
  