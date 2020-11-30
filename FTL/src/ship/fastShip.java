package ship;

import display.StdDraw;
import display.Vector2;
import module.Shield;
import module.WeaponControl;
import weapon.Minigun;
import weapon.Missile;
import weapon.Weapon;
import weapon.bustLaser;
import module.Module;
import module.Piloting;
import module.Reactor;

public class fastShip extends Ship {
	
	public fastShip(boolean isPlayer, Vector2<Double> position) {
		super(isPlayer,position);
		totalHull=30;
		currentHull=30;
		modules= new Module[4];
		
		//1st hud pos;
		double x=0.05;
		double y=0.095;
		
		//tile
		
		//coretile
		shield = new Shield(new Vector2<Double>(x+0.05*1, y),new Vector2<Double>(position.getX(),position.getY()), isPlayer,2,0.04,0.01);
		addTile(shield);
		//backtile
		reactor = new Reactor(new Vector2<Double>(x+0.05*0, y), new Vector2<Double>(position.getX()-shield.DftSizeX-0.02,position.getY()),isPlayer, 8,0.02,0.01);
		addTile(reactor);
		//cockpit
		pilot= new Piloting(new Vector2<Double>(x+0.05*2, y),new Vector2<Double>(position.getX()+shield.DftSizeX+0.01,position.getY()), isPlayer,6);
		addTile(pilot);
		//wingtile
		weaponControl = new WeaponControl(new Vector2<Double>(x+0.05*3, y),new Vector2<Double>(position.getX(),position.getY()+0.02), isPlayer,1,3, 0.02,0.01);
		addTile(weaponControl);
		//wingtile
		Tile wing = new Tile(new Vector2<Double>(position.getX(),position.getY()-0.02), isPlayer,0.02,0.01);
		addTile(wing);
		
		// Assigns the modules
		modules[0] = reactor;
		modules[3] = weaponControl;
		modules[1] = shield;
		modules[2] = pilot;
		// Creates the gun of the ship
		Weapon w = new Minigun();
//		Weapon w2= new Missile();
		// Assigns the gun to the weapon control
		weaponControl.addWeapon(w);
//		weaponControl.addWeapon(w2);
		// Places the weapon at the wing
		weaponControl.setWeapon(w);
//		wing.setWeapon(w2);
		CrewMember tam=new CrewMember("Tam");
		crew.add(tam);
		pilot.setCrewMember(tam);
		CrewMember josh=new CrewMember("Josh");
		crew.add(josh);
		pilot.setCrewMember(josh);
		
		CrewMember jose=new CrewMember("Jose");
		crew.add(jose);
		shield.setCrewMember(jose);
		
		weaponControl.setCrewMember(josh);
		
		weaponTile.add(pilot);
		weaponTile.add(wing);
	}
	
	@Override
	public void draw() {
		
		if(this.currentHull>0) {
			Double n =Math.random()*0.09+0.09;
			Double m= Math.random()*0.005;
			StdDraw.setPenColor(100,10,10);
			StdDraw.filledEllipse(position.getX()-0.1, position.getY(), n, m);
			StdDraw.filledEllipse(position.getX()-0.1, position.getY()-0.025, n/2, m/2);
			StdDraw.filledEllipse(position.getX()-0.1, position.getY()+0.025, n/2, m/2);
			StdDraw.setPenColor();
			StdDraw.picture(position.getX()+0.01,position.getY(), "F5S1.png",0.25,0.125);

		} else StdDraw.picture(position.getX(), position.getY(), "death.png",0.2,0.2);
		super.draw();
		StdDraw.picture(0.42, 0.05, "spcabibtn.png",0.34,0.06);
		StdDraw.text(0.42, 0.05, "SENTIENT AI");
		if(StdDraw.mouseX()>=(0.42-0.17) && StdDraw.mouseX()<=(0.42+0.17) &&
				StdDraw.mouseY()>=(0.05-0.03) && StdDraw.mouseY()<=(0.05+0.03)) {
			StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
			StdDraw.filledRectangle(StdDraw.mouseX(), StdDraw.mouseY()+0.05, 0.3, 0.03);
			StdDraw.setPenColor(StdDraw.DARK_GRAY);
			StdDraw.text(StdDraw.mouseX(), StdDraw.mouseY()+0.05, "Improve evasion for each HP missing.");
			StdDraw.setPenRadius(0.005);
			StdDraw.rectangle(StdDraw.mouseX(), StdDraw.mouseY()+0.05, 0.3, 0.03);
			StdDraw.setPenColor();
			StdDraw.setPenRadius();
		}
	}
	
	@Override
	public void shipSpAbility() {
		double bonusEva=totalHull-currentHull;
		this.pilot.setEvasion(pilot.getEvasion()*1.1*(1+bonusEva/44));
	}
	
	@Override
	public void drawShield() {
		if(shield==null) return;
		for(int i =0; i<shield.getLayer();i++) {	
			StdDraw.picture(this.position.getX()-0.01, this.position.getY(), "shield.png",0.45,0.15);
		}
	}

}
