/* [Turret.java]
 * The class of an in-game turret that constantly shoots
 * @author Dylan Wang
 * @version 1.0, Jan 18, 2021
 */

class Turret extends Entity {
  /** the direction the turret is facing */
  private int dir;
  /** the scale size */
  private int scale = 1;
  /** the colour */
  private int colour = Colours.get(111,222,411,432);
  /** the counter for each tick */
  private int tickCount;
  /** the last time a bullet was shot */
  public long lastBulletTime;
  /** the number of bullets */
  private int numBullets;
  
  /**
   * Creates an object from the Turret class
   * @param level The level that the turret is in
   * @param x The x coordinate of the turret
   * @param y The y coordinate of the turret
   * @param dir The direction the turret is facing
   */
  
  Turret(Level level, int x, int y, int dir) {
    super(level);
    this.x = x;
    this.y = y;
    this.tickCount = 0;
    this.dir = dir;
    this.lastBulletTime = System.currentTimeMillis();
  }
  
  /**
   * Creates an object from the Turret class
   * @param level The level that the turret is in
   * @param x The x coordinate of the turret
   * @param y The y coordinate of the turret
   * @param dir The direction the turret is facing
   * @param lastBulletTime The last time a bullet was shot
   */
  
  Turret(Level level, int x, int y, int dir, long lastBulletTime) {
    super(level);
    this.x = x;
    this.y = y;
    this.tickCount = 0;
    this.dir = dir;
    this.lastBulletTime = lastBulletTime;
  }
  
  /**
   * This method shoots a bullet based on the a reload time
   */
  
  public void tick() {
    tickCount++;
    
    //if the turret reload time passes, shoot a bullet
    if (System.currentTimeMillis() - this.lastBulletTime >= 1200) {
      this.lastBulletTime = System.currentTimeMillis();
      Bullet bullet = new Bullet(level,"Turret" + this.dir,this.x,this.y+4,2,this.dir,this.numBullets,1.0);
      int temp = this.dir == 0 ? 4 : 0;
      //send bullet data to server
      Packet03BulletEnter packet = new Packet03BulletEnter("Turret" + this.dir,this.x+temp,this.y+4,bullet.getSpeed(),bullet.getMovingDir(),this.numBullets,1.0);
      packet.writeData(DubG.game.socketClient);
      this.numBullets++;
    }
  }
  
  /**
   * This method renders/draws the turret onto the screen
   * @param screen The screen that is projected to the client
   */
  
  public void render(Screen screen) {
    //the appropriate x and y tile of the turret on the sprite sheet
    int xTile = this.dir * 2;
    int yTile = 10;
    int flip = 0;
    
    int modifier = 8 * scale;
    int xOffset = x - modifier/2, yOffset = y - modifier/2 - 4;
    
    //rendering all four images of the turret (split into quarters) to the screen
    screen.render(xOffset, yOffset, xTile + yTile * 32, colour, flip, scale);
    screen.render(xOffset + 8, yOffset, (xTile + 1) + yTile * 32, colour, flip, scale);
    screen.render(xOffset, yOffset + 8, xTile + (yTile + 1) * 32, colour, flip, scale);
    screen.render(xOffset + 8, yOffset + 8, (xTile + 1) + (yTile + 1) * 32, colour, flip, scale);
  }
}