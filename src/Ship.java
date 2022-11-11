import java.io.File;
import java.util.List;

import engine.Actor;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Ship extends Actor{

	Image img;
	private Image hurtImg;
	private double veloc = 0.1; // (r, 0) the r part of the polar coordinate for movement
	private double maxVeloc = 8;
	private double accel = 0.2;
	private double deaccel = 0.1;
	private boolean isMoving = false;
	
	
	
	//init cooldown
	private int laserCooldown = 0;
	
	// Its in degrees
	private double aVeloc = 0.05; // Angular momentum
	private double aMaxVeloc = 4;
	private double aAccel = 0.2;
	private double aDeaccel = 0.1;
	private boolean isTurningLeft = false;
	private boolean isTurningRight = false;
	
	private double dx = 2;
	private double dy = 2;
	
	private double w, h;
	//private double angle = Math.PI/2; //radians Not needed, use getRotate() which is in degrees
	
	//HEALTH
	private int health = 3;
	private boolean isHurt = false;
	private int hurtCount = 0;
	private int hurtDuration = 75;
	public int slot = 1;
	
	private int currentStyle;
	
	//Sound
	String coinCollect = "sprites/coin.mp3";
	String laserSound = "laser.mp3";
	
	
	public Ship() {
		img = new Image("sprites/" + SpaceWorld.getArtStyle() + "ship.png", 50, 50, false, false);
		hurtImg =  new Image("sprites/" + SpaceWorld.getArtStyle() + "hurtship.png", 50, 50, false, false);
		
		this.setImage(hurtImg);
		isHurt = true;
		hurtCount = 0;
		this.setRotate(-90);
		
		currentStyle = SpaceWorld.getArtStyle();
		
		w = img.getWidth();
		h = img.getHeight();
		//this.setX(this.getWorld().getWidth()/2);
		//this.setY(this.getWorld().getHeight()/2);
	}
	
	
	
	@Override
	public void act(long now) {
		if(currentStyle!=SpaceWorld.getArtStyle()) {
			img = new Image("sprites/" + SpaceWorld.getArtStyle() + "ship.png", 50, 50, false, false);
			hurtImg =  new Image("sprites/" + SpaceWorld.getArtStyle() + "hurtship.png", 50, 50, false, false);
			currentStyle = SpaceWorld.getArtStyle();
		}
		if(hurtCount>=hurtDuration) {
			isHurt = false;
			hurtCount = 0;
			setImage(img);
		}
		if(isHurt) {
			hurtCount++;
			if(hurtCount/5%2==0) {
				setImage(hurtImg);
			}else {
				setImage(img);
			}
		}
		
		laserCooldown--;
		//System.out.println(getRotate() + " " + isTurningLeft + " " + isTurningRight);
		//System.out.println(getX() + " " + getY());
		//System.out.println(getWidth());
		// Angular momentum logic ------------------------
		if(getWorld().isKeyPressed(KeyCode.LEFT)) {
			if(!isTurningLeft) {
				aVeloc = -0.1;
			}
			isTurningLeft = true;
			aVeloc -= aAccel;
			veloc -= 0.001; // dunno if this is realistic physics but whatever
		}else if(isTurningLeft){
			aVeloc += aDeaccel;
			if(Math.abs(aVeloc)<0.1) {
				isTurningLeft = false;
				isTurningRight = false;
				aVeloc = 0.1;
			}
		}
		if(Math.abs(aVeloc)<0.1) {
			//System.out.print(true);
			isTurningLeft = false;
			isTurningRight = false;
			aVeloc = 0.1;
		}
		
		if(aVeloc<0) {
			isTurningRight = false;
		}else if(aVeloc>0) {
			isTurningLeft = false;
		}
		
		
		if(getWorld().isKeyPressed(KeyCode.RIGHT)) {
			isTurningRight = true;
			aVeloc += aAccel;
			veloc -= 0.001;
		}else if(isTurningRight){
			aVeloc -= aDeaccel;
			if(Math.abs(aVeloc)<0.1) {
				isTurningLeft = false;
				isTurningRight = false;
				aVeloc = 0.1;
			}
		}
		
		
		if(Math.abs(aVeloc)>=aMaxVeloc) {
			if(aVeloc>0) aVeloc = aMaxVeloc;
			else aVeloc = - aMaxVeloc; 
		}
		
		if(isTurningLeft || isTurningRight) {
			this.setRotate(this.getRotate()+aVeloc);
		}
		
		
		
		// Moving Logic ------------------------
		if(getWorld().isKeyPressed(KeyCode.UP)) { // If the player is holding the up button
			if(isMoving) {
				veloc += accel;
				
			}
			isMoving = true;
		}else if(isMoving) { // If the ship is still in motion but the up button is not pressed
			if(veloc>1) {
				veloc -= deaccel;
			}else {
				veloc -= 0.01;
			}
		}
		
		if(veloc<=0) {  // If the ship has completetly deaccelerated
			isMoving = false;
			veloc = 0.1;
		}
		
		if(veloc>=maxVeloc) {
			veloc = maxVeloc;
		}
		
		if(isMoving) {
			dx = veloc * Math.cos(Math.toRadians(getRotate()));
			dy = veloc * Math.sin(Math.toRadians(getRotate()));
			this.move(dx, dy);
		}
		// Moving Logic ------------------------
		
		
		
		
		
		//Laser Logic
		if(getWorld().isKeyPressed(KeyCode.SPACE)) {
			if(laserCooldown <= 0) {
				//System.out.println((this.getRotate()));
				double r = 0.9*w/2;
				
				double tR = getRotate();
				setRotate(0);
				double cx = getX() + 0.9*w/2;
				double cy = getY() + 0.89*h/2;
				setRotate(tR);
				double tX = cx + r*Math.cos(Math.toRadians(getRotate()));
				double tY = cy + r*Math.sin(Math.toRadians(getRotate()));
				
				if(slot == 1) {
					Laser laser = new Laser(15, this.getRotate(), tX, tY);
					laser.setRotate(laser.getRotate());
					getWorld().add(laser);

					Media sound = new Media(new File(laserSound).toURI().toString());
					MediaPlayer mediaPlayer = new MediaPlayer(sound);
					mediaPlayer.setVolume(0.3);
					mediaPlayer.play();
					
					laserCooldown = 10;
				}else if(slot == 2){
					if(Missile.getAmmo()>0) {
						Missile missile = new Missile(15, this.getRotate(), tX, tY);
						missile.setRotate(this.getRotate());
						getWorld().add(missile);
						laserCooldown = 60;
						Missile.decrementAmmo();
						
						String missileLaunch = "missilelaunch.mp3";
						Media sound = new Media(new File(missileLaunch).toURI().toString());
						MediaPlayer mediaPlayer = new MediaPlayer(sound);
						mediaPlayer.setVolume(1);
						mediaPlayer.play();
					}
				}else {
					if(Mine.getAmmo()>0) {
						Mine mine = new Mine(tX, tY);
						getWorld().add(mine);
						laserCooldown = 80;
						Mine.decrementAmmo();
					}
				}
								
				
			}
			
		}
		
		//System.out.println(getWorld().getObjects(Laser.class).size());
		
		//Collect coins
		List<Coin> coins = getIntersectingObjects(Coin.class);
		for(Coin c : coins) {
			c.onDeath();
		}
		
		//Health
		List<AmmoPack> packs = getIntersectingObjects(AmmoPack.class);
		for(AmmoPack pack: packs) {
			pack.onCollision();
			//System.out.println("Ammo pack collected!");
			
		}
		
		//Asteroids
//		List<Asteroid> asteroids = getIntersectingObjects(Asteroid.class);
//		for(Asteroid a: asteroids) {
//			if(!isHurt) {
//				a.onCollision();
//				System.out.println("Got Hurt!");
//				health--;
//				isHurt = true;
//			}
//		}
		
		Asteroid hit = getOneIntersectingObject(Asteroid.class);
		if(!isHurt && hit!=null) {
			hit.onCollision();
			health--;
			isHurt = true;
//			System.out.println("Ship hit by Asteroid: \nShip position: "+ getX() + " " + getY());
//			System.out.println("Asteroid position: " + hit.getX() + " " + hit.getY() + " " + hit.getDx() + " " + hit.getDy() + " " + hit.getDied());
//			System.out.println("Asteroid size: " + hit.getSize());
		}
		
		List<Heart> hearts = getIntersectingObjects(Heart.class);
		for(Heart h : hearts) {
			if(health<3) {
				health++;
				h.onCollision();
			}
		}
		
	}
	
	@Override
	public void move(double dx, double dy) {
		double newX = getX() + dx;
		double newY = getY() + dy;
		if(newX< 0 - w) {
			newX = getWorld().getWidth();
		}else if(newX>getWorld().getWidth() + w) {
			newX = 0-w/2;
		}
		
		if(newY< 0 - this.getHeight()) {
			newY = getWorld().getHeight();
		}else if(newY>getWorld().getHeight() + getHeight()/2) {
			newY = 0-this.getHeight()/2;

		}
			
		setX(newX);
		setY(newY);
	}
	
	public void setSlot(int i) {
		slot = i;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getSlot() {
		return slot;
	}

}
