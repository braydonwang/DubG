/*---------------- IMPORTS ---------------*/
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
/*----------------------------------------*/

/* [WindowHandler.java]
 * Handles the closing of the program
 * @author Dylan Wang
 * @version 1.0, Jan 18, 2021
 */

class WindowHandler implements WindowListener {
  
  /** Stores the actual game */
  private final DubG game;
  
  /**
   * Creates an object from the WindowHandler class
   * @param game The actual game
   */
  
  WindowHandler(DubG game) {
    this.game = game;
    this.game.frame.addWindowListener(this);
  }
  
  public void windowActivated(WindowEvent event) {
    
  }
  
  public void windowClosed(WindowEvent event) {
    
  }
  
  /**
   * This method checks to see if the window is closing
   * If the window is closing, write this data to the server
   * @param event The action event that diplays if the window is closing
   */
  
  public void windowClosing(WindowEvent event) {
    Packet01Disconnect packet = new Packet01Disconnect(this.game.player.getUsername());
    packet.writeData(this.game.socketClient);
  }
  
  public void windowDeactivated(WindowEvent event) {
    
  }
  
  public void windowDeiconified(WindowEvent event) {
    
  }
  
  public void windowIconified(WindowEvent event) {
    
  }
  
  public void windowOpened(WindowEvent event) {
    
  }
}