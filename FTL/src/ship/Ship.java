package ship;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import display.Button;
import display.StdDraw;
import display.Vector2;
import main.GameOption;
import module.Module;
import module.Piloting;
import module.Reactor;
import module.Shield;
import module.WeaponControl;
import weapon.Projectile;
import weapon.Weapon;

/**
 * A ship is the composed of modules, tiles and crew members.
 * The ship has a hull that can be damaged by an opponent's ship.
 */
public abstract class Ship {
	
	
	protected Vector2<Double>			position;		// The position of the ship
	protected int						totalHull;		// The total hull integrity of the ship
	protected int						currentHull;	// The current hull integrity of the ship
	
	protected Reactor					reactor;		// The reactor of the ship
	protected WeaponControl				weaponControl;	// The weapon control system
	protected Shield					shield;
	protected Piloting					pilot;
	
	protected ArrayList<CrewMember> 	crew;			// The crew members in the ship
	protected ArrayList<Tile>			layout;			// The layout of the ship
	protected TileButton[]				tileBtns;
	protected CrewButton[]				crewBtns;
	public LinkedList<Tile>			weaponTile;
	protected boolean					isPlayer;		// Whether this ship is owned by the player
	protected Module[]					modules;		// The modules on the ship
	protected Collection<Projectile>	projectiles;	// The projectiles shot by the ship
	protected Tile						selectedTile;
	protected Tile						target;			// The targeted tile of the enemy ship
	protected CrewMember				selectedMember; // The currently selected crew member
	protected   GameOption				systemLog;
	private boolean autoFire;
	
	/**
	 * Creates a Ship for the player or the opponent at the provided position.
	 * @param isPlayer whether the ship is for the player
	 * @param position the location to create it
	 */
	public Ship(boolean isPlayer, Vector2<Double> position) {
		this.isPlayer = isPlayer;
		this.position = position;
		crew = new ArrayList<CrewMember>();
		projectiles = new ArrayList<Projectile>();
		layout = new ArrayList<Tile>();
		tileBtns = new TileButton[20];
		crewBtns = new CrewButton[10];
		systemLog = new GameOption();
		autoFire = false;
		weaponTile = new LinkedList<Tile>();
	}
	
	public WeaponControl getWeaponControl() {
		return weaponControl;
	}

	private class CrewButton extends Button {
		
		private int CrewIndex;
		
		public CrewButton(Vector2<Double> pos, Vector2<Double> dim, int CrewIndex) {
			super(pos, dim,true);
			this.CrewIndex = CrewIndex;
		}

		@Override
		protected void onLeftClick() {
				selectMember(CrewIndex);	
		}

		@Override
		protected void onRightClick() {
			
		}

		@Override
		protected void onMiddleClick() {}
		
		@Override
		public void draw() {
			if (draw) {
				StdDraw.setPenColor(StdDraw.WHITE);			
				StdDraw.rectangle(pos.getX(), pos.getY(), dim.getX(), dim.getY());
				if(crew.get(CrewIndex).isSelected()) {
					StdDraw.setPenColor(50,50,50);
					StdDraw.filledRectangle(pos.getX(), pos.getY(), dim.getX(), dim.getY());
					StdDraw.setPenColor(StdDraw.GREEN);	
					StdDraw.setFont(new Font("NasalizationRg-Regular",Font.BOLD,11));
					StdDraw.text(pos.getX(), pos.getY(), crew.get(CrewIndex).getName());
				} else {
					StdDraw.setPenColor(75,75,75);
					StdDraw.filledRectangle(pos.getX(), pos.getY(), dim.getX(), dim.getY());
					StdDraw.setPenColor(StdDraw.WHITE);	
					StdDraw.setFont(new Font("NasalizationRg-Regular",0,10));
					StdDraw.text(pos.getX(), pos.getY(), crew.get(CrewIndex).getName());
				}
				StdDraw.setFont(new Font("NasalizationRg-Regular",0,15));
				StdDraw.setPenColor();			
			}
		}
		
	}
	
	private class TileButton extends Button {
		
		private int TileIndex;
		
		public TileButton(Vector2<Double> pos, Vector2<Double> dim, int TileIndex) {
			super(pos, dim);
			this.TileIndex = TileIndex;
		}

		@Override
		protected void onLeftClick() {

		}

		@Override
		protected void onRightClick() {
			if(layout.get(TileIndex).isPlayer()) {
				if(selectedTile==null) {
					selectedTile = layout.get(TileIndex);
					selectedTile.markSelected();
				}
				else {
					selectedTile.unmarkSelected();
					selectedTile=layout.get(TileIndex);
					selectedTile.markSelected();
				}
			}moveCrewMember();
		}

