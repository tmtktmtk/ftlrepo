package module;
import java.awt.Font;

import display.Button;
import display.StdDraw;
import display.Vector2;
import ship.CrewMember;
import ship.Tile;
import weapon.Projectile;
import weapon.Weapon;

/**
 * A WeaponControl is a Module which handles weapons energy and activation.
 * This module has a specific HUD to display the weapons along with buttons
 * to interact with them.
 */
public class WeaponControl extends Module {
	
	private Weapon[] 		weapons;	// The weapon slots
	private WeaponButton[] 	weaponBtns;	// The button linked to the weapon slot
	private int				energyBase;
	private int				levelBase;
	/**
	 * A WeaponButton is an implementation of a Button
	 * which activates/deactivates the linked weapon when
	 * left/right clicked.
	 */
	private class WeaponButton extends Button {
		
		private int weaponIndex;
		
		public WeaponButton(Vector2<Double> pos, Vector2<Double> dim, int weaponIndex) {
			super(pos, dim);
			this.weaponIndex = weaponIndex;
		}

		@Override
		protected void onLeftClick() {
			activeWeapon(weaponIndex);
		}

		@Override
		protected void onRightClick() {
			deactiveWeapon(weaponIndex);
		}

		@Override
		protected void onMiddleClick() {}
		
	}
	
	/**
	 * Construct a WeaponControl owned by the player or the opponent.
	 * The WeaponControl's tile is drawn at the location given in tilePos.
	 * The WeaponControl's HUD is drawn at the location given in hudPos.
	 * The initialLevel of the WeaponControl is the amount of energy it
	 * can allocated when created.
	 * The amountWeapons defines the size of the weapon inventory.
	 * @param hudPos position at which to draw the HUD
	 * @param tilePos position at which to draw the tile
	 * @param isPlayer whether it belongs to the player
	 * @param initialLevel initial amount of energy which can be allocated
	 * @param amountWeapons the size of the weapon inventory
	 */
	public WeaponControl(Vector2<Double> hudPos, Vector2<Double> tilePos, boolean isPlayer, int initialLevel, int amountWeapons) {
		super(hudPos, tilePos, isPlayer);
		name = "Weapons";
		maxLevel = 8;
		currentLevel = initialLevel;
		levelBase = currentLevel;
		allocatedEnergy = 0;
		energyBase = allocatedEnergy;
		amountDamage = 0;
		canBeManned = true;
		weapons = new Weapon[amountWeapons];
		weaponBtns = new WeaponButton[amountWeapons];
	}
	
	public WeaponControl(Vector2<Double> hudPos, Vector2<Double> tilePos, boolean isPlayer, int initialLevel, int amountWeapons, double sizeX,double sizeY) {
		super(hudPos, tilePos, isPlayer,sizeX,sizeY);
		name = "Weapons";
		maxLevel = 8;
		currentLevel = initialLevel;
		levelBase = currentLevel;
		allocatedEnergy = 0;
		energyBase = allocatedEnergy;
		amountDamage = 0;
		canBeManned = true;
		weapons = new Weapon[amountWeapons];
		weaponBtns = new WeaponButton[amountWeapons];
	}
	
	/**
	 * Adds a weapon in the weapon inventory.
	 * @param w the weapon to add 
	 * @return whether the weapon has been added
	 */
	public boolean addWeapon(Weapon w) {
		int i;
		for (i = 0; i < weapons.length; i++) {
			if (weapons[i] == null)
				break;
		}
		if (i == weapons.length)
			return false;
		weapons[i] = w;
		return true;
	}
	
	/**
	 * Activates the weapon.
	 * @param weapon the index in the inventory of the weapon
	 * @return whether the weapon has been activated
	 */
	public boolean activeWeapon(int weapon) {
		if(deactivated) return false;
		if (weapon>=weapons.length) return false; 
		if (weapons[weapon] == null)
			return false;
		
		int energy = 0;
		for (Weapon w : weapons)
			if (w != null)
				energy += w.isActivated() ? w.getRequiredPower() : 0;
		Weapon w = weapons[weapon];
		if (allocatedEnergy< energy + w.getRequiredPower())
			return false;
		w.activate();
		return true;

	}
	
	public Weapon[] getWeapons() {
		return weapons;
	}

	/**
	 * Deactivates the weapon.
	 * @param weapon the index in the inventory of the weapon
	 */
	public boolean deactiveWeapon(int weapon) {
		if(weapons[weapon].isActivated()) {
			weapons[weapon].deactive();
			return true;
		}
			return false;
	}
	
	/**
	 * Gives the weapon of the inventory
	 * @param index location of the weapon in the inventory
	 * @return the weapon at location index
	 */
	public Weapon getWeapon(int index) {
		if(index>=weapons.length) return null;
		return weapons[index];
	}
	
	/**
	 * Charges the weapon.
	 * @param time the charging time to increase the weapon's charge by
	 */
	public void chargeTime(double time) {
		for (Weapon w : weapons)
			if (w != null) {
				if (w.isActivated())
					w.charge(time);
				else
					w.charge(-6*time);
			}
	}
	
	@Override
	public void draw() {
		super.draw();
		StdDraw.picture(tilePos.getX(), tilePos.getY(), "weaponicon.png",0.025,0.025);
	}
	
