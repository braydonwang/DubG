/**
 * [Bullet.java]
 * This program refers to all the bullets that are fired by the player and turrets.
 * All bullets have a speed, direction and an index to refer to who shot it.
 * @author Braydon Wang, Dylan Wang
 * @version 1.0, Jan. 25, 2021
 */

public class Bullet extends Entity {
  
  /** The username of the player who shot it */
  private String userName;
  /** The speed of the bullet */
  private int speed;
  /** The number of steps that the bullet took */
  private int numSteps = 0;
  /** Whether the bullet is moving or not */
  private boolean isMoving;
  /** The movement direction of the bullet */
  private int movingDir = 0;
  /** The scale that the bullet should be drawn at */
  private int scale = 1;
  /** The index of the bullet */
  private int id;
  /** The damage that the bullet deals */
  private double damage;
  /** The x tile of the bullet on the sprite sheet */
  private int xTileOG;
  /** The y tile of the bullet on the sprite sheet */
  private int yTileOG;
  /** The colour of the bullet */
  private int colour = Colours.get(000,111,-1,-1);
  
  /**
   * Creates an object from the Bullet class without x and y tile
   * @param level          the map level that is currently shown
   * @param userName the username of the entity who shot the bullet
   * @param x                the x coordinate of the bullet
   * @param y                the y coordinate of the bullet
   * @param speed        the speed of the bullet
   * @param movingDir the movement direction of the bullet
   * @param id               the index of the bullet for reference
   * @param damage     the damage that the bullet deals
   */
  
  Bullet (Level level, String userName, int x, int y, int speed, int movingDir, int id, double damage) {
    super(level);
    this.userName = userName;
    this.x = x;
    this.y = y;
    this.speed = speed;
    this.movingDir = movingDir;
    this.id = id;
    this.damage = damage;
    this.xTileOG = 0;
    this.yTileOG = 8;
  }
  
  /**
   * Creates an object from the Bullet class
   * @param level          the map level that is currently shown
   * @param userName the username of the entity who shot the bullet
   * @param x                the x coordinate of the bullet
   * @param y                the y coordinate of the bullet
   * @param speed        the speed of the bullet
   * @param movingDir the movement direction of the bullet
   * @param id               the index of the bullet for reference
   * @param damage     the damage that the bullet deals
   * @param xTileOG     the x tile of the bullet on the sprite sheet
   * @param yTile OG    the y tile of the bullet on the sprite sheet
   */
  
  Bullet (Level level, String userName, int x, int y, int speed, int movingDir, int id, double damage, int xTileOG, int yTileOG) {
    super(level);
    this.userName = userName;
    this.x = x;
    this.y = y;
    this.speed = speed;
    this.movingDir = movingDir;
    this.id = id;
    this.damage = damage;
    this.xTileOG = xTileOG;
    this.yTileOG = yTileOG;
  }

  /**
   * This method moves the bullet based on its direction.
   * @param xa the update needs to be done to the bullet's x coordinate
   * @param ya the update needs to be done to the bullet's y coordinate
   */
  
  public void move(int xa, int ya) {
    numSteps++;
    if (!hasCollided(xa,ya)) {
      x += xa * speed;
      y += ya * speed;
    } else {
      level.removeEntities.add(this);
      Packet04BulletLeave packet = new Packet04BulletLeave(this.userName,this.id);
      packet.writeData(DubG.game.socketClient);
    }
  }
  
  /**
   * This method checks if the bullet has collided with a player or a tile.
   * @param xa the update needs to be done to the bullet's x coordinate
   * @param ya the update needs to be done to the bullet's y coordinate
   */
  
  public boolean hasCollided(int xa, int ya) {    
    //the hitbox of the bullet in terms of x and y
    int xMin = -1, xMax = 0, yMin = -5, yMax = -2;
    if (movingDir == 2 || movingDir == 3) {
      xMin = -2; xMax = 1; yMin = -5; yMax = -4;
    }
    
    //checks if their is a solid tile inside of its hitbox
    for (int x = xMin; x < xMax; x++) {
      if (isSolidTile(xa, ya, x, yMin) || isSolidTile(xa, ya, x, yMax)) {
        return true;
      }
    }
    
    for (int y = yMin; y < yMax; y++) {
      if (isSolidTile(xa, ya, xMin, y) || isSolidTile(xa, ya, xMax, y)) {
        return true;
      }
    }
    
    boolean hitPlayer = false;
    
    //checks if the bullet collided with a player
    for (Entity e: level.getEntities()) {
      if (e instanceof Player) {
        if (!((Player)e).getUsername().equalsIgnoreCase(this.userName) && !((Player)e).getUsername().equals("SERVER")) {
          int tempMovingDir = ((Player)e).getMovingDir();
          //hitbox of the player
          int x2 = e.x, y2 = e.y, x3 = e.x, y3 = e.y;
          if (tempMovingDir == 0) {
            x2 -= 4; y2 += 5; x3 += 11; y3 -= 6;
          } else if (tempMovingDir == 1) {
            x2 -= 4; y2 += 6; x3 += 9; y3 -= 7;
          } else if (tempMovingDir == 2) {
            x2 -= 1; y2 += 9; x3 += 10; y3 -= 7;
          } else {
            x2 -= 4; y2 += 9; x3 += 9; y3 -= 7;
          }
          
          //checking if the two rectangular hit boxes overlap
          if (this.x + xa + xMin >= x3 || x2 >= this.x + xa + xMax) {
            continue;
          }
          if (this.y + ya + yMax <= y3 || y2 <= this.y + ya + yMin) {
            continue;
          }
          
          hitPlayer = true;
          //update the player's health
          ((Player)e).setHealth(((Player)e).getHealth() - this.damage);
          if (((Player)e).getUsername().equalsIgnoreCase(DubG.game.player.getUsername())) {
            DubG.sound("Hurt.wav");
          }
          break;
        }
      }
    }
    return hitPlayer;
  }
  
