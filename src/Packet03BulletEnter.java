/* [Packet03BulletEnter.java]
 * The class of a packet that stores the information of a bullet when it enters
 * @author Dylan Wang
 * @version 1.0, Jan 18, 2021
 */

class Packet03BulletEnter extends Packet {
  
  /** The player's username */
  private String username;
  /** The player's x coordinate */
  private int x;
  /** The player's y coordinate */
  private int y;
  /** The player's speed */
  private int speed;
  /** The player's direction */
  private int movingDir;
  /** The player's id */
  private int id;
  /** The player's damage */
  private double damage;
  /** The player's x tile */
  private int xTile;
  /** The player's y tile */
  private int yTile;
  
  /**
   * Creates an object from the Packet03BulletEnter class
   * @param data The data stored in a byte array
   */
  
  public Packet03BulletEnter(byte[] data) {
    super(03);
    String[] dataArray = readData(data).split(",");
    this.username = dataArray[0];
    this.x = Integer.parseInt(dataArray[1]);
    this.y = Integer.parseInt(dataArray[2]);
    this.speed = Integer.parseInt(dataArray[3]);
    this.movingDir = Integer.parseInt(dataArray[4]);
    this.id = Integer.parseInt(dataArray[5]);
    this.damage = Double.parseDouble(dataArray[6]);
    this.xTile = Integer.parseInt(dataArray[7]);
    this.yTile = Integer.parseInt(dataArray[8]);
  }
  
  /**
   * Creates an object from the Packet03BulletEnter class
   * @param userName The player's username
   * @param x The bullet's x coordinate
   * @param y The bullet's y coordinate
   * @param speed The bullet's speed
   * @param movingDir The bullet's moving direction
   * @param id The bullet's id
   * @param damage The bullet's damage
   */
  
  public Packet03BulletEnter(String username, int x, int y, int speed, int movingDir, int id, double damage) {
    super(03);
    this.username = username;
    this.x = x;
    this.y = y;
    this.speed = speed;
    this.movingDir = movingDir;
    this.id = id;
    this.damage = damage;
    this.xTile = 0;
    this.yTile = 8;
  }
  
  /**
   * Creates an object from the Packet03BulletEnter class
   * @param userName The player's username
   * @param x The bullet's x coordinate
   * @param y The bullet's y coordinate
   * @param speed The bullet's speed
   * @param movingDir The bullet's moving direction
   * @param id The bullet's id
   * @param damage The bullet's damage
   * @param xTile The bullet's x tile position on the spritesheet
   * @param yTile The bullet's y tile position on the spritesheet
   */
  
  public Packet03BulletEnter(String username, int x, int y, int speed, int movingDir, int id, double damage, int xTile, int yTile) {
    super(03);
    this.username = username;
    this.x = x;
    this.y = y;
    this.speed = speed;
    this.movingDir = movingDir;
    this.id = id;
    this.damage = damage;
    this.xTile = xTile;
    this.yTile = yTile;
  }
  
  /**
   * This method writes the packet data to the client
   * @param client The client whom is recieving the data
   */
  
  public void writeData(GameClient client) {
    client.sendData(getData());
  }
  
  /**
   * This method writes the packet data to the server
   * @param server The server whom is recieving the data
   */
  
  public void writeData(GameServer server) {
    server.sendDataToAllClients(getData());
  }
  
  /**
   * This method returns the data in an efficiently stored byte array
   * @return The data in a byte array
   */
  
  public byte[] getData() {
    return ("03" + this.username +"," + getX() + "," + getY() + "," + getSpeed() + "," + getMovingDir() + "," + getId() + "," + getDamage() + "," + getXTile() + "," + getYTile()).getBytes();
  }
  
  /**
   * This method returns the player's username
   * @return The player's username
   */
  
  public String getUsername() {
    return this.username;
  }
  
  /**
   * This method returns the player's x coordinate
   * @return The player's x coordinate
   */
  
  public int getX() {
    return this.x;
  }
  
  /**
   * This method returns the player's y coordinate
   * @return The player's y coordinate
   */
  
  public int getY() {
    return this.y;
  }
  
  /**
   * This method returns the player's speed
   * @return The player's speed
   */
  
  public int getSpeed() {
    return this.speed;
  }
  
  /**
   * This method returns the player's direction
   * @return The player's direction
   */
  
  public int getMovingDir() {
    return this.movingDir;
  }
  
  /**
   * This method returns the player's id
   * @return The player's id
   */
  
  public int getId() {
    return this.id;
  }
  
  /**
   * This method returns the player's damage
   * @return The player's damage
   */
  
  public double getDamage() {
    return this.damage;
  }
  
  /**
   * This method returns the player's x tile
   * @return The player's x tile
   */
  
  public int getXTile() {
    return this.xTile;
  }
  
  /**
   * This method returns the player's y tile
   * @return The player's y tile
   */
  
  public int getYTile() {
    return this.yTile;
  }
}