		@Override
		protected void onMiddleClick() {}
		
	}
	
	
	public Tile getTarget() {
		return target;
	}
	
	
	
	// Main Methods

	public Module[] getModules() {
		return modules;
	}

	public Vector2<Double> getPosition() {
		return position;
	}
	

	public void setPosition(Vector2<Double> position) {
		this.position = position;
	}

	private void autoFire() {
		StdDraw.rectangle(0.237, 0.135, 0.012, 0.04);
		if(StdDraw.isLeftClick() && StdDraw.mouseX()>=(0.237-0.012) && StdDraw.mouseX()<=(0.237+0.012) &&
				StdDraw.mouseY()>=(0.135-0.04) && StdDraw.mouseY()<=(0.135+0.04)) autoFire=!autoFire;
		if(autoFire) {
			for(int i=0;i<weaponControl.getWeapons().length;i++) {
				if(weaponControl.getWeapon(i)!=null) shotWeapon(i);
			}
		}
	}
	
	
	protected Tile findWeaponControl(Ship s) {
		
		for(Module m:s.modules) {
			if(m.getName().compareTo("Weapons")==0) return m;
		}
		return modules[0];
	}
	
	protected Tile findShield(Ship s) {
		
		for(Module m:s.modules) {
			if(m.getName().compareTo("Shield")==0) return m;
		}
		return modules[0];
	}
	
	protected Tile findPilot(Ship s) {
		
		for(Module m:s.modules) {
			if(m.getName().compareTo("Pilot")==0) return m;
		}
		return modules[0];
	}
	
	protected void activeAllAvabWeapon() {
		for(int i = 0; i<6;i++) this.activeWeapon(i);
	}
	
	protected void shotAllWeapon() {
		for(int i = 0; i<6;i++) this.shotWeapon(i);
	}
	
	/**
	 * Processes the action of the AI.
	 * @param player the enemy of the AI
	 */
	public void ai(Ship player) {
		if (isPlayer)
			return;
		if (currentHull==0) 
			return; 
		
		double r=Math.random();
		if(r<0.33) target=findWeaponControl(player);
		else if(r<0.66) target=findShield(player);
		else target=findPilot(player);
		activeAllAvabWeapon();
		shotAllWeapon();
	}
	
	/**
	 * Processes the time elapsed between the steps.
	 * @param elapsedTime the time elapsed since the last step
	 */
	public void step(double elapsedTime) {
		chargeWeapons(elapsedTime);
		processProjectiles(elapsedTime);
		repairModule(elapsedTime);
		if(shield!=null) {
			shield.rechargeShield(elapsedTime);
			shield.ionReactivate(elapsedTime);
		}
		reactivateIon(elapsedTime);
		pilot.setEvasion();
		weaponControl.crewBonus();
		shipSpAbility();
		if(isPlayer) autoFire();
	}
	
	private void reactivateIon(double time) {
		pilot.ionReactivate(time);
		weaponControl.ionReactivate(time);
		reactor.ionReactivate(time);
	}
	
	//Repair module Methods
	
	protected void repairModule(double time) {
		for(Module m: modules) {
			m.repair(time);
		}
	}
	
	
	// Drawing Methods
	
	/**
	 * Draws the ship and all its components.
	 */
	public void draw() {
			if(this.currentHull>0) {
				drawTiles();
				drawShield();
			}
			systemLog.writeLog();
	}
	
	public void drawProjectiles() {
		for (Projectile p : projectiles)
			p.draw();
	}

	public void drawShield() {
		if(shield==null) return;
		for(int i =0; i<shield.getLayer();i++) {
			StdDraw.picture(this.position.getX(), this.position.getY(), "shield.png",0.3,0.31);
			StdDraw.picture(this.position.getX(), this.position.getY(), "shield.png",0.3,0.31);
		}
	}
	
	/**
	 * Draw the tiles of the ship.
	 */
	private void drawTiles() {

		for (int i = 0; i< layout.size();++i) {
			layout.get(i).draw();
			//if isPlayer create tileBtns
			if (layout.get(i).isPlayer() && tileBtns[i] == null)
				tileBtns[i] = new TileButton(layout.get(i).getPosition(), new Vector2<Double>(layout.get(i).getDftSizeX(), layout.get(i).getDftSizeY()), i);
		}
		
	}

