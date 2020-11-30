package main;
import java.util.Collection;

import display.StdDraw;
import display.Vector2;
import module.Module;
import ship.DemoShip2;
import ship.Ship;
import ship.fastShip;
import ship.voidCruiser;
import ship.voidDestroyer;
import ship.voidMinion;
import weapon.Projectile;

/**
 * The world contains the ships and draws them to screen.
 */
public class World {
	
	private Bindings 	bind;	// The bindings of the game.
	private long 		time;	// The current time 
	private boolean isPaused;
	Ship player;				// The ship of the player
	Ship opponent;				// The ship of the opponent
	int enemyCount;
	int upgradePts;
	/**
	 * Creates the world with the bindings, the player ship
	 * and the opponent ship.
	 */
	public World() {
		bind = new Bindings(this);
		player = new DemoShip2(true, new Vector2<Double>(0.35, 0.55));
		opponent = new voidMinion(false, new Vector2<Double>(0.82, 0.57),enemyCount);
		time = System.currentTimeMillis();
		isPaused=false;
		enemyCount=0;
		upgradePts=2;
	}
	

	public boolean isPaused() {
		return isPaused;
	}


	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}


	public void initShip(int i) {
		switch(i) {
		case 1: player = new fastShip(true, new Vector2<Double>(0.35, 0.55)); break;
		case 2: player = new DemoShip2(true, new Vector2<Double>(0.35, 0.55)); break;
		}
	}
	
	
	/**
	 * Processes the key pressed.
	 */
	public void processKey(){
		this.bind.processKey();
	}
	
	/**
	 * Makes a step in the world.
	 */
	public void step() {
		if(!isPaused) {
			player.step(((double) (System.currentTimeMillis()-time))/1000);
			opponent.step(((double) (System.currentTimeMillis()-time))/1000);
		
			opponent.ai(player);
		
			processHit(player.getProjectiles(), true);
			processHit(opponent.getProjectiles(), false);
		}
		time = System.currentTimeMillis();
	}
	
	
	/**
	 * TODO
	 * Processes the projectiles hit
	 * @param projectiles collection of projectiles to check for hit
	 * @param isPlayer whether the own of the projectiles is the player
	 */
	private void processHit(Collection<Projectile> projectiles, boolean isPlayer) {
		for(Projectile p : projectiles) {
			if(p.isHit() && !p.DidDamage()) {
				if(p.isPlayer()) {
					opponent.applyDamage(p);
				}
				else {
					player.applyDamage(p);
				}
				p.setDidDamage(true);
			}
		}
	}
	
	public void clearProjectile() {
		player.getProjectiles().clear();
		opponent.getProjectiles().clear();
	}
	
	/**
	 * Draws the ships and HUDs.
	 */
	public void draw() {
//		StdDraw.picture(0.5, 0.5, "blackhole.png",1,0.6);
		player.drawHUD();		
		opponent.drawHUD();
		opponent.draw();
		player.draw();
		opponent.drawProjectiles();
		player.drawProjectiles();

	}
	
	public Module randMod() {
		int i = (int) Math.random()*4+1;
		return player.getModules()[i];
	}
	
}
