import engine.Actor;
import javafx.scene.image.Image;

public class HealthBar extends Actor {
	
	private static Image[] images = new Image[4];
	private int currentStyle;
	
	public HealthBar() {
		for(int i=0; i<=3; i++) {
			Image img = new Image("sprites/" + SpaceWorld.getArtStyle() + "health"+i+".png", 138*1.5, 30*1.5, false, false);
			images[i] = img;
		}
		
		this.setImage(images[0]);
		
		currentStyle = SpaceWorld.getArtStyle();
	}

	@Override
	public void act(long now) {
		if(currentStyle!=SpaceWorld.getArtStyle()) {
			currentStyle = SpaceWorld.getArtStyle();
			for(int i=0; i<=3; i++) {
				Image img = new Image("sprites/" + SpaceWorld.getArtStyle() + "health"+i+".png", 138*1.5, 30*1.5, false, false);
				images[i] = img;
			}
			this.setImage(images[getWorld().getObjects(Ship.class).get(0).getHealth()]);
		}
		Ship ship = getWorld().getObjects(Ship.class).get(0);
		if(ship!=null) {
			int health = ship.getHealth();
			if(health>=0) {
				this.setImage(images[health]);
			}
			if(health<=0) {
				SpaceWorld world = (SpaceWorld)getWorld();
				world.onGameOver();
			}
			
		}
	}
	

}
