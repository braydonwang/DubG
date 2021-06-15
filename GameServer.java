/* -------- IMPORTS ---------- */
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
/* -------------------------- */

/**
 * [GameServer.java]
 * This program refers to the game server that runs when a player is hosting the game.
 * The server runs on its own thread and acts as the middleman send info to each client.
 * @author Braydon Wang, Dylan Wang
 * @version 1.0, Jan. 25, 2021
 */

public class GameServer extends Thread {
  
  /** The socket that receives packets */
  private DatagramSocket socket;
  /** The current game initialized */
  private DubG game;
  /** The list of connected players in the game */
  private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();
  /** Map of connected players */
  private Map<String,PlayerMP> connPlayers = new HashMap<String,PlayerMP>();
  
  /**
   * Creates an object from the Game Server class
   * @param game the current game
   */
  
  public GameServer(DubG game) {
    this.game = game;
    try {
      this.socket = new DatagramSocket(1331);
    } catch (SocketException e) {
      e.printStackTrace();
    } 
  }
  
  /**
   * The method that continuously receives information through packets.
   */
  
  public void run() {
    while (true) {
      byte[] data = new byte [1024];
      DatagramPacket packet = new DatagramPacket(data,data.length);
      try {
        socket.receive(packet);
      } catch (IOException e) {
        e.printStackTrace();
      }
      
      this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
      
    }
  }
  
  /**
   * The method that parses packets based on their type and the information that they include.
   * @param data       the data of the packet in a byte array
   * @param address the ip address
   * @param port       the port number
   */
  
  private void parsePacket(byte[] data, InetAddress address, int port) {
    String message = new String(data).trim();
    Packet.PacketTypes type = Packet.lookupPacket(message.substring(0,2));
    Packet packet = null;
    //parsing all packets based on the information that it holds
    switch(type) {
      default:
      case INVALID:
        break;
      case LOGIN:
        packet = new Packet00Login(data);
        System.out.println("["+address.getHostAddress() + ":" + port + "]" + ((Packet00Login)packet).getUsername() + " has connected . . .");
        //creating a new player with the respective information from the packet and adding a connection to it
        PlayerMP player = new PlayerMP(game.level,((Packet00Login)packet).getX(), ((Packet00Login)packet).getY(), ((Packet00Login)packet).getUsername(), 400, address, port);
        this.addConnection(player,(Packet00Login)packet);
        break;
      case DISCONNECT:
        packet = new Packet01Disconnect(data);
        System.out.println("["+address.getHostAddress() + ":" + port + "]" + ((Packet01Disconnect)packet).getUsername() + " has left . . .");
        //removing the player from the connections list
        this.removeConnection((Packet01Disconnect)packet);
        break;
      case MOVE:
        packet = new Packet02Move(data);
        this.handleMove(((Packet02Move)packet));
        break;
      case ENTER:
        packet = new Packet03BulletEnter(data);
        this.handleEnter((Packet03BulletEnter)packet);
        break;
      case LEAVE:
        packet = new Packet04BulletLeave(data);
        this.handleLeave((Packet04BulletLeave)packet);
        break;
      case BULLET:
        packet = new Packet05BulletMove(data);
        this.handleBulletMove((Packet05BulletMove)packet);
        break;
      case CRATE:
        packet = new Packet06Crate(data);
        this.handleCrate((Packet06Crate)packet);
        break;
      case POTION:
        packet = new Packet07Potion(data);
        this.handlePotion((Packet07Potion)packet);
        break;
      case HEALTH:
        packet = new Packet10HealthPotion(data);
        this.handleHealth((Packet10HealthPotion)packet);
        break;
      case RAGESTART:
        packet = new Packet11RageStart(data);
        this.handleRageStart((Packet11RageStart)packet);
        break; 
      case RAGEEND:
        packet = new Packet12RageEnd(data);
        this.handleRageEnd((Packet12RageEnd)packet);
        break;
      case SPEEDSTART:
        packet = new Packet13SpeedStart(data);
        this.handleSpeedStart((Packet13SpeedStart)packet);
        break;
      case SPEEDEND:
        packet = new Packet14SpeedEnd(data);
        this.handleSpeedEnd((Packet14SpeedEnd)packet);
        break;
      case HEALSTOP:
        packet = new Packet15HealStop(data);
        this.handleHealStop((Packet15HealStop)packet);
        break;
      case EXPSTART:
        packet = new Packet16ExpStart(data);
        this.handleExpStart((Packet16ExpStart)packet);
        break;
      case EXPEND:
        packet = new Packet17ExpStop(data);
        this.handleExpStop((Packet17ExpStop)packet);
        break;
      case CLASS:
        packet = new Packet20ChangeClass(data);
        this.handleChangeClass((Packet20ChangeClass)packet);
        break;
    }
  }
  
