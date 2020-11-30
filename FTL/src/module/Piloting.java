package module;

import display.StdDraw;
import display.Vector2;
import ship.CrewMember;

public class Piloting extends Module {
	
	private double evasion;
	
	public Piloting(Vector2<Double> hudPos, Vector2<Double> tilePos, boolean isPlayer, int initialLevel) {
		super(hudPos,tilePos,isPlayer);
		name = "Pilot";
		maxLevel = 8;
		currentLevel = initialLevel;
		allocatedEnergy = 0;
		amountDamage = 0;
		canBeManned = true;
		evasion=0.0;
	}

	public Piloting(Vector2<Double> hudPos, Vector2<Double> tilePos, boolean isPlayer, int initialLevel, double sizeX,double sizeY) {
		super(hudPos,tilePos,isPlayer,sizeX,sizeY);
		name = "Pilot";
		maxLevel = 8;
		currentLevel = initialLevel;
		allocatedEnergy = 0;
		amountDamage = 0;
		canBeManned = true;
		evasion=0.0;
	}
	
	@Override
	public void draw() {
		super.draw();
		StdDraw.picture(tilePos.getX(), tilePos.getY(), "piloticon.png",0.022,0.022);
	}
	
	
	@Override
	public void drawHud() {
		super.drawHud();
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.picture(hudPos.getX(), hudPos.getY()-0.061, "piloticon.png",0.043,0.043);
		StdDraw.setPenRadius(0.004);
		StdDraw.rectangle(0.275, 0.915, 0.085, 0.025);
		if(deactivated) StdDraw.setPenColor(100,10,10);
		else StdDraw.setPenColor(StdDraw.DARK_GRAY);
		StdDraw.filledRectangle(0.275, 0.915, 0.085, 0.025);
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.setPenRadius();
		StdDraw.text(0.275, 0.915, "Evade: "+Math.round(evasion*100)+"%");
		StdDraw.setPenColor();
	}
	
	
	public void setEvasion() {
		int nbMemInModule=0;
		for(CrewMember c: member) {
			if(c!=null) nbMemInModule++;
		}
		if(this.deactivated) evasion=0.0;
		else evasion=(this.allocatedEnergy*0.05)*(1+(0.2*nbMemInModule));
	}
	

	public double getEvasion() {
		return evasion;
	}

	public void setEvasion(double evasion) {
		this.evasion = evasion;
	}
	
	
	
}
