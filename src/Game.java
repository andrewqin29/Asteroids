import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import engine.World;
import javafx.application.Application;

/**
 * Name: Andrew Qin
 * Date: Apr 6, 2022
 * Period 1
 * 
 * Does this lab work? 
 * Any other comments?
 */

// Double check your spelling
public class Game extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	
	@Override
	public void start(Stage stage) throws Exception {
		/*
		stage.setTitle("Game");
		stage.sizeToScene();
		
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root);
		
		BallWorld world = new BallWorld(100, 100);
		//root.setCenter(world);
		root.getChildren().add(world);
		world.start();
		stage.show();
		
		System.out.println(world.getChildren());
		*/
		stage.setTitle("Asteroids But Better");
		stage.setResizable(false);
		BorderPane root = new BorderPane();
		World world = new SpaceWorld(600*1.5, 400*1.5);
		
		root.setCenter(world);
		Scene scene = new Scene(root);
		
		world.start();
		
		stage.setScene(scene);
		stage.show();
		
		
		//System.out.println(world.getChildren());
		//System.out.println(world.getChildren().get(0).getLayoutX());
		
	}
}