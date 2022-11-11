import java.util.Random;

import engine.Actor;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;

public class Heart extends Actor implements Collidable{

	private int currentStyle;
	
	public Heart() {
		
		
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
		
		currentStyle = SpaceWorld.getArtStyle();
		
		setImage(new Image("sprites/" + SpaceWorld.getArtStyle() + "heart.png", 30, 30, false, false));
	}
	@Override
	public void onCollision() {
		onDeath();
	}

	@Override
	public void onDeath() {
		getWorld().remove(this);
	}

	@Override
	public void act(long now) {
		if(currentStyle!=SpaceWorld.getArtStyle()) {
			currentStyle = SpaceWorld.getArtStyle();
			setImage(new Image("sprites/" + SpaceWorld.getArtStyle() + "heart.png", 30, 30, false, false));
		}
		move(dx, dy);
		if(getX()<=-getWidth()/2 || getX()>=getWorld().getWidth()+getWidth()/2) {
			dx = -dx;
		}
		if(getY()<=-getHeight()/2 || getY()>=getWorld().getHeight()+getHeight()/2) {
			dy = -dy;
		}
	} 
}
