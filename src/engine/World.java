package engine;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public abstract class World extends Pane{
	private MyAnimationTimer timer;
	private boolean isTimerRunning;
	private Set<KeyCode> keySet;
	private boolean isHeightSet, isWidthSet;// do later
	private boolean dimensionsAlreadyInitialized;
	double delay; //in seconds
	public Score score;
	
	private boolean isPaused;
	
	public World() {
		this.setPrefSize(this.getWidth(), this.getHeight()); // Maybe not needed; "this" is for readability
		keySet = new HashSet<KeyCode>();
		timer = new MyAnimationTimer();
		
		this.widthProperty().addListener(new ChangeListener<Number>() {
	         public void changed(ObservableValue <?extends Number>observable, Number oldValue, Number newValue){
	        	 isWidthSet = true;
	        	 if(isHeightSet && !dimensionsAlreadyInitialized) {
	        		 onDimensionsInitialized();
	        		 dimensionsAlreadyInitialized = true;
	        	 }
		     }
		});
		this.heightProperty().addListener(new ChangeListener<Number>() {
	         public void changed(ObservableValue <?extends Number>observable, Number oldValue, Number newValue){
	        	 isHeightSet = true;
	        	 if(isWidthSet && !dimensionsAlreadyInitialized) {
	        		 onDimensionsInitialized();
	        		 dimensionsAlreadyInitialized = true;

	        	 }
		     }
		});
		
		this.sceneProperty().addListener(new ChangeListener<Scene>() {
			public void changed(ObservableValue<?extends Scene>observable, Scene oldScene, Scene newScene) {
				if(newScene!=null) {
					getFocus();
				}
			}
		});
		
		this.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				keySet.add(event.getCode());
			}
		});
		
		this.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				keySet.remove(event.getCode());
			}
		});
		
		delay = 1/100;
		
	}
	
	public void getFocus() {
		this.requestFocus();
	}
	
	public abstract void onDimensionsInitialized();
	
	public abstract void act(long now);
	
	public void add(Actor actor) {
		getChildren().add(actor);
		actor.addedToWorld();
	}
	public void remove(Actor actor) {
		getChildren().remove(actor);
	}
	
	public void start() {
		timer.start();
		isTimerRunning = true;
	}
	public void stop() {
		timer.stop();
		isTimerRunning = false;
	}
	
	public boolean isStopped() {
		return !isTimerRunning;
	}
	
	//DO NOT REMOVE THIS PRARTHAN!!
	public Score getScore() {
		for(Node c : getChildren()) {
			if(c instanceof Score) return (Score)c;
		}
		return null;
	}
	
	public boolean getActor(Actor a) {
		List<Actor> listActors = getObjects(Actor.class);
		for(Actor act : listActors) {
			if(act==a) return true;
		}
		return false;
	}
	
	
	public <A extends Actor> List<A> getObjects(Class<A> classType){
		ArrayList<A> arr = new ArrayList<A>();
		for(Node c: getChildren()) {
			if(classType.isAssignableFrom(c.getClass())) arr.add((A) c);
			//if(c instanceof Actor && classType.isInstance(c)) arr.add((A) c);
		}
		
		return arr;
	}
	
	public <A extends Actor> List<A> getObjectsAt(double x, double y, Class<A> cls) {
		List<A> list = getObjects(cls);
		List<A> ret = new ArrayList<A>();
		for(A actor : list) {
			/*
			Point2D p = actor.sceneToLocal(x, y);
			Rectangle r = new Rectangle();
			r.setX(p.getX());
			r.setY(p.getY());
			r.setWidth(actor.getWidth());
			r.setHeight(actor.getHeight());
			*/
			/*
			System.out.println(actor.sceneToLocal(x, y));
			if(actor.contains(actor.sceneToLocal(x, y))) {
			//if(r.contains(actor.sceneToLocal(x, y))) {
			//if(actor.contains(actor.sceneToLocal(x, y))) {
			//if(actor.contains(x, y)) {
				ret.add(actor);
			}
			*/
			if(actor.getBoundsInParent().contains(x, y)){
				ret.add(actor);
			}
		}
		return ret;
	}
	
	public boolean isKeyPressed(KeyCode code) {
		return keySet.contains(code);
	}
	
	public abstract void onGameOver();
	
	public void unpause() {
		isPaused = false;
	}
	
	public void pause() {
		isPaused = true;
	}
	
	
	class MyAnimationTimer extends AnimationTimer {
		long old = 0;

		@Override
		public void handle(long now) {
			if(now-old > (delay)*(Math.pow(10, 9))) {
				old = now;
				
				act(now);
				if(!isPaused) {
					List<Actor> as = getObjects(Actor.class);
					for (int i = 0; i <  as.size(); i++) {
						Actor a = as.get(i);
						if(a.getWorld()!=null) as.get(i).act(now);
					}	
				}
			}
		}	
		
	}
}
