package main;


import java.awt.Font;
import java.util.LinkedList;

import display.StdDraw;

public class GameOption {

	private boolean showInfo;
	private boolean gamePause;
	boolean boss;
	private LinkedList<String> systemLog; 
	int mod2update;
	int randWeapon;
	
	public GameOption() {
		this.showInfo=true;
		this.gamePause=false;
		this.systemLog=new LinkedList<String>();
		this.mod2update=(int)(Math.random()*0+3);
		this.randWeapon=(int)(Math.random()*4+1);
		this.boss=false;
	}
	
	
	
	public boolean isGamePause() {
		return gamePause;
	}

	

	public void setGamePause(boolean gamePause) {
		this.gamePause = gamePause;
	}



	public void showMenu() {
		//info button
		StdDraw.picture(0.975, 0.975, "Infobtn.png",0.05,0.05);
		if(StdDraw.isLeftClick() && 
				(StdDraw.mouseX()>=0.95 & StdDraw.mouseX()<=1) &&
					(StdDraw.mouseY()>=0.95 & StdDraw.mouseY()<=1)) showInfo=!showInfo;
		
		StdDraw.picture(0.925, 0.975, "pausebtn.png",0.05,0.05);
		if(StdDraw.isLeftClick() && 
				(StdDraw.mouseX()>=0.9 & StdDraw.mouseX()<=0.95) &&
					(StdDraw.mouseY()>=0.95 & StdDraw.mouseY()<=1)) gamePause=true;
		StdDraw.picture(0.875, 0.975, "playbtn.png",0.05,0.05);
		if(StdDraw.isLeftClick() && 
				(StdDraw.mouseX()>=0.85 & StdDraw.mouseX()<=0.9) &&
					(StdDraw.mouseY()>=0.95 & StdDraw.mouseY()<=1)) gamePause=false;
		if(StdDraw.isKeyPressed(80)) gamePause=!gamePause;
		
		StdDraw.setPenColor(StdDraw.RED);
		if(gamePause) StdDraw.text(0.5, 0.5, "PAUSED");
		
		if(showInfo) {
			StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
			StdDraw.filledRectangle(0.5, 0.5, 0.45, 0.45);
			StdDraw.setPenColor(StdDraw.DARK_GRAY);
			StdDraw.setPenRadius(0.005);
			StdDraw.rectangle(0.5, 0.5, 0.45, 0.45);
			StdDraw.text(0.5, 0.75, "CONTROL PROCEDURE:");
			StdDraw.text(0.5, 0.7, "Use number key to select respective weapon.");
			StdDraw.text(0.5,0.65, "Click on the i symbol to return to game.");
			StdDraw.text(0.5,0.60, "Press P to pause or unpause.");
			StdDraw.text(0.5,0.50, "CREW BONUS:");
			StdDraw.text(0.5,0.45, "Shield: Faster shield recharge time.");
			StdDraw.text(0.5,0.40, "Cockpit: Improve ship maneuvering.");
			StdDraw.text(0.5,0.35, "Weapon Control: Manual weapon control (bonus Energy).");
			StdDraw.text(0.5,0.25, "OBJECTIF: DESTROY THE ENEMY'S VOID CRUISER.");
			StdDraw.setPenRadius();	
			StdDraw.setPenColor();
		}
	}
	
	public void pause() {
		
	}
	
	public void addLog(String s) {
		if(systemLog.size()<5) {
			systemLog.add(s);
		}else {
			systemLog.remove(0);
			addLog(s);
		}
	}
	
	public void writeLog() {
		StdDraw.picture(0.82, 0.105, "screen.png",0.35,0.21);
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.text(0.7, 0.18, "Log:");
		StdDraw.setFont(new Font("NasalizationRg-Regular",0,13));
		int i = 0;

		for(String s: systemLog) {
			if(s!=null) {
				StdDraw.text(0.82, 0.15-i*0.025, "> "+s);
				i++;
			}
		}
		StdDraw.setPenColor();
		StdDraw.setFont(new Font("NasalizationRg-Regular",0,15));
	}
	
	public int selectShip() {
		boolean choosing = true;
		while(choosing) {
			StdDraw.picture(0.5, 0.5, "blackhole.png",1,1);
			StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
			StdDraw.text(0.5, 0.95, "SELECT SHIP");
			StdDraw.setPenColor();
			if(drawShip1(0.6,0.7)) { choosing=false ; return 1;}
			if(drawShip2(0.4,0.4)) { choosing=false ; return 2;}
			popup1(0.6,0.7);
			popup2(0.4,0.4);
			showMenu();

			
			StdDraw.show();
		}
		return 0;
	}
	
	private boolean drawShip1(double x, double y) {
		Double n =Math.random()*0.09+0.09;
		Double m= Math.random()*0.005;
		StdDraw.setPenColor(100,10,10);
		StdDraw.filledEllipse(x-0.1, y, n, m);
		StdDraw.filledEllipse(x-0.1, y+0.02, n/2, m/2);
		StdDraw.filledEllipse(x-0.1, y-0.02, n/2, m/2);
		StdDraw.setPenColor();
		StdDraw.picture(x,y, "F5S1.png",0.25,0.125);
//		StdDraw.setPenColor(StdDraw.WHITE);
//		StdDraw.rectangle(x, y, 0.08, 0.05);

		
		StdDraw.setPenColor();
		return (StdDraw.mouseX()>=(x-0.08) && StdDraw.mouseX()<=(x+0.08) &&
				StdDraw.mouseY()>=(y-0.05) && StdDraw.mouseY()<=(y+0.05) && StdDraw.isLeftClick());
	}
	
