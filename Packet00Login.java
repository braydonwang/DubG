/* [Packet00Login.java]
 * The class of a packet that stores the information of a player when they login
 * @author Dylan Wang
 * @version 1.0, Jan 18, 2021
 */

class Packet00Login extends Packet {
  
  /** The player's username */
  private String username;
  /** The player's x coordinate */
  private int x;
  /** The player's y coordinate */
  private int y;
  
  /**
   * Creates an object from the Packet00Login class
   * @param data The data stored in a byte array
   */
  
  public Packet00Login(byte[] data) {
    super(00);
    String[] dataArray = readData(data).split(",");
    this.username = dataArray[0];
    this.x = Integer.parseInt(dataArray[1]);
    this.y = Integer.parseInt(dataArray[2]);
  }
  
  /**
   * Creates an object from the Packet00Login class
   * @param username The player's username
   * @param x The player's x coordinate
   * @param y The player's y coordinate
   */
  
  public Packet00Login(String username, int x, int y) {
    super(00);
    this.username = username;
    this.x = x;
    this.y = y;
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
    return ("00" + this.username +"," + getX() + "," + getY()).getBytes();
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
    return x;
  }
  
  /**
   * This method returns the player's y coordinate
   * @return The player's y coordinate
   */
  
  public int getY() {
    return y;
  }
}