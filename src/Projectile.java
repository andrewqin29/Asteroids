

import engine.Actor;

public abstract class Projectile extends Actor{
	
	protected double speed;
	protected int damage;
	protected double x, y;
	protected int frameCount = 0;
		
	//in degrees
	protected double angle;
	
	public abstract void onHit(Actor a);
	public void move() {
		setX(getX()+speed*Math.cos(Math.toRadians(angle)));
		setY(getY()+speed*Math.sin(Math.toRadians(angle)));
	}
}
