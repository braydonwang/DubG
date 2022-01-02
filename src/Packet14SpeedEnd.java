/* [Packet14SpeedEnd.java]
 * The class of a packet that stores the information of a player when their speed boost ends
 * @author Dylan Wang
 * @version 1.0, Jan 18, 2021
 */

class Packet14SpeedEnd extends Packet {
  
  /** The player's username */
  private String userName;
  
  /**
   * Creates an object from the Packet14SpeedEnd class
   * @param data The data stored in a byte array
   */
  
  public Packet14SpeedEnd(byte[] data) {
    super(14);
    this.userName = readData(data);
  }
  
  /**
   * Creates an object from the Packet14SpeedEnd class
   * @param userName The player's username
   */
  
  public Packet14SpeedEnd(String userName) {
    super(14);
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
    return ("14" + this.userName).getBytes();
  }
  
  /**
   * This method returns the player's username
   * @return The player's username
   */
  
  public String getUsername() {
    return this.userName;
  }
}