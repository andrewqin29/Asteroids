import java.util.List;

import engine.Actor;
import javafx.scene.image.Image;

public class Missile extends Projectile {
	
	private static int missileNum = 5;
	
	
	public Missile(double speed, double angle, double x, double y) {
		Image img = new Image("sprites/" + SpaceWorld.getArtStyle() + "missile.png", 30, 20, false, false);
		this.angle = angle;
		this.setImage(img);
		this.speed = speed;
		this.x = x;
		this.y = y;
		this.setX(x);
		this.setY(y);
		damage = 100;
	}

	@Override
	public void onHit(Actor a) {
		if(a instanceof Asteroid) {
			//((Asteroid)a).setHealth(((Asteroid)a).getHealth()-damage);
			((Asteroid)a).missileDeath();
			//System.out.println("HIT ASTEROID with missile");
		}else {
			//enemy spacecraft
		}
	}

	@Override
	public void act(long now) {
		//this.frameCount++;
		if(frameCount == 300) {
			getWorld().remove(this);
			this.frameCount = 0;
		}
		move();
		//hit asteroids
		List<Asteroid> hit = getIntersectingObjects(Asteroid.class);
		if(hit.size()!=0) {
			Actor a = hit.get(0);
			onHit(a);
			getWorld().remove(this);
		}
		//hit other items code below
		
		if((this!=null) && (getWorld()!=null) && (getX()>=getWorld().getWidth() || getX()<=0 || getY()>=getWorld().getHeight() || getY()<=0)) {
			getWorld().remove(this);
		}
	}
	
	public static void decrementAmmo() {
		missileNum--;
	}
	
	public static int getAmmo() {
		return missileNum;
	}
	
	public static void setAmmo(int n) {
		missileNum = n;
	}
}
