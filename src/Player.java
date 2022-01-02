/*---------------- IMPORTS ---------------*/
import java.util.Queue;
import java.util.LinkedList;
import javax.swing.JPanel;
/*----------------------------------------*/

/* [Player.java]
 * The class of a player
 * @author Dylan Wang
 * @version 1.0, Jan 18, 2021
 */

class Player extends Mob {
  
  /** Handles the player's input */
  private InputHandler input;
  /** The colour of the player */
  public int colour = Colours.get(-1,111,151,411);
  /** The colour of the player's first class option */
  public int classColour = Colours.get(210,430,-1,-1);
  /** The colour of the player's second class option */
  public int classColour2 = Colours.get(210,430,-1,-1);
  /** The colour of player's quit option */
  public int quitColour = Colours.get(400,-1,-1,544);
  /** The player's username */
  private String userName;
  /** The number of bullets shot */
  private int numBullets = 0;
  /** The time of the last bullet shot */
  private long lastBulletTime;
  /** The player's reload time */
  private int reloadTime;
  /** The player's current health */
  private double health;
  /** The player's damage per bullet */
  private double damage;
  /** The player's bullet speed */
  private int bulletSpeed;
  /** The player's inventory */
  private Queue<Potion> inventory;
  /** The time of the last potion drank */
  private long lastPotionTime;
  /** The player's potion cooldown */
  private int potionCD;
  /** The player's experience level */
  public int expLvl;
  /** Whether the player is healing */
  private boolean isHealed;
  /** The time the player started healing */
  private long startHeal;
  /** The player's heal cooldown */
  private int healCD;
  /** Whether the player is levelling */
  private boolean isExped;
  /** The time the player started levelling */
  private long startExp;
  /** The player's experience potion cooldown */
  private int expCD;
  /** Whether the player is raged */
  private boolean isRaged;
  /** The time the player started to be enraged */
  private long startRage;
  /** The player's rage cooldown */
  private int rageCD;
  /** Whether the player is super fast */
  private boolean isFast;
  /** The time the player started to be super fast */
  private long startSpeed;
  /** The player's speed boost cooldown */
  private int speedCD;
  /** The potion's x position on the screen */
  private int potionX;
  /** The potion's y position on the screen */
  private int potionY;
  /** The player's current class type */
  public int classType;
  /** The position of the class type options on the screen */
  private int scrollDown;
  /** The player's x tile position on the sprite sheet */
  public int xTileOG;
  /** The player's y tile position on the sprite sheet */
  public int yTileOG;
  /** Whether the player is dead */
  public boolean dead;
  
  /**
   * Creates an object from the Player class
   * @param level The level that the player is in
   * @param x The x position of the player
   * @param y The y position of the player
   * @param input The class that handles the player's input
   * @param username The player's username
   * @param reloadTime The player's reload time
   */
  
  Player(Level level, int x, int y, InputHandler input, String userName, int reloadTime) {
    super(level,"Player",x,y,1);
    this.input = input;
    this.userName = userName;
    this.lastBulletTime = System.currentTimeMillis();
    this.reloadTime = reloadTime;
    this.health = 5.0;
    this.damage = 0.5;
    this.bulletSpeed = 2;
    this.inventory = new LinkedList<Potion>();
    this.lastPotionTime = System.currentTimeMillis();
    this.potionCD = 2000;
    this.expLvl = 1;
    this.isHealed = false;
    this.startHeal = System.currentTimeMillis();
    this.healCD = 500;
    this.isExped = false;
    this.startExp = System.currentTimeMillis();
    this.expCD = 500;
    this.isRaged = false;
    this.startRage = System.currentTimeMillis();
    this.rageCD = 10000;
    this.isFast = false;
    this.startSpeed = System.currentTimeMillis();
    this.speedCD = 10000;
    this.potionX = 0;
    this.potionY = 0;
    this.classType = 0;
    this.scrollDown = 32;
    this.xTileOG = 0;
    this.yTileOG = 28;
    this.dead = false;
  }
  
