/* [Potion.java]
 * The class of an in-game potion
 * @author Dylan Wang
 * @version 1.0, Jan 18, 2021
 */

class Potion extends Entity {
  /** The id of the potion */
  private int id;
  /** The scale size of the potion */
  private int scale = 1;
  /** The colour of the potion */
  private int colour;
  /** The counter for each tick */
  private int tickCount;
  
  /**
   * Creates an object from the Potion class
   * @param level The level that the potion is in
   * @param x The x position of the potion
   * @param y The y position of the potion
   * @param id The id of the potion
   */
  
  Potion(Level level, int x, int y, int id) {
    super(level);
    this.x = x;
    this.y = y;
    this.tickCount = 0;
    this.id = id;

    if (this.id == 0) {
      colour = Colours.get(000, 411, 320, -1);
    } else if (this.id == 1) {
      colour = Colours.get(000, 204, 320, -1);
    } else if (this.id == 2) {
      colour = Colours.get(000, 044, 320, -1);
    } else if (this.id == 3) {
      colour = Colours.get(000, 055, 320, -1);
    }
  }
  
  /**
   * This method increases tickcount by 1 each tick
   */
  
  public void tick() {
    tickCount++;
  }
  
  /**
   * This method draws the potion to the screen
   * @param screen The screen projected to the client
   */
  
  public void render(Screen screen) {
    int xTile = 0;
    int yTile = 7;
    int flip = 0;
    
    int modifier = 8 * scale;
    int xOffset = x - modifier/2, yOffset = y - modifier/2 - 4;
    
    if (tickCount  >= 30 && tickCount <= 60) {
      yOffset--;
    } else if (tickCount >= 90 && tickCount <= 120) {
      yOffset++;
    } else if (tickCount > 120) {
      tickCount = 0;
    }
    
    //displays potion
    screen.render(xOffset, yOffset, xTile + yTile * 32, colour, flip, scale);
  }
  
  /**
   * This method returns the id of the potion
   * @return The id of the potion
   */
  
  public int getId() {
    return this.id;
  }
}