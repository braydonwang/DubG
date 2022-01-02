/**
 * [BasicTile.java]
 * This program refers to basic tiles on the map.
 * This refers to all tiles shown on the map.
 * @author Braydon Wang, Dylan Wang
 * @version 1.0, Jan. 25, 2021
 */

class BasicTile extends Tile {
  
  /** The index of the tile */
  protected int tileId;
  /** The colour of the tile that is rendered */
  protected int tileColour;
  
  /**
   * Creates an object from the Basic Tile class
   * @param id              the tile index
   * @param x               the x coordinate of the tile on the map
   * @param y               the y coordinate of the tile on the map
   * @param tileColour the colour of tile that should be rendered
   * @param colour       the colour of the tile that shows up on the map
   */
  
  BasicTile(int id, int x, int y, int tileColour, int colour) {
    super(id,false,false,colour);
    this.tileId = x + y * 32;
    this.tileColour = tileColour;
  }
  
  /**
   * This method ticks each time the loop is run.
   */
  
  public void tick() {
    
  }
  
  /**
   * This method updates the screen with what needs to be rendered.
   * @param screen the screen that the image is drawn to
   * @param level    the map level that is currently shown
   * @param x          the x coordinates of the tile
   * @param y          the y coordinates of the tile
   */
  
  public void render(Screen screen, Level level, int x, int y) {
    screen.render(x,y,tileId,tileColour,0,1);
  }
}