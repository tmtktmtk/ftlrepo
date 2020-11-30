

// Install Nasa Font in FTL folder

// Binome: TRUONG Khac Minh Tam et DROUET-CHEN Jonathan

// Groupe BX

// All neccessary images are included in the project.























package main;
import java.awt.Color;
import java.awt.Font;

import display.StdDraw;
import display.Vector2;
import ship.Tile;
import ship.voidCruiser;
import ship.voidDestroyer;
import ship.voidMinion;
import weapon.Ion;
import weapon.Laser;
import weapon.Minigun;
import weapon.Missile;
import weapon.Weapon;
import weapon.bustLaser;

/**
 * This class starts the game by creating the canvas
 * in which the game will be drawn in and the world as
 * well as the main loop of the game.
 */
public class Start {
	
	public static void main(String[] args) {
		// Creates the canvas of the game

		Color c = new Color(10,0,0);
		
		StdDraw.setCanvasSize(600, 600);

		Font f = new Font("NasalizationRg-Regular",0,15);
		StdDraw.setFont(f);
		
		// Enables double buffering to allow animation
		StdDraw.enableDoubleBuffering();
		
		//Info
		GameOption go= new GameOption();		
		int s = go.selectShip();
		// Creates the world
		World w = new World();
		w.initShip(s);
		// Game infinite loop
		while(true) {
			
			// Clears the canvas of the previous frame
			StdDraw.clear(c);
			
			StdDraw.picture(0.5, 0.5, "blackhole.png",1,1);
			

			w.setPaused(go.isGamePause());
			// Processes the key pressed during the last frame
			w.processKey();
			
			// Makes a step of the world
			w.step();

			// Draws the world to the canvas
			w.draw();

			//show Info
			go.showMenu();

			if(w.player.getCurrentHull()==0) go.gameOver(w.enemyCount);
			if(w.opponent.getCurrentHull()==0) {
				go.setGamePause(true);
				w.clearProjectile();
				if(go.boss) go.victory();

				if(StdDraw.isKeyPressed(72) && w.upgradePts>0) {
					w.player.setCurrentHull(((w.player.getTotalHull()<=w.player.getCurrentHull()+w.player.getTotalHull()*2/10)?w.player.getTotalHull():(w.player.getCurrentHull()+w.player.getTotalHull()*2/10)));
					w.upgradePts--;
				}
				if(StdDraw.isKeyPressed(85) && w.upgradePts>0){
					int lvl= w.player.getModules()[go.mod2update].getCurrentLevel();
					if(!((w.player.getModules()[go.mod2update].getCurrentLevel()+1)>(w.player.getModules()[go.mod2update].getMaxLevel()))) {
						w.player.getModules()[go.mod2update].setCurrentLevel(lvl+1);
						w.upgradePts--;
					}
				}
				if(StdDraw.isKeyPressed(75) && w.upgradePts>0){
					int lvl= w.player.getModules()[0].getCurrentLevel();
					if(!((w.player.getModules()[0].getCurrentLevel()+1)>(w.player.getModules()[0].getMaxLevel()))) {
						w.player.getModules()[0].setCurrentLevel(lvl+1);
						w.player.getModules()[0].setAllocatedEnergy(w.player.getModules()[0].getAllocatedEnergy()+1);
						w.upgradePts--;
					}
				}
				
				if(StdDraw.isKeyPressed(79) && w.upgradePts>0){
					Weapon a=null;
					switch(go.randWeapon) {
					case 1:a = new Minigun(); break;
					case 2: a = new Laser(); break;
					case 3: a = new Missile(); break;
					case 4: a = new Ion();break;
					}
					
					if(a!=null && w.player.getWeaponControl().addWeapon(a)) {
						for(Tile t:w.player.weaponTile) {
							if(t!=null) {
								t.setWeapon(a);
								w.player.weaponTile.remove(t);	
								w.upgradePts--;
							}
						} 
					}else System.out.println("Max weapon capacity.");
				}
				
				if(!go.boss && go.updateShip(w.upgradePts,w.player.getModules()[go.mod2update].getName(),go.randWeapon)) {
					if(w.enemyCount<3) {
						w.opponent = new voidMinion(false, new Vector2<Double>(0.82, 0.57),w.enemyCount);
					} else if(w.enemyCount<5) w.opponent = new voidDestroyer(false, new Vector2<Double>(0.82, 0.57),w.enemyCount);
					else {
						w.opponent = new voidCruiser(false, new Vector2<Double>(0.82, 0.57),w.enemyCount);
						go.boss=true;
					}
					w.upgradePts+=2;
					w.enemyCount++;
					go.mod2update=(int)(Math.random()*3+1);
					go.randWeapon=(int)(Math.random()*4+1);
				}

			}
			
			
			// Shows the canvas to screen
			StdDraw.show();
			
			// Waits for 20 milliseconds before drawing next frame.
			StdDraw.pause(20);
		}
		
	}

}
