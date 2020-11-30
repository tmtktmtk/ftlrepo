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

public class voidDestroyer extends Ship {
	
	private int bonus;
	
	public voidDestroyer(boolean isPlayer, Vector2<Double> position,int bonus) {
		// Creates the ship
		super(isPlayer, position);
		this.bonus=bonus;
		// Sets the characteristics of the ship.
		totalHull 		= 8+bonus+bonus/2;
		currentHull		= 8+bonus+bonus/2;
		modules = new Module[4];
		
		double x = position.getX();
		double y = position.getY()-0.05;
		
		// Creates the tiles for the layout of the ship
		pilot = new Piloting(new Vector2<Double>(0.014, 0.015),new Vector2<Double>(x, y+0.04), isPlayer, 2,0.01,0.02);
		addTile(pilot);
		pilot.setCurrentLevel(pilot.getCurrentLevel()+bonus/2);
		reactor = new Reactor(new Vector2<Double>(0.025, 0.015), new Vector2<Double>(x, y-0.03),isPlayer, 12);
		addTile(reactor);
		reactor.setCurrentLevel(reactor.getCurrentLevel()+bonus);
		shield = new Shield(new Vector2<Double>(0.08, 0.1),new Vector2<Double>(x+0.05, y+0.01), isPlayer,4,0.02,0.01);
		addTile(shield);
		shield.setCurrentLevel(shield.getCurrentLevel()+bonus/4);
		weaponControl = new WeaponControl(new Vector2<Double>(0.08, 0.015), new Vector2<Double>(x, y), isPlayer, 5, 3,0.03,0.02);
		addTile(weaponControl);
		weaponControl.setCurrentLevel(weaponControl.getCurrentLevel()+bonus);
		Tile back = new Tile(new Vector2<Double>(x-0.05, y+0.01), isPlayer,0.02,0.01);
		addTile(back);
		
		// Assigns the modules
		modules[0] = reactor;
		modules[1] = weaponControl;
		modules[2] = shield;
		modules[3] = pilot;
		// Creates the gun of the ship
		Weapon w = new Laser();
		Weapon w1 = new Ion();
		Weapon w2 = new Missile();
		// Assigns the gun to the weapon control
		weaponControl.addWeapon(w);
		weaponControl.addWeapon(w1);
		weaponControl.addWeapon(w2);
		// Places the weapon at the front
		pilot.setWeapon(w1);
		shield.setWeapon(w);
		back.setWeapon(w2);
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
		this.addEnergy(3);
		this.addEnergy(3);
		this.addEnergy(3);
		this.addEnergy(2);
		this.addEnergy(2);
	}
	
	@Override
	public void draw() {
		if(this.currentHull>0) {

				StdDraw.picture(position.getX()-0.001,position.getY()-0.02, "voiddes.png",0.2,0.2);
				Double n =Math.random()*0.02+0.004;
				Double m= Math.random()*0.01;
				StdDraw.setPenColor(StdDraw.BLACK);
				StdDraw.filledEllipse(position.getX()-0.01, position.getY()-0.14, m/2, n*2);
				StdDraw.filledEllipse(position.getX()+0.01, position.getY()-0.14, m/2, n*2);
				StdDraw.setPenColor();
			
		} else StdDraw.picture(position.getX(), position.getY(), "death.png",0.3,0.3);
					super.draw();
					
					
					StdDraw.setFont(new Font("NasalizationRg-Regular",Font.BOLD,8));
					StdDraw.text(0.76, 0.78, "VOID DESTROYER");	
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
			StdDraw.picture(this.position.getX(), this.position.getY()-0.03, "shieldvoid.png",0.35,0.25,90);
		}
	}
	
}