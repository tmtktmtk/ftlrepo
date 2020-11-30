package ship;
import java.util.ArrayList;
import java.util.Collection;

import display.StdDraw;
import display.Vector2;
import weapon.Weapon;

/**
 * A tile is a cell of the ship's layout.
 * A weapon can be attached to the tile and a crew member 
 * can be on the tile.
 */
public class Tile {
	
	private 		Weapon 			weapon;		// The weapon assigned to the tile
	protected 		int 			nbMaxCrew;
	protected 		CrewMember[] 	member;		// The crew member on the tile
	private 		boolean 		isAimed;	// Whether the tile aimed at
	private 		boolean 		isPlayer;	// Whether the tile is owned by the player
	protected final Vector2<Double> tilePos;	// The position of the tile
	protected  		double			DftSizeX;
	protected  		double			DftSizeY;
	private			boolean			isSelected;
	
	
	
	public double getDftSizeX() {
		return DftSizeX;
	}


	public double getDftSizeY() {
		return DftSizeY;
	}


	public boolean isPlayer() {
		return isPlayer;
	}


	/**
	 * Creates a tile for the player of the opponent
	 * which is drawn at the given position.
	 * @param position location to draw the tile
	 * @param isPlayer whether it is owned by the player ship
	 */
	public Tile(Vector2<Double> position, boolean isPlayer) {
		this.tilePos = position;
		this.isPlayer = isPlayer;
		this.DftSizeX =0.01;
		this.DftSizeY = 0.01;
		this.nbMaxCrew = 1;
		this.member=new CrewMember[nbMaxCrew];
	}
	
	public Tile(Vector2<Double> position, boolean isPlayer, double sizeX, double sizeY) {
		this.tilePos = position;
		this.isPlayer = isPlayer;
		this.DftSizeX = sizeX;
		this.DftSizeY = sizeY;
		this.nbMaxCrew= (int)((sizeX*sizeY)/(0.01*0.01));
		this.member=new CrewMember[nbMaxCrew];
	}
	
	/**
	 * Checks whether a crew member is inside the tile.
	 * @return whether the tile has a crew member
	 */
	public boolean hasfullCrewMember() {
		for(CrewMember m:member) {
			if(m==null) return false;
		} return true;

	}
	
	/**
	 * Sets the given crew member has inside the tile.
	 * @param member the crew member to put inside the tile
	 */
	public void setCrewMember(CrewMember newmember) {
		for(int i=0;i<member.length;++i)
			if(this.member[i]==null) {
				this.member[i] = newmember;
				break;
			}
	}
	
	public void unsetCrewMember(CrewMember newmember) {
		for(int i=0;i<member.length;++i)
			if(this.member[i]==newmember)
				this.member[i] = null;
	}	
	
	public CrewMember[] getMember() {
		return member;
	}



	/**
	 * Draws the tile, the member inside and the weapon.
	 */
	public void draw() {
		if (tilePos == null)
			return;
		double x = tilePos.getX();
		double y = tilePos.getY();
		ArrayList<Vector2<Double>> pos= listBasicTile();

		StdDraw.setPenRadius(0.001);
		for(Vector2<Double> p:pos) {
			StdDraw.setPenColor(StdDraw.WHITE);
			StdDraw.filledRectangle(p.getX(), p.getY(), 0.01, 0.01);
			StdDraw.setPenColor();		
			StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
			StdDraw.rectangle(p.getX(), p.getY(), 0.01, 0.01);
		}
		StdDraw.setPenColor();
		StdDraw.setPenRadius(0.004);
 
		if(isSelected) {
			StdDraw.setPenColor(StdDraw.YELLOW);
			StdDraw.filledRectangle(x, y, DftSizeX, DftSizeY);
			StdDraw.setPenColor(StdDraw.BLACK);
		}
		if (isAimed) {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.circle(x, y, Math.min(DftSizeX, DftSizeY));
			StdDraw.filledCircle(x, y, Math.min(DftSizeX, DftSizeY)/2);
			StdDraw.setPenColor(StdDraw.BLACK);
		}		
		StdDraw.rectangle(x, y, DftSizeX, DftSizeY);
		StdDraw.setPenRadius();
		if (weapon != null)
			if (isPlayer)
				drawWeaponHorizontal(x, y);
			else
				drawWeaponVertical(x, y);

		drawCrew();
	}
	
	
	public void drawCrew() {
		ArrayList<Vector2<Double>> pos= listBasicTile();
		for(int i=0;i<member.length;i++)
			if(member[i] != null)member[i].draw(pos.get(i));
	}
	
	public boolean isInTile(double x, double y) {
		return x >= this.tilePos.getX()-this.DftSizeX && x <= this.tilePos.getX()+this.DftSizeX && y >= this.tilePos.getY()-this.DftSizeY && y <= this.tilePos.getY()+this.DftSizeY;
	}
	
