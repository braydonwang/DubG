/* -------- IMPORTS ---------- */
import java.awt.Canvas;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Scanner;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineEvent;
/* -------------------------- */

/**
 * [DubG.java]
 * This program refers to the main game that is run.
 * This game is a multiplayer, action-arcade, battle royale game with turrets, crates, potions, etc.
 * You have a choice to host a game (server) or be a player (client)
 * @author Braydon Wang, Dylan Wang
 * @version 1.0, Jan. 25, 2021
 */

public class DubG extends Canvas implements Runnable {
  
  /** The serial version id of the game */
  private static final long serialVersionUID = 1L;
  /** The width of the screen */
  static final int WIDTH = 160;
  /** The height of the screen in proportion to the width */
  static final int HEIGHT = WIDTH / 12 * 9;
  /** The scale size of the screen */
  static final int SCALE = 3;
  /** The name of the game */
  static final String NAME = "DubG";
  /** The dimensions of the game with respect to width, height and the scale */
  static final Dimension DIMENSIONS = new Dimension(WIDTH*SCALE, HEIGHT*SCALE);
  /** Whether the server is running or not */
  static boolean runServer = false;
  /** The current game initialized */
  public static DubG game;
  
  /** The Jframe used to draw the game to */
  JFrame frame;
  /** Whether the game is running or not */
  boolean running = false;
  /** The number of ticks the game has updated so far */
  int tickCount = 0;
  
  /** The image that is drawn to the screen with its colours rendered */
  private BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
  /** The number of pixels inside of the image */
  private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
  /** The colours represented by their RGB representation */
  private int[] colours = new int[6*6*6];
  
  /** The screen that the game is drawn to */
  private Screen screen;
  /** The input handler used to handler player input */
  InputHandler input;
  /** The mouse handler that deals with mouse movement */
  MouseHandler mouseHandler;
  /** The window handler that deals with the actions of the window */
  WindowHandler windowHandler;
  
  /** The current level of the map that is being drawn */
  Level level;
  /** The current player that has joined the game */
  Player player;
  
  /** The socket client */
  GameClient socketClient;
  /** The socket server */
  GameServer socketServer;
  
  /** The JFrame used to draw the main menu on to */
  static JFrame menu;
  
  /** The spawn coordinates of each new player */
  public int[][] spawnCoords = {{256,47}, {32,252}, {256,468}, {480,252}};
  
  /**
   * Creates an object from the DubG class
   */
  
  public DubG() {
    //setting the minimum and maximum size of the game
    setMinimumSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
    setMaximumSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
    setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
    
    //creating the frame
    frame = new JFrame(NAME);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());
    
    //Center the canvas inside the jframe
    frame.add(this,BorderLayout.CENTER);
    frame.pack();
    
    //adding basic features to the frame
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
  
  /**
   * This method initializes all of the important variables necessary to start the game.
   */
  
  public void init() {
    game = this;
    int index = 0;
    
    //intializing the different shades of colours given by each integer value of RGB
    for (int r = 0; r < 6; r++) {
      for (int g = 0; g < 6; g++) {
        for (int b = 0; b < 6; b++) {
          int rr = (r * 255/5);
          int gg = (g * 255/5);
          int bb = (b * 255/5);
          
          colours[index++] = rr << 16 | gg << 8 | bb;
        }
      }
    }
    
    //initializing all handlers and important objects needed to function the game
    screen = new Screen(WIDTH,HEIGHT,new SpriteSheet("res/sprite_sheet.png"));
    input = new InputHandler(this);
    mouseHandler = new MouseHandler(this);
    windowHandler = new WindowHandler(this);
    level = new Level("res/Main_Map.png");
    
    //creating the player at a random spawn location
    if (socketServer == null) {
      int rand = (int)(Math.random() * 100);
      int randId = 0;
      
      if (rand <= 25) {
        randId = 0;
      } else if (rand > 25 && rand <= 50) {
        randId = 1;
      } else if (rand > 50 && rand <= 75) {
        randId = 2;
      } else {
        randId = 3;
      }
      
      player = new PlayerMP(level,spawnCoords[randId][0],spawnCoords[randId][1],input,JOptionPane.showInputDialog(this,"Please enter a username:"),400,null,-1);
    } else {
      player = new PlayerMP(level,257,253,input,"SERVER",400,null,-1);
    }
    
    //adding the player to the list of entities and setting input handlers to it
    input.setPlayer(player);
    mouseHandler.setPlayer(player);
    level.addEntity(player);
    
    //sending a login packet to the server
    Packet00Login loginPacket = new Packet00Login(player.getUsername(), player.x, player.y);
    if (socketServer != null) {
      socketServer.addConnection((PlayerMP)player,loginPacket);
    }
    loginPacket.writeData(socketClient);
  }
  