	/**
	 * Draws the HUD of the ship.
	 */
	public void drawHUD() {
		if (isPlayer)
			drawPlayerHUD();
		else
			drawOpponentHUD();
	}
	
	/**
	 * Draws the HUD of the player.
	 */
	private void drawPlayerHUD() {
		StdDraw.setPenRadius(0.006);
		StdDraw.setPenColor(50,50,20);	
		StdDraw.rectangle(0.29, 0.99, 0.35, 0.11);
		StdDraw.setPenRadius();
		StdDraw.picture(0.30, 0.9855, "greybg.png",0.675,0.21);	
		
		StdDraw.picture(0.32, 0.105, "controlpannel.png",0.63,0.205);
		StdDraw.setPenColor(150,150,15);	
		StdDraw.picture(0.34, 0.55, "yellowbg.png",0.552,0.6);
		StdDraw.rectangle(0.34, 0.55, 0.28, 0.3);
		StdDraw.setPenRadius();
		StdDraw.setPenColor();
		
		//autoFireBtns
		StdDraw.setPenColor(50,50,20);
		StdDraw.setPenRadius(0.006);
		StdDraw.rectangle(0.237, 0.135, 0.012, 0.04);
		if(!autoFire) StdDraw.setPenColor(); else StdDraw.setPenColor(100,150,50);
		StdDraw.text(0.2375, 0.135, "Auto",90);
		StdDraw.setPenRadius();
		
		// Modules
		for (Module m : modules)
			m.drawHud();
		// Health bar
		double i = currentHull;
		StdDraw.setPenColor(100,150,50);		
		StdDraw.setPenRadius(0.005);
		StdDraw.picture(0.32, 0.97, "healthbar.png",0.63,0.05);
		if(currentHull!=0)
			StdDraw.filledRectangle(0.32-(0.305-(i/totalHull)*0.305), 0.9709, (i/totalHull)*0.305, 0.02);
		// createCrewbutton;
		StdDraw.setPenRadius(0.005);
		StdDraw.setPenColor(StdDraw.WHITE);	
		StdDraw.text(0.32, 0.9709, currentHull+"|"+totalHull);
		StdDraw.rectangle(0.5, 0.915, 0.125, 0.025);
		for(int m=0;m<crew.size();m++) {
			if(crewBtns[m]==null) 
				crewBtns[m] = new CrewButton(new Vector2<Double> (0.4+m*0.05,0.915), new Vector2<Double> (0.025,0.025),m);
			else crewBtns[m].draw();		
		}
		StdDraw.setPenRadius();
		StdDraw.setPenColor();	
		
	}
	
	/**
	 * Draws the HUD of the opponent.
	 */
	private void drawOpponentHUD() {
		double i = currentHull;
		
		
		//box
		StdDraw.picture(0.82, 0.55, "redbg.png",0.295,0.5);
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.text(0.768, 0.82, "Enemy sighted");
		StdDraw.setPenRadius(0.005);
		StdDraw.rectangle(0.82, 0.55, 0.15, 0.25);
		//health bar
		StdDraw.filledRectangle(0.76-(0.08-(i/totalHull)*0.08), 0.78,(i/totalHull)*0.08, 0.01);
		StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
		StdDraw.rectangle(0.76, 0.78, 0.08, 0.01);

		//shield bar:
		for(int j =0; j<shield.getLayer();j++) {
			StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
			StdDraw.rectangle(0.865+j*0.0275, 0.78, 0.01, 0.01);
			StdDraw.setPenColor(50,50,50);
			StdDraw.filledRectangle(0.865+j*0.0275, 0.78, 0.01, 0.01);
		}		
		StdDraw.setPenRadius();
		StdDraw.setPenColor();
	}
	
	// Crew Methods
	
	/**
	 * Check if a crew member is currently selected.
	 * @return whether a crew member is selected
	 */
	public boolean isCrewMemberSelected() {
		return selectedMember != null;
	}

	/**
	 * Unselects the selected crew member.
	 */
	public void unselectCrewMember() {
		if (!isCrewMemberSelected())
			return;
		selectedMember.unselect();
		selectedMember = null;
	}

	/**
	 * Selects the crew member.
	 * @param i -th crew member
	 */
	public void selectMember(int i) {
		int j = 0;
		for (CrewMember m : crew)
			if (j++ == i) {
				unselectCrewMember();
				selectedMember = m;
				selectedMember.select();
				return;
			}
				
	}
	