  /**
   * The method adds a connection to a newly joined player.
   * @param player  the new player that joined
   * @param packet the login packet
   */
  
  public void addConnection(PlayerMP player, Packet00Login packet) {
    boolean alreadyConnected = false;
    //checking all players in the list of connected players
    for (PlayerMP p: this.connectedPlayers) {
      if (player.getUsername().equalsIgnoreCase(p.getUsername())) {
        //setting the ip address if not done so
        if (p.ipAddress == null) {
          p.ipAddress = player.ipAddress;
        }
        
        //setting the port if not done so
        if (p.port == -1) {
          p.port = player.port;
        }
        alreadyConnected = true;
      } else {
        //send to already connected players
        sendData(packet.getData(),p.ipAddress,p.port);
        
        //send to new player
        Packet00Login packet2 = new Packet00Login(p.getUsername(), p.x, p.y);
        sendData(packet2.getData(),player.ipAddress,player.port);
      }
    }
    
    //if not already connected, add it to the connected players list
    if (!alreadyConnected) {
      this.connectedPlayers.add(player);
    }
    
    for (String name: connPlayers.keySet()) {
      PlayerMP p = connPlayers.get(name);
      if (!name.equalsIgnoreCase(packet.getUsername())) {
        //sendData(packet.getData(),p.ipAddress,p.port);
      }
    }

    if (connPlayers.containsKey(packet.getUsername())) {
      this.connPlayers.put(packet.getUsername(), player);
    }
  }
  
  /**
   * The method that removes a connection with a disconnected player.
   * @param packet the disconnect packet
   */
  
  public void removeConnection(Packet01Disconnect packet) {
    int ind = getPlayerMPIndex(packet.getUsername());
    //remove the player from the connected players list using the index found
    if (ind < this.connectedPlayers.size()) {
      this.connectedPlayers.remove(getPlayerMPIndex(packet.getUsername()));
    }
    
    if (connPlayers.containsKey(packet.getUsername())) { 
      this.connPlayers.remove(packet.getUsername());
    }
    
    packet.writeData(this);
  }
  
  /**
   * The method gets the player specified by the username.
   * @param username the username of the player
   * @return the player with that username
   */
  
  public PlayerMP getPlayerMP(String username) {
    for (PlayerMP player: this.connectedPlayers) {
      if (player.getUsername().equalsIgnoreCase(username)) {
        return player;
      }
    }
    return null;
  }
  
  /**
   * The method gets the index of the player specified by the username.
   * @param username the username of the player
   * @return the index of the player with that username
   */
  
  public int getPlayerMPIndex(String username) {
    int index = 0;
    for (PlayerMP player: this.connectedPlayers) {
      if (player.getUsername().equalsIgnoreCase(username)) {
        break;
      }
      index++;
    }
    return index;
  }
  
  /**
   * The method sends the data of the packet to the clients.
   * @param data         the byte array data of the packet
   * @param ipAddres the ip address of the client
   * @param port         the port number
   */
  
