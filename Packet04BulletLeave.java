/* [Packet04BulletLeave.java]
 * The class of a packet that stores the information of a bullet when it dissappears
 * @author Dylan Wang
 * @version 1.0, Jan 18, 2021
 */

class Packet04BulletLeave extends Packet {
  
  /** The player's username */
  private String username;
  /** The bullet's id */
  private int id;
  
  /**
   * Creates an object from the Packet04BulletLeave class
   * @param data The data stored in a byte array
   */
  
  public Packet04BulletLeave(byte[] data) {
    super(04);
    String[] dataArray = readData(data).split(",");
    this.username = dataArray[0];
    this.id = Integer.parseInt(dataArray[1]);
  }
  
  /**
   * Creates an object from the Packet04BulletLeave class
   * @param userName The player's username
   * @param id The bullet's id
   */
  
  public Packet04BulletLeave(String username, int id) {
    super(04);
    this.username = username;
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
    return ("04" + this.getUsername() + "," + this.getId()).getBytes();
  }
  
  /**
   * This method returns the player's username
   * @return The player's username
   */
  
  public String getUsername() {
    return this.username;
  }
  
  /**
   * This method returns the bullet's id
   * @return The bullet's id
   */
  
  public int getId() {
    return this.id;
  }
}