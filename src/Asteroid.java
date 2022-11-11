
import java.io.File;
import java.util.Random;

import engine.Actor;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Asteroid extends Actor implements Collidable{
	
	private int size;
	private static String[] sizeToName = {"small","medium","big","heavy"};
	private static int[] sizeToDimensions = {30,40,50,60};
	private int health;
	private AsteroidTimer timer;
	private Asteroid a;
	private int asteroidCount = 0;
	private int currentStyle;
	
	private String currentImg;
	
	private boolean died = false;
	
	public Asteroid() {
		a = this;
		timer = new AsteroidTimer();
		Random rand = new Random();
		currentImg = "sprites/" + SpaceWorld.getArtStyle() + "small"+1+".png";
		int x = rand.nextInt(4)+1;
		if(x==1) {
			int pic = rand.nextInt(4)+1;
			String imageName = "sprites/" + SpaceWorld.getArtStyle() + "small"+pic+".png";
			currentImg = imageName;
			Image img = new Image(imageName, 30, 30, false, false);
			this.setImage(img);
			health = 20;
		}else if(x==2) {
			int pic = rand.nextInt(2)+1;
			String imageName = "sprites/" + SpaceWorld.getArtStyle() + "medium"+pic+".png";
			currentImg = imageName;
			Image img = new Image(imageName, 40, 40, false, false);
			this.setImage(img);
			health = 50;
		}else if(x==3) {
			int pic = rand.nextInt(2)+1;
			String imageName = "sprites/" + SpaceWorld.getArtStyle() + "big"+pic+".png";
			currentImg = imageName;
			Image img = new Image(imageName, 50, 50, false, false);
			this.setImage(img);
			health = 70;
		}else {
			int pic = rand.nextInt(2)+1;
			String imageName = "sprites/" + SpaceWorld.getArtStyle() + "heavy"+pic+".png";
			currentImg = imageName;
			Image img = new Image(imageName, 60, 60, false, false);
			this.setImage(img);
			health = 100;
		}
		
		size = x;
		dx = Math.random()+0.1;
		dy = Math.random()+0.1;
		int changeX = rand.nextInt(2);
		int changeY = rand.nextInt(2);
		if(changeX==0) {
			dx=-dx;
		}
		if(changeY==0) {
			dy=-dy;
		}
		
		currentStyle = SpaceWorld.getArtStyle();
	}
	
	public void setHealth(int n) {
		health = n;
		if(health<=0) {
			onDeath();
		}
	}
	
	public int getHealth() {
		return health;
	}
	
	public Asteroid(int n) {
		/*
		 * n is size of asteroid
		 * 1 = small
		 * 2 = medium
		 * 3 = big
		 * 4 = heavy
		 */
		size = n;
		Random rand = new Random();
		String imgName = "sprites/" + SpaceWorld.getArtStyle() +sizeToName[n-1];
		if(n!=1) {
			imgName+=(rand.nextInt(2)+1)+".png";
		}else {
			imgName+=(rand.nextInt(4)+1)+".png";
		}
		
		Image img = new Image(imgName, sizeToDimensions[n-1], sizeToDimensions[n-1], false, false);
		
		this.setImage(img);
		dx = Math.random()+0.5;
		dy = Math.random()+0.5;
		
		// Make it negative 50% chance
		int changeX = rand.nextInt(2);
		int changeY = rand.nextInt(2);
		if(changeX==0) {
			dx=-dx;
		}
		if(changeY==0) {
			dy=-dy;
		}
		
		timer = new AsteroidTimer();
	}

	
	public void onCollision() {
		//COLLISION WITH OTHER ACTOR NOT PLAYER
	}

	public void setSize(int e) {
		size = e;
	}
	
	public void onDeath() {
		//heavy -> 2 big; spawn chance = 7%
		//big --> 3 medium OR 2 small; spawn chance = 13%
		//medium --> 1 OR 2 small; spawn chance = 30%
		//small --> death; spawn chance = 50%
		if(size==4) {
			for(int i=0; i<2; i++) {
				Asteroid a = new Asteroid(size-1);
				a.setX(this.getX());
				a.setY(this.getY());
				getWorld().add(a);
			}
			getWorld().remove(this);
		}else if(size==3) {
			Random rand = new Random();
			int chance = rand.nextInt(2);
			if(chance==0) {
				for(int i=0; i<2; i++) {
					Asteroid a = new Asteroid(size-1);
					a.setX(this.getX());
					a.setY(this.getY());
					getWorld().add(a);
				}
			}else {
				for(int i=0; i<3; i++) {
					Asteroid a = new Asteroid(size-2);
					a.setX(this.getX());
					a.setY(this.getY());
					getWorld().add(a);
				}
			}
			getWorld().remove(this);
		}else if(size==2) {
			Random rand = new Random();
			int num = rand.nextInt(2)+1;
			for(int i=0; i<num; i++) {
				Asteroid a = new Asteroid(size-1);
				a.setX(this.getX());
				a.setY(this.getY());
				getWorld().add(a);
			}
			getWorld().remove(this);
		}else {
			//add coin
			String explosionSound = "explosion.mp3";     // For example
			Media sound = new Media(new File(explosionSound).toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(sound);
			mediaPlayer.setVolume(0.4);
			mediaPlayer.play();
			
			Coin c = new Coin();
			c.setX(getX());
			c.setY(getY());
			getWorld().add(c);
			Image explosion = new Image("sprites/" + SpaceWorld.getArtStyle() + "explosion.png", 50, 50, false, false);
			setImage(explosion);			
			dx = 0;
			dy = 0;
			timer.start();
			
			
		}
		
		died = true;
		
	}
	
	public int getSize() {
		return this.size;
	}
	
	public boolean getDied() {
		return died;
	}
	
	public void missileDeath() {
		//add explosion later :)
		Coin c = new Coin();
		c.setX(getX());
		c.setY(getY());
		getWorld().add(c);
		Image explosion = new Image("sprites/" + SpaceWorld.getArtStyle() + "explosion.png", 50, 50, false, false);
		setImage(explosion);			
		dx = 0;
		dy = 0;
		timer.start();
	}
	
	
	public void completeDeath() {
		Coin c = new Coin();
		c.setX(getX());
		c.setY(getY());
		getWorld().add(c);
		Image explosion = new Image("sprites/" + SpaceWorld.getArtStyle() + "explosion.png", 50, 50, false, false);
		setImage(explosion);			
		dx = 0;
		dy = 0;
		timer.start();
	}

	@Override
	public void act(long now) {
		if(currentStyle!=SpaceWorld.getArtStyle() && currentImg!=null) {
			this.setImage(new Image("sprites/" + SpaceWorld.getArtStyle() + currentImg.substring(9), 30, 30, false, false));
			currentStyle = SpaceWorld.getArtStyle();
		}
		move(dx, dy);
		
		if(asteroidCount>=1) {
			timer.stop();
			getWorld().remove(this);
		}
	}
	
	public double getDx() {
		return this.dx;
	}
	
	public double getDy() {
		return this.dy;
	}
	
	@Override
	public void move(double dx, double dy) {
		double newX = getX() + dx;
		double newY = getY() + dy;
		if(newX< 0 - getWidth()) {
			newX = getWorld().getWidth();
		}else if(newX>getWorld().getWidth() + getWidth()) {
			newX = 0-getWidth()/2;
		}
		
		if(newY< 0 - this.getHeight()) {
			newY = getWorld().getHeight();
		}else if(newY>getWorld().getHeight() + getHeight()/2) {
			newY = 0-this.getHeight()/2;

		}
		setX(newX);
		setY(newY);
	}
	
	class AsteroidTimer extends AnimationTimer {
		long old = 0;

		
		@Override
		public void handle(long now) {
			if (old == 0) old = now;
			if(now-old > 0.1*(Math.pow(10, 9))) {
				old = now;
				asteroidCount++;
				if(getWorld()!=null) {
					getWorld().remove(Asteroid.this);
				}
				timer.stop();
			}
		}
		
	}
	
}