  public void sendData(byte[] data, InetAddress ipAddress, int port) {
    DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
    try{
      this.socket.send(packet); 
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * The method sends the data of the packet to all the clients.
   * @param data         the byte array data of the packet
   */
  
  public void sendDataToAllClients(byte[] data) {
    for (PlayerMP p : connectedPlayers) {
      sendData(data, p.ipAddress, p.port);
    }
  }
  
  /**
   * The method sends the data of the packet to a specific client.
   * @param data          the byte array data of the packet
   * @param username the name of the client
   */
  
  public void sendDataToSpecificClient(byte[] data, String username) {
    PlayerMP p = connPlayers.get(username);
    sendData(data, p.ipAddress, p.port);
  }
  
  /**
   * The method handles movement by the player.
   * @param packet the move packet
   */
  
  private void handleMove(Packet02Move packet) {
    if (getPlayerMP(packet.getUsername()) != null) {
      int index = getPlayerMPIndex(packet.getUsername());
      //updating the information of the player based on the contents of the packet
      PlayerMP player = this.connectedPlayers.get(index);
      player.x = packet.getX();
      player.y = packet.getY();
      player.setMoving(packet.isMoving());
      player.setMovingDir(packet.getMovingDir());
      player.setNumSteps(packet.getNumSteps());
      packet.writeData(this);
    }
  }
  
  /**
   * The method handles bullets entering the game.
   * @param packet the bullet enter packet
   */
  
  private void handleEnter(Packet03BulletEnter packet) {
    for (PlayerMP p: this.connectedPlayers) {
      if (!p.getUsername().equalsIgnoreCase(packet.getUsername())) {
        sendData(packet.getData(),p.ipAddress,p.port);
      }
    }
  }
  
  /**
   * The method handles bullets leaving the game.
   * @param packet the bullet leave packet
   */
  
  private void handleLeave(Packet04BulletLeave packet) {
    for (PlayerMP p: this.connectedPlayers) {
      if (!p.getUsername().equalsIgnoreCase(packet.getUsername())) {
        sendData(packet.getData(),p.ipAddress,p.port);
      }
    }
  }
  
  /**
   * The method handles movement of the bullets.
   * @param packet the bullet move packet
   */
  
  private void handleBulletMove(Packet05BulletMove packet) {
    for (PlayerMP p: this.connectedPlayers) {
      if (!p.getUsername().equalsIgnoreCase(packet.getUsername())) {
        sendData(packet.getData(),p.ipAddress,p.port);
      }
    }
  }
  
  /**
   * The method handles crates breaking.
   * @param packet the crate packet
   */
  
  private void handleCrate(Packet06Crate packet) {
    for (PlayerMP p: this.connectedPlayers) {
      if (!p.getUsername().equalsIgnoreCase(packet.getUsername())) {
        sendData(packet.getData(),p.ipAddress,p.port);
        break;
      }
    }
  }
  
  /**
   * The method handles potions coming from crates.
   * @param packet the potion packet
   */
  
  private void handlePotion(Packet07Potion packet) {
    for (PlayerMP p: this.connectedPlayers) {
      if (!p.getUsername().equalsIgnoreCase(packet.getUsername())) {
        sendData(packet.getData(),p.ipAddress,p.port);
      }
    }
  }
  
  /**
   * The method handles health potion consumed by player.
   * @param packet the health potion packet
   */
  
  private void handleHealth(Packet10HealthPotion packet) {
    for (PlayerMP p: this.connectedPlayers) {
      if (!p.getUsername().equalsIgnoreCase(packet.getUsername())) {
        sendData(packet.getData(),p.ipAddress,p.port);
      }
    }
  }
  
  /**
   * The method handles rage potion start time.
   * @param packet the rage potion start packet
   */
  
  private void handleRageStart(Packet11RageStart packet) {
    for (PlayerMP p: this.connectedPlayers) {
      if (!p.getUsername().equalsIgnoreCase(packet.getUsername())) {
        sendData(packet.getData(),p.ipAddress,p.port);
      }
    }
  }
  
  /**
   * The method handles rage potion end time.
   * @param packet the rage potion end packet
   */
  
  private void handleRageEnd(Packet12RageEnd packet) {
    for (PlayerMP p: this.connectedPlayers) {
      if (!p.getUsername().equalsIgnoreCase(packet.getUsername())) {
        sendData(packet.getData(),p.ipAddress,p.port);
      }
    }
  }
  
  /**
   * The method handles speed potion start time.
   * @param packet the speed potion start packet
   */
  
  private void handleSpeedStart(Packet13SpeedStart packet) {
    for (PlayerMP p: this.connectedPlayers) {
      if (!p.getUsername().equalsIgnoreCase(packet.getUsername())) {
        sendData(packet.getData(),p.ipAddress,p.port);
      }
    }
  }
  
  /**
   * The method handles speed potion end time.
   * @param packet the speed potion end packet
   */
  
  private void handleSpeedEnd(Packet14SpeedEnd packet) {
    for (PlayerMP p: this.connectedPlayers) {
      if (!p.getUsername().equalsIgnoreCase(packet.getUsername())) {
        sendData(packet.getData(),p.ipAddress,p.port);
      }
    }
  }
  
  /**
   * The method handles health potion end time.
   * @param packet the health potion end packet
   */
  
  private void handleHealStop(Packet15HealStop packet) {
    for (PlayerMP p: this.connectedPlayers) {
      if (!p.getUsername().equalsIgnoreCase(packet.getUsername())) {
        sendData(packet.getData(),p.ipAddress,p.port);
      }
    }
  }
  
  /**
   * The method handles experience potion start time.
   * @param packet the experience potion start packet
   */
  
  private void handleExpStart(Packet16ExpStart packet) {
    for (PlayerMP p: this.connectedPlayers) {
      if (!p.getUsername().equalsIgnoreCase(packet.getUsername())) {
        sendData(packet.getData(),p.ipAddress,p.port);
      }
    }
  }
  
  /**
   * The method handles experience potion end time.
   * @param packet the experience potion end packet
   */
  
  private void handleExpStop(Packet17ExpStop packet) {
    for (PlayerMP p: this.connectedPlayers) {
      if (!p.getUsername().equalsIgnoreCase(packet.getUsername())) {
        sendData(packet.getData(),p.ipAddress,p.port);
      }
    }
  }
  
  /**
   * The method handles player's changing class.
   * @param packet the change class packet
   */
  
  private void handleChangeClass(Packet20ChangeClass packet) {
    for (PlayerMP p: this.connectedPlayers) {
      if (!p.getUsername().equalsIgnoreCase(packet.getUsername())) {
        sendData(packet.getData(),p.ipAddress,p.port);
      }
    }
  }
}