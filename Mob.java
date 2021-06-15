/**
 * [Level.java]
 * This abstract class deals with the mobs in the game.
 * These mobs refer to all entities that can move.
 * @author Braydon Wang, Dylan Wang
 * @version 1.0, Jan. 25, 2021
 */

abstract class Mob extends Entity {
  
  /** The name of the mob */
  protected String name;
  /** The speed of the mob */
  protected int speed;
  /** The number of steps */
  protected int numSteps = 0;
  /** The movement status */
  protected boolean isMoving;
  /** The movement direction */
  protected int movingDir = 1;
  /** The scale to be drawn */
  protected int scale = 1;
  
  /**
   * Creates an object from the Mob class
   * @param level   the current map level
   * @param name the name of the mob
   * @param x        the x coordinate
   * @param y        the y coordinate
   * @param speed the speed of the mob
   */
  
  Mob(Level level, String name, int x, int y, int speed) {
    super(level);
    this.name = name;
    this.x = x;
    this.y = y;
    this.speed = speed;
  }
  
  /**
   * This method moves the mob based on changes with its x and y coordinate
   * @param xa the change in x
   * @param ya the change in y
   */
  
  public void move(int xa, int ya) {
    //moving the mob x and y independently
    if (xa != 0 && ya != 0) {
      move(xa,0); move(0,ya);
      //decreased the number of steps because of over counting
      numSteps--;
      return;
    } 
    numSteps++;
    
    //check if the mob will collide with the changes in x and y
    if (!hasCollided(xa,ya)) {
      if (ya < 0) {
        movingDir = 0;
      }
      if (ya > 0) {
        movingDir = 1;
      }
      if (xa < 0) {
        movingDir = 2;
      }
      if (xa > 0) {
        movingDir = 3;
      }
      //increase both x and y coordinates based on its speed
      x += xa * speed;
      y += ya * speed;
    }
  }
  
  /**
   * This method checks if the mob has collided with changes to its x and y
   * @param xa the change in x
   * @param ya the change in y
   */
  
  public abstract boolean hasCollided(int xa, int ya);
  
  /**
   * This method checks if the change in x and y refers to a solid tile
   * @param xa the change in x
   * @param ya the change in y
   * @param x   the x coordinate of the current tile
   * @param y   the y coordinate of the current tile
   */
  
  protected boolean isSolidTile(int xa, int ya, int x, int y) {
    if (level == null) {
      return false;
    }
    //getting the last and the new tile with the changes in x and y and the coordinates
    Tile lastTile = level.getTile((this.x + x) >> 3, (this.y + y) >> 3);
    Tile newTile = level.getTile((this.x + x + xa) >> 3, (this.y + y + ya) >> 3);
    if (!lastTile.equals(newTile) && newTile.isSolid()) {
      return true;
    }
    return false;
  }
  
  /**
   * This method gets the potion at the specified change in x and y
   * @param xa the change in x
   * @param ya the change in y
   * @param the x coordinate
   * @param the y coordinate
   */
  
  protected Potion getPotion(int xa, int ya, int x, int y) {
    return level.getPotion((this.x + x + xa) >> 3, (this.y + y + ya) >> 3);
  }
  
   /**
   * This method sets the potion at the specified change in x and y
   * @param xa the change in x
   * @param ya the change in y
   * @param the x coordinate
   * @param the y coordinate
   * @param userName the name of the player
   */
  
  protected void setPotion(int xa, int ya, int x, int y, String userName) {
    level.setPotion((this.x + x + xa) >> 3, (this.y + y + ya) >> 3,-1);
    Packet07Potion packet = new Packet07Potion((this.x + x + xa) >> 3,(this.y + y + ya) >> 3, userName,-1);
    packet.writeData(DubG.game.socketClient);
  }
  
   /**
   * This method returns the name of the mob
   * @return the name
   */
  
  public String getName() {
    return this.name;
  }
  
  /**
   * This method returns the number of steps of the mob
   * @return the number of steps
   */
  
  public int getNumSteps() {
    return this.numSteps;
  }
  
  /**
   * This method returns the moving status of the mob
   * @return the moving status
   */
  
  public boolean isMoving() {
    return this.isMoving;
  }
  
  /**
   * This method returns the movement direction of the mob
   * @return the movement direction
   */
  
  public int getMovingDir() {
    return this.movingDir;
  }
  
  /**
   * This method sets the number of steps of the mob
   * @param numSteps the new number of steps
   */
  
  public void setNumSteps(int numSteps) {
    this.numSteps = numSteps;
  }
  
  /**
   * This method sets the new moving status of the mob
   * @param isMoving the new moving status
   */
  
  public void setMoving(boolean isMoving) {
    this.isMoving = isMoving;
  }
  
  /**
   * This method sets the new moving direction of the mob
   * @param movingDir the new moving direction
   */
  
  public void setMovingDir(int movingDir) {
    this.movingDir = movingDir;
  }
}