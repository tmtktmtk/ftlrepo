package ship;

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
import weapon.Projectile;
import weapon.Weapon;
import weapon.bustLaser;
import weapon.voidCanon;

public class DemoShip2 extends Ship {

	private double abiDuration;
	private double rechargeTime;
	
	public DemoShip2(boolean isPlayer, Vector2<Double> position) {
		// Creates the ship
		super(isPlayer, position);
		
		abiDuration=0.0;
		rechargeTime=0.0;
		// Sets the characteristics of the ship.
		totalHull 		= 35;
		currentHull		= 35;
		modules = new Module[4];
		
		double x= 0.05;
		double y= 0.095; 
		
		// Creates the tiles for the layout of the ship
		//core tile		
		shield = new Shield(new Vector2<Double>(x+0.05*1, y),new Vector2<Double>(position.getX(),position.getY()), isPlayer,2,0.02,0.03);
		addTile(shield);
		//cockpit
		pilot= new Piloting(new Vector2<Double>(x+0.05*2, y),new Vector2<Double>(position.getX()+0.03,position.getY()), isPlayer,4);
		addTile(pilot);
		//tail tile
		reactor = new Reactor(new Vector2<Double>(x+0.05*0, y), new Vector2<Double>(position.getX()-2*shield.DftSizeX,position.getY()),isPlayer, 8,0.02,0.01);
		addTile(reactor);
		//wing tile
		weaponControl = new WeaponControl(new Vector2<Double>(x+0.05*3, y),new Vector2<Double>(position.getX()-0.01,position.getY()-0.06), isPlayer,2,4, 0.01,0.03);
		addTile(weaponControl);
		Tile wingup = new Tile(new Vector2<Double>(position.getX()-0.01,position.getY()+0.06), isPlayer,0.01,0.03);
		addTile(wingup);
		Tile wingfront = new Tile(new Vector2<Double>(position.getX()+0.01,position.getY()+0.04), isPlayer);
		addTile(wingfront);
		Tile wingfront2 = new Tile(new Vector2<Double>(position.getX()+0.01,position.getY()-0.04), isPlayer);
		addTile(wingfront2);
		// Assigns the modules
		modules[0] = reactor;
		modules[3] = weaponControl;
		modules[1] = shield;
		modules[2] = pilot;
		
		// Creates the gun of the ship
		Weapon w = new Laser();
		Weapon w2= new bustLaser();
//		Weapon w3= new Ion();
		// Assigns the gun to the weapon control
		weaponControl.addWeapon(w);
		weaponControl.addWeapon(w2);
//		weaponControl.addWeapon(w3);
		// Places the weapon at the wing
		weaponControl.setWeapon(w);
		wingup.setWeapon(w2);
//		pilot.setWeapon(w3);
		CrewMember nupe=new CrewMember("Nupe");
		CrewMember jose=new CrewMember("Jose");
		CrewMember tam=new CrewMember("Tam");
		crew.add(nupe);
		crew.add(jose);
		crew.add(tam);
		weaponControl.setCrewMember(jose);
		shield.setCrewMember(nupe);
		pilot.setCrewMember(tam);
		CrewMember josh=new CrewMember("Josh");
		crew.add(josh);
		pilot.setCrewMember(josh);
		CrewMember nick=new CrewMember("Nick");
		crew.add(nick);
		pilot.setCrewMember(nick);
		shield.setCrewMember(nick);
		shield.setCrewMember(josh);
		//Tile available to set weapon;
		weaponTile.add(wingfront);
		weaponTile.add(wingfront2);
		
	}
	
