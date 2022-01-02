/**
 * [Packet.java]
 * This abstract class deals with all the packets sent between the clients and the servers.
 * They are all identified by a 2 digit code in their name.
 * @author Braydon Wang, Dylan Wang
 * @version 1.0, Jan. 25, 2021
 */

abstract class Packet {
  
  /** The types of each packet */
  public static enum PacketTypes {
    INVALID(-1), LOGIN(00), DISCONNECT(01), MOVE(02), ENTER(03), LEAVE(04), 
      BULLET(05), CRATE(06), POTION(07), HEALTH(10), RAGESTART(11), RAGEEND(12), 
      SPEEDSTART(13), SPEEDEND(14), HEALSTOP(15), EXPSTART(16), EXPEND(17), CLASS(20);
    
    /** The index of the packet */
    private int packetId;
    
    /**
   * This method sets the id of the packet to a new id
   * @param packetId the new packet id
   */
    
    private PacketTypes(int packetId) {
      this.packetId = packetId;
    }
    
    /**
   * This method gets the id of the packet
   * @return the id
   */
    
    public int getId() {
      return this.packetId;
    }
  }
  
  /** The id of the packet */
  public byte packetId;
  
  /**
   * Creates an object from the Packet class
   * @param packetId the packet's id
   */
  
  public Packet(int packetId) {
    this.packetId = (byte)packetId;
  }
  
  /**
   * This method sends data to the game client.
   * @param client the game client
   */
  
  public abstract void writeData(GameClient client);
  
  /**
   * This method sends data to the game server.
   * @param server the game server
   */
  
  public abstract void writeData(GameServer server);
  
  /**
   * This method reads data from the byte array
   * @param data the data array
   */
  
  public String readData(byte[] data) {
    String message = new String(data).trim();
    return message.substring(2);
  }
  
  /**
   * This method returns the byte data array
   * @return the data array
   */
  
  public abstract byte[] getData();
  
  /**
   * This method returns the packet type from the packet id
   * @param packetId the id of the packet
   * @return the packet type
   */
  
  public static PacketTypes lookupPacket(String packetId) {
    try {
      return lookupPacket(Integer.parseInt(packetId));
    } catch (NumberFormatException e) {
      return PacketTypes.INVALID;
    }
  }
  
  /**
   * This method returns the packet type from the packet id
   * @param id the id of the packet
   * @return the packet type
   */
  
  public static PacketTypes lookupPacket(int id) {
    for (PacketTypes p : PacketTypes.values()) {
      if (p.getId() == id) {
        return p;
      }
    }
    return PacketTypes.INVALID;
  }
}