  /**
   * This method checks whether the player has collided with a solid tile, player or potion
   * @param xa The player's required change in x position
   * @param ya The player's required change in y position
   * @return Whether the player has collided
   */
  
  public boolean hasCollided(int xa, int ya) {  
    //if the client is the host then there is no collision
    if (this.userName.equals("SERVER") && DubG.game.socketServer != null) {
      return false;
    }
    
    //set the collision box for each moving direction
    int xMin = 4, xMax = 11, yMin = -4, yMax = 9;
    if (movingDir == 0) {
      xMin = 2; yMin = -6; yMax = 3;
    } else if (movingDir == 1) {
      xMin = 2; yMin = 0; yMax = 7;
    } else if (movingDir == 2) {
      xMin = -2; xMax = 5; yMin = -4;
    } 
    
    //loop through each x coordinate the player is occupying
    for (int x = xMin; x < xMax; x++) {
      if (isSolidTile(xa, ya, x, yMin) || isSolidTile(xa, ya, x, yMax)) { //has collided with solid tile
        return true;
      } else if (getPotion(xa, ya, x, yMin) != null) { //has collided with potion
        //add potion to inventory
        inventory.add(getPotion(xa, ya, x, yMin));
        //remove potion from map
        setPotion(xa, ya, x, yMin, this.userName);
      } else if (getPotion(xa, ya, x, yMax) != null) { //has collided with potion
        //add potion to inventory
        inventory.add(getPotion(xa, ya, x, yMax));
        //remove potion from map
        setPotion(xa, ya, x, yMax, this.userName);
      }
    }
    
    //loop through each y coordinate the player is occupying
    for (int y = yMin; y < yMax; y++) {
      if (isSolidTile(xa, ya, xMin, y) || isSolidTile(xa, ya, xMax, y)) { //has collided with solid tile
        return true;
      } else if (getPotion(xa, ya, xMin, y) != null) { //has collided with potion
        //add potion to inventory
        inventory.add(getPotion(xa, ya, xMin, y));
        //remove potion from map
        setPotion(xa, ya, xMin, y, this.userName);
      } else if (getPotion(xa, ya, xMax, y) != null) { //has collided with potion
        //add potion to inventory
        inventory.add(getPotion(xa, ya, xMax, y));
        //remove potion from map
        setPotion(xa, ya, xMax, y, this.userName);
      }
    }
    
    //whether the player collides with another player
    boolean hitPlayer = false;
    
    //loop through all entities
    for (Entity e: level.getEntities()) {
      if (e instanceof Player) { //if the entity is a player
        if (!((Player)e).getUsername().equalsIgnoreCase(this.userName) && !((Player)e).getUsername().equals("SERVER")) { //ignore collision with self and server
          int tempMovingDir = ((Player)e).getMovingDir();
          int x2 = e.x, y2 = e.y, x3 = e.x, y3 = e.y;
          //adjust collision based on moving direction
          if (tempMovingDir == 0) {
            x2 += 2; y2 += 3; x3 += 11; y3 -= 6;
          } else if (tempMovingDir == 1) {
            x2 += 2; y2 += 7; x3 += 11;
          } else if (tempMovingDir == 2) {
            x2 -= 2; y2 += 9; x3 += 5;
          } else {
            x2 += 4; y2 += 9; x3 += 11;
          }
          
          if (this.x + xa + xMin >= x3 || x2 >= this.x + xa + xMax) {
            continue;
          }
          if (this.y + ya + yMax <= y3 || y2 <= this.y + ya + yMin) {
            continue;
          }
          //player collided with another player
          hitPlayer = true;
          break;
        }
      }
    }
    return hitPlayer;
  }
  
  /**
   * This method updates the player each tick
   */
  
