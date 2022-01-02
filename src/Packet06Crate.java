/* [Packet06Crate.java]
 * The class of a packet that stores the information of a crate being destroyed
 * @author Dylan Wang
 * @version 1.0, Jan 18, 2021
 */

class Packet06Crate extends Packet {
  
  /** The crate's id */
  private int id;
  /** The player's username */
  private String userName;
  
  /**
   * Creates an object from the Packet06Crate class
   * @param data The data stored in a byte array
   */
  
  public Packet06Crate(byte[] data) {
    super(06);
    String[] dataArray = readData(data).split(",");
    this.userName = dataArray[0];
    this.id = Integer.parseInt(dataArray[1]);
  }
  
  /**
   * Creates an object from the Packet06Crate class
   * @param id The id of the crate
   */
  
  public Packet06Crate(int id) {
    super(06);
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
    return ("06" + this.userName + "," + this.id).getBytes();
  }
  
  /**
   * This method returns the id of the crate
   * @return The crate's id
   */
  
  public int getId() {
    return this.id;
  }
  
  /**
   * This method returns the player's username
   * @return The player's username
   */
  
  public String getUsername() {
    return this.userName;
  }
}