	/**
	 * Adds a crew member to the ship.
	 * @param member the crew member to add
	 */
	public void addCrewMember(CrewMember member) {
		Tile t = getEmptyTile();
		if (t == null) {
			System.err.println("The ship is full ! Sorry " + member.getName() + "...");
			return;
		}
		crew.add(member);
		t.setCrewMember(member);
	}
	
	public void moveCrewMember() {
		if(selectedMember==null) {
			System.out.println("Please select a crewmember to move.");
			systemLog.addLog("No member selected.");
			return;
		}
		if(selectedTile==null) {
			System.out.println("No destination.");
			systemLog.addLog("No destination.");
			return;
		}
		if(selectedTile.hasfullCrewMember()) {
			System.out.println("Room is full.");
			systemLog.addLog("Room's full");
			return;
		}
		for(Tile t:layout) {
			//find selectedMember's current tile
			for(CrewMember m:t.getMember()) {
				if(m==selectedMember) {
						t.unsetCrewMember(selectedMember);
						selectedTile.setCrewMember(selectedMember);	
				}
				
			}
		}
		
	}
	
	// Layout Methods
	
	/**
	 * Adds a tile to the ship.
	 * @param t the tile to add
	 */
	protected void addTile(Tile t) {
		layout.add(t);
	}
	
	/**
	 * Gives an empty tile of the ship
	 * @return a tile empty of crew member
	 */
	private Tile getEmptyTile() {
		Iterator<Tile> it = layout.iterator();
		while(it.hasNext()) {
			Tile t = it.next();
			if (!t.hasfullCrewMember())
				return t;
		}
		return null;
	}
	
	/**
	 * Gives the first tile of the ship.
	 * @return the first tile of the ship
	 */
	private Tile getFirstTile() {
		return layout.iterator().next();
	}
	

	
	
	// Energy Methods

	/**
	 * Decreased the energy allocated in the module. 
	 * @param module the module to remove energy from
	 */
	public void removeEnergy(int module) {
		if (module >= modules.length)
			return;
		if (modules[module].removeEnergy())
			reactor.addEnergy();
	}
	
	/**
	 * Increases the energy allocated in the module.
	 * @param module the module to add energy to
	 */
	public void addEnergy(int module) {
		if (module >= modules.length)
			return;
		if(modules[module].getAllocatedEnergy()>=modules[module].getCurrentLevel()) systemLog.addLog("Max power cap.");
		if (reactor.getAllocatedEnergy() > 0 && modules[module].addEnergy()) 
			reactor.removeEnergy();
		
	}
	
	// Weapon Methods
	
	/**
	 * Deactivates a weapon. 
	 * @param weapon the weapon to deactivate
	 */
	public void deactiveWeapon(int weapon) {
		if(weaponControl.deactiveWeapon(weapon))
			systemLog.addLog(weaponControl.getWeapon(weapon).getName()+" deactivated");
	}
	
	/**
	 * Activates a weapon. 
	 * @param weapon the weapon to activate
	 */
	public void activeWeapon(int weapon) {
		if(weaponControl.activeWeapon(weapon))
			systemLog.addLog(weaponControl.getWeapon(weapon).getName()+" activated");
	}
	
	/**
	 * Charges the weapons by the time provided
	 * @param time the time to charge the weapons by
	 */
	public void chargeWeapons(double time) {
		weaponControl.chargeTime(time);
	}

