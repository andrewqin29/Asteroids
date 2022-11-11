import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import engine.Actor;
import engine.Score;
import engine.World;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SpaceWorld extends World{

	private boolean gameOver = false;
	private double width, height;
	private Ship player;
	private int frameCount = 0;
	private int numAsteroids = 0;
	private int numAmmo = 0;
	private Font mainBitFont = Font.loadFont(SpaceWorld.class.getClassLoader().getResourceAsStream("sprites/" + getArtStyle() + "8bitfont.ttf"), 22);
	private Font hiBitFont = Font.loadFont(SpaceWorld.class.getClassLoader().getResourceAsStream("sprites/" + getArtStyle() + "8bitfont.ttf"), 14);
	private int delay = 10;
	private Image hotbar;
	private ImageView hotbarView;
	private int hotbarSlot = 1;
	private BufferedReader br;
	
	ImageView missile;
	ImageView laser;
	ImageView mines;
	
	private ImageView playButton;
	private ImageView gameOverButton;
	private HealthBar healthbar;
	private StringTokenizer st;
	private Text hiScore;
	private int highScore = 0;
	private ImageView retryButton;
	
	private Text missileText;
	private Text mineText;
	private Text laserText;
	
	private ImageView pauseButton;
	private Image pauseImg;
	private Image resumeImg;
	private long pauseCount = 0;
	
	private ImageView styleButton;
	private Image style1;
	private Image style2;
	private static int style = 1;
	
	//hotbar images and etc
	private ImageView bulletView, bombView, missileView;
	private ImageView ult;
	
	private boolean isPaused = false;
	
	private Background bg;
	
	//sound
	Media sound;
	MediaPlayer mediaPlayer;
	private String musicFile = "theme.mp3";
	
	//wait for game to start
	private boolean gameStarted = false;
	
	
	public SpaceWorld(double width, double height) {
		this.width = width;
		this.height = height;
		
		sound = new Media(new File(musicFile).toURI().toString());
		mediaPlayer = new MediaPlayer(sound);
		
		hotbar = new Image("sprites/" + getArtStyle() + "hotbar" + hotbarSlot + ".png",120*1.5, 40*1.5, false, false);
		healthbar = new HealthBar();
		bulletView = new ImageView(new Image("sprites/" + getArtStyle() + "bullet.png", 25*1.5, 25*1.5, false, false));
		bombView = new ImageView(new Image("sprites/" + getArtStyle() + "bomb.png", 24*1.5, 24*1.5, false, false));
		missileView = new ImageView(new Image("sprites/" + getArtStyle() + "missile.png", 40*1.5, 20*1.5, false, false));
		hiScore = new Score();
		ult = new ImageView(new Image("sprites/" + getArtStyle() + "ult1.png", 60, 60, false, false));
		playButton = new ImageView(new Image("sprites/" + getArtStyle() + "playButton.png", 250, 125, false, false));
		playButton.setOnMouseClicked(new MouseHandler());
		
		pauseImg = new Image("sprites/" + getArtStyle() + "pause.png", 50, 50, false, false);
		resumeImg = new Image("sprites/" + getArtStyle() + "resume.png", 50, 50, false, false);
		pauseButton = new ImageView(pauseImg);
		pauseButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        public void handle(MouseEvent event) {
	        	handlePause();
	        }
		});
		
		retryButton = new ImageView(new Image("sprites/" + getArtStyle() + "retry1.png", 400, 200, false, false));
		retryButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        public void handle(MouseEvent event) {
	        	retryGame();
	        }
		});
		
		style1 = new Image("sprites/" + 2 + "style1.png", 130, 50, false, false);
		style2 = new Image("sprites/" + 1 + "style2.png", 130, 50, false, false);
		style = 1;
		styleButton = new ImageView(style2);
		styleButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if(style==1) {
					style = 2;
					styleButton.setImage(style1);
					resetStyle();
		
				}else {
					styleButton.setImage(style2);
					style = 1;
					resetStyle();
				}
			}
			
		});
		
		this.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCharacter().equals("p")) {
					handlePause();
				}
			}
		});
		
		
		
		this.setPrefSize(this.width, this.height);
	}
	
	
	@Override
	public void onDimensionsInitialized() {
		
		bg = new Background(new Image("sprites/" + getArtStyle() + "spacebg.png", getWidth(), getHeight(), false, false));
		bg.setX(0);
		bg.setY(0);
		bg.toBack();
		add(bg);

		
		
		playButton.setX(getWidth()*0.35);
		playButton.setY(getHeight()*0.35);
		getChildren().add(playButton);
		
		
	}
	
	private class MouseHandler implements EventHandler<MouseEvent>{

		@Override
		public void handle(MouseEvent mouse) {
			// TODO Auto-generated method stub
			if(mouse.getSource() == retryButton) {
				retryGame();
			}else if(mouse.getSource() == playButton) {
				startGame();
				playButton.setImage(null);
			}
			
		}
		
	}
	
	
	public void retryGame() {
		getChildren().remove(styleButton);
		unpause();
    	List<Actor> actors = getObjects(Actor.class);
		for(Actor a: actors) {
			if(!(a instanceof Background)) {
				remove(a);
			}
		}
		
		Missile.setAmmo(5);
		Mine.setAmmo(3);
		
		getChildren().remove(pauseButton);
		getChildren().remove(score);
		getChildren().remove(retryButton);
		getChildren().remove(bulletView);
		getChildren().remove(missileView);
		getChildren().remove(bombView);
		getChildren().remove(missileText);
		getChildren().remove(mineText);
		getChildren().remove(laserText);
		getChildren().remove(hiScore);
    	startGame();
	}

	@Override
	public void act(long now) {
		//System.out.println(isPaused);
		bg.toBack();
		//System.out.println(getObjects(Asteroid.class).size());
		
		if(gameOver) {
			onGameOver();
		}else if(gameStarted) {
			
			boolean playing = mediaPlayer.getStatus().equals(Status.PLAYING);
			if(!playing && gameOver) {
				mediaPlayer.play();
			}
			
			/*
			if(isKeyPressed(KeyCode.P)) {
				if(!isPaused) {
					handlePause();
				}else {
					pauseCount++;
					if(pauseCount%2==0) {
						handlePause();
						pauseCount = 0;
					}
				}
			}
			*/
			
			
			frameCount++;
			
			numAsteroids = getObjects(Asteroid.class).size();
			
			if(!isPaused) {
			
				//adding asteroids
				int spawnRate = 200;
				int maxNumAsteroids = 3;
				if(frameCount>2000) {
					spawnRate = 100;
					maxNumAsteroids = 5;
				}
				if(frameCount>3000) {
					spawnRate = 50;
					maxNumAsteroids = 7;
				}
				if(frameCount>4000) {
					spawnRate = 25;
					maxNumAsteroids = 9;
				}
				if(frameCount % spawnRate == 0 && numAsteroids<=maxNumAsteroids) {
					Asteroid as = new Asteroid();
					
					Random rand = new Random();
					int x = rand.nextInt(4);
					if(x==0) {
						as.setX(0);
						as.setY(Math.random()*getHeight());
					}else if(x==1) {
						as.setX(getWidth());
						as.setY(Math.random()*getHeight());
					}else if(x==2) {
						as.setY(0);
						as.setX(Math.random()*getWidth());
					}else {
						as.setY(getHeight());
						as.setX(Math.random()*getWidth());
					}
					
					add(as);
				}
				
				if(frameCount%1000==0 && getObjects(Heart.class).size()==0) {
					Heart h = new Heart();
						Random rand = new Random();
					int x = rand.nextInt(4);
					if(x==0) {
						h.setX(0);
						h.setY(Math.random()*getHeight());
					}else if(x==1) {
						h.setX(getWidth());
						h.setY(Math.random()*getHeight());
					}else if(x==2) {
						h.setY(0);
						h.setX(Math.random()*getWidth());
					}else {
						h.setY(getHeight());
						h.setX(Math.random()*getWidth());
					}
					
					add(h);
				}
				
				numAmmo = getObjects(AmmoPack.class).size();
				
				Random rand = new Random();
				int ammoPackChance = rand.nextInt(1000);
				if(ammoPackChance==0 && numAmmo==0) {
					AmmoPack ammo = new AmmoPack();
					int spawn = rand.nextInt(4);
					if(spawn==0) {
						ammo.setX(0);
						ammo.setY(Math.random()*getHeight());
					}else if(spawn==1) {
						ammo.setX(getWidth());
						ammo.setY(Math.random()*getHeight());
					}else if(spawn==2) {
						ammo.setY(0);
						ammo.setX(Math.random()*getWidth());
					}else {
						ammo.setY(getHeight());
						ammo.setX(Math.random()*getWidth());
					}
					
					add(ammo);
				}
				
				if(isKeyPressed(KeyCode.Z)) {
					if(delay < 0) {
						hotbarSlot--;
						if(hotbarSlot < 1) {
							hotbarSlot = 3;
						}
						hotbarView.setImage(new Image("sprites/" + getArtStyle() + "hotbar" + hotbarSlot + ".png",120*1.5, 45*1.5, false, false));
						delay = 10;
						player.setSlot(hotbarSlot);
					}
				}
				
				if(isKeyPressed(KeyCode.X)) {
					if(delay < 0) {
						hotbarSlot++;
						if(hotbarSlot > 3) {
							hotbarSlot = 1;
						}
						hotbarView.setImage(new Image("sprites/" + getArtStyle() + "hotbar" + hotbarSlot + ".png",120*1.5, 45*1.5, false, false));
						delay = 10;
						player.setSlot(hotbarSlot);
					}
					
				}
				if(missileText!=null) {
					missileText.setText(" " + Missile.getAmmo());
				}
				if(mineText!=null) {
					mineText.setText("" + Mine.getAmmo());
				}
				
				delay--;
			}
		}
		

	}
	
	public void startGame() {
		
		mediaPlayer.play();
		
		this.unpause();
		isPaused = false;
		getChildren().remove(gameOverButton);
		gameStarted = true;
		
		player = new Ship();
		player.setX(100);
		player.setY(100);
		add(player);
		
		
		Asteroid as = new Asteroid();
		as.setX(200);
		as.setY(200);
		getChildren().add(as);
		
		score = new Score();
		score.setX(10);
		score.setY(27);
		score.setFill(Color.WHITE);
		score.setFont(mainBitFont);
		getChildren().add(score);
		
		hotbarView = new ImageView(hotbar);
		hotbarView.setX(getWidth() - 120*1.55);
		hotbarView.setY(getHeight() - 45*1.5);
		getChildren().add(hotbarView);
		
		healthbar.setY(getHeight()-healthbar.getHeight());
		healthbar.setX(0);
		getChildren().add(healthbar);
		
		bulletView.setX(getWidth()-0.19*getWidth());
		bulletView.setY(getHeight()-0.091*getHeight());
		getChildren().add(bulletView);
		
		missileView.setX(getWidth()-0.14*getWidth());
		missileView.setY(getHeight()-0.082*getHeight());
		missileView.setRotate(-35);
		getChildren().add(missileView);
		
		bombView.setX(getWidth()-0.06*getWidth());
		bombView.setY(getHeight()-0.095*getHeight());
		getChildren().add(bombView);
		
		//hiscore
		
		try {
			br = new BufferedReader(new FileReader(new File("HiScore.txt")));
			st = new StringTokenizer(br.readLine());
			highScore = Integer.parseInt(st.nextToken());
			System.out.println(br);
			br.close();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		hiScore = new Text("Hi-Score: " + highScore);
		hiScore.setFont(hiBitFont);
		hiScore.setFill(Color.WHITE);
		hiScore.setX(10);
		hiScore.setY(47);
		getChildren().add(hiScore);
		
		missileText = new Text("5");
		missileText.setFont(hiBitFont);
		missileText.setFill(Color.WHITE);
		missileText.setX(0.872*getWidth());
		missileText.setY(0.885*getHeight());
		getChildren().add(missileText);
		
		mineText = new Text("5");
		mineText.setFont(hiBitFont);
		mineText.setFill(Color.WHITE);
		mineText.setX(0.952*getWidth());
		mineText.setY(0.885*getHeight());
		getChildren().add(mineText);
		
		laserText = new Text(String.valueOf(Character.toString('\u221E')));
		laserText.setFont(Font.loadFont(SpaceWorld.class.getClassLoader().getResourceAsStream("sprites/8bitfont.ttf"), 18));
		laserText.setFill(Color.WHITE);
		laserText.setX(0.82*getWidth());
		laserText.setY(0.885*getHeight());
		getChildren().add(laserText);
		
		pauseButton.setX(getWidth()*0.935);
		pauseButton.setY(getHeight()*0.01);
		getChildren().add(pauseButton);
		
		resetStyle();
	}


	@Override
	public void onGameOver() {
		mediaPlayer.stop();
		
		int score = getScore().getScore();
		try {
			BufferedReader br1 = new BufferedReader(new FileReader(new File("HiScore.txt")));
			StringTokenizer st1 = new StringTokenizer(br1.readLine());
			int currHighScore = Integer.parseInt(st1.nextToken());
			if(score>currHighScore) {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("HiScore.txt"))));
				pw.println(score);
				pw.close();
			}
			
			br1.close();

		}catch(IOException e) {
			e.printStackTrace();
		}
		
		//System.out.println("Game Over");
		List<Actor> actors = getObjects(Actor.class);
		for(Actor a: actors) {
			if(!(a instanceof Background)) {
				this.remove(a);
			}
		}
		this.getChildren().remove(pauseButton);
		
		gameOverButton = new ImageView(new Image("sprites/" + getArtStyle() + "gameover.png", getWidth()*0.5, getHeight()*0.2, false, false));
		gameOverButton.setX(getWidth()*0.25);
		gameOverButton.setY(getHeight()*0.35);
		getChildren().add(gameOverButton);
		
		getChildren().remove(retryButton);
		getChildren().remove(styleButton);
		gameOver = false;
		getChildren().add(retryButton);
		retryButton.setX(getWidth()*0.30);
		retryButton.setY(getHeight()*0.9);
		
		styleButton.setX(getWidth()*0.52);
		styleButton.setY(getHeight()*0.908);
		
		Missile.setAmmo(5);
		Mine.setAmmo(3);
		
		this.pause();
		isPaused = true;
		
		
	}
	
	public void isGameOver() {
		List<Ship> list = getObjects(Ship.class);
		if(gameStarted && list.isEmpty()) {
			gameOver =  true;
		}
		if(!list.isEmpty() && list.get(0).getHealth()==0) {
			gameOver = true;
		}
		gameOver = false;
		
	}
	
	private void handlePause() {
		if(isPaused) {
    		isPaused = false;
    		pauseButton.setImage(pauseImg);
    		getChildren().remove(retryButton);
    		getChildren().remove(styleButton);
    		unpause();
    	}else {
    		isPaused = true;
    		pauseButton.setImage(resumeImg);
    		getChildren().add(retryButton);
    		retryButton.setX(getWidth()*0.30);
    		retryButton.setY(getHeight()*0.9);
    		
    		getChildren().add(styleButton);
    		styleButton.setX(getWidth()*0.52);
    		styleButton.setY(getHeight()*0.908);
    		pause();
    	}
	}
	
	public static int getArtStyle() {
		return style;
	}
	
	public void resetStyle() {
		bg.setImage(new Image("sprites/" + getArtStyle() + "spacebg.png", getWidth(), getHeight(), false, false));
		player.setImage(new Image("sprites/" + SpaceWorld.getArtStyle() + "ship.png", 50, 50, false, false));
		pauseImg = new Image("sprites/" + getArtStyle() + "pause.png", 50, 50, false, false);
		resumeImg = new Image("sprites/" + getArtStyle() + "resume.png", 50, 50, false, false);
		hotbarView.setImage(new Image("sprites/" + getArtStyle() + "hotbar" + hotbarSlot + ".png",120*1.5, 45*1.5, false, false));
		retryButton.setImage(new Image("sprites/" + getArtStyle() + "retry1.png", 400, 200, false, false));
		if(style==2) {
			retryButton.setImage(new Image("sprites/" + getArtStyle() + "retry1.png", 180, 50, false, false));
		}
		
		if(isPaused) {
			//System.out.println(32);
			pauseButton.setImage(resumeImg);
		}else {
			pauseButton.setImage(pauseImg);
		}
		mainBitFont = Font.loadFont(SpaceWorld.class.getClassLoader().getResourceAsStream("sprites/" + getArtStyle() + "8bitfont.ttf"), 22);
		hiBitFont = Font.loadFont(SpaceWorld.class.getClassLoader().getResourceAsStream("sprites/" + getArtStyle() + "8bitfont.ttf"), 14);
		
		bulletView.setImage(new Image("sprites/" + getArtStyle() + "bullet.png", 25*1.5, 25*1.5, false, false));
		bombView.setImage(new Image("sprites/" + getArtStyle() + "bomb.png", 24*1.5, 24*1.5, false, false));
		missileView.setImage(new Image("sprites/" + getArtStyle() + "missile.png", 40*1.5, 20*1.5, false, false));
		if(style==2) {
			bulletView.setX(bulletView.getX()*0.99);
			bombView.setX(bombView.getX()*0.995);
			missileView.setX(missileView.getX()*0.995);
			
			mainBitFont = Font.loadFont(SpaceWorld.class.getClassLoader().getResourceAsStream("sprites/" + getArtStyle() + "8bitfont.ttf"), 30);
			hiBitFont = Font.loadFont(SpaceWorld.class.getClassLoader().getResourceAsStream("sprites/" + getArtStyle() + "8bitfont.ttf"), 25);
			
			mineText.setFont(hiBitFont);
			mineText.setFill(Color.BLACK);
			missileText.setFont(hiBitFont);
			missileText.setFill(Color.BLACK);
			laserText.setFont(hiBitFont);
			laserText.setFill(Color.BLACK);
			score.setFont(mainBitFont);
			score.setFill(Color.BLACK);
			hiScore.setFont(mainBitFont);
			hiScore.setFill(Color.BLACK);
			
			styleButton.setImage(style1);
		}else {
			bulletView.setX(getWidth()-0.19*getWidth());
			bulletView.setY(getHeight()-0.091*getHeight());
			
			missileView.setX(getWidth()-0.14*getWidth());
			missileView.setY(getHeight()-0.082*getHeight());
			
			bombView.setX(getWidth()-0.06*getWidth());
			bombView.setY(getHeight()-0.095*getHeight());
			
			styleButton.setImage(style2);
			
			
			
			mineText.setFont(hiBitFont);
			mineText.setFill(Color.WHITE);
			missileText.setFont(hiBitFont);
			missileText.setFill(Color.WHITE);
			laserText.setFont(hiBitFont);
			laserText.setFill(Color.WHITE);
			score.setFont(mainBitFont);
			score.setFill(Color.WHITE);
			hiScore.setFont(mainBitFont);
			hiScore.setFill(Color.WHITE);
			
		}
		
		
	}
	
	
	
	
}
