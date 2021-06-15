/*---------------- IMPORTS ---------------*/
import java.net.InetAddress;
/*----------------------------------------*/

/* [PlayerMP.java]
 * The class of a multiplayer player
 * @author Dylan Wang
 * @version 1.0, Jan 18, 2021
 */

class PlayerMP extends Player {
  
  /** The ip address */
  public InetAddress ipAddress;
  /** The port number */
  public int port;
  
  /**
   * Creates an object from the PlayerMP class
   * @param level The level that the player is in
   * @param x The x position of the player
   * @param y The y position of the player
   * @param input The class that handles the player's input
   * @param username The player's username
   * @param reloadTime The player's reload time
   * @param ipAddress The ip address of the player
   * @param port The port number of the player
   */
  
  PlayerMP(Level level, int x, int y, InputHandler input, String username, int reloadTime, InetAddress ipAddress, int port) {
    super(level, x, y, input, username,reloadTime);
    this.ipAddress = ipAddress;
    this.port = port;
  }
  
  /**
   * Creates an object from the PlayerMP class
   * @param level The level that the player is in
   * @param x The x position of the player
   * @param y The y position of the player
   * @param username The player's username
   * @param reloadTime The player's reload time
   * @param ipAddress The ip address of the player
   * @param port The port number of the player
   */
  
  PlayerMP(Level level, int x, int y, String username, int reloadTime, InetAddress ipAddress, int port) {
    super(level, x, y, null, username,reloadTime);
    this.ipAddress = ipAddress;
    this.port = port;
  }
  
  /**
   * This method handles each tick (same as player)
   */
  
  public void tick() {
    super.tick();
  }
  
}