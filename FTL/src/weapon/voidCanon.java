package weapon;

import display.StdDraw;
import display.Vector2;


public class voidCanon extends Weapon {
	private class MissileProjectile extends Projectile {

		public MissileProjectile(Vector2<Double> pos, Vector2<Double> dir) {
			super(0.01, 0.005);
			this.x = pos.getX();
			this.y = pos.getY();
			this.cSpeed = 0.15;
			this.xSpeed = dir.getX()*cSpeed;
			this.ySpeed = dir.getY()*cSpeed;
			this.color = StdDraw.WHITE;
			this.damage = shotDamage;
			this.type = -1;
		}
		@Override
		public void draw() {
			if(hit && !isEvaded) {
				if(!isShielded) {
					StdDraw.circle(x, y, 0.004);
					StdDraw.filledCircle(x+(Math.random()*0.5-1)*0.001, y+(Math.random()*0.5-1)*0.001, Math.random()*0.02);
					StdDraw.filledCircle(x+(Math.random()*0.5-1)*0.001, y+(Math.random()*0.5-1)*0.001, Math.random()*0.03);
					StdDraw.filledCircle(x+(Math.random()*0.5-1)*0.001, y+(Math.random()*0.5-1)*0.001, Math.random()*0.04);
					StdDraw.filledCircle(x+(Math.random()*0.5-1)*0.001, y+(Math.random()*0.5-1)*0.001, Math.random()*0.05);
				}
			}
			else {
				if(isPlayer) StdDraw.picture(x, y, "shadowball.png",0.06,0.06,angle);
				else {
					StdDraw.picture(x, y, "shadowball.png",0.06,0.06,90+angle);
				}
			}
			
		}
	}
	public voidCanon() {
		name = "voidCanon";
		requiredPower = 1;
		chargeTime = 30;
		shotDamage = 4;
		shotPerCharge = 1;
	}
	@Override
	public Projectile shot(Vector2<Double> pos, Vector2<Double> dir) {
		double x = pos.getX();
		return new MissileProjectile(new Vector2<Double>(x,pos.getY()), dir);
	}
}