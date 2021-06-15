/* [Packet12RageEnd.java]
 * The class of a packet that stores the information of a player when their rage ends
 * @author Dylan Wang
 * @version 1.0, Jan 18, 2021
 */

class Packet12RageEnd extends Packet {
  
  /** The player's username */
  private String userName;
  
  /**
   * Creates an object from the Packet12RageEnd class
   * @param data The data stored in a byte array
   */
  
  public Packet12RageEnd(byte[] data) {
    super(12);
    this.userName = readData(data);
  }
  
  /**
   * Creates an object from the Packet12RageEnd class
   * @param userName The player's username
   */
  
  public Packet12RageEnd(String userName) {
    super(12);
    this.userName = userName;
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
    return ("12" + this.userName).getBytes();
  }
  
  /**
   * This method returns the player's username
   * @return The player's username
   */
  
  public String getUsername() {
    return this.userName;
  }
}