	@Override
	public void draw() {

		if(this.currentHull>0) {
			if(this.abiDuration>0) StdDraw.picture(position.getX()-0.013,position.getY()-0.003, "strikerhalo.png",0.33,0.33);
				StdDraw.picture(position.getX()-0.013,position.getY()-0.003, "striker.png",0.33,0.33);
				Double n =Math.random()*0.02+0.004;
				Double m= Math.random()*0.01;
				StdDraw.setPenColor(150,0,0);
				StdDraw.filledEllipse(position.getX()-0.1, position.getY(), n, m/2);
				StdDraw.setPenColor();
		} else StdDraw.picture(position.getX(), position.getY(), "death.png",0.3,0.3);
		super.draw();
		StdDraw.picture(0.42, 0.05, "spcabibtn.png",0.34,0.06);
		StdDraw.text(0.42, 0.05, "OVERCHARGE: "+((rechargeTime==22)?"Ready":"On cd."));
		if(StdDraw.mouseX()>=(0.42-0.17) && StdDraw.mouseX()<=(0.42+0.17) &&
				StdDraw.mouseY()>=(0.05-0.03) && StdDraw.mouseY()<=(0.05+0.03)) {
			StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
			StdDraw.filledRectangle(StdDraw.mouseX(), StdDraw.mouseY()+0.08, 0.35, 0.05);
			StdDraw.setPenColor(150,150,10);
			StdDraw.filledRectangle(StdDraw.mouseX()-(0.35-0.35*(rechargeTime/22.0)), StdDraw.mouseY()+0.08, 0.35*(rechargeTime/22.0), 0.05);
			StdDraw.setPenColor(StdDraw.DARK_GRAY);
			StdDraw.text(StdDraw.mouseX(), StdDraw.mouseY()+0.095, "Active: Negate all damage for 3 seconds");
			StdDraw.text(StdDraw.mouseX(), StdDraw.mouseY()+0.065, "Passive: Improve energy weapon's damage.");
			StdDraw.setPenRadius(0.005);
			StdDraw.rectangle(StdDraw.mouseX(), StdDraw.mouseY()+0.08, 0.35, 0.05);
			StdDraw.setPenColor();
			StdDraw.setPenRadius();
		}
		if(StdDraw.mouseX()>=(0.42-0.17) && StdDraw.mouseX()<=(0.42+0.17) &&
				StdDraw.mouseY()>=(0.05-0.03) && StdDraw.mouseY()<=(0.05+0.03) && StdDraw.isLeftClick()) {
			if(rechargeTime==22) {
				abiDuration=3;
				rechargeTime=0;
				systemLog.addLog("System Overcharged!");
			} else systemLog.addLog("On cooldown.");
		} 
	}
	
	@Override
	public void drawShield() {
		if(shield==null) return;
		for(int i =0; i<shield.getLayer();i++) {	
			StdDraw.picture(this.position.getX()-0.01, this.position.getY(), "shield.png",0.35,0.35);
		}
	}
	@Override
	public void shotWeapon(int weapon) {

		double xSpeed;
		double ySpeed;
		if(isPlayer) {
			xSpeed = 1;
			ySpeed = 0;			
		} else {
			xSpeed = 0;
			ySpeed = 1;	
		}
		
		Projectile p = weaponControl.shotWeapon(weapon, getWeaponTile(weaponControl.getWeapon(weapon)), new Vector2<Double>(xSpeed, ySpeed));

		if(target!=null && p!=null) {
			p.setTarget(target);
			if(p.getType() == 2) {
				p.setProjectileDamage((int)(1*weaponControl.getAllocatedEnergy()*1.5));
			}
			if(p.getType() == 1) {
				p.setProjectileDamage(p.getDamage()+1);
			}
		}
		if (p != null){		
			p.setPlayer(isPlayer);
			projectiles.add(p);
		}
	}
	
	
	
	@Override
	public void applyDamage(Projectile p) {
		if(this.abiDuration>0) {
			return;
		}
		super.applyDamage(p);
	}
	
	
	
//	@Override
//	public void shipSpAbility() {
//		if(abiDuration>0) {
//			for(Projectile p: projectiles) {
//				if(p.getType()==1 || p.getType()==2)
//					p.setDamage(p.getDamage()*2);
//			}
//		}
//	}
	
	@Override
	public void step(double elapsedTime) {
		super.step(elapsedTime);
		if((rechargeTime+=elapsedTime)>=22.0) {rechargeTime=22.0;}
		if((abiDuration-=elapsedTime)<=0.0) {abiDuration=0.0;};
	}
}
