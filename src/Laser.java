import java.util.List;

import engine.Actor;
import javafx.scene.image.Image;

public class Laser extends Projectile {
	
	
	public Laser(double speed, double angle, double x, double y) {
		Image img = new Image("sprites/" + SpaceWorld.getArtStyle() + "bullet.png", 10, 10, false, false);
		this.angle = angle;
		this.setImage(img);
		this.speed = speed;
		this.x = x;
		this.y = y;
		this.setX(x);
		this.setY(y);
		damage = 15;
	}

	@Override
	public void onHit(Actor a) {
		if(a instanceof Asteroid) {
			//((Asteroid)a).setHealth(((Asteroid)a).getHealth()-damage);
			((Asteroid)a).onDeath();
			//System.out.println("HIT ASTEROID");
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

}
