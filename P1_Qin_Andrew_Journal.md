## 5/11 ##
Drew Asteroid class sprites on MS Paint and added them into the folder for later use. ~ 30 min 

## 5/13 ## 
Implemented Asteroid completeDeath() function and screen edge wrapping for Asteroid movement. Added basic functionality to SpaceWorld class by implementing act method and isGameOver method. Asteroid splits into several other asteroids when hit by Laser.~ 1 hour

## 5/14 ##
Made many changes:
- Improved Asteroid class by improving constructors using Random class to generate random size Asteroids. Added "health" functionality. Added AsteroidTimer class to display an explosion animation upon Asteroid death.
- Implemented entire Coin class to spawn upon Asteroid death and increment score when collected. Implemented CoinTimer to animate the coin slowly turning while floating in the SpaceWorld.
- Edited Laser class to improve image sizing and functionality when it hits an Asteroid. Implemented bounds so when Laser goes off screen it is removed.
- Made more, minor changes to following classes: Ship, SpaceWorld, Actor, Score.
- Added more images to sprites folder.
Total: ~ 2.5 hours

## 5/16 ##
- Implemented HealthBar class and functionality. Automatically switches images when Ship is hit to provide clear indication of heath. HealthBar is responsible for tracking number of 
lives of Ship. 
- Implemented Ship collecting AmmoPacks
Total: ~ 1 hour

## 5/17 ##
- Added some more images and cleaned some up by resizing/cropping. 
- Added HiScore Text feature to keep track of highest score through a text file on the player's system (Did not finish fully implementing). Utilized BufferedReader and StringTokenizer and other IO features.
- Added HotBar items to the SpaceWorld and adjusted position to fit within the Hotbar object. This was so users could see which weapons they were swapping to and improving overall experience.
Total: ~ 1.5 hours

## 5/18 ##
- Implemented Mine class and resolved some merge conflicts. Added functionality: Mine will automatically, fully destroy any Asteroids it comes in contact with.
- Made some minor changes to AmmoPack and Missile class so that when AmmoPack is collected the Missile class increments its static 'missileNum' field which is responsible for tracking the amount of missiles the player has left to use.
- Troubleshot bug with Asteroid explosion animation not fully working. Troubleshot an annoying NullPointerException issue. Turns out I had previously forgotten to initialize the Timer object (AsteroidTimer) in one of the Asteroid classes constructors.
Total: ~ 1 hour


## 5/20 ##
Prarthan and I worked together to fix bug intersections. ~ 1 hour

## 5/21 ##
- Fully implemented AmmoPack to correctly increment weapons ammo when collected. Used static field in every weapon class to keep track of number of ammo left. Utilized getter and setter methods to be accessable from Ship class which is responsible for key input so when a key (firing) is pressed, the ammo can be adjusted. Decrement when weapon is fired.
- Added AmmoPack spawning chances and location. Random chance it will spawn on either of the 4 boundaries (top, bottom, left, right). Looks more natural than spawning in the middle of the screen out of nowhere.
Total: ~ 1 hour

## 5/25 ##
- Tweaked some variables in Ship class to make movement easier and more controllable.
- Implemented a cool looking 8-bit font to fit with the game's theme.
- Scaled world to be 900x600 instead of 600x400 to make the world bigger and more easily navigable. More space for ship to move making it easier and less crammed as well. Had to scale other previously hard coded components, making them adjustable and responsive to screen dimensions
- Tried to troubleshoot bug of Ship losing health when not colliding with Asteroid. 
Total: ~ 1.5 hours

## 5/31 ##
- Final touchups of games and check.
- Fully implemented HiScore functionality.
Total: ~ 45 mins

