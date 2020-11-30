package weapon;

import display.StdDraw;
import display.Vector2;


public class Ion extends Weapon {
	private class IonProjectile extends Projectile {

		public IonProjectile(Vector2<Double> pos, Vector2<Double> dir) {
			super(0.01, 0.005);
			this.x = pos.getX();
			this.y = pos.getY();
			this.cSpeed = 0.2;
			this.xSpeed = dir.getX()*cSpeed;
			this.ySpeed = dir.getY()*cSpeed;
			this.color = StdDraw.RED;
			this.damage = shotDamage;
			this.type = 3;
		}
		
		@Override
		public void draw() {
			if(hit && !isEvaded) {
				if(!isShielded) {
					StdDraw.setPenColor(StdDraw.BLUE);
					StdDraw.circle(x, y, 0.006);
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
				if(isPlayer) StdDraw.picture(x, y, "ion.png",0.03,0.03,angle);
				else StdDraw.picture(x, y, "ionvoid.png",0.03,0.03,90+angle);
			}
			
		}
	}
	public Ion() {
		name = "Ion";
		requiredPower = 2;
		chargeTime = 20;
		shotDamage = 0;
		shotPerCharge = 1;
	}
	@Override
	public Projectile shot(Vector2<Double> pos, Vector2<Double> dir) {
		double x = pos.getX();
		return new IonProjectile(new Vector2<Double>(x,pos.getY()), dir);
	}
}