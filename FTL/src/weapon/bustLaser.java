package weapon;

import display.StdDraw;
import display.Vector2;

/**
 * A dummy gun is an example of a gun which shots dummy pojectiles
 */
public class bustLaser extends Weapon {
	
	/**
	 * A dummy projectile is an example of a projectile
	 */
	private class DummyGunProjectile extends Projectile {

		public DummyGunProjectile(Vector2<Double> pos, Vector2<Double> dir) {
			super(0.01, 0.005);
			this.x = pos.getX();
			this.y = pos.getY();
			this.cSpeed = 0.25;
			this.xSpeed = dir.getX()*cSpeed;
			this.ySpeed = dir.getY()*cSpeed;
			this.color = StdDraw.RED;
			this.damage = shotDamage;
		}
	}
	
	/**
	 * Creates a dummy gun
	 */
	public bustLaser() {
		name = "Flak Laser";
		requiredPower = 1;
		chargeTime = 9;
		shotDamage = 1;
		shotPerCharge = 1;
	}

	/**
	 * Shots a dummy projectile
	 * @see weapon.Weapon#shot(display.Vector2, display.Vector2)
	 */
	@Override
	public Projectile shot(Vector2<Double> pos, Vector2<Double> dir) {
		double x = pos.getX();
		return new DummyGunProjectile(new Vector2<Double>(x,pos.getY()), dir);
	}

	
	
}
