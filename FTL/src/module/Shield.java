package module;

import display.StdDraw;
import display.Vector2;
import ship.CrewMember;


public class Shield extends Module{
	
	private int layer;
	private double elapsedRechargeTime;
	
	public Shield(Vector2<Double> hudPos, Vector2<Double> tilePos, boolean isPlayer, int initialLevel) {
		super(hudPos,tilePos,isPlayer);
		name = "Shield";
		maxLevel = 8;
		currentLevel = initialLevel;
		allocatedEnergy = 0;
		amountDamage = 0;
		canBeManned = true;
		layer=0;
	}

	public Shield(Vector2<Double> hudPos, Vector2<Double> tilePos, boolean isPlayer, int initialLevel, double sizeX,double sizeY) {
		super(hudPos,tilePos,isPlayer,sizeX,sizeY);
		name = "Shield";
		maxLevel = 8;
		currentLevel = initialLevel;
		allocatedEnergy = 0;
		amountDamage = 0;
		canBeManned = true;
		layer=0;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}
	
	@Override
	public void draw() {
		super.draw();
		StdDraw.picture(tilePos.getX(), tilePos.getY(), "shieldicon.png",0.02,0.02);
	}
	
	@Override
	public void drawHud() {
		super.drawHud();
		StdDraw.picture(hudPos.getX(), hudPos.getY()-0.061, "shieldicon.png",0.03,0.03);

		StdDraw.setPenRadius(0.004);
		for(int i=0;i<4;i++) {
			StdDraw.setPenColor(StdDraw.WHITE);	
			StdDraw.rectangle(0.03+i*0.042, 0.92, 0.02, 0.02);
			if(deactivated) StdDraw.setPenColor(100,10,10);
			else StdDraw.setPenColor(StdDraw.DARK_GRAY);
			StdDraw.filledRectangle(0.03+i*0.042, 0.92, 0.02, 0.02);
		}
		StdDraw.setPenColor(StdDraw.BOOK_BLUE);
		StdDraw.setPenRadius();
		for(int i=0;i<layer;i++)
			StdDraw.filledRectangle(0.03+i*0.042, 0.92, 0.018, 0.018);
		StdDraw.setPenColor(StdDraw.DARK_GRAY);
		StdDraw.filledRectangle(0.0925, 0.894, 0.0833, 0.005);
		if(deactivated) StdDraw.setPenColor(100,10,10);
		else StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
		double n=(shieldFullyCharged()&&layer>0)?0.083:0.083*(elapsedRechargeTime/layerRechargeTime());
		StdDraw.filledRectangle(0.0925, 0.894, n, 0.0045);
		StdDraw.setPenColor();
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.rectangle(0.0925, 0.894, 0.0833, 0.005);
		StdDraw.setPenColor();
	}

	
	@Override
	public boolean removeEnergy() {
		if(super.removeEnergy()) {
			if(this.allocatedEnergy%2==0) layer = this.allocatedEnergy/2;
			else layer = (this.allocatedEnergy+1)/2;
			return true;
		}
		return false;
	}
	
	private boolean shieldFullyCharged() {
		int m = (this.allocatedEnergy%2==0)?this.allocatedEnergy:this.allocatedEnergy+1;
		return layer==m/2;
	}
	
	public void rechargeShield(double time) {
		if(shieldFullyCharged()) {
			elapsedRechargeTime=0.0;
			return;
		}
		if(!this.deactivated && (this.elapsedRechargeTime+=time)>=layerRechargeTime()) {
			layer++;
			elapsedRechargeTime=0.0;
			return;
		}
//		System.out.println(elapsedRechargeTime +" "+ layerRechargeTime() +" "+this.allocatedEnergy/2 + " " + this.layer);
	}
	
	private double layerRechargeTime() {
		int nbMemInModule=0;
		for(CrewMember c: member) {
			if(c!=null) nbMemInModule++;
		}
		if((this.allocatedEnergy/2-this.layer-1)>=0) return 1.5*(1-nbMemInModule*0.05);
		else return 2.0*(1-nbMemInModule*0.05);
	}
	
	@Override
	public void deactivate() {
		this.layer = 0;
		super.deactivate();

	}
	
}
