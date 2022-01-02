/* [Packet11RageStart.java]
 * The class of a packet that stores the information of a player when their rage starts
 * @author Dylan Wang
 * @version 1.0, Jan 18, 2021
 */

class Packet11RageStart extends Packet {
  
  /** The player's username */
  private String userName;
  
  /**
   * Creates an object from the Packet11RageStart class
   * @param data The data stored in a byte array
   */
  
  public Packet11RageStart(byte[] data) {
    super(11);
    this.userName = readData(data);
  }
  
  /**
   * Creates an object from the Packet11RageStart class
   * @param userName The player's username
   */
  
  public Packet11RageStart(String userName) {
    super(11);
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
    return ("11" + this.userName).getBytes();
  }
  
  /**
   * This method returns the player's username
   * @return The player's username
   */
  
  public String getUsername() {
    return this.userName;
  }
}