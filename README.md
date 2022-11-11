# README #

## Group Information ##

**Team Members**
Andrew Qin
Advaith Nair
Prarthan Ghosh

**Group Number:** 10

**Period:**	1

**Game Title:** Asteroids but Better

## Game Proposal ##

The base game will be a copy of Asteroids, and incorporate some more complex features. For example, there will be a missile system. 
Additionally, there will be in game currency that can be used to upgrade the ship's stats (in a different window). 
To do this, we will also have to implement a pause feature.

Game Controls:

Arrow keys to move.
Primary mouse button to shoot lasers.
Secondary mouse button to shoot missiles.

Game Elements:
Asteroids which break up into smaller asteroids.
Missiles, lasers.
Coins which spawn when an asteroid is hit.
Ship with some internal stats (health, laser damage, missile damage, speed, maneuverability)
If we feel adventurous, an enemy ship or a 2-player system (WASD for the second player, etc).
A pause feature.
A separate window with menu options to upgrade the ship's stats if there are enough coins.
Different sprits for different upgrades, if we feel artsy.

How to Win:

Survive for a certain length of time. We can make each level have a different amount of time, 
and each level will have an increasing speed and size of asteroids.

## Link Examples ##
Provide links to examples of your game idea.  This can be a playable online game, screenshots, YouTube videos of gameplay, etc.

+ [Asteroids](https://freeasteroids.org/)
+ [Starblast](https://starblast.io/)

## Teacher Response ##

Your teacher can add comments and suggestions here

## Class Design and Brainstorm ##

Actor

Ship extends Actor
Asteroid extends Actor
Laser extends Actor
Laser_Split extends Laser
Missile extends Actor
Big_Missile extends Missile
Tracker_Missile extends Big_Missile

***
***
