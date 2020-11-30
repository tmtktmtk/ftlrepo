package module;
import display.StdDraw;
import display.Vector2;
import ship.CrewMember;
import ship.Tile;
import weapon.Projectile;

/**
 * A module is an implementation of Tile which handles energy.
 * This tile has a HUD to display its current energy level.
 */
public abstract class Module extends Tile {

	protected	String				name;				// Name of the module
	protected	int 				maxLevel;			// Maximum level of the module
	protected 	int 				currentLevel;		// Current level of the module
	protected 	int 				allocatedEnergy;	// Amount of energy allocated to the module
	protected 	int					amountDamage;		// Amount of damage done to the module
	protected  	boolean 			canBeManned; 		// Can a crew member man this module
	protected 	Vector2<Double> 	hudPos;				// HUD position of the module
	private		double				currentRepairTime;
	protected   boolean				deactivated;
	protected   double				ionTime;
	/**
	 * Construct a module owned by the player or the opponent.
	 * The module's tile is drawn at the location given in tilePos.
	 * The module's HUD is drawn at the location given in hudPos.
	 * @param hudPos position at which to draw the HUD
	 * @param tilePos position at which to draw the tile
	 * @param isPlayer whether it belongs to the player
	 */
	public Module(Vector2<Double> hudPos, Vector2<Double> tilePos, boolean isPlayer) {
		super(tilePos, isPlayer);
		this.hudPos = hudPos;
		this.currentRepairTime=0.0;
		this.deactivated = false;
		this.ionTime = 0.0;
	}
	
	public Module(Vector2<Double> hudPos, Vector2<Double> tilePos, boolean isPlayer, double sizeX, double sizeY) {
		super(tilePos, isPlayer,sizeX,sizeY);
		this.hudPos = hudPos;
		this.currentRepairTime=0.0;
		this.deactivated = false;
		this.ionTime = 0.0;
	}
	
	/**
	 * Increments energy allocated to the module.
	 * @return whether the energy has been added or not
	 */
	public boolean addEnergy() {
		if (allocatedEnergy < currentLevel - amountDamage) {
			++allocatedEnergy;
			return true;
		}
		return false;
	}
	
	/**
	 * Decrements energy allocated to the module.
	 * @return whether the energy has been added or not
	 */
	public boolean removeEnergy() {
		if (allocatedEnergy > 0) {
			--allocatedEnergy;
			return true;
		}
		return false;
	}
	
	// Draw Methods
	
	/**
	 * Draw the module's tile. 
	 */
	@Override
	public void draw() {
		super.draw();
		if(this.currentRepairTime>0) {
			double n=Math.random()*0.0026+0.0025;
			StdDraw.setPenColor(StdDraw.GREEN);
			StdDraw.filledRectangle(this.tilePos.getX(), this.tilePos.getY(), n, n*3);
			StdDraw.filledRectangle(this.tilePos.getX(), this.tilePos.getY(), n*3, n);
			StdDraw.setPenColor();
		}
	}
	
	/**
	 * Draw the module's HUD.
	 */
	public void drawHud() {
		double x = hudPos.getX();
		double y = hudPos.getY();
		double e = allocatedEnergy;
		double d = amountDamage;
		StdDraw.picture(x, y+0.005, "energybar.png",0.04,0.18);
		StdDraw.setPenColor(100,100,10);
		StdDraw.filledRectangle(x, y-(0.08-0.08*(e/currentLevel)), 0.015, 0.08*(e/currentLevel));
		StdDraw.setPenColor(20,20,20);
		StdDraw.filledRectangle(x, y+(0.08-0.08*(d/currentLevel)), 0.015, 0.08*(d/currentLevel));
		StdDraw.setPenColor();
		StdDraw.text(x+0.001,y,allocatedEnergy+"|"+currentLevel,90);		
	}

	public void setCurrentLevel(int currentLevel) {
		if(currentLevel<=this.maxLevel)
			this.currentLevel = currentLevel;
		else this.currentLevel = this.maxLevel;
	}

	/**
	 * Gives the name of the module.
	 * @return name of the module
	 */
	public String getName() {
		return name;
	}

	/////////////
	// Getters //
	/////////////
	
	public int 		getMaxLevel() 			{ return maxLevel;			}
	public int 		getCurrentLevel()		{ return currentLevel; 		}
	public int		getAllocatedEnergy()	{ return allocatedEnergy;	}
	public int		getAmountDamage()		{ return amountDamage;		}
	public boolean 	getCanBeManned() 		{ return canBeManned; 		}
	
	public void moduleApplyDamage(Projectile p) {
		if(this.amountDamage+p.getDamage()>=this.currentLevel) {
			this.amountDamage=this.currentLevel;
			return;
		}
		this.amountDamage=this.amountDamage+p.getDamage();
//		System.out.println(this.amountDamage);
	}
	
	
	public void setAllocatedEnergy(int allocatedEnergy) {
		this.allocatedEnergy = allocatedEnergy;
	}

	public void repair(double time) {
		int nbMemInModule=0;
		for(CrewMember c: member) {
			if(c!=null) nbMemInModule++;
		}
		if(canBeManned && nbMemInModule>00 && this.amountDamage>0 && (this.currentRepairTime+=time)>=2/nbMemInModule) {
			this.amountDamage--;
			this.currentRepairTime=0.0;
			}
//		System.out.println(this.currentRepairTime);
		}

	public void autoRepair(double time) {
		if(this.amountDamage>0 && (this.currentRepairTime+=time)>=6) {
			this.amountDamage--;
			this.currentRepairTime=0.0;
		}
	}
	
	
	public void deactivate() {
		this.deactivated = true;
	}
	public void ionReactivate(double time) {
		if(deactivated && (this.ionTime+=time)>=4) {
			deactivated = false;
			ionTime=0.0;
			return;
		}
	}
}
