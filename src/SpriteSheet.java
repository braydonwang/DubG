/*---------------- IMPORTS ---------------*/
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
/*----------------------------------------*/

/* [SpriteSheet.java]
 * The class of the sprite sheet that contains the pixel drawings
 * @author Dylan Wang
 * @version 1.0, Jan 18, 2021
 */

class SpriteSheet {
  
  /** The location of the spritesheet */
  String path;
  /** The width of the spritesheet */
  int width;
  /** The height of the spritesheet */
  int height;
  /** Stores each individual pixel colour of the spritesheet */
  int[] pixels;
  
  /**
   * Creates an object from the SpriteSheet class
   * @param path The location of the spritesheet
   */
  
  SpriteSheet(String path) {
    BufferedImage image = null;
    
    try {
      image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    if (image == null) {
      return;
    }
    
    this.path = path;
    this.width = image.getWidth();
    this.height = image.getHeight();
    
    pixels = image.getRGB(0,0,width,height,null,0,width);
    
    for (int i = 0; i < pixels.length; i++) {
      //to get rid of the alpha channel and since we only use four colours
      pixels[i] = (pixels[i] & 0xff) / 64;
    }
  }
}