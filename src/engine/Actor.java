package engine;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Actor extends ImageView{
	
	protected double dx;
	protected double dy;
	
	public Actor() {};
	
	public abstract void act(long now);
	
	public double getHeight() {
		return getBoundsInParent().getHeight();
	}
	public double getWidth() {
		return getBoundsInParent().getWidth();
	}
	
	public World getWorld() {
		if(getParent() instanceof World) {
			return (World)getParent();
		}else {
			return null;
		}
	}
	
	public void addedToWorld() {
		
	}
	
	public void move(double dx, double dy) {
		setX(getX()+dx);
		setY(getY()+dy);
				
	}
	
	public <A extends Actor> List<A> getIntersectingObjects(Class<A> classType) {
		ArrayList<A> list = new ArrayList<>();
		World world = getWorld();
		for(Node n : world.getObjects(classType)) {
			//if(!(classType.isAssignableFrom(n.getClass()))) continue;
			//System.out.println(n.getClass() + " " + classType+ " "+ n.getClass().isInstance(classType) + n.getClass().equals(classType)+ classType.isInstance(n));
			//if(n.getClass().isInstance(classType)) continue;
			if(classType.isInstance(n) && !n.equals(this) && n.getBoundsInParent().intersects(getBoundsInParent())){
				list.add((A) n);
			}
		}
		
		return list;
		
	}
	
	public <A extends Actor> A getOneIntersectingObject(Class<A> classType) {
		World world = getWorld();
		for(Node n : world.getObjects(classType)) {
			if(!(classType.isAssignableFrom(n.getClass()))) continue;
			
			if(!n.equals(this) && n.getBoundsInParent().intersects(getBoundsInParent())){
				return (A) n;
			}
		}
		return null;
		
	}
	
	//scale image to desired size
	public Image scale(Image source, int targetWidth, int targetHeight) {
	    ImageView imageView = new ImageView(source);
	    imageView.setPreserveRatio(true);
	    imageView.setFitWidth(targetWidth);
	    imageView.setFitHeight(targetHeight);
	    return imageView.snapshot(null, null);
	}
}


