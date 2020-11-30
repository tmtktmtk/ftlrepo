package weapon;

import display.StdDraw;
import display.Vector2;


public class Minigun extends Weapon {
	private class MissileProjectile extends Projectile {

		public MissileProjectile(Vector2<Double> pos, Vector2<Double> dir) {
			super(0.01, 0.005);
			this.x = pos.getX();
			this.y = pos.getY();
			this.cSpeed = 0.25;
			this.xSpeed = dir.getX()*cSpeed;
			this.ySpeed = dir.getY()*cSpeed;
			this.color = StdDraw.WHITE;
			this.damage = shotDamage;
			this.type = 4;
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
				} 
					StdDraw.setPenColor();
			}
			else {
				StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
				StdDraw.filledCircle(x, y, 0.0035);
				StdDraw.setPenColor();
			}
			
		}
	}
	public Minigun() {
		name = "Minigun";
		requiredPower = 1;
		chargeTime = 8;
		shotDamage = 1;
		shotPerCharge = 1;
	}
	@Override
	public Projectile shot(Vector2<Double> pos, Vector2<Double> dir) {
		double x = pos.getX();
		return new MissileProjectile(new Vector2<Double>(x,pos.getY()), dir);
	}
}