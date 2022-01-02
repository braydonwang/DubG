/**
 * [AnimatedTile.java]
 * This program refers to the animated tiles on the map.
 * In particular, it helps render and animate the water tiles through the tick function.
 * @author Braydon Wang, Dylan Wang
 * @version 1.0, Jan. 25, 2021
 */

class AnimatedTile extends BasicSolidTile {
  
  /** The tile coordinates on the sprite sheet of the animated images */
  private int[][] animationTileCoords;
  /** Stores the current animation index referring to the index of the animation images */
  private int currentAnimationIndex;
  /** The last time the animation was updated */
  private long lastIterationTime;
  /** The delay between each successive animation */
  private int animationSwitchDelay;
  
  /**
   * Creates an object from the Animated Tile class
   * @param id                                 the tile index
   * @param animationCoords        the tile coordinates of each animation image
   * @param tileColour                    the colour of the tile that should be rendered
   * @param levelColour                  the colour of the tile on the map
   * @param animationSwitchDelay the delay between each animation
   */
  
  public AnimatedTile(int id, int[][] animationCoords, int tileColour, int levelColour, int animationSwitchDelay) {
    super(id, animationCoords[0][0], animationCoords[0][1], tileColour, levelColour);
    this.animationTileCoords = animationCoords;
    this.currentAnimationIndex = 0;
    this.lastIterationTime = System.currentTimeMillis();
    this.animationSwitchDelay = animationSwitchDelay;
  }
  
  /**
   * This method ticks each time the loop is run and checks if the animation needs to be updated.
   */
  
  public void tick() {
    //updating the animation when the delay is reached
    if ((System.currentTimeMillis() - lastIterationTime) >= (animationSwitchDelay)) {
      lastIterationTime = System.currentTimeMillis();
      currentAnimationIndex = (currentAnimationIndex + 1) % animationTileCoords.length;
      this.tileId = (animationTileCoords[currentAnimationIndex][0] + (animationTileCoords[currentAnimationIndex][1] * 32));
    }
  }
}