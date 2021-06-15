/* [Packet01Disconnect.java]
 * The class of a packet that stores the information of a player when they disconnect
 * @author Dylan Wang
 * @version 1.0, Jan 18, 2021
 */

class Packet01Disconnect extends Packet {
  
  /** The player's username */
  private String username;
  
  /**
   * Creates an object from the Packet01Disconnect class
   * @param data The data stored in a byte array
   */
  
  public Packet01Disconnect(byte[] data) {
    super(01);
    this.username = readData(data);
  }
  
  /**
   * Creates an object from the Packet01Disconnect class
   * @param username The player's username
   */
  
  public Packet01Disconnect(String username) {
    super(01);
    this.username = username;
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
    return ("01" + this.username).getBytes();
  }
  
  /**
   * This method returns the player's username
   * @return The player's username
   */
  
  public String getUsername() {
    return this.username;
  }
}