/* [Screen.java]
 * The class of the screen projected to the client
 * @author Dylan Wang
 * @version 1.0, Jan 18, 2021
 */

class Screen {
  
  /** The width of the map */
  static final int MAP_WIDTH = 64;
  /** The masked width of the map */
  static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;
  /** The x reflection */
  static final byte BIT_MIRROR_X = 0x01;
  /** The y reflection */
  static final byte BIT_MIRROR_Y = 0x02;
  
  /** An array of each pixel on the screen */
  int[] pixels;
  
  /** The x offset */
  int xOffset = 0;
  /** The y offset */
  int yOffset = 0;
  
  /** The width of the screen */
  int width;
  /** The height of the screen */
  int height;
  
  /** The spritesheet which contains the pixel drawings */
  SpriteSheet sheet;
  
  /**
   * Creates an object from the Screen class
   * @param width The width of the screen
   * @param height The height of the screen
   * @param sheet The spritesheet which contains the pixel drawings
   */
  
  Screen(int width, int height, SpriteSheet sheet) {
    this.width = width;
    this.height = height;
    this.sheet = sheet;
    
    pixels = new int[width * height];
  }
  
  /**
   * This method draws the pixels to the screen
   * @param xPos The x position 
   * @param yPos The y position
   * @param tile The specific tile
   * @param colour The specific colour
   * @param mirrorDir The reflection direction
   * @param scale The scale size
   */
  
  public void render(int xPos, int yPos, int tile, int colour, int mirrorDir, int scale) {
    //updating the coordinates based on the screen offset
    xPos -= xOffset;
    yPos -= yOffset;
    
    //intializing for mirrored/flipped images based on its mirror direction
    boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
    boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;
      
    //initializing the scale of the map, the x and y tiles and the tile offsets
    int scaleMap = scale - 1;
    int xTile = tile % 32;
    int yTile = tile / 32;
    int tileOffset = (xTile << 3) + (yTile << 3) * sheet.width;
    
    //for every pixel on the sprite sheet's grids (8 x 8)
    for (int y = 0; y < 8; y++) {
      int ySheet = y;
      //mirror along the y axis
      if (mirrorY) {
        ySheet = 7 - y;
      }
      int yPixel = y + yPos + (y * scaleMap) - ((scaleMap << 3) / 2);
      for (int x = 0; x < 8; x++) {
        int xSheet = x;
        //mirror along the x axis
        if (mirrorX) {
          xSheet = 7 - x;
        }
        int xPixel = x + xPos + (x * scaleMap) - ((scaleMap << 3) / 2);
        
        //getting the appropriate colour from the pixel sheet 
        int col = (colour >> (sheet.pixels[xSheet + ySheet * sheet.width + tileOffset] * 8)) & 255;
        if (col < 255) {
          for (int yScale = 0; yScale < scale; yScale++) {
            //checking that the pixel is within the bounds of the screen
            if (yPixel + yScale < 0 || yPixel + yScale >= height) {
              continue;
            }
            for (int xScale = 0; xScale < scale; xScale++) {
              //checking that the pixel is within the bounds of the screen
              if (xPixel + xScale < 0 || xPixel + xScale >= width) {
                continue;
              }
              //setting the position of the pixel to be this certain colour
              pixels[(xPixel + xScale) + (yPixel + yScale) * width] = col;
            }
          }
        }
      }
    }
  }
  
  /**
   * This method sets the x offset and y offset
   * @param xOffset The x offset
   * @param yOffset The y offset
   */
  
  public void setOffset(int xOffset, int yOffset) {
    this.xOffset = xOffset;
    this.yOffset = yOffset;
  }
}