	public ArrayList<Vector2<Double>> listBasicTile(){
		ArrayList<Vector2<Double>> listpos= new ArrayList<Vector2<Double>>();

		for(double y = this.tilePos.getY()+this.DftSizeY-0.01; y>this.tilePos.getY()-this.DftSizeY;y-=0.02) {
			for(double x = this.tilePos.getX()-this.DftSizeX+0.01 ; x<this.tilePos.getX()+this.DftSizeX;x+=0.02) {
				if(isInTile(x,y)) 
					listpos.add(new Vector2<Double>(x,y));
			}
		}
		return listpos;
	}
	
	/**
	 * Draws a wall of the tile horizontally.
	 * @param x X start position
	 * @param y Y start position
	 */
//	private void drawHorizontalWall(double x, double y) {
//			StdDraw.line(x-0.005, y, x-0.015, y);
//			StdDraw.line(x, y, x-0.005, y);
//			StdDraw.line(x-0.015, y, x-0.02, y);
//	}
	
	/**
	 * Draws a wall of the tile vertically.
	 * @param x X start position
	 * @param y Y start position
	 */
//	private void drawVerticalWall(double x, double y) {
//			StdDraw.line(x, y+0.005, x, y+0.015);
//			StdDraw.line(x, y, x, y+0.005);
//			StdDraw.line(x, y+0.015, x, y+0.02);
//	}
	
	/**
	 * Draws the weapon of the tile horizontally.
	 * @param x X start position
	 * @param y Y start position
	 */
	private void drawWeaponHorizontal(double x, double y) {
		StdDraw.setPenColor(StdDraw.GRAY);
		StdDraw.filledRectangle(x+DftSizeX+0.005, y, 0.005, 0.0025);
		StdDraw.setPenColor(StdDraw.BLACK);
	}
	
	/**
	 * Draws the weapon of the tile vertically
	 * @param x X start position
	 * @param y Y start position
	 */
	private void drawWeaponVertical(double x, double y) {
		StdDraw.setPenColor(StdDraw.GRAY);
		StdDraw.filledRectangle(x, y+DftSizeY+0.005, 0.0025, 0.005);
		StdDraw.setPenColor(StdDraw.BLACK);
	}

	/**
	 * Assigns a weapon to the tile.
	 * @param w the weapon to assign
	 */
	public void setWeapon(Weapon w) {
		if (weapon == null)
			weapon = w;
	}

	/**
	 * Gives the assigned weapon.
	 * @return the weapon
	 */
	public Weapon getWeapon() {
		return weapon;
	}

	/**
	 * Gives the horizontal position of the weapon.
	 * @return the position
	 */
	private Vector2<Double> getWeaponHorizontalPosition() {
		return new Vector2<Double>(tilePos.getX(), tilePos.getY());
	}
	
	/**
	 * Gives the vertical position of the weapon.
	 * @return the position
	 */
	private Vector2<Double> getWeaponVerticalPosition() {
		return new Vector2<Double>(tilePos.getX(), tilePos.getY());
	}
	
	/**
	 * Gives the position of the weapon.
	 * @return the position
	 */
	public Vector2<Double> getWeaponPosition() {
		if (isPlayer)
			return getWeaponHorizontalPosition();
		else	
			return getWeaponVerticalPosition();
	}

	/**
	 * Gives the position of the tile.
	 * @return the position
	 */
	public Vector2<Double> getPosition() {
		return tilePos;
	}
	
	/**
	 * Gives the center position of the tile.
	 * @return the position
	 */
	public Vector2<Double> getCenterPosition() {
		return new Vector2<Double>(tilePos.getX(), tilePos.getY());
	}
	
	/**
	 * Marks the tile as targeted.
	 */
	public void markTarget() {
		isAimed = true;
	}
	
	/**
	 * Unmarks the tile as targeted.
	 */
	public void unmarkTarget() {
		isAimed = false;
	}
	
	/**
	 * Marks the tile as selected.
	 */
	public void markSelected() {
		isSelected = true;
	}
	
	/**
	 * Unmarks the tile as selected.
	 */
	public void unmarkSelected() {
		isSelected = false;
	}
	
	/**
	 * Checks whether the given crew member is the on in the tile.
	 * @param member the crew member to compare it to
	 * @return whether the crew member is the one in the tile
	 */
	public boolean isCrewMember(CrewMember newmember) {
		for(int i=0;i<member.length;++i)
			if(this.member[i] == newmember) return true;
		return false;
	}

	/**
	 * Removes the crew member of the tile.
	 */
	public void removeAllCrewMember() {
		member = null;
	}
	
}
