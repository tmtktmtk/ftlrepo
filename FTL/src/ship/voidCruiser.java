package ship;
import java.awt.Font;

import display.StdDraw;
import display.Vector2;
import module.Module;
import module.Piloting;
import module.Reactor;
import module.Shield;
import module.WeaponControl;
import weapon.Ion;
import weapon.Laser;
import weapon.Missile;
import weapon.Weapon;
import weapon.bustLaser;
import weapon.voidCanon;

public class voidCruiser extends Ship {
	
	private int bonus;
	
	public voidCruiser(boolean isPlayer, Vector2<Double> position,int bonus) {
		// Creates the ship
		super(isPlayer, position);
		this.bonus=bonus;
		// Sets the characteristics of the ship.
		totalHull 		= 26+bonus*2;
		currentHull		= 26+bonus*2;
		modules = new Module[4];
		
		double x = position.getX();
		double y = position.getY()-0.05;
		
		// Creates the tiles for the layout of the ship
		pilot = new Piloting(new Vector2<Double>(0.014, 0.015),new Vector2<Double>(x, y+0.12), isPlayer, 3,0.02,0.02);
		addTile(pilot);
		pilot.setCurrentLevel(pilot.getCurrentLevel()+bonus/4);
		reactor = new Reactor(new Vector2<Double>(0.025, 0.015), new Vector2<Double>(x, y-0.06),isPlayer, 25,0.04,0.02);
		addTile(reactor);
		reactor.setCurrentLevel(reactor.getCurrentLevel()+bonus);
		shield = new Shield(new Vector2<Double>(0.08, 0.1),new Vector2<Double>(x, y+0.07), isPlayer,8,0.02,0.03);
		addTile(shield);
		shield.setCurrentLevel(shield.getCurrentLevel()+bonus/4);
		weaponControl = new WeaponControl(new Vector2<Double>(0.08, 0.015), new Vector2<Double>(x, y), isPlayer, 8, 5,0.02,0.04);
		addTile(weaponControl);
		weaponControl.setCurrentLevel(weaponControl.getCurrentLevel()+bonus);
		Tile wingback = new Tile(new Vector2<Double>(x+0.06, y-0.05), isPlayer,0.02,0.01);
		addTile(wingback);
		Tile wingback2 = new Tile(new Vector2<Double>(x-0.06, y-0.05), isPlayer,0.02,0.01);
		addTile(wingback2);
		Tile wingfront = new Tile(new Vector2<Double>(x+0.04, y+0.09), isPlayer,0.02,0.01);
		addTile(wingfront);
		Tile wingfront2 = new Tile(new Vector2<Double>(x-0.04, y+0.09), isPlayer,0.02,0.01);
		addTile(wingfront2);
		Tile back = new Tile(new Vector2<Double>(x, y-0.1), isPlayer,0.02,0.02);
		addTile(back);
		// Assigns the modules
		modules[0] = reactor;
		modules[1] = weaponControl;
		modules[2] = shield;
		modules[3] = pilot;
		// Creates the gun of the ship
		Weapon w4 = new Missile();
		Weapon w1 = new Ion();
		Weapon w2 = new Laser();
		Weapon w3 = new Laser();
		Weapon w = new voidCanon();

		// Assigns the gun to the weapon control
		weaponControl.addWeapon(w);
		weaponControl.addWeapon(w1);
		weaponControl.addWeapon(w2);
		weaponControl.addWeapon(w3);
		// Places the weapon at the front
		wingfront2.setWeapon(w3);
		wingfront.setWeapon(w1);
		wingback2.setWeapon(w4);
		wingback.setWeapon(w2);
		pilot.setWeapon(w);
	}
	
	@Override
	protected void repairModule(double time) {
		for(Module m: modules) {
			m.autoRepair(time);
		}
	}
	@Override
	public void ai(Ship player) {
		super.ai(player);
		this.addEnergy(1);
		this.addEnergy(1);
		this.addEnergy(1);
		this.addEnergy(1);
		this.addEnergy(1);
		this.addEnergy(1);
		this.addEnergy(1);
		this.addEnergy(3);
		this.addEnergy(3);
		this.addEnergy(3);
		this.addEnergy(2);
		this.addEnergy(2);
	}
	
	@Override
	public void draw() {
		if(this.currentHull>0) {
				Double m= Math.random()*0.01+0.034;
				StdDraw.setPenColor(StdDraw.BLACK);
				StdDraw.filledCircle(position.getX()+0.001, position.getY()-0.2, m);
				StdDraw.setPenColor();
				StdDraw.picture(position.getX()+0.001,position.getY()-0.04, "voidcruiser.png",0.26,0.45);

			
		} else StdDraw.picture(position.getX(), position.getY(), "death.png",0.3,0.3);
					
				
		super.draw();					
		StdDraw.setFont(new Font("NasalizationRg-Regular",Font.BOLD,8));
					
		StdDraw.text(0.76, 0.78, "VOID CRUISER");	
					
		StdDraw.setFont(new Font("NasalizationRg-Regular",Font.BOLD,15));

					
					

					
//					StdDraw.picture(this.position.getX(), this.position.getY()-0.035, "shield.png",0.30,0.25,90);
//					StdDraw.picture(this.position.getX(), this.position.getY()-0.035, "shield.png",0.30,0.25,90);

	}
	
//	@Override
//	public void drawHUD() {
//		super.drawHUD();	
//	}
//	


	@Override
	public void drawShield() {
		if(shield==null) return;
		for(int i =0; i<shield.getLayer();i++) {	
			StdDraw.picture(this.position.getX(), this.position.getY()-0.03, "shieldcruiser.png",0.26,0.45);
		}
	}
	
}