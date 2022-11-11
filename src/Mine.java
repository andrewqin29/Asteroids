
import java.io.File;
import java.util.List;

import engine.Actor;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Mine extends Actor implements Collidable{

	public static int mineNum = 3;
	MediaPlayer mediaPlayer;
	
	
	public Mine(double tx, double ty) {
		
		/*
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
		*/
		dx = 0;
		dy = 0;
		setX(tx);
		setY(ty);
		
		setImage(new Image("sprites/" + SpaceWorld.getArtStyle() + "mine.png", 30, 30, false, false));
	}
	@Override
	public void onCollision() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeath() {	
		getWorld().remove(this);

	}

	@Override
	public void act(long now) {

		setRotate(this.getRotate() - 5);
		//this.frameCount++;
		
		//hit asteroids
		List<Asteroid> hit = getIntersectingObjects(Asteroid.class);
		if(hit.size()!=0) {
			
			String bombSound = "bombexplosion.mp3";
			Media sound = new Media(new File(bombSound).toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(sound);
			mediaPlayer.play();
			
			Actor a = hit.get(0);
			((Asteroid)a).missileDeath();
			getWorld().remove(this);
			
		}
		//hit other items code below
		
		if(getWorld()!=null) {
			
			if((this!=null) && (getWorld()!=null) && (getX()>=getWorld().getWidth() || getX()<=0 || getY()>=getWorld().getHeight() || getY()<=0)) {
				getWorld().remove(this);
			}
		
	
			if(getWorld()!=null && (getX()<=getWidth()/2 || getX()>=getWorld().getWidth()-getWidth()/2)) {
				dx = -dx;
			}
			if(getWorld()!=null && (getY()<=getHeight()/2 || getY()>=getWorld().getHeight()-getHeight()/2)) {
				dy = -dy;
			}
		}
	} 
	
	public static void decrementAmmo() {
		mineNum--;
	}
	
	public static int getAmmo() {
		return mineNum;
	}
	
	public static void setAmmo(int n) {
		mineNum = n;
	}

}
