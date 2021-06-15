/* [Packet02Move.java]
 * The class of a packet that stores the information of a player when they move
 * @author Dylan Wang
 * @version 1.0, Jan 18, 2021
 */

class Packet02Move extends Packet {
  
  /** The player's username */
  private String username;
  /** The player's x coordinate */
  private int x;
  /** The player's y coordinate */
  private int y;
  /** The player's number of steps */
  private int numSteps = 0;
  /** The player's moving status */
  private boolean isMoving;
  /** The player's direction */
  private int movingDir = 1;
  
  /**
   * Creates an object from the Packet02Move class
   * @param data The data stored in a byte array
   */
  
  public Packet02Move(byte[] data) {
    super(02);
    String[] dataArray = readData(data).split(",");
    this.username = dataArray[0];
    this.x = Integer.parseInt(dataArray[1]);
    this.y = Integer.parseInt(dataArray[2]);
    this.numSteps = Integer.parseInt(dataArray[3]);
    this.isMoving = Integer.parseInt(dataArray[4]) == 1;
    this.movingDir = Integer.parseInt(dataArray[5]);
  }
  
  /**
   * Creates an object from the Packet02Move class
   * @param userName The player's username
   * @param x The player's x coordinate
   * @param y The player's y coordinate
   * @param numSteps The player's number of steps
   * @param isMoving The player's moving status
   * @param movingDir The player's moving direction
   */
  
  public Packet02Move(String username, int x, int y, int numSteps, boolean isMoving, int movingDir) {
    super(02);
    this.username = username;
    this.x = x;
    this.y = y;
    this.numSteps = numSteps;
    this.isMoving = isMoving;
    this.movingDir = movingDir;
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
    return ("02" + this.username + "," + this.x + "," + this.y + "," + this.numSteps + "," + (isMoving?1:0) + "," + this.movingDir).getBytes();
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
   * This method returns the player's number of steps
   * @return The player's x number of steps
   */
  
  public int getNumSteps() {
    return this.numSteps;
  }
  
  /**
   * This method returns the player's moving status
   * @return The player's moving status
   */
  
  public boolean isMoving() {
    return this.isMoving;
  }
  
  /**
   * This method returns the player's direction
   * @return The player's diirection
   */
  
  public int getMovingDir() {
    return this.movingDir;
  }
}