import engine.Actor;
import javafx.scene.image.Image;

public class Background extends Actor{

	
	Image img;
	public Background(Image i) {
		img = i;
		this.setImage(img);
	}
	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
		
	}
	
}