	/**
	 * Shots a weapon.
	 * @param weapon the weapon to shot
	 */
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
				p.setProjectileDamage(1*weaponControl.getAllocatedEnergy());
			}
		}
		if (p != null){		
			p.setPlayer(isPlayer);
			projectiles.add(p);
		}
	}
	
	/**
	 * Gives the tile on which the weapon is.
	 * @param w the weapon to get the tile from
	 * @return the tile on which the weapon is attached
	 */
	protected Tile getWeaponTile(Weapon w) {
		for (Tile t : layout)
			if (t.getWeapon() == w)
				return t;
		return null;
	}
	
	// Projectile Methods
	private static Vector2<Double> randomOnEdge(double xCenter, double yCenter,double halfWidth, double halfHeight) {
		Vector2<Double> pos=null;
		pos = (Math.random()>=Math.random())
				? new Vector2<Double>(Math.random()*(2*halfWidth)+xCenter-halfWidth,(Math.random()>=Math.random())?yCenter+halfHeight:yCenter-halfHeight)
				: new Vector2<Double>((Math.random()>=Math.random())?xCenter+halfWidth:xCenter-halfWidth,Math.random()*(2*halfHeight)+yCenter-halfHeight);
		return pos;
	}
	
	
	/**
	 * Processes the projectiles with the time elapsed since
	 * the last processing.
	 * @param elapsedTime the time elapsed since the last call
	 */
	private void processProjectiles(double elapsedTime) {
		Collection<Projectile> p2remove= new ArrayList<Projectile>();
		for (Projectile p : projectiles) {
			if (p != null) {

				p.step(elapsedTime);
				
				p.setInPlayerBox(!p.isOutOfRectangle(0.34, 0.55, 0.28, 0.3));
				if(p.getTarget()==null) {
					if(p.isInPlayerBox()) {
						if(p.isOutOfRectangle(0.34, 0.55, 0.28, 0.3)) p2remove.add(p);
					}
					else {
						if(p.isOutOfRectangle(0.82, 0.55, 0.15, 0.25)) p2remove.add(p);
					}
				}
				if(p.getTarget()!=null) {
					//when out of player's box, re appear from the edge of the opponent's box and aim at target, for player
					if(p.isOutOfRectangle(0.34, 0.55, 0.28, 0.3) && this.isPlayer && !p.DidDamage()) {
						if(p.isOutOfRectangle(0.82, 0.55, 0.15, 0.25)) {
							Vector2<Double> pos = randomOnEdge(0.82,0.55,0.14,0.24);
							p.setPosition(pos.getX(), pos.getY());
						}
						p.computeDirection();
					}
					
					//when out of opponent's box, re appear from the edge of the player's box and aim at target, for opponent
					if(p.isOutOfRectangle(0.82, 0.55, 0.15, 0.25) && !this.isPlayer && !p.DidDamage()) {
						if(p.isOutOfRectangle(0.34, 0.55, 0.28, 0.3)) {
							Vector2<Double> pos2 = randomOnEdge(0.34, 0.55, 0.27, 0.29);
							p.setPosition(pos2.getX(), pos2.getY());
						}
						p.computeDirection();
					}
					
					//when hit target, explode and disappear
					if(!p.isOutOfRectangle(p.getTarget().tilePos.getX(),
							p.getTarget().tilePos.getY(), p.getTarget().DftSizeX, p.getTarget().DftSizeY)) {
						p.didHit(true);
					}
					if(p.isOutOfRectangle(p.getTarget().tilePos.getX(),
							p.getTarget().tilePos.getY(), p.getTarget().DftSizeX, p.getTarget().DftSizeY) && p.isHit() && !p.isEvaded())
						p2remove.add(p);
				}
			}
		}
		for (Projectile p : p2remove) if(p!=null) projectiles.remove(p);
		p2remove.clear();
	}
	
	
	/**
	 * Gives the projectiles shot by the ship.
	 * @return A collection of projectiles
	 */
	public Collection<Projectile> getProjectiles(){
		return projectiles;
	}

	/**
	 * TODO
	 * Applies the damage a projectile did.
	 * @param p the projectile to process
	 */
	public void applyDamage(Projectile p) {


		
		if(!p.isEvaded() && Math.random()>(1-pilot.getEvasion()-((p.getType()==4)?0.15:0)+((p.getType()==-1)?0.15:0))) {
			p.setIsEvaded(true);
			p.didHit(false);
			p.setTarget(null);
			return;
		}
		
		if(p.getType()==-1) {
			this.currentHull=(this.currentHull-p.getDamage()<=0)?0:this.currentHull-p.getDamage();
			applyModuleDamage(p);
			for(Module m: modules) m.deactivate();
		}
		
		if(p.getType() == 4) {
			this.currentHull=(this.currentHull-p.getDamage()<=0)?0:this.currentHull-p.getDamage();
			applyModuleDamage(p);
			return;
		}
		if(shield!=null && shield.getLayer()>0) {
			p.setIsShielded(true);
			if(p.getType() == 3) {
				shield.deactivate();
			}
			shield.setLayer(shield.getLayer()-1); return;
		}else {p.setIsShielded(false);
		if(p.getType() == 3) {
			deactivateModule(p);
		}
		}
		
			this.currentHull=(this.currentHull-p.getDamage()<=0)?0:this.currentHull-p.getDamage();
			applyModuleDamage(p);
	}

	private void deactivateModule(Projectile p) {
		for(int i=0;i<modules.length;i++) {
			if(p.getTarget().equals(modules[i])) {
				modules[i].deactivate();
			}
		}
	}
	
	private void applyModuleDamage(Projectile p) {
		for(int i=0;i<modules.length;i++) {
			if(p.getTarget().equals(modules[i])) {
				modules[i].moduleApplyDamage(p);
				int d=(modules[i].getCurrentLevel()-modules[i].getAmountDamage());
				if(d<0)d=0;
				while(d<modules[i].getAllocatedEnergy()) {
					removeEnergy(i);
//					System.out.println(modules[i].getName()+" "+modules[i].getAllocatedEnergy()+" "+ d);
				}
			}
		}
	}
	
	// Aiming Methods
	
	private static boolean isInTile(double x, double y, double xCenter, double yCenter, double halfWidth, double halfHeight) {
		return x >= xCenter-halfWidth && x <= xCenter+halfWidth && y >= yCenter-halfHeight && y <= yCenter+halfHeight;
	}
	
	
	/**
	 * TODO
	 * Aims the guns up.
	 * @param opponent the ship to aim at
	 */
	public void aimUp(Ship opponent) {
		if (target == null) {
			target = opponent.getFirstTile();
			target.markTarget();
			return;
		}else {
			double x = target.getPosition().getX();
			double y = target.getPosition().getY()+target.DftSizeY+0.005;
			for(double i=x-target.DftSizeX;i<x+target.DftSizeX;i+=target.DftSizeX) {
				for(Tile t : opponent.layout) {
					if(isInTile(i,y,t.getPosition().getX(),t.getPosition().getY(),t.DftSizeX,t.DftSizeY)){
						target.unmarkTarget();
						target=t;
						target.markTarget();
						return;
					}
				}
				systemLog.addLog("Target not valid.");
				target.unmarkTarget();
			}
			target=null;
		}
	}
	
	/**
	 * TODO
	 * Aims the guns down.
	 * @param opponent the ship to aim at
	 */
	public void aimDown(Ship opponent) {
		if (target == null) {
			target = opponent.getFirstTile();
			target.markTarget();
			return;
		}else {
			double x = target.getPosition().getX();
			double y = target.getPosition().getY()-target.DftSizeY-0.005;
			for(double i=x-target.DftSizeX;i<x+target.DftSizeX;i+=target.DftSizeX) {
				for(Tile t : opponent.layout) {
					if(isInTile(i,y,t.getPosition().getX(),t.getPosition().getY(),t.DftSizeX,t.DftSizeY)){
						target.unmarkTarget();
						target=t;
						target.markTarget();
						return;
					}
				}
				systemLog.addLog("Target not valid.");
				target.unmarkTarget();
			}
			target=null;					
		}
	}
	
	/**
	 * TODO
	 * Aims the guns left.
	 * @param opponent the ship to aim at
	 */
	public void aimLeft(Ship opponent) {
		if (target == null) {
			target = opponent.getFirstTile();
			target.markTarget();
			return;
		}else {
			double x = target.getPosition().getX()-target.DftSizeX-0.005;
			double y = target.getPosition().getY();
			for(double i=y-target.DftSizeY;i<y+target.DftSizeY;i+=target.DftSizeY) {
				for(Tile t : opponent.layout) {
					if(isInTile(x,i,t.getPosition().getX(),t.getPosition().getY(),t.DftSizeX,t.DftSizeY)){
						target.unmarkTarget();
						target=t;
						target.markTarget();
						return;
					}
				}
				systemLog.addLog("Target not valid.");
				target.unmarkTarget();
			}
			target=null;					
		}
	}
	
	/**
	 * TODO
	 * Aims the guns right.
	 * @param opponent the ship to aim at
	 */
	public void aimRight(Ship opponent) {
		if (target == null) {
			target = opponent.getFirstTile();
			target.markTarget();
			return;
		}else {
			double x = target.getPosition().getX()+target.DftSizeX+0.005;
			double y = target.getPosition().getY();
			for(double i=y-target.DftSizeY;i<y+target.DftSizeY;i+=target.DftSizeY) {
				for(Tile t : opponent.layout) {
					if(isInTile(x,i,t.getPosition().getX(),t.getPosition().getY(),t.DftSizeX,t.DftSizeY)){
						target.unmarkTarget();
						target=t;
						target.markTarget();
						return;
					}
				}
				systemLog.addLog("Target not valid.");
				target.unmarkTarget();
			}
			target=null;	
		}
	}

	public void shipSpAbility() {
	}



	public int getCurrentHull() {
		return currentHull;
	}

	public void upgrade() {}



	public int getTotalHull() {
		return totalHull;
	}



	public void setCurrentHull(int currentHull) {
		this.currentHull = currentHull;
	}
	
	
}
