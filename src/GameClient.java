/* -------- IMPORTS ---------- */
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.IOException;
/* -------------------------- */

/**
 * [GameClient.java]
 * This program refers to the game client that runs when a player joins the game.
 * The game client runs on its own thread and receives packets/information from the server.
 * @author Braydon Wang, Dylan Wang
 * @version 1.0, Jan. 25, 2021
 */

public class GameClient extends Thread {
  
  /** The ip address of the server */
  private InetAddress ipAddress;
  /** The socket that receives packets */
  private DatagramSocket socket;
  /** The current game that is initialized */
  private DubG game;
  
  /**
   * Creates an object from the Game Client class
   * @param game the current game
   * @param ipAddress the ip address of the server
   */
  
  public GameClient(DubG game, String ipAddress) {
    this.game = game;
    try {
      this.socket = new DatagramSocket();
      this.ipAddress = InetAddress.getByName(ipAddress);
    } catch (SocketException e) {
      e.printStackTrace();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * The method that continuously receives information through packets.
   */
  
  public void run() {
    while (true) {
      //the array of bytes of data that we will send to and from the server
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
    //parsing each of the packet types based on what information it hold
    switch(type) {
      default:
      case INVALID:
        break;
      case LOGIN:
        packet = new Packet00Login(data);
        this.handleLogin((Packet00Login)packet, address, port);
        break;
      case DISCONNECT:
        packet = new Packet01Disconnect(data);
        System.out.println("["+address.getHostAddress() + ":" + port + "]" + ((Packet01Disconnect)packet).getUsername() + " has left the world. . .");
        game.level.removePlayerMP(((Packet01Disconnect)packet).getUsername());
        break;
      case MOVE:
        packet = new Packet02Move(data);
        this.handleMove((Packet02Move)packet);
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
   * The method sends the data of the packet to the server.
   * @param data the byte array data of the packet
   */
  
  public void sendData(byte[] data) {
    DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
    try {
      socket.send(packet); 
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * The method handles login packets.
   * @param packet   the login packet
   * @param address the ip address
   * @param port       the port number
   */
  
  private void handleLogin(Packet00Login packet, InetAddress address, int port) {
    System.out.println("["+address.getHostAddress() + ":" + port + "]" + packet.getUsername() + " has joined the game . . .");
    PlayerMP player = new PlayerMP(game.level, packet.getX(), packet.getY(), packet.getUsername(), 400, address, port);
    game.level.addedEntities.add(player);
  }
  
  /**
   * The method handles player movement packets.
   * @param packet   the move packet
   */
  
  private void handleMove(Packet02Move packet) {
    this.game.level.movePlayer(packet.getUsername(), packet.getX(), packet.getY(), packet.getNumSteps(), packet.isMoving(), packet.getMovingDir());
  }
  
  /**
   * The method handles bullets joining the game packets.
   * @param packet   the bullet packet
   */
  
  private void handleEnter(Packet03BulletEnter packet) {
    Bullet bullet = new Bullet(game.level,packet.getUsername(),packet.getX(),packet.getY(),packet.getSpeed(),packet.getMovingDir(),packet.getId(),packet.getDamage(),packet.getXTile(),packet.getYTile());
    game.level.addedEntities.add(bullet);
    //handling bullets shot by the turrets
    if (packet.getUsername().equalsIgnoreCase("Turret3")) {
      ((Turret)game.level.tilesOver[10 + 9 * game.level.width]).lastBulletTime = System.currentTimeMillis();
    } else if (packet.getUsername().equalsIgnoreCase("Turret0")) {
      ((Turret)game.level.tilesOver[50 + 53 * game.level.width]).lastBulletTime = System.currentTimeMillis();
    } else if (packet.getUsername().equalsIgnoreCase("Turret2")) {
      ((Turret)game.level.tilesOver[42 + 13 * game.level.width]).lastBulletTime = System.currentTimeMillis();
    }
  }
  
  /**
   * The method handles bullets leaving the game packets.
   * @param packet   the bullet leave packet
   */
  
  private void handleLeave(Packet04BulletLeave packet) {
    game.level.removeBullet(packet.getUsername(),packet.getId());
  }
  
  /**
   * The method handles bullets moving packets.
   * @param packet   the bullet move packet
   */
  
  private void handleBulletMove(Packet05BulletMove packet) {
    game.level.moveBullet(packet.getUsername(),packet.getX(),packet.getY(),packet.getNumSteps(),packet.isMoving(),packet.getMovingDir(),packet.getId());
  }
  
  /**
   * The method handles crates breaking packets.
   * @param packet   the crate packet
   */
  
  private void handleCrate(Packet06Crate packet) {
    game.level.breakCrate(packet.getId());
  }
  
  /**
   * The method handles potions coming from crates packets.
   * @param packet   the potion packet
   */
  
  private void handlePotion(Packet07Potion packet) {
    game.level.setPotion(packet.getX(),packet.getY(),packet.getId());
  }
  
  /**
   * The method handles the health potion packet.
   * @param packet   the health potion packet
   */
  
  private void handleHealth(Packet10HealthPotion packet) {
    game.level.setPlayerHealth(packet.getUsername());
    game.level.setPlayerHealed(packet.getUsername(), true);
  }
  
  /**
   * The method handles the rage potion start packet.
   * @param packet   the rage potion start packet
   */
  
  private void handleRageStart(Packet11RageStart packet) {
    game.level.setPlayerRage(packet.getUsername(), true);
  }
  
  /**
   * The method handles the rage potion end packet.
   * @param packet   the rage potion end packet
   */
  
  private void handleRageEnd(Packet12RageEnd packet) {
    game.level.setPlayerRage(packet.getUsername(), false);
  }
  
  /**
   * The method handles the speed potion start packet.
   * @param packet   the speed potion start packet
   */
  
  private void handleSpeedStart(Packet13SpeedStart packet) {
    game.level.setPlayerFast(packet.getUsername(), true);
  }
  
  /**
   * The method handles the speed potion end packet.
   * @param packet   the speed potion end packet
   */
  
  private void handleSpeedEnd(Packet14SpeedEnd packet) {
    game.level.setPlayerFast(packet.getUsername(), false);
  }
  
  /**
   * The method handles the health potion end packet.
   * @param packet   the health potion end packet
   */
  
  private void handleHealStop(Packet15HealStop packet) {
    game.level.setPlayerHealed(packet.getUsername(), false);
  }
  
  /**
   * The method handles the experience potion start packet.
   * @param packet   the experience potion start packet
   */
  
  private void handleExpStart(Packet16ExpStart packet) {
    game.level.setPlayerExped(packet.getUsername(), true);
  }
  
  /**
   * The method handles the experience potion stop packet.
   * @param packet   the experience potion stop packet
   */
  
  private void handleExpStop(Packet17ExpStop packet) {
    game.level.setPlayerExped(packet.getUsername(), false);
  }
  
  /**
   * The method handles the player's change class packet.
   * @param packet   the change class packet
   */
  
  private void handleChangeClass(Packet20ChangeClass packet) {
    game.level.changePlayerClass(packet.getUsername(),packet.getClassType(),packet.getColour(),packet.getXTile(),packet.getYTile());
  }
}