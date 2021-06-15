/**
 * [BasicSolidTile.java]
 * This program refers to basic solid tiles on the map.
 * These tiles in particular cannot be walked on and block bullets.
 * @author Braydon Wang, Dylan Wang
 * @version 1.0, Jan. 25, 2021
 */

class BasicSolidTile extends BasicTile {
  
  /**
   * Creates an object from the Basic Solid Tile class
   * @param id              the tile index
   * @param x               the x coordinate of the tile on the map
   * @param y               the y coordinate of the tile on the map
   * @param tileColour the colour of tile that should be rendered
   * @param colour       the colour of the tile that shows up on the map
   */
  
  BasicSolidTile(int id, int x, int y, int tileColour, int colour) {
    super(id,x,y,tileColour,colour);
    this.solid = true;
  }
}