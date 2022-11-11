import java.util.Random;

import engine.Actor;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;

public class AmmoPack extends Actor implements Collidable{

	
	public AmmoPack() {
		
		
		dx = Math.random();
		dy = Math.random();
		Random rand = new Random();
		int changeX = rand.nextInt(2);
		int changeY = rand.nextInt(2);
		if(changeX==0) {
			dx=-dx;
		}
		if(changeY==0) {
			dy=-dy;
		}
		
		setImage(new Image("sprites/" + SpaceWorld.getArtStyle() + "ammo_pack.png", 40, 40, false, false));
	}
	@Override
	public void onCollision() {
		Missile.setAmmo(Missile.getAmmo()+5);
		Mine.setAmmo(Mine.getAmmo()+3);
		onDeath();
	}

	@Override
	public void onDeath() {
		getWorld().remove(this);
	}

	@Override
	public void act(long now) {
		move(dx, dy);
		if(getX()<=-getWidth()/2 || getX()>=getWorld().getWidth()+getWidth()/2) {
			dx = -dx;
		}
		if(getY()<=-getHeight()/2 || getY()>=getWorld().getHeight()+getHeight()/2) {
			dy = -dy;
		}
	} 
}
