/**
 * [Colours.java]
 * This program refers to all the colours that are used in the game.
 * The colours follow the order of RGB, but only use 6 values for each shade (from 0 to 5)
 * @author Braydon Wang, Dylan Wang
 * @version 1.0, Jan. 25, 2021
 */

class Colours {
  
  /**
   * This method changes the specified shades into an integer colour value
   * @param colour1 the darkest shade colour on the sprite sheet
   * @param colour2 the second darkest shade colour on the sprite sheet
   * @param colour3 the second lightest shade colour on the sprite sheet
   * @param colour4 the lightest shade colour on the sprite sheet
   * @return               the integer colour value
   */
  
  static int get(int colour1, int colour2, int colour3, int colour4) {
    return (get(colour4) << 24) + (get(colour3) << 16) + (get(colour2) << 8) + (get(colour1));
  }
  
  /**
   * This method seperates the RGB values of the integer colour value into its respective colours.
   * @param colour the integer colour value
   * @return             the new colour value seperated
   */
  
  static int get(int colour) {
    if (colour < 0) {
      return 255;
    }
    //the first digit is red, the second is green and the third is blue
    int r = colour/100 % 10;
    int g = colour/10 % 10;
    int b = colour % 10;
    
    return r*36 + g*6 + b;
  }
}