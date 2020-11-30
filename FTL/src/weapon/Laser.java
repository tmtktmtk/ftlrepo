package weapon;

import display.StdDraw;
import display.Vector2;


public class Laser extends Weapon {
	private class LaserProjectile extends Projectile {

		public LaserProjectile(Vector2<Double> pos, Vector2<Double> dir) {
			super(0.01, 0.005);
			this.x = pos.getX();
			this.y = pos.getY();
			this.cSpeed = 0.25;
			this.xSpeed = dir.getX()*cSpeed;
			this.ySpeed = dir.getY()*cSpeed;
			this.color = StdDraw.RED;
			this.damage = shotDamage;
			this.type = 2;
			
			
		}
		@Override
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
				if(isPlayer) StdDraw.picture(x, y, "laserbullet.png",0.08,0.01,angle);
				else StdDraw.picture(x, y, "laserbulletvoid.png",0.08,0.01,90+angle);
			}
		}
	}
	public Laser() {
		name = "Thermal Lance";
		requiredPower = 2;
		chargeTime = 15;
		shotDamage = 1;
		shotPerCharge = 1;
	}
	@Override
	public Projectile shot(Vector2<Double> pos, Vector2<Double> dir) {
		double x = pos.getX();
		return new LaserProjectile(new Vector2<Double>(x,pos.getY()), dir);
	}

}