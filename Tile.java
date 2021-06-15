/* [Tile.java]
 * The abstract class of a tile on the map
 * @author Dylan Wang
 * @version 1.0, Jan 18, 2021
 */
abstract class Tile {
  
  /** The array of the tiles */
  static final Tile[] tiles = new Tile[256];
  /** A void tile */
  public static final Tile VOID = new BasicSolidTile(0, 0, 0, Colours.get(432, -1, -1, -1), 0xFF610000);
  /** A wall tile */
  static final Tile WALL = new BasicSolidTile(1,4,0,Colours.get(310,444,020,-1),0xFFFF0000);
  /** A ground tile */
  static final Tile GROUND = new BasicTile(2,1,0,Colours.get(-1,-1,-1,432),0xFF00FF00);
  /** A water tile */
  public static final Tile WATER = new AnimatedTile(3, new int[][] { { 0, 5 }, { 1, 5 }, { 2, 5 }, { 1, 5 } },
                                                    Colours.get(-1, 133, 444, -1), 0xFF0000FF, 1000);
  /** A bone tile */
  static final Tile BONE = new BasicTile(4,3,0,Colours.get(432,555,411,-1),0xFFFFFFFF);
  /** A path tile */
  static final Tile PATH = new BasicTile(5,5,0,Colours.get(432,222,333,444),0xFF3A2A1D);
  /** A crate tile */
  static final Tile CRATE = new CrateTile(6,new int[][] {{6,0},{0,6},{1,6},{1,0}},Colours.get(320,210,-1,432),0xFFF0F000);
  /** A cactus tile */
  static final Tile CACTUS = new BasicSolidTile(7,7,0,Colours.get(432,020,030,040),0xFF000000);
  /** A mud tile */
  static final Tile MUD = new BasicTile(8,8,0,Colours.get(432,320,310,210),0xFF646464);
  /** A second mud tile */
  static final Tile MUD2 = new BasicTile(9,9,0,Colours.get(432,320,310,210),0xFFC8C8C8);
  
  /** the id of the tile */
  protected byte id;
  /** whether the tile is solid */
  protected boolean solid;
  /** whether the tile is an emitter */
  protected boolean emitter;
  /** the colour of the tile */
  private int colour;
  
  /**
   * Creates an object from the Tile class
   * @param id The id of the tile
   * @param solid Whether the tile is solid
   * @param emitter Whether the tile is an emitter
   * @param colour The colour of the tile
   */
  
  Tile(int id, boolean solid, boolean emitter, int colour) {
    this.id = (byte)id;
    if (tiles[id] != null) {
      throw new RuntimeException("Duplicate tile id on " + id);
    }
    this.solid = solid;
    this.emitter = emitter;
    this.colour = colour;
    tiles[id] = this;
  }
  
  /**
   * A method that returns the id of the tile
   * @return The id of the tile
   */
  
  public byte getId() {
    return this.id;
  }
  
  /**
   * A method that returns whether the tile is solid or not
   * @return Whether the tile is solid
   */
  
  public boolean isSolid() {
    return this.solid;
  }
  
  /**
   * A method that returns whether the tile is an emitter or not
   * @return Whether the tile is an emitter
   */
  
  public boolean isEmitter() {
    return this.emitter;
  }
  
  /**
   * A method that returns the colour of the tile
   * @return The colour of the tile
   */
  
  public int getColour() {
    return this.colour;
  }
  
  /**
   * A method that does something each tick
   */
  
  public abstract void tick();
  
  /**
   * A method that renders/draws the tile onto the screen
   * @param screen The screen projected to the client
   * @param level The level that the tile is on
   * @param x The x coordinates of the tile
   * @param y The y coordinates of the tile
   */
  
  public abstract void render(Screen screen, Level level, int x, int y);
}