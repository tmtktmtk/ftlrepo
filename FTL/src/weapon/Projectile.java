package weapon;

import java.awt.Color;

import display.StdDraw;
import display.Vector2;
import ship.Tile;

/**
 * A projectile is shot by a weapon at a position and 
 * follows the direction provided at a constant speed.
 */
public abstract class Projectile {
	
	protected 		double cSpeed;	// The constant speed
	protected 		double x;		// The X position
	protected 		double y;		// The Y position
	protected final double width;	// The width
	protected final double height;	// The height
	protected 		double xSpeed;	// The current x speed
	protected 		double ySpeed;	// The current y speed
	protected 		Color	color;	// The color 
	protected 		int damage;		// The amount of damage the projectile does
	private			Tile target;
	protected		boolean hit;
	protected 		boolean didDamage;
	protected		double angle;
	protected		boolean isPlayer;
	protected		boolean isShielded;
	protected		boolean isEvaded;
	protected		boolean inPlayerBox; //true ifPlayer else false
	protected       int type;
	/**
	 * Creates a projectile of the provided dimensions.
	 * @param width of the projectile
	 * @param height of the projectile
	 */
	protected Projectile(double width, double height) {
		this.width = width;
		this.height = height;
		this.hit=false;
		this.target= null;
		this.didDamage=false;
		this.angle=0.0;
		this.isShielded=false;
		this.isEvaded=false;
		this.type = 1;
	}
	
	
	/**
	 * Moves the projectile by the time provided.
	 * @param time elapsed time
	 */
	public void step(double time) {
		x += xSpeed*time;
		y += ySpeed*time;
	}
	
	/**
	 * Draws the projectile.
	 */
	public void draw() {
		if(hit && !isEvaded) {
			if(!isShielded) {
				StdDraw.setPenColor(StdDraw.ORANGE);
				StdDraw.circle(x, y, 0.004);
				StdDraw.filledCircle(x+(Math.random()*0.5-1)*0.001, y+(Math.random()*0.5-1)*0.001, Math.random()*0.03);
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.filledCircle(x+(Math.random()*0.5-1)*0.001, y+(Math.random()*0.5-1)*0.001, Math.random()*0.02);
				StdDraw.setPenColor(StdDraw.YELLOW);
				StdDraw.filledCircle(x+(Math.random()*0.5-1)*0.001, y+(Math.random()*0.5-1)*0.001, Math.random()*0.03);
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.filledCircle(x+(Math.random()*0.5-1)*0.001, y+(Math.random()*0.5-1)*0.001, Math.random()*0.02);
			} else {
				StdDraw.setPenColor(StdDraw.BLUE);
				StdDraw.circle(x, y, 0.004);
				StdDraw.filledCircle(x+(Math.random()*0.5-1)*0.001, y+(Math.random()*0.5-1)*0.001, Math.random()*0.03);
				StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
				StdDraw.filledCircle(x+(Math.random()*0.5-1)*0.001, y+(Math.random()*0.5-1)*0.001, Math.random()*0.02);
				StdDraw.setPenColor(StdDraw.BOOK_BLUE);
				StdDraw.filledCircle(x+(Math.random()*0.5-1)*0.001, y+(Math.random()*0.5-1)*0.001, Math.random()*0.03);
				StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
				StdDraw.filledCircle(x+(Math.random()*0.5-1)*0.001, y+(Math.random()*0.5-1)*0.001, Math.random()*0.02);
			}
				StdDraw.setPenColor();
		}
		else {
			if(isPlayer) StdDraw.picture(x, y, "laserbullet.png",0.02,0.01,angle);
			else StdDraw.picture(x, y, "laserbulletvoid.png",0.02,0.01,90+angle);
		}
	}

	/**
	 * Sets the position of the projectile. 
	 * @param x X position
	 * @param y Y position
	 */
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Checks whether the projectile is out of the screen.
	 * @return whether the projectile is out of the screen
	 */
	public boolean isOutOfScreen() {
		return x > 1 || y > 1 || x < 0 || y < 0;
	}
	
	/**
	 * Checks whether the projectile is out of a provided rectangle.
	 * @param xCenter X center of the rectangle
	 * @param yCenter Y center of the rectangle
	 * @param halfWidth half of the width of the rectangle
	 * @param halfHeight half of the height of the rectangle
	 * @return whether the projectile is out of the rectangle
	 */
	public boolean isOutOfRectangle(double xCenter, double yCenter, double halfWidth, double halfHeight) {
		return x < xCenter-halfWidth || x > xCenter+halfWidth || y < yCenter-halfHeight || y > yCenter+halfHeight;
	}
	
	/**
	 * Given a location to target, compute the direction of the projectile.
	 * @param targetPos the target to aim at
	 */
	public void computeDirection() {
		double norm = Math.sqrt(Math.pow((target.getPosition().getX() - x),2) + Math.pow(target.getPosition().getY() - y, 2));
		this.xSpeed = (target.getPosition().getX() - x)/norm;
		this.ySpeed = (target.getPosition().getY() - y)/norm;	
		this.xSpeed *= cSpeed;
		this.ySpeed *= cSpeed;
		this.angle=Math.toDegrees(Math.acos((target.getPosition().getX() - x)/norm));
		this.angle=(target.getPosition().getY()<y)?this.angle-2*this.angle:
			this.angle;
		this.angle=(this.isPlayer)?this.angle:this.angle-90;
	}
	

	public boolean isPlayer() {
		return isPlayer;
	}


	public void setPlayer(boolean isPlayer) {
		this.isPlayer = isPlayer;
	}


	public Tile getTarget() {
		return target;
	}

	public void setTarget(Tile target) {
		this.target=target;
	}

	/**
	 * Gives the damage the projectile does.
	 * @return the damage
	 */
	public int getDamage() {
		return damage;
	}

	public boolean isHit() {
		return hit;
	}

	public void didHit(boolean hit) {
		this.hit = hit;
	}

	public double getxSpeed() {
		return xSpeed;
	}

	public void setxSpeed(double xSpeed) {
		this.xSpeed = xSpeed;
	}

	public double getySpeed() {
		return ySpeed;
	}

	public void setySpeed(double ySpeed) {
		this.ySpeed = ySpeed;
	}


	public boolean DidDamage() {
		return didDamage;
	}


	public void setDidDamage(boolean didDamage) {
		this.didDamage = didDamage;
	}


	public boolean isShielded() {
		return isShielded;
	}


	public void setIsShielded(boolean isShielded) {
		this.isShielded = isShielded;
	}


	public boolean isEvaded() {
		return isEvaded;
	}


	public void setIsEvaded(boolean isEvaded) {
		this.isEvaded = isEvaded;
	}


	public boolean isInPlayerBox() {
		return inPlayerBox;
	}


	public void setInPlayerBox(boolean inPlayerBox) {
		this.inPlayerBox = inPlayerBox;
	}


	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getType() {
		return this.type;
	}
	public void setProjectileDamage(int d) {
		this.damage = d;
	}

	
	
	
}