  /**
   * This method creates the socket server and the socket client using the specified IP address.
   */
  
  public synchronized void start() {
    running = true;
    new Thread(this).start();
    
    //running the server only if the player is a host
    if (runServer) {
      socketServer = new GameServer(this);
      socketServer.start();
    }
    
    //creating a client on the local ip address
    socketClient = new GameClient(this, "localhost");
    socketClient.start();
  }
  
  /**
   * This method stops running the game.
   */
  
  public synchronized void stop() {
    running = false;
  }
  
  /**
   * This method continuously runs and updates the game using the tick and frame counter.
   */
  
  public void run() {
    long lastTime = System.nanoTime();
    double nsPerTick = 1000000000D/60D;
    
    int ticks = 0;
    int frames = 0;
    
    long lastTimer = System.currentTimeMillis();
    double delta = 0;
    
    //initializing the game
    if (socketServer == null) {
      init();
    }
    
    //updating all of the tick and frame counters and tells the game when to render
    while (running) {
      long now = System.nanoTime();
      delta += (now - lastTime) / nsPerTick;
      lastTime = now;
      boolean shouldRender = true;
      
      //updating the game when the counter is greater than the time set
      while (delta >= 1) {
        ticks++;
        tick();
        delta--;
        shouldRender = true;
      }
      
      //adding some delay time
      try {
        Thread.sleep(2);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      
      //rendering the screen when necessary
      if (shouldRender) {
        frames++;
        render();
      }
      
      //updating the number of frames and ticks every second
      if (System.currentTimeMillis() - lastTimer >= 1000) {
        lastTimer += 1000;
        frames = 0;
        ticks = 0;
      }
    }
  }
  
  /**
   * This method updates the tick counter and ticks all of the entities part of the map level.
   */
  
  public void tick() {
    tickCount++;
    level.tick();
  }
  
  /**
   * This method renders and draws all of the entities and tiles onto the frame.
   */
  
  public void render() {
    BufferStrategy bs = getBufferStrategy();
    if (bs == null) {
      //triple buffering (the higher the buffer, the more tearing that is reduced in the image)
      createBufferStrategy(3);
      return;
    }
    
    //setting the x and y offsets of the screen
    int xOffset = player.x - (screen.width/2), yOffset = player.y - (screen.height/2);
    //rendering the map level with the appropriate offsets
    level.renderTiles(screen,xOffset,yOffset);
    
    //rendering the entities on the screen
    level.renderEntities(screen);
    
    //getting all of the colours of the pixels from the already initialized colours
    for (int y = 0; y < screen.height; y++) {
      for (int x = 0; x < screen.width; x++) {
        int colourCode = screen.pixels[x+y*screen.width];
        if (colourCode < 255) {
          pixels[x+y*WIDTH] = colours[colourCode];
        }
      }
    }
    
    //displaying the image on to the screen
    Graphics g = bs.getDrawGraphics();    
    g.drawImage(image,0,0,getWidth(),getHeight(),null);
    
    g.dispose();
    bs.show();
  }
  
  /**
   * This method plays sound effects with the specified file name using audio input stream
   * @param fileName the name of the sound effect file
   */
  
  public static void sound(String fileName) {
    try {
      File audioFile = new File(fileName);
      AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
      DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
      Clip clip = (Clip) AudioSystem.getLine(info);
      
      //depending on whether the file is the background music or not, stop the clip or repeat
      if (fileName.equals("IntroMusic.wav")) {
        clip.addLineListener(new RepeatListener());
      } else {
        clip.addLineListener(new SoundListener());
      }
      clip.open(audioStream);
      clip.start();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * This class stops playing the specified audio when the clip is over.
   * @author Braydon Wang, Dylan Wang
   * @version 1.0, Jan. 25, 2021
   */
  
  static class SoundListener implements LineListener {
    public void update(LineEvent event) {
      if (event.getType() == LineEvent.Type.STOP) {
        event.getLine().close();
      }
    }
  }
  
  /**
   * This class repeats the background music of the game when the audio clip finishes.
   * @author Braydon Wang, Dylan Wang
   * @version 1.0, Jan. 25, 2021
   */
  
  static class RepeatListener implements LineListener {
    public void update(LineEvent event) {
      if (event.getType() == LineEvent.Type.STOP) {
        event.getLine().close();
        sound("IntroMusic.wav");
      }
    }
  }
  
  /**
   * This method draws the main menu onto the screen using menu panel.
   */
  
  static void displayMenu() {
    //creating a new JFrame to hold the contents of the menu
    menu = new JFrame(NAME);
    menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    menu.setLayout(new BorderLayout());
    menu.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
    
    //Center the canvas inside the jframe
    MenuPanel menuPanel = new MenuPanel();
    menu.add(menuPanel,BorderLayout.CENTER);
    
    menu.pack();
    
    //setting basic features of the frame
    menu.setResizable(false);
    menu.setLocationRelativeTo(null);
    menu.setVisible(true);
  }
  
  /**
   * This class draws the main menu using Paint Component and also deals with mouse input.
   * @author Braydon Wang, Dylan Wang
   * @version 1.0, Jan. 25, 2021
   */
  
  static class MenuPanel extends JPanel implements MouseListener, MouseMotionListener {
    
    /* -------- COLOURS --------- */
    Color BLACK = new Color(0,0,0);
    Color WHITE = new Color(255,255,255);
    Color LIGHT_BLUE = new Color(190,226,240);
    Color DARK_RED = new Color(88,0,0);
    /* -------------------------- */
    
    /** Status of player in the main menu */
    boolean inMenu = true;
    /** Hovering over the play button */
    boolean playHover = false;
    /** Hovering over the controls button */
    boolean controlHover = false;
    /** Hovering over the quit button */
    boolean quitHover = false;
    /** Clicked the play button */
    boolean playClick = false;
    /** Hovering over the host button */
    boolean hostHover = false;
    /** Hovering over the player button */
    boolean playerHover = false;
    /** Hovering over the left arrow button */
    boolean leftHover = false;
    /** Hovering over the right arrow button */
    boolean rightHover = false;
    /** The index of the current controls page */
    int controlPic = 0;
    
    /* -------- IMAGES --------- */
    Image title = Toolkit.getDefaultToolkit().getImage("Title.png");
    Image subtitle = Toolkit.getDefaultToolkit().getImage("Subtitle.png");
    Image pixelGuy = Toolkit.getDefaultToolkit().getImage("Pixel Guy.gif");
    Image rightArrow = Toolkit.getDefaultToolkit().getImage("ArrowRight.png");
    Image leftArrow = Toolkit.getDefaultToolkit().getImage("ArrowLeft.png");
    Image leftArrowWhite = Toolkit.getDefaultToolkit().getImage("ArrowLeftWhite.png");
    Image rightArrowWhite = Toolkit.getDefaultToolkit().getImage("ArrowRightWhite.png");
    Image[] controls = {Toolkit.getDefaultToolkit().getImage("ControlPage1.png"), Toolkit.getDefaultToolkit().getImage("ControlPage2.png"),
      Toolkit.getDefaultToolkit().getImage("ControlPage3.png")};
    /* -------------------------- */
    
    /**
   * Creates an object from the Menu Panel class
   */
    
    MenuPanel() {
      addMouseListener(this);
      addMouseMotionListener(this);
    }
    
    /**
   * This method continuously draws the main menu onto the screen using graphics
   * @param g the graphics variable
   */
    
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (inMenu) {
        //setting the background
        g.setColor(BLACK);
        g.fillRect(0,0,1000,1000);
        
        //drawing the title and subtitle
        g.drawImage(title,20,20,260,80,this);
        g.drawImage(subtitle,30,96,260,30,this);
        
        g.drawImage(pixelGuy,300,50,170,240,this);
        
        //drawing the buttons depending on whether the mouse is hovering over or not
        g.setColor(WHITE);
        
        if (!playClick) {
          g.drawRect(29,149,251,36);
        } else {
          g.drawRect(29,149,120,36);
          g.drawRect(159,149,120,36);
        }
        g.drawRect(29,199,251,36);
        g.drawRect(29,249,251,36);
        
        //play button
        if (!playClick) {
          if (playHover) {
            g.setColor(WHITE);
          } else {
            g.setColor(BLACK);
          }
          g.fillRect(30,150,250,35);
        } else {
          //host button
          if (hostHover) {
            g.setColor(WHITE);
          } else {
            g.setColor(BLACK);
          }
          g.fillRect(30,150,119,35);
          
          //player button
          if (playerHover) {
            g.setColor(WHITE);
          } else {
            g.setColor(BLACK);
          }
          g.fillRect(160,150,119,35);
        }
        
        //controls button
        if (controlHover) {
          g.setColor(WHITE);
        } else {
          g.setColor(BLACK);
        }
        g.fillRect(30,200,250,35);
        
        //quit button
        if (quitHover) {
          g.setColor(WHITE);
        } else {
          g.setColor(BLACK);
        }
        g.fillRect(30,250,250,35);
        
        g.setFont(new Font("Serif", Font.PLAIN, 20));
        
        //play font
        if (!playClick) {
          if (playHover) {
            g.setColor(BLACK);
          } else {
            g.setColor(WHITE);
          }
          g.drawString("PLAY",40,175);
        } else {
          //host font
          if (hostHover) {
            g.setColor(BLACK);
          } else {
            g.setColor(WHITE);
          }
          g.drawString("HOST",40,175);
          
          //player font
          if (playerHover) {
            g.setColor(BLACK);
          } else {
            g.setColor(WHITE);
          }
          g.drawString("PLAYER",170,175);
        }
        
        //controls font
        if (controlHover) {
          g.setColor(BLACK);
        } else {
          g.setColor(WHITE);
        }
        g.drawString("CONTROLS",40,225);
        
        //quit font
        if (quitHover) {
          g.setColor(BLACK);
        } else {
          g.setColor(WHITE);
        }
        g.drawString("QUIT",40,275);
      } else {
        //displaying the controls screen with left and right arrows
        g.drawImage(controls[controlPic-1],0,0,480,329,this);
        if (leftHover) {
          g.drawImage(leftArrowWhite,20,0,80,60,this);
        } else {
          g.drawImage(leftArrow,20,0,80,60,this);
        }
        
        if (rightHover) {
          g.drawImage(rightArrowWhite,375,0,80,60,this);
        } else {
          g.drawImage(rightArrow,375,0,80,60,this);
        }
      }
    }
    
    /**
     * This method gets the updated position of the mouse in the main menu screen.
     * @param e the event of the mouse
     */
    
    public void mouseMoved(MouseEvent e) {
      //x and y coordinates of the mouse
      int x = e.getX(), y = e.getY();
      if (inMenu) {
        //displaying the different buttons if mouse is hovered over or not
        if (x >= 31 && x <= 279 && y >= 153 && y <= 187 && !playClick) {
          playHover = true;
        } else if (x >= 31 && x <= 279 && y >= 203 && y <= 238) {
          controlHover = true;
        } else if (x >= 31 && x <= 279 && y >= 252 && y <= 287) {
          quitHover = true;
        } else if (x >= 31 && x <= 149 && y >= 153 && y <= 187 && playClick) {
          hostHover = true;
        } else if (x >= 161 && x <= 279 && y >= 153 && y <= 187 && playClick) {
          playerHover = true;
        } else {
          playHover = false;
          controlHover = false;
          quitHover = false;
          hostHover = false;
          playerHover = false;
        }
      } else {
        if (x >= 45 && x <= 93 && y >= 22 && y <= 42) {
          leftHover = true;
        } else if (x >= 383 && x <= 446 && y >= 22 && y <= 42) {
          rightHover = true;
        } else {
          leftHover = false;
          rightHover = false;
        }
      }
    }
  
    /**
     * This method checks to see if the user has clicked using the mouse.
     * @param e the event of the mouse
     */
    
    public void mousePressed(MouseEvent e) {
      //x and y coordinates of the mouse
      int x = e.getX(), y = e.getY();
      if (inMenu) {
        //moving to the different commands if the buttons are clicked or not
        if (x >= 31 && x <= 279 && y >= 153 && y <= 187 && !playClick) {
          playClick = true;
        } else if (x >= 31 && x <= 279 && y >= 203 && y <= 238) {
          inMenu = false;
          controlPic = 1;
        } else if (x >= 31 && x <= 279 && y >= 252 && y <= 287) {
          System.exit(0);
        } else if (x >= 31 && x <= 149 && y >= 153 && y <= 187 && playClick) {
          runServer = true;
          menu.dispose();
          new DubG().start();
        } else if (x >= 161 && x <= 279 && y >= 153 && y <= 187 && playClick) {
          menu.dispose();
          new DubG().start();
        }
      } else {
        if (x >= 45 && x <= 93 && y >= 22 && y <= 42) {
          controlPic--;
          if (controlPic == 0) {
            inMenu = true;
          }
        } else if (x >= 383 && x <= 446 && y >= 22 && y <= 42) {
          controlPic++;
          if (controlPic == 4) {
            inMenu = true;
            controlPic = 0;
          }
        }
      }
    }
    
    public void mouseDragged(MouseEvent e) {
    
    }
    
    public void mouseReleased(MouseEvent e) {
      
    }
    
    public void mouseEntered(MouseEvent e) {
      
    }
    
    public void mouseExited(MouseEvent e) {
      
    }
    
    public void mouseClicked(MouseEvent e) {
      
    }
  }
  
  /**
   * This main method that starts running the main menu screen before the actual game is run
   */
  
  public static void main(String[] args) throws Exception {
    //playing the background music
    sound("IntroMusic.wav");
    displayMenu();
  }
}