	/**
	 * Draws the weapon inventory and the weapon in it as well
	 * as their charging time.
	 */
	@Override
	public void drawHud() {
		super.drawHud();
		double x = hudPos.getX();
		double y = hudPos.getY();
		double bonus=allocatedEnergy-energyBase;
		StdDraw.setPenColor(200,200,20);
		StdDraw.filledRectangle(x, y-(0.08-0.08*(bonus/currentLevel)), 0.015, 0.08*(bonus/currentLevel));
		StdDraw.setPenColor();
		StdDraw.text(x+0.001,y,allocatedEnergy+"|"+currentLevel,90);
		StdDraw.setPenColor(50,50,15);
		StdDraw.setPenRadius(0.004);
		if(deactivated) {
			StdDraw.setPenColor(100,10,10);
			StdDraw.filledRectangle(x+0.05+(0.043*weapons.length), y+0.04, (0.042*weapons.length), 0.035);
			StdDraw.setPenColor(50,50,15);
		}
		StdDraw.rectangle(x+0.05+(0.043*weapons.length), y+0.04, (0.043*weapons.length), 0.04);
		StdDraw.picture(x, y-0.06, "weaponicon.png",0.04,0.04);
		for (int i = 0; i < weapons.length; i++) {
			Weapon w = weapons[i];
			if (w == null)
				continue;
			StdDraw.setPenColor(StdDraw.GRAY);
			if (w.getCurrentCharge() == w.getChargeTime())
				StdDraw.setPenColor(StdDraw.YELLOW);
			StdDraw.filledRectangle(x+0.095+(2*0.042*i), y+0.1-0.06, 0.04, (w.getCurrentCharge()/w.getChargeTime())*0.035);
			if (w.isActivated())
				StdDraw.setPenColor(StdDraw.ORANGE);
			if (weaponBtns[i] == null)
				weaponBtns[i] = new WeaponButton(new Vector2<Double>(x+0.1+(2*0.046*i), y+0.1-0.06), new Vector2<Double>(0.045, 0.035), i);
			else
				weaponBtns[i].draw();
			StdDraw.rectangle(x+0.095+(2*0.042*i), y+0.1-0.06, 0.04, 0.035);
			StdDraw.setPenColor();
			StdDraw.setPenColor();
			StdDraw.setFont(new Font("NasalizationRg-Regular",0,10));
			if(w.getName().compareTo("Thermal Lance")==0) {
				StdDraw.text(x+0.095+(2*0.042*i), y+0.1-0.055, "Thermal");
				StdDraw.text(x+0.095+(2*0.042*i), y+0.1-0.075, "Lance");
			}else if(w.getName().compareTo("Flak Laser")==0) {
				StdDraw.text(x+0.095+(2*0.042*i), y+0.1-0.055, "Flak");
				StdDraw.text(x+0.095+(2*0.042*i), y+0.1-0.075, "Laser");
			}
			else StdDraw.text(x+0.095+(2*0.042*i), y+0.1-0.06, w.getName());
			StdDraw.setPenColor();
			StdDraw.setFont(new Font("NasalizationRg-Regular",0,15));
			StdDraw.setPenRadius();
		}
	}
	
	/**
	 * Shots the weapon from the tile in the direction provided.
	 * @param weapon the weapon to shot
	 * @param tile the tile to shot it from
	 * @param dir the direction in which to shot it
	 * @return the projectile created by the weapon
	 */
	public Projectile shotWeapon(int weapon, Tile tile, Vector2<Double> dir) {
		if ( weapon>=weapons.length || weapons[weapon] == null || !weapons[weapon].isCharged() || deactivated)
			return null;
		Vector2<Double> v = tile.getPosition();
		weapons[weapon].resetCharge();
		return weapons[weapon].shot(new Vector2<Double>(v.getX(), v.getY()), dir);
	}
	
	/**
	 * Removes energy for the WeaponControl and deactivates
	 * weapons if energy is not sufficient anymore. 
	 */
	@Override
	public boolean removeEnergy() {
		if(amountDamage>0){
			allocatedEnergy=energyBase;
			currentLevel=levelBase;
		}
		if (energyBase > 0) {
			--allocatedEnergy;
			--energyBase;
			int energy = 0;
			for (Weapon w : weapons)
				if (w != null)
					energy += w.isActivated() ? w.getRequiredPower() : 0;
			Weapon last = null;
			if (energy > allocatedEnergy)
				for (Weapon w : weapons)
					if (w != null && w.isActivated())
						last = w;
			if (last != null)
				last.deactive();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean addEnergy() {
		if(super.addEnergy()) {
			++energyBase;
			return true;
		}
		return false;
	}
	
	@Override
	public void moduleApplyDamage(Projectile p) {
		if(this.amountDamage+p.getDamage()>=this.levelBase) {
			this.amountDamage=this.levelBase;
			return;
		}
		this.amountDamage=this.amountDamage+p.getDamage();
		if(deactivated) {
			for(int i=0;i<weapons.length;i++) {
				if(weapons[i]!=null)weapons[i].deactive();
			}
		}
	}
	
	
	public void crewBonus() {
		if(amountDamage>0){
			allocatedEnergy=energyBase;
			currentLevel=levelBase;
			int energy = 0;
			for (Weapon w : weapons)
				if (w != null)
					energy += w.isActivated() ? w.getRequiredPower() : 0;
			Weapon last = null;
			if (energy > allocatedEnergy)
				for (Weapon w : weapons)
					if (w != null && w.isActivated())
						last = w;
			if (last != null)
				last.deactive();
		} else {
			int nbMemInModule=0;
			for(CrewMember c: member) {
				if(c!=null) nbMemInModule++;
			}
			allocatedEnergy=energyBase+nbMemInModule;
			currentLevel=levelBase+nbMemInModule;
		}
	}

	@Override
	public void setCurrentLevel(int currentLevel) {
		this.levelBase=this.levelBase+(currentLevel-this.currentLevel);
		if(this.levelBase>this.maxLevel) this.levelBase=this.maxLevel;

	}
	
}
