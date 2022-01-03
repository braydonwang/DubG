# DubG Video Game

![Project Image](https://braydonwang.github.io/dubg.png)

> This is my DubG Video Game project that I created for my ICS4UE course

---

## Description

A multiplayer, action-arcade, battle royale shooter game with features including turrets, experience levels, crates, and potions. The user can choose to either be the server and host a game or be the client and join an existing game. Made with Dylan Wang.

### In-depth Overview

#### Home Screen
---

![HomeScreen](https://github.com/braydonwang/DubG/blob/main/HomeScreen.png)

#### Controls Page
---
![Controls1](https://github.com/braydonwang/DubG/blob/main/ControlsPage1.png)
![Controls2](https://github.com/braydonwang/DubG/blob/main/ControlsPage2.png)
![Controls3](https://github.com/braydonwang/DubG/blob/main/ControlsPage3.png)

#### How to Play

- Use WASD to move, press the spacebar to shoot, press 'p' to consume potions
- All player have their usernames and health points displayed above their character
- Shoot crates to break them, crates drop a random potion
- Terrain like walls and bushes block projectiles and cannot be walked through
- Water can be shot over, but cannot be walked through
- Use the wisdom (blue) potion to level up
- Once you reach level 3 or level 5, you have the ability to change classes

#### Terrain

![CrateTile](https://github.com/braydonwang/DubG/blob/main/CrateTile.png)
![WallTile](https://github.com/braydonwang/DubG/blob/main/WallTile.png)
![WaterTile](https://github.com/braydonwang/DubG/blob/main/WaterTile.png)
![BushTile](https://github.com/braydonwang/DubG/blob/main/BushTile.png)
![RockTile](https://github.com/braydonwang/DubG/blob/main/RockTIle.png)
![WeedTile](https://github.com/braydonwang/DubG/blob/main/WeedTile.png)
![MushroomTile](https://github.com/braydonwang/DubG/blob/main/MushroomTile.png)

#### Potions

There are four potions in the game. The first one is health, which when consumed, grants the player 2 extra hearts. The second one is speed that increases the player's movement speed. The third is rage which makes the player deal more damage with their projectiles. The last is wisdom which grants the player an additional experience level.

![HealthPot](https://github.com/braydonwang/DubG/blob/main/HealthPot.png)
![SpeedPot](https://github.com/braydonwang/DubG/blob/main/SpeedPot.png)
![RagePot](https://github.com/braydonwang/DubG/blob/main/RagePot.png)
![WisdomPot](https://github.com/braydonwang/DubG/blob/main/WisdomPot.png)


#### Classes

There are six classes in the game other than the starting class. These classes follow a pyramid structure where each class opens up options to two other classes that are similar to its previous class. These classes include sniper, dual wielder, marksman, rocketeer, shotgunner, and gunner. All classes have their own specialty, whether that's attack speed, damage or number of projectiles. 

![Class1](https://github.com/braydonwang/DubG/blob/main/Class1.png)
![Class2](https://github.com/braydonwang/DubG/blob/main/Class2.png)
![Class3](https://github.com/braydonwang/DubG/blob/main/Class3.png)
![Class4](https://github.com/braydonwang/DubG/blob/main/Class4.png)
![Class5](https://github.com/braydonwang/DubG/blob/main/Class5.png)
![Class6](https://github.com/braydonwang/DubG/blob/main/Class6.png)


#### Server POV

Due to the fact that this is a multiplay game, there needs to be a server in all games. The server is able to travel across the entire map, has no health points and cannot be seen by clients. The server's job is to watch over the entire game. Once the server is connected, clients can join the existing game and that's where the multiplayer begins. This was done using Java's DatagramSocket and DatagramPacket class.

![ServerPOV](https://github.com/braydonwang/DubG/blob/main/ServerPOV.png)

#### Client POV

This is what normal gameplay would look like. Notice how every player can see each other but cannot see the server.

![ClientPOV](https://github.com/braydonwang/DubG/blob/main/ClientPOV.png)

#### Language

- Java

#### Contributors

- [Dylan Wang](https://github.com/dylanwang0)

---
