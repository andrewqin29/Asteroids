import java.io.File;
import java.util.Random;

import engine.Actor;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Coin extends Actor implements Collidable{
	
	private static Image[] imgIndex = new Image[6];
	private static int imgPos = 0;
	private CoinTimer timer;
	
	private int count = 0;
	
	public Coin() {
		
		timer = new CoinTimer();
		
		for(int i=0; i<6; i++) {
			if(i!=3) {
				imgIndex[i] = new Image("sprites/" + SpaceWorld.getArtStyle() + "coin"+(i+1)+".png", 20, 20, false, false);
			}else {
				imgIndex[i] = new Image("sprites/" + SpaceWorld.getArtStyle() + "coin"+(i+1)+".png", 10, 20, false, false);
			}
		}
		
		
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
		
		setImage(imgIndex[imgPos]);
		timer.start();
	}
	@Override
	public void onCollision() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeath() {
		
		String musicFile = "coin.mp3";
		Media sound = new Media(new File(musicFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	    
		getWorld().score.setScore(getWorld().score.getScore()+5);
		getWorld().remove(this);
		
	}

	@Override
	public void act(long now) {
		
		move(dx, dy);
		if(getX()<=getWidth()/2 || getX()>=getWorld().getWidth()-getWidth()/2) {
			dx = -dx;
		}
		if(getY()<=getHeight()/2 || getY()>=getWorld().getHeight()-getHeight()/2) {
			dy = -dy;
		}
		if(count>=500) {
			
			getWorld().remove(this);
		}
		count++;
	} 
	
	class CoinTimer extends AnimationTimer {
		long old = 0;
		int count = 0;

		@Override
		public void handle(long now) {
			if(now-old > (Math.pow(10, 9))) {
				old = now;
				imgPos++;
				if(imgPos>=imgIndex.length) {
					imgPos = 0;
				}
				setImage(imgIndex[imgPos]);				
			}
		}
		
	}
}
