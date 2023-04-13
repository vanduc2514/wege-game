import javafx.scene.control.Button;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Dimension2D;
import javafx.geometry.Pos;

/**
 * A JavaFX Button that has a Wege game tile painted on it.
 * 
 * @author Harold Connamacher
 */
public class WegeButton extends Button {
  
  /** The game card that is displayed by this button */
  private WegeCard card;
  
  /** The color used if there is no card on this game tile */
  private static Color emptyColor = Color.GREY;
  
  /** The color of the swamp/moss game background */
  private static Color backgroundColor = Color.GREEN;
  
  /** The color of "land" */
  private static Color landColor = Color.BROWN;
  
  /** The color of "water" */
  private static Color waterColor = Color.BLUE;
  
  /** The color of a bridge */
  private static Color bridgeColor = Color.TAN;
  
  /** The color of a land gnome */
  private static Color landGnomeColor = Color.CYAN;
  
  /** The color of a water gnome */
  private static Color waterGnomeColor = Color.YELLOW;
  
  /**
   * Create a Wege game button
   * @param width the desired with of the button
   * @param height the desired height of the button
   */
  public WegeButton(int width, int height) {
    super();
    setMinWidth(width);
    setMinHeight(height);
    setGraphic(new GameTile(width, height));
  }
  
  /**
   * Returns the card displayed on the button.
   * @return the card displayed on the button or null if there is no card.
   */
  public WegeCard getCard() {
    return card;
  }
  
  /**
   * Changes the card displayed on the button.
   * @param card the card to display on the button or <tt>null</tt> if the button is to be empty.
   */
  public void setCard(WegeCard card) {
    this.card = card;
    repaint();
  }
  
  /**
   * Rotate the card displayed on this button 90 degrees clockwise.
   */
  public void rotate() {
    getCard().setOrientation(getCard().rotate(getCard().getOrientation()));
    repaint();
  }
  
  /**
   * Returns the color used for land.
   * @return the color used for land
   */
  public static Color getLandColor() {
    return landColor;
  }
  
  /**
   * Changes the color used for land from the default.
   * @param color the new desired color for land.
   */
  public static void setLandColor(Color color) {
    landColor = color;
  }
  
  /**
   * Returns the color used for water.
   * @return the color used for water.
   */
  public static Color getWaterColor() {
    return waterColor;
  }
  
  /**
   * Changes the color used for water from the default.
   * @param color the new desired color for water.
   */
  public static void setWaterColor(Color color) {
    waterColor = color;
  }
  
  /**
   * Returns the color used for a bridge.
   * @return the color used for a bridge.
   */
  public static Color getBridgeColor() {
    return bridgeColor;
  }
  
  /**
   * Changes the color used for a bridge from the default.
   * @param color the new desired color for a bridge.
   */
  public static void setBridgeColor(Color color) {
    bridgeColor = color;
  }
  
  /**
   * Returns the color used for a land gnome.
   * @return the color used for a land gnome.
   */
  public static Color getLandGnomeColor() { 
    return landGnomeColor;
  }
  
  /**
   * Changes the color used for a land gnome from the default.
   * @param color the new desired color for a land gnome.
   */
  public static void setLandGnomeColor(Color color) {
    landGnomeColor = color;
  }
  
  /**
   * Returns the color used for a water gnome.
   * @return the color used for a water gnome.
   */
  public static Color getWaterGnomeColor() {
    return waterGnomeColor;
  }
  
  /**
   * Changes the color used for a water gnome from the default.
   * @param color the new desired color for a water gnome.
   */
  public static void setWaterGnomeColor(Color color) {
    waterGnomeColor = color;
  }
  
  /**
   * Returns the color used for an empty tile.
   * @return the color used for an empty tile.
   */
  public static Color getEmptyColor() {
    return emptyColor;
  }
  
  /**
   * Changes the color used for an empty tile from the default.
   * @param color the new desired color for an empty tile.
   */
  public static void setEmptyColor(Color color) {
    emptyColor = color;
  }
  
  /**
   * Retrieve the color used for the background of the game card.
   * @return the background color of the card display
   */
  public static Color getBackgroundColor() {
    return backgroundColor;
  }
  
  /**
   * Change the color used for the background of the game card.
   * @param color  the background color of the card
   */
  public static void setBackgroundColor(Color color) {
    backgroundColor = color;
  }
  
  /**
   * Redisplays the tile image
   */
  private void repaint() {
    ((GameTile)getGraphic()).paintTile();
  }
 
  
  /** The image on a tile of the Wege game. Plus any stones that are on the tile. */
  private class GameTile extends Canvas {
    
    /**
     * Create a tile of the game
     * @param width the width of the game tile in pixels
     * @param height the height of the game time in pixels
     */
    public GameTile(int width, int height) {
      super(width, height);
      paintTile();
    }
    