  /**
   * This method checks if the specified tile is solid or not.
   * @param xa the update needs to be done to the bullet's x coordinate
   * @param ya the update needs to be done to the bullet's y coordinate
   * @param x   the x coordinate of the specified tile
   * @param y   the y coordinate of the specified tile
   */
  
  protected boolean isSolidTile(int xa, int ya, int x, int y) {
    if (level == null) {
      return false;
    }
    //the last tile and the new tile with its respective coordinates
    Tile lastTile = level.getTile((this.x + x) >> 3, (this.y + y) >> 3);
    Tile newTile = level.getTile((this.x + x + xa) >> 3, (this.y + y + ya) >> 3);
    
    //checks if the new tile is a solid tile
    if (!lastTile.equals(newTile) && newTile.isSolid()) {
      if (newTile.id != 3) {
        if (newTile.id >= 10 && ((CrateTile)newTile).count < 5) {
          ((CrateTile)newTile).count++;
          
          Packet06Crate packet = new Packet06Crate(newTile.id);
          packet.writeData(DubG.game.socketClient);
          
          //creates a potion when the crate is destroyed
          if (((CrateTile)newTile).count >= 5) {
            int rand = (int)(Math.random() * 100);
            int randId = 0;
            
            if (rand <= 15) {
              randId = 0;
            } else if (rand > 15 && rand <= 30) {
              randId = 1;
            } else if (rand > 30 && rand <= 45) {
              randId = 2;
            } else {
              randId = 3;
            }

            //places the potion in the tiles over array and sends a packet to the server
            if (level.tilesOver[((this.x + x + xa) >> 3) + (((this.y + y + ya) >> 3) * level.width)] == null) {
              Potion p = new Potion(level,this.x + x + xa + 4, this.y + y + ya + 4, randId);
              level.tilesOver[((this.x + x + xa) >> 3) + (((this.y + y + ya) >> 3) * level.width)] = p;
              
              Packet07Potion packet2 = new Packet07Potion((this.x + x + xa) >> 3, (this.y + y + ya) >> 3, this.userName, randId);
              packet2.writeData(DubG.game.socketClient);
            }
          }
          return true;
        } else if (newTile.id <= 10) {
          return true;
        }
      }
    }
    return false;
  }
  
  /**
   * This method renders the bullet onto the screen.
   * @param screen the screen that will display the bullet.
   */
  
  public void render(Screen screen) {
    int xTile = xTileOG, yTile = yTileOG;
    int flip = 0;
    
    //updating the x and y tile of the bullet from the sprite sheet
    if (movingDir == 1) {
      flip = 2;
    } else if (movingDir == 2) {
      xTile++;
      flip = 1;
    } else if (movingDir == 3) {
      xTile++;
    }
    
    int modifier = 8 * scale;
    int xOffset = x - modifier/2, yOffset = y - modifier/2 - 4;
    
    if (yTile == 9) {
      colour = Colours.get(411,111,-1,-1);
    }
    screen.render(xOffset, yOffset, xTile + yTile * 32, colour, flip, scale);
  }
  
  /**
   * This method updates the bullets x and y coordinates for each tick.
   */
  
  public void tick() {
    int xa = 0;
    int ya = 0;
    
    //updating the coordinates of the bullet based on its direction
    if (movingDir == 0) {
      ya--;
    }
    else if (movingDir == 1) {
      ya++;
    }
    else if (movingDir == 2) {
      xa--;
    }
    else if (movingDir == 3) {
      xa++;
    }
    
    //moves the bullet and sends it to the server
    if (xa != 0 || ya != 0) {
      move(xa,ya);
      isMoving = true;
      
      Packet05BulletMove packet = new Packet05BulletMove(this.userName,this.x,this.y,this.numSteps,this.isMoving,this.movingDir,this.id);
      packet.writeData(DubG.game.socketClient);
    }
    else {
      isMoving = false;
    }
  }
  
  /**
   * This method returns the username of the bullet.
   * @return the username
   */
  
  public String getUsername() {
    return this.userName;
  }
  
  /**
   * This method returns the index of the bullet.
   * @return the index
   */
  
  public int getId() {
    return this.id;
  }
  
  /**
   * This method returns the speed of the bullet.
   * @return the speed
   */
  
  public int getSpeed() {
    return this.speed;
  }
  
  /**
   * This method returns the number of steps of the bullet.
   * @return the number of steps
   */
  
  public int getNumSteps() {
    return this.numSteps;
  }
  
  /**
   * This method returns whether the bullet is moving or not.
   * @return whether its moving or not
   */
  
  public boolean isMoving() {
    return this.isMoving;
  }
  
  /**
   * This method returns the direction of the bullet.
   * @return the direction
   */
  
  public int getMovingDir() {
    return this.movingDir;
  }
  
  /**
   * This method sets the number of steps of the bullet.
   * @param numSteps the new number of steps
   */
  
  public void setNumSteps(int numSteps) {
    this.numSteps = numSteps;
  }
  
  /**
   * This method sets the movement of the bullet.
   * @param isMoving whether the bullet is moving or not
   */
  
  public void setMoving(boolean isMoving) {
    this.isMoving = isMoving;
  }
  
  /**
   * This method sets the direction of the bullet.
   * @param movingDir the new direction of the bullet.
   */
  
  public void setMovingDir(int movingDir) {
    this.movingDir = movingDir;
  }
}