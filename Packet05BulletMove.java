/* [Packet05BulletMove.java]
 * The class of a packet that stores the information of a bullet when it moves
 * @author Dylan Wang
 * @version 1.0, Jan 18, 2021
 */

class Packet05BulletMove extends Packet {
  
  /** The player's username */
  private String username;
  /** The bullet's x coordinate */
  private int x;
  /** The bullet's x coordinate */
  private int y;
  /** The bullet's number of steps */
  private int numSteps = 0;
  /** Whether the bullet is moving */
  private boolean isMoving;
  /** The bullet's moving direction */
  private int movingDir = 1;
  /** The bullet's id */
  private int id;
  
  /**
   * Creates an object from the Packet05BulletMove class
   * @param data The data stored in a byte array
   */
  
  public Packet05BulletMove(byte[] data) {
    super(05);
    String[] dataArray = readData(data).split(",");
    this.username = dataArray[0];
    this.x = Integer.parseInt(dataArray[1]);
    this.y = Integer.parseInt(dataArray[2]);
    this.numSteps = Integer.parseInt(dataArray[3]);
    this.isMoving = Integer.parseInt(dataArray[4]) == 1;
    this.movingDir = Integer.parseInt(dataArray[5]);
    this.id = Integer.parseInt(dataArray[6]);
  }
  
  /**
   * Creates an object from the Packet05BulletMove class
   * @param userName The player's username
   * @param x The bullet's x coordinate
   * @param y The bullet's y coordinate
   * @param numSteps The bullet's number of steps
   * @param isMoving Whether the bullet is moving
   * @param movingDir The bullet's moving direction
   * @param id The bullet's id
   */
  
  public Packet05BulletMove(String username, int x, int y, int numSteps, boolean isMoving, int movingDir, int id) {
    super(05);
    this.username = username;
    this.x = x;
    this.y = y;
    this.numSteps = numSteps;
    this.isMoving = isMoving;
    this.movingDir = movingDir;
    this.id = id;
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
    return ("05" + this.username + "," + this.x + "," + this.y + "," + this.numSteps + "," + (isMoving?1:0) + "," + this.movingDir + "," + this.id).getBytes();
  }
  
  /**
   * This method returns the player's username
   * @return The player's username
   */
  
  public String getUsername() {
    return this.username;
  }
  
  /**
   * This method returns the bullet's x coordinate
   * @return The bullet's x coordinate
   */
  
  public int getX() {
    return this.x;
  }
  
  /**
   * This method returns the bullet's y coordinate
   * @return The bullet's y coordinate
   */
  
  public int getY() {
    return this.y;
  }
  
  /**
   * This method returns the bullet's id
   * @return The bullet's id
   */
  
  public int getId() {
    return this.id;
  }
  
  /**
   * This method returns the bullet's number of steps
   * @return The bullet's number of steps
   */
  
  public int getNumSteps() {
    return this.numSteps;
  }
  
  /**
   * This method returns whether the bullet is moving
   * @return Whether the bullet is moving
   */
  
  public boolean isMoving() {
    return this.isMoving;
  }
  
  /**
   * This method returns the bullet's moving direction
   * @return The bullet's moving direction
   */
  
  public int getMovingDir() {
    return this.movingDir;
  }
}