/* -------- IMPORTS ---------- */
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/* -------------------------- */

/**
 * [InputHandler.java]
 * This program handles all of the input issued by the player.
 * This includes movement, potion usage and bullet shooting.
 * @author Braydon Wang, Dylan Wang
 * @version 1.0, Jan. 25, 2021
 */

class InputHandler implements KeyListener {
  
  /** The current player */
  Player player;
  
  /**
   * Creates an object from the Input Handler class
   * @param game the current game
   */
  
  InputHandler(DubG game) {
    game.addKeyListener(this);
  }
  
  /**
   * The method sets the player to a new player.
   * @param player the new player
   */
  
  public void setPlayer(Player player) {
    this.player = player;
  }
  
  /**
   * Key
   * This program refers to all of the keys that the player can press.
   * @author Braydon Wang, Dylan Wang
   * @version 1.0, Jan. 25, 2021
   */
  
  class Key {
    /** The number of times the key is pressed */
    private int numTimesPressed = 0;
    /** Whether the key is currently being pressed or not */
    private boolean pressed = false;
    
    /**
   * The method returns the number of times the key is pressed
   * @return the number of times pressed
   */
    
    public int getNumTimesPressed() {
      return numTimesPressed;
    }
    
    /**
   * The method returns whether the key is pressed or not.
   * @return is pressed or not
   */
    
    public boolean isPressed() {
      return pressed;
    }
    
    /**
   * The method updates the key depending on the status of it.
   * @param isPressed whether the key is pressed or not
   */
    
    public void toggle(boolean isPressed) {
      pressed = isPressed;
      if (isPressed) {
        numTimesPressed++;
      }
    }
    
  }
  
  Key up = new Key(), down = new Key(), left = new Key(), right = new Key(), shoot = new Key(), use = new Key();
  
  /**
   * The method checks if the key is pressed.
   * @param e the event of the key
   */
  
  public void keyPressed(KeyEvent e) {
    toggleKey(e.getKeyCode(),true);
  }
  
  /**
   * The method checks if the key is released.
   * @param e the event of the key
   */
  
  public void keyReleased(KeyEvent e) {
    toggleKey(e.getKeyCode(),false);
  }
  
  public void keyTyped(KeyEvent e) {
    
  }
  
  /**
   * The method updates eachkey based on the key code and the status of the key.
   * @param keyCode   the key currently referenced
   * @param isPressed the status of the key
   */
  
  public void toggleKey(int keyCode, boolean isPressed) {
    //toggling each key based on what button the user has pressed
    if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
      up.toggle(isPressed);
    }
    if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
      down.toggle(isPressed);
    }
    if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
      left.toggle(isPressed);
    }
    if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
      right.toggle(isPressed);
    }
    if (keyCode == KeyEvent.VK_SPACE) {
      if (isPressed) {
        //if the player's reload time has passed, the player may shoot
        if (System.currentTimeMillis() - player.getLastBulletTime() >= player.getReloadTime()) {
          player.setLastBulletTime(System.currentTimeMillis());
          shoot.toggle(isPressed);
        }
      } else {
        shoot.toggle(isPressed);
      }
    }
    if (keyCode == KeyEvent.VK_P) {
      if (isPressed) {
        //if the player's potion cooldown time has passed, the player may consume a potion
        if (System.currentTimeMillis() - player.getLastPotionTime() >= player.getPotionCD()) {
          player.setLastPotionTime(System.currentTimeMillis());
          use.toggle(isPressed);
        }
      } else {
        use.toggle(isPressed);
      }
    }
  }
}