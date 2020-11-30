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
import weapon.Weapon;
import weapon.bustLaser;
import weapon.voidCanon;

public class voidMinion extends Ship {
	
	private int bonus;
	
	public voidMinion(boolean isPlayer, Vector2<Double> position,int bonus) {
		// Creates the ship
		super(isPlayer, position);
		this.bonus=bonus;
		// Sets the characteristics of the ship.
		totalHull 		= 5+bonus;
		currentHull		= 5+bonus;
		modules = new Module[4];
		
		// Creates the tiles for the layout of the ship
		pilot = new Piloting(new Vector2<Double>(0.014, 0.015),getNextTilePosition(), isPlayer, 6);
		addTile(pilot);
		pilot.setCurrentLevel(pilot.getCurrentLevel()+bonus/2);
		reactor = new Reactor(new Vector2<Double>(0.025, 0.015), getNextTilePosition(),isPlayer, 9);
		addTile(reactor);
		reactor.setCurrentLevel(reactor.getCurrentLevel()+bonus);
		shield = new Shield(new Vector2<Double>(0.08, 0.1),getNextTilePosition(), isPlayer,0);
		addTile(shield);
		shield.setCurrentLevel(shield.getCurrentLevel()+bonus);
		weaponControl = new WeaponControl(new Vector2<Double>(0.08, 0.015), getNextTilePosition(), isPlayer, 2, 2);
		addTile(weaponControl);
		weaponControl.setCurrentLevel(weaponControl.getCurrentLevel()+bonus/4);
		Tile back = new Tile(getNextTilePosition(), isPlayer);
		addTile(back);
		
		// Assigns the modules
		modules[0] = reactor;
		modules[1] = weaponControl;
		modules[2] = shield;
		modules[3] = pilot;
		// Creates the gun of the ship
		Weapon w = new bustLaser();
		Weapon w2 = new bustLaser();
		// Assigns the gun to the weapon control
		weaponControl.addWeapon(w);
		weaponControl.addWeapon(w2);
		
		// Places the weapon at the front
		pilot.setWeapon(w);
		reactor.setWeapon(w2);
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
		this.addEnergy(3);
		this.addEnergy(3);
		this.addEnergy(3);
		this.addEnergy(2);
		this.addEnergy(2);
	}
	
	@Override
	public void draw() {
		if(this.currentHull>0) {

				StdDraw.picture(position.getX()-0.001,position.getY()-0.04, "voidminion.png",0.18,0.18);
				Double n =Math.random()*0.02+0.004;
				Double m= Math.random()*0.01;
				StdDraw.setPenColor(StdDraw.BLACK);
				StdDraw.filledEllipse(position.getX(), position.getY()-0.14, m/2, n);
				StdDraw.setPenColor();
			
		} else StdDraw.picture(position.getX(), position.getY(), "death.png",0.3,0.3);
		super.draw();
		StdDraw.setFont(new Font("NasalizationRg-Regular",Font.BOLD,8));
		StdDraw.text(0.76, 0.78, "VOID MINION");	
		StdDraw.setFont(new Font("NasalizationRg-Regular",Font.BOLD,15));


//					StdDraw.picture(this.position.getX(), this.position.getY()-0.035, "shield.png",0.30,0.25,90);
//					StdDraw.picture(this.position.getX(), this.position.getY()-0.035, "shield.png",0.30,0.25,90);

	}
	
//	@Override
//	public void drawHUD() {
//		super.drawHUD();	
//	}
//	
	private Vector2<Double> getNextTilePosition() {
		if (isPlayer)
			return new Vector2<Double>(position.getX()-(layout.size()*0.02), position.getY()); 
		else
			return new Vector2<Double>(position.getX(), position.getY()-(layout.size()*0.02));
	}

	@Override
	public void drawShield() {
		if(shield==null) return;
		for(int i =0; i<shield.getLayer();i++) {	
			StdDraw.picture(this.position.getX(), this.position.getY()-0.04, "shieldvoid.png",0.3,0.2,90);
		}
	}
	
}