  public void tick() {
    //if the player's health does below zero
    if (this.health <= 0 && !dead) {
      //play death sound
      DubG.sound("DeathSound.wav");
      //tell the server to disconnect the player
      Packet01Disconnect packet = new Packet01Disconnect(this.userName);
      packet.writeData(DubG.game.socketClient);
      //the player is dead
      dead = true;
    }
    
    //the change in x position and y position
    int xa = 0, ya = 0;
    
    //if input handler is initialized and the player is alive
    if (input != null && !this.dead) {
      if (input.up.isPressed()) { //move up
        ya--;
      }
      if (input.down.isPressed()) { //move down
        ya++;
      }
      if (input.left.isPressed()) { //move left
        xa--;
      }
      if (input.right.isPressed()) { //move right
        xa++;
      }
      if (input.shoot.isPressed() && !this.userName.equals("SERVER")) { //shoot bullet
        //plays shooting sound for specific class types
        if (this.classType == 1 || this.classType == 3) { //snipers
          DubG.sound("SniperSound.wav");
        } else if (this.classType == 6) { //shotgun
          DubG.sound("Shotgun.wav");
        } else if (this.classType == 4) { //bazooka
          DubG.sound("BazookaSound.wav");
        } else { //pistols
          DubG.sound("GunshotSound.wav");
        }
        if (this.classType == 4) { //class type is rocketeer
          //create projectile
          Bullet bullet = new Bullet(level,this.userName,this.x,this.y,this.bulletSpeed,this.getMovingDir(),numBullets,this.damage,0,9);
          //add projectile to the level
          level.addedEntities.add(bullet);
          //send projectile enter data to the server
          Packet03BulletEnter packet = new Packet03BulletEnter(this.userName,this.x,this.y,bullet.getSpeed(),bullet.getMovingDir(),this.numBullets,this.damage,0,9);
          packet.writeData(DubG.game.socketClient);
          numBullets++;
          //shooting stops
          input.shoot.toggle(false);
        } else { //class type is not rocketeer
          //create projectile
          Bullet bullet = new Bullet(level,this.userName,this.x,this.y,this.bulletSpeed,this.getMovingDir(),numBullets,this.damage);
          //add projectile to the level
          level.addedEntities.add(bullet);
          //send projectile enter data to the server
          Packet03BulletEnter packet = new Packet03BulletEnter(this.userName,this.x,this.y,bullet.getSpeed(),bullet.getMovingDir(),this.numBullets,this.damage);
          packet.writeData(DubG.game.socketClient);
          numBullets++; 
          //shooting stops
          input.shoot.toggle(false);
          //these class types shoot extra bullets
          if (this.classType == 2 || this.classType == 5 || this.classType == 6) { //dual pistols 1 & 2, shotgun
            bullet = null;
            if (this.getMovingDir() <= 1) {
              //create projectile
              bullet = new Bullet(level,this.userName,this.x + 5,this.y,this.bulletSpeed,this.getMovingDir(),numBullets,this.damage);
            } else {
              //create projectile
              bullet = new Bullet(level,this.userName,this.x,this.y + 5,this.bulletSpeed,this.getMovingDir(),numBullets,this.damage);
            }
            //add projectile to the level
            level.addedEntities.add(bullet);
            //send projectile enter data to the server
            Packet03BulletEnter packet2 = new Packet03BulletEnter(this.userName,bullet.x,bullet.y,bullet.getSpeed(),bullet.getMovingDir(),this.numBullets,this.damage);
            packet2.writeData(DubG.game.socketClient);
            numBullets++; 
            if (this.classType == 6) { //shotgun
              bullet = null;
              if (this.getMovingDir() == 0) {
                //create projectile
                bullet = new Bullet(level,this.userName,this.x + 3,this.y - 4,this.bulletSpeed,this.getMovingDir(),numBullets,this.damage);
              } else if (this.getMovingDir() == 1) {
                //create projectile
                bullet = new Bullet(level,this.userName,this.x + 3,this.y + 4,this.bulletSpeed,this.getMovingDir(),numBullets,this.damage);
              } else if (this.getMovingDir() == 2) {
                //create projectile
                bullet = new Bullet(level,this.userName,this.x - 4,this.y + 3,this.bulletSpeed,this.getMovingDir(),numBullets,this.damage);
              } else {
                //create projectile
                bullet = new Bullet(level,this.userName,this.x + 4,this.y + 3,this.bulletSpeed,this.getMovingDir(),numBullets,this.damage);
              }
              //add projectile to the level
              level.addedEntities.add(bullet);
              //send projectile enter data to the server
              Packet03BulletEnter packet3 = new Packet03BulletEnter(this.userName,bullet.x,bullet.y,bullet.getSpeed(),bullet.getMovingDir(),this.numBullets,this.damage);
              packet3.writeData(DubG.game.socketClient);
              numBullets++; 
            }
          } 
        }
      }
      if (input.use.isPressed() && !inventory.isEmpty()) { //uses potion
        //plays potion sound
        DubG.sound("DrinkSound.wav"); 
        //uses the first potion in the queue and removes it
        Potion usedPotion = inventory.poll();
        if (usedPotion.getId() == 0) { //health potion
          //increase player health
          this.setHealth(Math.min(this.getHealth() + 2.0, 5.0));
          this.isHealed = true;
          //update start potion time
          this.startHeal = System.currentTimeMillis();
          //send health increase packet to server
          Packet10HealthPotion packet = new Packet10HealthPotion(this.userName);
          packet.writeData(DubG.game.socketClient);
          
        } else if (usedPotion.getId() == 1) { //rage potion 
          if (isRaged == false) {
            //increase player damage
            this.setDamage(this.getDamage() * 2.0);
            this.isRaged = true;
            //send rage packet to server
            Packet11RageStart packet = new Packet11RageStart(this.userName);
            packet.writeData(DubG.game.socketClient);
          }
          //update start potion time
          this.startRage = System.currentTimeMillis();
        } else if (usedPotion.getId() == 2) { //swift potion
          if (isFast == false) {
            //increase player speed
            this.speed *= 2;
            this.isFast = true;
            //send speed packet to server
            Packet13SpeedStart packet = new Packet13SpeedStart(this.userName);
            packet.writeData(DubG.game.socketClient);
          }
          //update start potion time
          this.startSpeed = System.currentTimeMillis();
        } else if (usedPotion.getId() == 3) { //wisdom potion
          //increase player experience level
          this.expLvl ++;
          this.isExped = true;
          //update start potion time
          this.startExp = System.currentTimeMillis();
          //send wisdom packet to server
          Packet16ExpStart packet = new Packet16ExpStart(this.userName);
          packet.writeData(DubG.game.socketClient);
        }
        //p pressed is set to false
        input.use.toggle(false);
      }
    }
    
    //when the heal time is up, the player stops healing
    if (isHealed == true && System.currentTimeMillis() - startHeal >= healCD) {
      this.isHealed = false;
      Packet15HealStop packet = new Packet15HealStop(this.userName);
      packet.writeData(DubG.game.socketClient);
    }
    
    //when the rage time is up, the player is no longer raged
    if (isRaged == true && System.currentTimeMillis() - startRage >= rageCD) {
      this.setDamage(this.getDamage() / 2.0);
      this.isRaged = false;
      Packet12RageEnd packet = new Packet12RageEnd(this.userName);
      packet.writeData(DubG.game.socketClient);
    }
    
    //when the speed boost time is up, the player is no super fast
    if (isFast == true && System.currentTimeMillis() - startSpeed >= speedCD) {
      this.speed /= 2;
      this.isFast = false;
      Packet14SpeedEnd packet = new Packet14SpeedEnd(this.userName);
      packet.writeData(DubG.game.socketClient);
    }
    
    //when the experience time is up, the player is no levelling
    if (isExped == true && System.currentTimeMillis() - startExp >= expCD) {
      this.isExped = false;
      Packet17ExpStop packet = new Packet17ExpStop(this.userName);
      packet.writeData(DubG.game.socketClient);
    }
    
    //move the player
    if (xa != 0 || ya != 0) {
      move(xa,ya);
      isMoving = true;
      
      //send packet to server to update players about his current position
      Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y,this.numSteps,this.isMoving,this.movingDir);
      packet.writeData(DubG.game.socketClient);
      
    } else {
      //player is not longer moving
      isMoving = false;
    }
    
