/* -------- IMPORTS ---------- */
import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
/* -------------------------- */

/**
 * [Level.java]
 * This program deals with the map level that is currently being drawn.
 * Using the colours on the tile map, the entire level can be drawn.
 * @author Braydon Wang, Dylan Wang
 * @version 1.0, Jan. 25, 2021
 */

class Level {
  
  /** The tiles that are to be drawn */
  private byte[] tiles;
  /** The entities that are drawn over the level tiles */
  Entity[] tilesOver;
  /** The path to the image */
  private String imagePath;
  /** The image of the sprite sheet */
  private BufferedImage image;
  /** The width of the level */
  int width;
  /** The height of the level */
  int height;
  /** The entities on the screen */
  List<Entity> entities = new ArrayList<Entity>();
  /** The entities that are to be added to prevent concurrent modification */
  List<Entity> addedEntities = new ArrayList<Entity>();
  /** The entities that are to be removed to prevent concurrent modification */
  List<Entity> removeEntities = new ArrayList<Entity>();
  
  /**
   * Creates an object from the Level class
   * @param imagePath the path to the image
   */
  
  Level(String imagePath) {
    //initializing the dimensions of the map and generating it
    if (imagePath != null) {
      this.imagePath = imagePath;
      this.loadLevelFromFile();
    } else {
      this.width = 64;
      this.height = 64;
      this.generateLevel();
    }
  }
  
  /**
   * The method loads the level from a file.
   */
  
  private void loadLevelFromFile() {
    try {
      //getting the image's dimensions and initializing the tiles and tile over array
      this.image = ImageIO.read(Level.class.getResourceAsStream(this.imagePath));
      this.width = image.getWidth();
      this.height = image.getHeight();
      tiles = new byte[(width + 1) * (height + 1)];
      tilesOver = new Entity[(width + 1) * (height + 1)];
      this.loadTiles();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * The method loads the tiles based on its arrangement on the small map.
   */
  
  private void loadTiles() {
    //using the colours on the map, load each tile
    int[] tileColours = this.image.getRGB(0,0,width,height,null,0,width);
    byte index = 10;
    Tile[] temp = Tile.tiles;
    
    //for all tiles in the dimensions of the map level
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        for (Tile t: temp) {
          if (t == null) {
            break;
          }
          
          //right facing turret
          if (tileColours[x + y * width] == 0xFFC72ABB) {
            Turret turret = new Turret(this,x*8 + 4,y*8 + 8,3);
            tilesOver[x + y * width] = turret;
            break;
            
          //up facing turret
          } else if (tileColours[x + y * width] == 0xFF00965A) {
            Turret turret = new Turret(this,x*8 + 4, y * 8 + 8, 0);
            tilesOver[x + y * width] = turret;
            break;
            
          //left facing turret
          } else if (tileColours[x + y * width] == 0xFF1D0098) {
            Turret turret = new Turret(this,x*8 + 4, y * 8 + 8, 2);
            tilesOver[x + y * width] = turret;
            break;
          }
          
          //initializing each tile based on its colour
          if (t.getColour() == tileColours[x + y * width]) {
            //all create tiles are initialized as their own object
            if (t.getColour() == 0xFFF0F000) {
              this.tiles[x + y * width] = index;
              Tile CRATE = new CrateTile(index,new int[][] {{6,0},{0,6},{1,6},{1,0}},Colours.get(320,210,-1,432),0xFFA1B3C2);
              index++;
            } else {
              this.tiles[x + y * width] = t.getId();
            }
          }
        }
      }
    }
  }
  
  /**
   * The method alters the tiles to a new tile.
   * @param x            the x coordinate of the current tile
   * @param y            the y coordinate of the current tile
   * @param newTile the new tile
   */
  
  public void alterTile(int x, int y, Tile newTile) {
    this.tiles[x + y * width] = newTile.getId();
    //setting the images RGB pixel to a new colour
    image.setRGB(x, y, newTile.getColour());
  }
  
  /**
   * The method generates the level if a file is not given.
   */
  
