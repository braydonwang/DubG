/* [Packet20ChangeClass.java]
 * The class of a packet that stores the information of a player change of class
 * @author Dylan Wang
 * @version 1.0, Jan 18, 2021
 */

class Packet20ChangeClass extends Packet {
  
  /** The player's username */
  private String username;
  /** The player's new class type */
  private int classType;
  /** The colour of the player */
  private int colour;
  /** The player's x tile position */
  private int xTileOG;
  /** The player's y tile position */
  private int yTileOG;
  
  /**
   * Creates an object from the Packet20ChangeClass class
   * @param data The data stored in a byte array
   */
  
  public Packet20ChangeClass(byte[] data) {
    super(20);
    String[] dataArray = readData(data).split(",");
    this.username = dataArray[0];
    this.classType = Integer.parseInt(dataArray[1]);
    this.colour = Integer.parseInt(dataArray[2]);
    this.xTileOG = Integer.parseInt(dataArray[3]);
    this.yTileOG = Integer.parseInt(dataArray[4]);
  }
  
  /**
   * Creates an object from the Packet20ChangeClass class
   * @param username The player's username
   * @param classType The player's class type
   * @param colour The colour of the player
   * @param xTileOG The player's x tile position
   * @param yTileOG The player's y tile position
   */
  
  public Packet20ChangeClass(String username, int classType, int colour, int xTileOG, int yTileOG) {
    super(20);
    this.username = username;
    this.classType = classType;
    this.colour = colour;
    this.xTileOG = xTileOG;
    this.yTileOG = yTileOG;
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
    return ("20" + this.getUsername() + "," + this.getClassType() + "," + this.getColour() + "," + this.getXTile() + "," + this.getYTile()).getBytes();
  }
  
  /**
   * This method returns the player's username
   * @return The player's username
   */
  
  public String getUsername() {
    return this.username;
  }
  
  /**
   * This method returns the player's class type
   * @return The player's class type
   */
  
  public int getClassType() {
    return this.classType;
  }
  
  /**
   * This method returns the colour of the player
   * @return The colour of the player
   */
  
  public int getColour() {
    return this.colour;
  }
  
  /**
   * This method returns the player's x tile position
   * @return The player's x tile position
   */
  
  public int getXTile() {
    return this.xTileOG;
  }
  
  /**
   * This method returns the player's y tile position
   * @return The player's y tile position
   */
  
  public int getYTile() {
    return this.yTileOG;
  }
}