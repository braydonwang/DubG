/* [Packet07Potion.java]
 * The class of a packet that stores the information of a potion when it enters
 * @author Dylan Wang
 * @version 1.0, Jan 18, 2021
 */

class Packet07Potion extends Packet {
  
  /** The potion's x coordinate */
  private int x;
  /** The potion's y coordinate */
  private int y;
  /** The player's username */
  private String userName;
  /** The potion's id */
  private int id;
  
  /**
   * Creates an object from the Packet07Potion class
   * @param data The data stored in a byte array
   */
  
  public Packet07Potion(byte[] data) {
    super(07);
    String[] dataArray = readData(data).split(",");
    this.x = Integer.parseInt(dataArray[0]);
    this.y = Integer.parseInt(dataArray[1]);
    this.userName = dataArray[2];
    this.id = Integer.parseInt(dataArray[3]);
  }
  
  /**
   * Creates an object from the Packet07Potion class
   * @param x The potion's x coordinate
   * @param y The potion's y coordinate
   * @param userName The player's username
   * @param The potion's id
   */
  
  public Packet07Potion(int x, int y, String userName, int id) {
    super(07);
    this.x = x;
    this.y = y;
    this.userName = userName;
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
    return ("07" + this.x + "," + this.y + "," + this.userName + "," + this.id).getBytes();
  }
  
  /**
   * This method returns the x coordinate of the potion
   * @return The potion's x coordinate
   */
  
  public int getX() {
    return this.x;
  }
  
  /**
   * This method returns the y coordinate of the potion
   * @return The potion's y coordinate
   */
  
  public int getY() {
    return this.y;
  }
  
  /**
   * This method returns the player's username
   * @return The player's username
   */
  
  public String getUsername() {
    return this.userName;
  }
  
  /**
   * This method returns the id of the potion
   * @return The potion's id
   */
  
  public int getId() {
    return this.id;
  }
}