  public void generateLevel() {
    //generating the level using a certain algorithm
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (x * y % 10 < 7) {
          tiles[x + y * width] = Tile.GROUND.getId();
        } else {
          tiles[x + y * width] = Tile.WALL.getId();
        }
      }
    }
  }
  
  /**
   * The method returns the entities on the screen.
   * @return the list of entities
   */
  
  public synchronized List<Entity> getEntities() {
    return this.entities;
  }
  
  /**
   * The method returns the entities to be added onto the screen.
   * @return the list of added entities
   */
  
  public synchronized List<Entity> getAddedEntities() {
    return this.addedEntities;
  }
  
  /**
   * The method returns the entities to be removed from the screen.
   * @return the list of removed entities
   */
  
  public synchronized List<Entity> getRemovedEntities() {
    return this.removeEntities;
  }
  
  /**
   * The method updates the entities every tick count
   */
  
  public synchronized void tick() {
    for (Entity e: this.getEntities()) {
      e.tick();
    }
    
    //adding the need to be added entities to the list of entities
    List<Entity> temp = this.getAddedEntities();
    while (temp.size() != 0) {
      this.addEntity(temp.get(0));
      temp.remove(0);
    }
  
    //removing the need to be removed entities from the list of entities
    temp = this.getRemovedEntities();
    while (temp.size() != 0) {
      this.removeEntity(temp.get(0));
      temp.remove(0);
    }
    
    //updating each tile based on the ticks
    for (Tile t: Tile.tiles) {
      if (t == null) {
        break;
      }
      t.tick();
    }
    
    //updating each potion in the tile over array for each tick
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (tilesOver[x + y * width] != null) {
          tilesOver[x + y * width].tick();
        }
      }
    }
  }
  
  /**
   * The method renders all of the tiles on the screen that can be seen.
   * @param screen the screen that shows eveything
   * @param xOffset the x position of the screen
   * @param yOffset the y postion of the screen
   */
  
  public void renderTiles(Screen screen, int xOffset, int yOffset) {
    //setting the x and y offsets of the screen so that the player remains in the center, except when the map reaches the end
    if (xOffset < 0) {
      xOffset = 0;
    }
    if (xOffset > ((width << 3) - screen.width)) {
      xOffset = ((width << 3) - screen.width);
    }
    if (yOffset < 0) {
      yOffset = 0;
    }
    if (yOffset > ((height << 3) - screen.height)) {
      yOffset = ((height << 3) - screen.height);
    }
    screen.setOffset(xOffset,yOffset);
    
    //rendering each tile 
    for (int y = (yOffset >> 3); y < (yOffset  + screen.height >> 3) + 1; y++) {
      for (int x = (xOffset >> 3); x < (xOffset + screen.width >> 3) + 1; x++) {
        getTile(x,y).render(screen,this, x << 3, y << 3);
      }
    }
    
    //rendering each tile over (potion) or a death screen if the player has dead
    for (int y = (yOffset >> 3); y < (yOffset  + screen.height >> 3) + 1; y++) {
      for (int x = (xOffset >> 3); x < (xOffset + screen.width >> 3) + 1; x++) {
        if (DubG.game.player.dead) {
          screen.render(x << 3, y << 3, 0, Colours.get(000,000,000,000), 0, 1);
        } else {
          if (tilesOver[x + y * width] != null) {
            tilesOver[x + y * width].render(screen);
          }
        }
      }
    }
  }
  
  /**
   * The method renders all of the entities on to the screen.
   * @param screen the screen that holds all entities
   */
  
  public void renderEntities(Screen screen) {
    List<Entity> temp = this.getEntities();
    //rendering each entity in the list of entities
    for (int i = temp.size() - 1; i >= 0; i--) {
      Entity e = temp.get(i);
      e.render(screen);
    }
  }
  
  /**
   * The method returns the specified tile
   * @param x the x coordinate
   * @param y the y coordinate
   * @return the tile
   */
  
  public Tile getTile(int x, int y) {
    if (0 > x || x >= width || 0 > y || y >= height) {
      return Tile.VOID;
    }
    return Tile.tiles[tiles[x + y * width]];
  }
  
  /**
   * The method returns the potion specified at the coordinate
   * @param x the x coordinate
   * @param y the y coordinate
   * @return the potion
   */
  
  public Potion getPotion(int x, int y) {
    if (tilesOver[x + y * width] instanceof Potion) {
      return (Potion)tilesOver[x + y * width];
    }
    return null;
  }
  
  /**
   * The method sets the potion at a specfic coordinate
   * @param x   the x coordinate
   * @param y   the y coordinate
   * @param id  the index of the potion
   */
  
  public void setPotion(int x, int y, int id) {
    if (id == -1) {
      tilesOver[x + y * width] = null;
    } else {
      tilesOver[x + y * width] = new Potion(this, x * 8 + 4, y * 8 + 4, id);
    }
  }
  
  /**
   * The method updates the player's health after drinking a health potion
   * @param username the name of the player
   */
  
  public void setPlayerHealth(String username) {
    int index = getPlayerMPIndex(username);
    PlayerMP player = (PlayerMP)this.getEntities().get(index);
    player.setHealth(Math.min(player.getHealth() + 2.0,5.0));
  }
  
  /**
   * The method updates the player's healing status
   * @param username the name of the player
   * @param isHealed   the healing status
   */
  
  public void setPlayerHealed(String username, boolean isHealed) {
    int index = getPlayerMPIndex(username);
    PlayerMP player = (PlayerMP)this.getEntities().get(index);
    player.setHealed(isHealed);
  }
  
  /**
   * The method updates the player's rage status
   * @param username the name of the player
   * @param isRaged   the rage status
   */
  
  public void setPlayerRage(String username, boolean isRaged) {
    int index = getPlayerMPIndex(username);
    PlayerMP player = (PlayerMP)this.getEntities().get(index);
    player.setRage(isRaged);
  }
  
  /**
   * The method updates the player's speed status
   * @param username the name of the player
   * @param isFast   the speed status
   */
  
  public void setPlayerFast(String username, boolean isFast) {
    int index = getPlayerMPIndex(username);
    PlayerMP player = (PlayerMP)this.getEntities().get(index);
    player.setFast(isFast);
  }
  
  /**
   * The method updates the player's experience status
   * @param username the name of the player
   * @param isExped   the experience status
   */
  
  public void setPlayerExped(String username, boolean isExped) {
    int index = getPlayerMPIndex(username);
    PlayerMP player = (PlayerMP)this.getEntities().get(index);
    player.setExped(isExped);
  }
  
  /**
   * The method updates the player's class type
   * @param username the name of the player
   * @param classType the class type
   * @param colour       the colour
   * @param xTileOG     the x tile
   * @param yTileOG     the y tile
   */
  
  public void changePlayerClass(String username, int classType, int colour, int xTileOG, int yTileOG) {
    int index = getPlayerMPIndex(username);
    PlayerMP player = (PlayerMP)this.getEntities().get(index);
    player.classType = classType;
    player.colour = colour;
    player.xTileOG = xTileOG;
    player.yTileOG = yTileOG;
  }
  
  /**
   * The method adds an entity to the list of entities
   * @param entity the new entity
   */
  
  public void addEntity(Entity entity) {
    this.getEntities().add(entity);
  }
  
  /**
   * The method removes an entity from the list of entities
   * @param entity the removed entity
   */
  
  public void removeEntity(Entity entity) {
    this.getEntities().remove(entity);
  }
  
  /**
   * The method removes a player's connection from the list of entities
   * @param userName the name of the player
   */
  
  public void removePlayerMP(String userName) {
    int index = 0;
    List<Entity> temp = this.getEntities();
    for (Entity e: temp) {
      if (e instanceof PlayerMP && ((PlayerMP)e).getUsername().equalsIgnoreCase(userName)) {
        break;
      }
      index++;
    }
    if (index < temp.size()) {
      temp.remove(index);
    }
  }
  
  /**
   * The method removes a bullet from the list of entities
   * @param userName the name of the bullet
   * @param id               the index of the bullet for reference
   */
  
  public synchronized void removeBullet(String userName, int id) {
    int index = 0;
    List<Entity> temp = this.getEntities();
    for (Entity e: temp) {
      if (e instanceof Bullet && ((Bullet)e).getUsername().equals(userName) && ((Bullet)e).getId() == id) {
        break;
      }
      index++;
    }
    //add the bullet that needs to be removed to the removed entities list
    if (index < temp.size()) {
      this.getRemovedEntities().add(temp.get(index));
    }
  }
  
  /**
   * The method returns the index of the player by its username
   * @param username the name
   * @return the index
   */
  
  private int getPlayerMPIndex(String username) {
    int index = 0;
    List<Entity> temp = this.getEntities();
    for (Entity e: temp) {
      //getting the index of the player based on its username
      if (e instanceof PlayerMP && ((PlayerMP)e).getUsername().equals(username)) {
        break;
      }
      index++;
    }
    return index;
  }
  
  /**
   * The method moves the specified player
   * @param username the name
   * @param x               the x coordinate
   * @param y               the y coordinate
   * @param numSteps the number of steps
   * @param isMoving  the movement status
   * @param movingDir the movement direction
   */
  
  public void movePlayer(String username, int x, int y, int numSteps, boolean isMoving, int movingDir) {
    int index = getPlayerMPIndex(username);
    List<Entity> temp = this.getEntities();
    if (index < temp.size()) {
      //updating the necessary information in the player
      PlayerMP player = (PlayerMP)temp.get(index);
      player.x = x;
      player.y = y;
      player.setMoving(isMoving);
      player.setNumSteps(numSteps);
      player.setMovingDir(movingDir);
    }
  }
  
  /**
   * The method moves the specified bullet
   * @param username the name
   * @param x               the x coordinate
   * @param y               the y coordinate
   * @param numSteps the number of steps
   * @param isMoving  the movement status
   * @param movingDir the movement direction
   * @param id the index of the bullet
   */
  
  public void moveBullet(String userName, int x, int y, int numSteps, boolean isMoving, int movingDir, int id) {
    int index = 0;
    List<Entity> temp = this.getEntities();
    for (Entity e: temp) {
      if (e instanceof Bullet && ((Bullet)e).getUsername().equals(userName) && ((Bullet)e).getId() == id) {
        break;
      }
      index++;
    }
    
    if (index < temp.size()) {
      //updating the bullet information
      Bullet bullet = (Bullet)temp.get(index);
      bullet.x = x;
      bullet.y = y;
      bullet.setMoving(isMoving);
      bullet.setNumSteps(numSteps);
      bullet.setMovingDir(movingDir);
    }
  }
  
  /**
   * The method updates the break counter of the specified crate
   * @param id the index of the crate
   */
  
  public void breakCrate(int id) {
    ((CrateTile)Tile.tiles[id]).count = ((CrateTile)Tile.tiles[id]).count;
  }
}