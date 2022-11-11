package engine;


import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Score extends Text{
	private int score;
	
	
	public Score() {
		super("Score: " + (0 + ""));
		score = 0;
		this.setFont(new Font(20));
		this.setFill(Color.WHITE);
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int arg) {
		score = arg;
		updateDisplay();
	}
	public void updateDisplay() {
		setText("Score: " + score);
	}
}