    /**
     * Draw the Wege card image onto the button
     */
    public void paintTile() {
      GraphicsContext gc = getGraphicsContext2D();
      
      // if their is no card, use the blank display
      if (getCard() == null) {
        gc.setFill(getEmptyColor());
        gc.fillRect(0, 0, getHeight(), getWidth());
        return;
      }
      
      // otherwise, use the generic background
      gc.setFill(getBackgroundColor());
      gc.fillRect(0, 0, getHeight(), getWidth());
      
      // and draw the graphics depending on the type of card
      if (getCard().getCardType() == WegeCard.CardType.COSSACK) {
        drawDeadEnd(gc, getLandColor(), getCard().getOrientation());
        drawDeadEnd(gc, getWaterColor(), getCard().rotate(getCard().getOrientation()));
        drawDeadEnd(gc, getLandColor(), getCard().rotate(getCard().rotate(getCard().getOrientation())));
        drawDeadEnd(gc, getWaterColor(), getCard().rotate(getCard().rotate(getCard().rotate(getCard().getOrientation()))));
      }
      else if (getCard().getCardType() == WegeCard.CardType.BRIDGE) {
        drawPath(gc, getWaterColor(), getCard().rotate(getCard().getOrientation()));
        drawPath(gc, getBridgeColor(), getCard().getOrientation());
        drawDeadEnd(gc, getLandColor(), getCard().getOrientation());
        drawDeadEnd(gc, getLandColor(), getCard().rotate(getCard().rotate(getCard().getOrientation())));
      }
      else {
        Color pathColor, deadColor, gnomeColor, offGnomeColor;
        
        if (getCard().getCardType() == WegeCard.CardType.LAND) {
          pathColor = getLandColor();
          deadColor = getWaterColor();
          gnomeColor = getLandGnomeColor();
          offGnomeColor = getWaterGnomeColor();
        }
        else {
          pathColor = getWaterColor();
          deadColor = getLandColor();
          gnomeColor = getWaterGnomeColor();
          offGnomeColor = getLandGnomeColor();
        }
        
        drawPath(gc, pathColor, getCard().getOrientation());
        drawDeadEnd(gc, deadColor, getCard().rotate(getCard().getOrientation()));
        drawDeadEnd(gc, deadColor, getCard().rotate(getCard().rotate(getCard().rotate(getCard().getOrientation()))));
        
        if (getCard().hasGnome() && getCard().isPathGnome()) {
          drawGnome(gc, gnomeColor, getCard().getOrientation());
        }
        else if (getCard().hasGnome()) {
          drawGnome(gc, offGnomeColor, getCard().rotate(getCard().getOrientation()));
        } 
      }
    }
    
    /*
     * Draws a path or stream across a card using the given color and the given starting corner.
     */
    private void drawPath(GraphicsContext gc, Color color, Pos startCorner) {
      double start_x, start_y, end_x, end_y;
      
      switch (startCorner) {
        case TOP_LEFT: case BOTTOM_RIGHT:
          start_x = 0;
          start_y = 0;
          end_x   = getWidth() - 1;
          end_y   = getHeight() - 1;
          break;
        default:
          start_x = getWidth() - 1;
          start_y = 0;
          end_x   = 0;
          end_y   = getHeight() - 1;
      }
      gc.setStroke(color);
      gc.setLineWidth(getWidth() / 3);
      gc.strokeLine(start_x, start_y, end_x, end_y);
    }
    
    /* 
     * Draws a dead end for either a path or a stream at the corner of the card.
     */
    private void drawDeadEnd(GraphicsContext gc, Color color, Pos corner) {
      double x, y, height, width, arc_start;
      switch (corner) {
        case TOP_LEFT:
          x = - getWidth() / 4;
          y = - getHeight() / 4;
          arc_start = 270;
          break;
        case TOP_RIGHT:
          x = getWidth() - getWidth() / 4;
          y = - getHeight() / 4;
          arc_start = 180;
          break;
        case BOTTOM_RIGHT:
          x = getWidth() - getWidth() / 4;
          y = getHeight() - getHeight() / 4;
          arc_start = 90;
          break;
        default:
          x = - getWidth() / 4;
          y = getHeight() - getHeight() / 4;
          arc_start = 0;
      }
    
      gc.setFill(color);
      gc.fillArc(x, y, getWidth() / 2, getHeight() / 2, arc_start, 90, javafx.scene.shape.ArcType.ROUND);
    }
    
    /*
     * Draw a gnome in a given corner of the card.
     */
    private void drawGnome(GraphicsContext gc, Color color, Pos corner) {
      double x, y, width, height, rotation;
      
      switch (corner) {
        case TOP_LEFT:
          x = getWidth() / 8 - getWidth() / 12;
          y = getHeight() / 8 - getWidth() / 12;
          rotation = 45;
          break;
        case TOP_RIGHT:
          x = getWidth() - getWidth() / 8 - getWidth() / 12;
          y = getHeight() / 8 - getWidth() / 12;
          rotation = -45;
          break;
        case BOTTOM_LEFT:
          x = getWidth() / 8 - getWidth() / 12;
          y = getHeight() - getHeight() / 8 - getWidth() / 12;
          rotation = 135;
          break;
        default:
          x = getWidth() - getWidth() / 8 - getWidth() / 12;
          y = getHeight() - getHeight() / 8 - getWidth() / 12;
          rotation = -135;
      }
      
      gc.setFill(color);
      gc.fillOval(x, y, getWidth() / 6, getHeight() / 6);
    }
  }

}