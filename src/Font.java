/**
 * [Font.java]
 * This class deals with the custom font that is drawn to the screen.
 * The font is made in the sprite sheet and includes all capital letters, numbers and symbols.
 * @author Braydon Wang, Dylan Wang
 * @version 1.0, Jan. 25, 2021
 */

class Font {
  
  /** The order of the string of characters that the font is in */
  private static String chars =  "ABCDEFGHIJKLMNOPQRSTUVWXYZ      " + "0123456789.,:;'\"!?$%()-=+/      ";
    
  /**
   * This method renders the font on to the screen.
   */
  
  static void render(String msg, Screen screen, int x, int y, int colour, int scale) {
    msg = msg.toUpperCase();
    
    //depending on the position of the character in the character order, display the letter
    for (int i = 0; i < msg.length(); i++) {
      //index of character in string of characters
      int charIndex = chars.indexOf(msg.charAt(i));
      if (charIndex >= 0) {
        //rendering the font on to the screen
        screen.render(x + (i*scale*8), y, charIndex + 30 * 32,colour,0,scale);
      }
    }
  }
}