	private void popup1(double x, double y) {
		if(StdDraw.mouseX()>=(x-0.08) && StdDraw.mouseX()<=(x+0.08) &&
				StdDraw.mouseY()>=(y-0.05) && StdDraw.mouseY()<=(y+0.05)) {
			StdDraw.setPenColor(StdDraw.DARK_GRAY);
			StdDraw.filledRectangle(StdDraw.mouseX()-0.25, StdDraw.mouseY()+0.05, 0.2, 0.15);
			StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
			StdDraw.text(StdDraw.mouseX()-0.25, StdDraw.mouseY()+0.16, "ISS LONGSHOT");
			StdDraw.text(StdDraw.mouseX()-0.25, StdDraw.mouseY()+0.12, "Fast and agile scoutship.");
			StdDraw.text(StdDraw.mouseX()-0.25, StdDraw.mouseY()+0.08, "Operating mainly behind");
			StdDraw.text(StdDraw.mouseX()-0.25, StdDraw.mouseY()+0.06, "enemy's line.");
			StdDraw.text(StdDraw.mouseX()-0.25, StdDraw.mouseY()+0.02, "Ability: Void Reactor.");
			
			StdDraw.setPenRadius(0.005);
			StdDraw.rectangle(StdDraw.mouseX()-0.25, StdDraw.mouseY()+0.05, 0.2, 0.15);
			StdDraw.setPenColor();
			StdDraw.setPenRadius();
		}
	}
	
	private boolean drawShip2(double x, double y) {
		StdDraw.picture(x-0.013,y-0.003, "striker.png",0.32,0.32);
		Double n =Math.random()*0.02+0.004;
		Double m= Math.random()*0.01;
		StdDraw.setPenColor(150,0,0);
		StdDraw.filledEllipse(x-0.1, y, n, m/2);
		StdDraw.setPenColor();
		return (StdDraw.mouseX()>=(x-0.08) && StdDraw.mouseX()<=(x+0.08) &&
				StdDraw.mouseY()>=(y-0.05) && StdDraw.mouseY()<=(y+0.05) && StdDraw.isLeftClick());
	}
	
	private void popup2(double x , double y) {
		if(StdDraw.mouseX()>=(x-0.08) && StdDraw.mouseX()<=(x+0.08) &&
				StdDraw.mouseY()>=(y-0.05) && StdDraw.mouseY()<=(y+0.05)) {
			StdDraw.setPenColor(StdDraw.DARK_GRAY);
			StdDraw.filledRectangle(StdDraw.mouseX()+0.25, StdDraw.mouseY(), 0.2, 0.15);
			StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
			StdDraw.text(StdDraw.mouseX()+0.25, StdDraw.mouseY()+0.1, "STINGRAY");
			StdDraw.text(StdDraw.mouseX()+0.25, StdDraw.mouseY()+0.08, "Pirate corvette.");
			StdDraw.text(StdDraw.mouseX()+0.25, StdDraw.mouseY()+0.04, "All rounded striker ship");
			StdDraw.text(StdDraw.mouseX()+0.25, StdDraw.mouseY(), "Ability: Overcharge.");
			StdDraw.setPenRadius(0.005);
			StdDraw.rectangle(StdDraw.mouseX()+0.25, StdDraw.mouseY(), 0.2, 0.15);
			StdDraw.setPenColor();
			StdDraw.setPenRadius();
		}
	}
	
	public void gameOver(int enemyDestroyed) {
		StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
		StdDraw.filledRectangle(0.5, 0.5, 0.45, 0.45);
		StdDraw.setPenColor(150,0,0);
		StdDraw.setPenRadius(0.005);
		StdDraw.rectangle(0.5, 0.5, 0.45, 0.45);
		StdDraw.text(0.5, 0.6, "GAME OVER.");
		StdDraw.text(0.5,0.45, enemyDestroyed+" enemies destroyed.");
		StdDraw.setPenRadius();	
		StdDraw.setPenColor();
	}
	
	public void victory() {
		StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
		StdDraw.filledRectangle(0.5, 0.5, 0.45, 0.45);
		StdDraw.setPenColor(150,0,0);
		StdDraw.setPenRadius(0.005);
		StdDraw.rectangle(0.5, 0.5, 0.45, 0.45);
		StdDraw.text(0.5, 0.6, "CONGRATULATION");
		StdDraw.text(0.5,0.45, "You have destroyed the enemy's flag ship.");
		StdDraw.setPenRadius();	
		StdDraw.setPenColor();
	}
	
	public boolean updateShip(int upgradepts, String moduleName, int randWeapon) {
		StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
		StdDraw.filledRectangle(0.5, 0.5, 0.45, 0.45);
		StdDraw.setPenColor(150,0,0);
		StdDraw.setPenRadius(0.005);
		StdDraw.rectangle(0.5, 0.5, 0.45, 0.45);
		StdDraw.text(0.5, 0.8, "ENEMY DESTROYED");
		StdDraw.text(0.5, 0.6, "Select an upgrade ("+ upgradepts+" points available):");
		StdDraw.text(0.5, 0.5, "Press H to heal for 20% of max Health");
		StdDraw.text(0.5, 0.45, "Press K to upgrade Reactor.");
		StdDraw.text(0.5, 0.4, "Press U to upgrade "+moduleName+" max Energy");
		String w="";
		switch (randWeapon) {
		case 1: w="Minigun"; break;
		case 2: w="Thermal Lance"; break;
		case 3: w="Missile Laucher"; break;
		case 4: w="Ion Gun"; break;
		}
		StdDraw.text(0.5, 0.35, "Press O to get "+w);
		StdDraw.text(0.5, 0.2, "Press SPACE to continue.");
		if(StdDraw.isKeyPressed(32)) return true;
		StdDraw.setPenRadius();	
		StdDraw.setPenColor();
		return false;
	}
	
	
}
