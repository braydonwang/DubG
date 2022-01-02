/**
 * [CrateTile.java]
 * This program refers to the crate tiles on the map.
 * These crate tiles are all created as their own objects and are also animated when shot at.
 * @author Braydon Wang, Dylan Wang
 * @version 1.0, Jan. 25, 2021
 */

class CrateTile extends BasicSolidTile {
  
  /** The tile coordinates on the sprite sheet of the animated images */
  private int[][] animationTileCoords;
  /** The current index of the animation */
  private int currentAnimationIndex;
  /** The number of times the crate was shot */
  public int count;
  
  /**
   * Creates an object from the Crate Tile class
   * @param id                                 the tile index
   * @param animationCoords        the tile coordinates of each animation image
   * @param tileColour                    the colour of the tile that should be rendered
   * @param colour                          the colour of the tile on the map
   */
  
  CrateTile(int id, int[][] animationTileCoords, int tileColour, int colour) {
    super(id,animationTileCoords[0][0],animationTileCoords[0][1],tileColour,colour);
    this.animationTileCoords = animationTileCoords;
    this.currentAnimationIndex = 0;
    this.count = 0;
  }
  
  /**
   * This method ticks each time the loop is run and checks if the crate needs to be updated.
   */
  
  public void tick() {
    //renders the appropriate crate image based on how many bullets it has been hit by
    if (count >= 1 && count <= 2 && currentAnimationIndex == 0) {
      currentAnimationIndex++;
    } else if (count >= 3 && count <= 4 && currentAnimationIndex == 1) {
      currentAnimationIndex++;
    } else if (count >= 5 && currentAnimationIndex == 2) {
      currentAnimationIndex++;
      this.solid = false;
    }
    
    //updates the tile id with its new animation index
    this.tileId = (animationTileCoords[currentAnimationIndex][0] + (animationTileCoords[currentAnimationIndex][1] * 32));
  }
}