/**
 * [Entity.java]
 * This abstract class encompasses all entities that are in the game.
 * These include players, turrets and bullets.
 * @author Braydon Wang, Dylan Wang
 * @version 1.0, Jan. 25, 2021
 */

abstract class Entity {
  
  /** The x coordinate of the entity */
  int x;
  /** The y coordinate of the entity */
  int y;
  /** The current map level that the entity is on */
  protected Level level;
  
  /**
   * Creates an object from the Entity class
   * @param level the current map level
   */
  
  Entity(Level level) {
    init(level);
  }
  
  /**
   * This method initializes the current map level only once.
   * @param level the current map level
   */
  
  public final void init(Level level) {
    this.level = level;
  }
  
  /**
   * This method ticks each time the loop.
   */
  
  public abstract void tick();
  
  /**
   * This method renders the entities on to the screen.
   */
  
  public abstract void render(Screen screen);
}