    //the class option bar drops down when a certain level is reached and goes back up when a class is selected
    if (this.expLvl == 3 && this.classType == 0 && scrollDown > 0) {
      this.scrollDown--;
    } else if (this.expLvl == 3 && (this.classType == 1 || this.classType == 2) && scrollDown < 33) {
      this.scrollDown++;
    } else if (this.expLvl == 5 && this.classType <= 2 && scrollDown > 0) {
      this.scrollDown--;
    } else if (this.expLvl == 5 && this.classType >= 3 && this.classType <= 6 && scrollDown < 33) {
      this.scrollDown++;
    }
  }
  
  /**
   * This method renders/draws the player and game components to the screen
   * @param screen The screen that is projected to the client
   */
  
  public void render(Screen screen) {
    
    //ignore host client
    if (this.userName.equals("SERVER") && DubG.game.socketServer == null) {
      return;
    }
    
    //ignore dead players
    if (!this.userName.equals(DubG.game.player.getUsername()) && DubG.game.player.dead) {
      return;
    }
    
    //x and y tile position on spritesheet
    int xTile = xTileOG, yTile = yTileOG;
    //colour of player
    int tempColour = this.colour;
    
    //host colours are different
    if (this.userName.equals("SERVER") && DubG.game.socketServer != null) {
      xTile = 0; yTile = 22;
      tempColour = Colours.get(-1, 333, 444, 555);
    }
    
    //determine tile and reflection based on moving direcion
    int walkingSpeed = 4;
    int flipTop = (numSteps >> walkingSpeed) & 1;
    int flipBottom = (numSteps >> walkingSpeed) & 1;
    
    if (movingDir == 0) { //facing up
      xTile += ((numSteps >> walkingSpeed) & 1) * 2;
      flipTop = (movingDir) % 2;
      flipBottom = (movingDir) % 2;
    } else if (movingDir == 1) { //facing down
      xTile += ((numSteps >> walkingSpeed) & 1) * 2;
      flipTop = 2;
      flipBottom = 2;
    } else if (movingDir > 1) { //facing left or right
      xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
      flipTop = (movingDir - 1) % 2;
      flipBottom = (movingDir - 1) % 2;
    }
    
    int modifier = 8 * scale;
    int xOffset = x - modifier/2, yOffset = y - modifier/2 - 4;
    
    //display player death screen
    if (dead && this.userName.equals(DubG.game.player.getUsername())) {
      
      if (xOffset < 76) {
        xOffset = 76;
      } else if (xOffset > 428) {
        xOffset = 428;
      }
      
      if (yOffset < 50) {
        yOffset = 50;
      } else if (yOffset > 445) {
        yOffset = 445;
      }
      
      //display player death screen
      Font.render("You died!", screen, xOffset - 60, yOffset - 30, Colours.get(-1,-1,-1,555), 2);
      Font.render("Better luck", screen, xOffset - 40, yOffset - 5, Colours.get(-1,-1,-1,500), 1);
      Font.render("next time", screen, xOffset - 33, yOffset + 10, Colours.get(-1,-1,-1,500), 1);
      screen.render(xOffset - 10, yOffset + 40, 0 + 1 * 32, this.quitColour, 0, 3);
      screen.render(xOffset + 14, yOffset + 40, 1 + 1 * 32, this.quitColour, 0, 3);
      Font.render("Quit", screen, xOffset - 10, yOffset + 40, Colours.get(-1, -1, -1, 000), 1);
      return;
    }
    
    int temp = 0;
    
    if (movingDir == 1) {
      temp = 8 * scale;
    }
    
    //change colours to show active potion effects
    if (isHealed == true) {
      tempColour = Colours.get(-1,111,522,411);
    } else if (isExped == true) {
      tempColour = Colours.get(-1,111,355,055);
    } else if (isRaged == true) {
      tempColour = Colours.get(-1,111,324,204);
    } else if (isFast == true) {
      tempColour = Colours.get(-1,111,252,141);
    } 
    
    //draw the player on the screen
    screen.render(xOffset + (modifier * flipTop) - 2*temp, yOffset + temp, xTile + yTile * 32, tempColour,flipTop,scale);
    screen.render(xOffset + modifier - (modifier * flipTop) + 2*temp, yOffset + temp, (xTile + 1) + yTile * 32, tempColour,flipTop,scale);
    screen.render(xOffset + (modifier * flipBottom) - 2*temp, yOffset + modifier - temp, xTile + (yTile + 1) * 32, tempColour,flipBottom,scale);
    screen.render(xOffset + modifier - (modifier * flipBottom) + 2*temp, yOffset + modifier - temp, (xTile + 1) + (yTile + 1) * 32, tempColour,flipBottom,scale);
    
    //display the player's username
    if (userName != null) {
      Font.render(userName, screen, xOffset - (userName.length() / 2 * 8 - (userName.length() % 2 == 0 ? 8 : 4)), yOffset - 18, Colours.get(-1,-1,-1,555), 1);
    }
    
    //ignore for host
    if (this.userName.equals("SERVER") && DubG.game.socketServer != null) {
      return;
    }
    
    int i = 0;
    for (; i < (int)this.health; i++) {
      screen.render(xOffset - 12 + (i * 8) , yOffset - 8, 4 * 32, Colours.get(000,400,-1,-1),0,1);
    }
    if (this.health - (int)this.health == 0.5) {
      screen.render(xOffset - 12 + (i * 8), yOffset - 8, 4 * 32 + 1, Colours.get(000,400,111,-1),0,1);
      i++;
    }
    for (; i < 5; i++) {
      screen.render(xOffset - 12 + (i * 8) , yOffset - 8, 4 * 32 + 2, Colours.get(000,-1,111,-1),0,1);
    }
    
    if (!DubG.game.player.getUsername().equalsIgnoreCase(this.userName)) {
      return;
    }
    
    //gets the first potion in the player's inventory (queue)
    Potion curPotion = inventory.peek();
    int potionColour = 0;
    
    potionX = xOffset + 56; potionY = yOffset + 40;
    
    //makes sure the potion doesn't move at borders of the map
    if (potionX < 132) {
      potionX = 132;
    } else if (potionX > 484) {
      potionX = 484;
    }
    
    //makes sure the potion doesn't move at borders of the map
    if (potionY < 90) {
      potionY = 90;
    } else if (potionY > 485) {
      potionY = 485;
    }
    
    //set potion colour based on type
    if (curPotion != null) {
      if (curPotion.getId() == 0) {
        potionColour = Colours.get(000, 411, 320, -1);
      } else if (curPotion.getId() == 1) {
        potionColour = Colours.get(000, 204, 320, -1);
      } else if (curPotion.getId() == 2) {
        potionColour = Colours.get(000, 044, 320, -1);
      } else if (curPotion.getId() == 3) {
        potionColour = Colours.get(000, 055, 320, -1);
      }
    }
    
    //draw the circle representing the player 1-item inventory
    int circleColour = Colours.get(210,431,-1,-1);
    potionX -= 13; potionY -= 10;
    screen.render(potionX, potionY, 0 + 12 * 32, circleColour, 0, 3);
    screen.render(potionX + 24, potionY, 1 + 12 * 32, circleColour, 0, 3);
    screen.render(potionX, potionY + 24, 0 + 13 * 32, circleColour, 0, 3);
    screen.render(potionX + 24, potionY + 24, 1 + 13 * 32, circleColour, 0, 3);
    
    potionX += 13; potionY += 10;
    //display potion in the circle
    if (potionColour != 0) {
      screen.render(potionX, potionY,  0 + 7 * 32, potionColour, 0, 2);
    }
    
    //displays player experience level
    int levelColour = Colours.get(000,055,-1,-1);
    potionX -= 133; potionY -= 90;
    screen.render(potionX, potionY, 0 + 12 * 32, levelColour, 0, 1);
    screen.render(potionX + 8, potionY, 1 + 12 * 32, levelColour, 0, 1);
    screen.render(potionX, potionY + 8, 0 + 13 * 32, levelColour, 0, 1);
    screen.render(potionX + 8, potionY + 8, 1 + 13 * 32, levelColour, 0, 1);
    Font.render(this.expLvl+"",screen,potionX + 4,potionY + 3,Colours.get(-1,-1,-1,000),1);
    
    //displays class drop down options
    if ((this.expLvl == 3 || this.expLvl == 5) && this.scrollDown < 32) { 
      potionX += 56; potionY += 4 - this.scrollDown;
      //display 2 drop down class options
      screen.render(potionX, potionY, 0 + 26 * 32, classColour,0,2);
      screen.render(potionX + 16, potionY, 1 + 26 * 32, classColour,0,2);
      screen.render(potionX, potionY + 16, 0 + 27 * 32, classColour,0,2);
      screen.render(potionX + 16, potionY + 16, 1 + 27 * 32, classColour,0,2);
      screen.render(potionX + 32, potionY, 2 + 26 * 32, classColour2,0,2);
      screen.render(potionX + 48, potionY, 3 + 26 * 32, classColour2,0,2);
      screen.render(potionX + 32, potionY + 16, 2 + 27 * 32, classColour2,0,2);
      screen.render(potionX + 48, potionY + 16, 3 + 27 * 32, classColour2,0,2);
      if (this.expLvl == 3) { //marksman
        int colour = Colours.get(-1,111,151,541);
        potionX += 5; potionY += 5;
        //display 2 drop down class options
        screen.render(potionX, potionY, 4 + 26 * 32, colour,0,1);
        screen.render(potionX + 8, potionY, 5 + 26 * 32, colour,0,1);
        screen.render(potionX, potionY + 8, 4 + 27 * 32, colour,0,1);
        screen.render(potionX + 8, potionY + 8, 5 + 27 * 32, colour,0,1);
        potionX += 31; colour = Colours.get(-1,111,151,224);
        screen.render(potionX, potionY, 6 + 26 * 32, colour,0,1);
        screen.render(potionX + 8, potionY, 7 + 26 * 32, colour,0,1);
        screen.render(potionX, potionY + 8, 6 + 27 * 32, colour,0,1);
        screen.render(potionX + 8, potionY + 8, 7 + 27 * 32, colour,0,1);
      } else { 
        if (this.classType == 1) { //sniper
          int colour = Colours.get(-1,111,151,055);
          potionX += 5; potionY += 5;
          //display 2 drop down class options
          screen.render(potionX, potionY, 8 + 26 * 32, colour,0,1);
          screen.render(potionX + 8, potionY, 9 + 26 * 32, colour,0,1);
          screen.render(potionX, potionY + 8, 8 + 27 * 32, colour,0,1);
          screen.render(potionX + 8, potionY + 8, 9 + 27 * 32, colour,0,1);
          potionX += 31; colour = Colours.get(-1,111,151,214);
          screen.render(potionX, potionY, 10 + 26 * 32, colour,0,1);
          screen.render(potionX + 8, potionY, 11 + 26 * 32, colour,0,1);
          screen.render(potionX, potionY + 8, 10 + 27 * 32, colour,0,1);
          screen.render(potionX + 8, potionY + 8, 11 + 27 * 32, colour,0,1);
        } else { //the rest of the classes
          int colour = Colours.get(-1,111,151,040);
          potionX += 5; potionY += 5;
          //display 2 drop down class options
          screen.render(potionX, potionY, 12 + 26 * 32, colour,0,1);
          screen.render(potionX + 8, potionY, 13 + 26 * 32, colour,0,1);
          screen.render(potionX, potionY + 8, 12 + 27 * 32, colour,0,1);
          screen.render(potionX + 8, potionY + 8, 13 + 27 * 32, colour,0,1);
          potionX += 31; colour = Colours.get(-1,111,151,210);
          screen.render(potionX, potionY, 14 + 26 * 32, colour,0,1);
          screen.render(potionX + 8, potionY, 15 + 26 * 32, colour,0,1);
          screen.render(potionX, potionY + 8, 14 + 27 * 32, colour,0,1);
          screen.render(potionX + 8, potionY + 8, 15 + 27 * 32, colour,0,1);
        }
      }
    }
  }
  
  /**
   * This method returns the player's username
   * @return The player's username
   */
  
  public String getUsername() {
    return this.userName;
  }
  
  /**
   * This method returns the last time the player shot
   * @return The last time the player shot
   */
  
  public long getLastBulletTime() {
    return this.lastBulletTime;
  }
  
  /**
   * This method returns the player's reload time
   * @return The player's reload time
   */
  
  public int getReloadTime() {
    return this.reloadTime;
  }
  
  /**
   * This method sets/updates the last time the player shot
   * @param lastBulletTime The last time the player shot
   */
  
  public void setLastBulletTime(long lastBulletTime) {
    this.lastBulletTime = lastBulletTime;
  }
  
  /**
   * This method sets/updates the player's reload time
   * @param reloadTime The player's reload time
   */
  
  public void setReloadTime(int reloadTime) {
    this.reloadTime = reloadTime;
  }
  
  /**
   * This method returns the player's current health
   * @return The player's current health
   */
  
  public double getHealth() {
    return this.health;
  }
  
  /**
   * This method sets/updates the player's current health
   * @param health The player's current health
   */
  
  public void setHealth(double health) {
    this.health = health;
  }
  
  /**
   * This method returns the player's damage per bullet
   * @return The player's damage per bullet
   */
  
  public double getDamage() {
    return this.damage;
  }
  
  /**
   * This method sets/updates the player's damage per bullet
   * @param damage The player's damage per bullet
   */
  
  public void setDamage(double damage) {
    this.damage = damage;
  }
  
  /**
   * This method returns the player's bullet speed
   * @return The player's bullet speed
   */
  
  public int getBulletSpeed() {
    return this.bulletSpeed;
  }
  
  /**
   * This method sets/updates the player's bullet speed
   * @param bulletSpeed The player's bullet speed
   */
  
  public void setBulletSpeed(int bulletSpeed) {
    this.bulletSpeed = bulletSpeed;
  }
  
  /**
   * This method returns the last time the player used a potion
   * @return The last time the player used a potion
   */
  
  public long getLastPotionTime() {
    return this.lastPotionTime;
  }
  
  /**
   * This method sets/updates the last time the player used a potion
   * @param lastPotionTime The last time the player used a potion
   */
  
  public void setLastPotionTime(long lastPotionTime) {
    this.lastPotionTime = lastPotionTime;
  }
  
  /**
   * This method returns the player's potion cooldown
   * @return The player's potion cooldown
   */
  
  public int getPotionCD() {
    return this.potionCD;
  }
  
  /**
   * This method sets/updates whether the player is raged 
   * @param isRaged Whether the player is raged
   */
  
  public void setRage(boolean isRaged) {
    this.isRaged = isRaged;
    this.startRage = System.currentTimeMillis();
  }
  
  /**
   * This method sets/updates whether the player is sped up
   * @param isFast Whether the player is sped up
   */
  
  public void setFast(boolean isFast) {
    this.isFast = isFast;
    this.startSpeed = System.currentTimeMillis();
  }
  
  /**
   * This method sets/updates whether the player is healing
   * @param isHealed Whether the player is healing
   */
  
  public void setHealed(boolean isHealed) {
    this.isHealed = isHealed;
    this.startHeal = System.currentTimeMillis();
  }
  
  /**
   * This method sets/updates whether the player is levelling
   * @param isExped Whether the player is levelling
   */
  
  public void setExped(boolean isExped) {
    this.isExped = isExped;
    this.startExp = System.currentTimeMillis();
  }
}