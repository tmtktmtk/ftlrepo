package ship;

import display.StdDraw;
import display.Vector2;

/**
 * A CrewMember is a character inside the ship.
 */
public class CrewMember {
	
	private String 	name;		// The name of the crew member
	private boolean isSelected; // Whether he/she is selected
	/**
	 * Creates a CrewMember.
	 * @param name the name the crew member
	 */
	public CrewMember(String name) {
		this.name = name;
	}

	/**
	 * Draws the CrewMember at the location provided.
	 * @param location where to draw the crew member
	 */
	public void draw(Vector2<Double> location) {
		if (isSelected) {
			StdDraw.setPenColor(StdDraw.GREEN);
			StdDraw.setPenRadius(0.005);
			StdDraw.circle(location.getX(), location.getY(), 0.0085);
			StdDraw.setPenColor();
			StdDraw.setPenRadius();
		}
		StdDraw.picture(location.getX(), location.getY(), "human.png",0.018,0.018);
	}
	
	/**
	 * Selects the crew member.
	 */
	public void select() {
		isSelected = true;
	}
	
	/**
	 * Unselects the crew member.
	 */
	public void unselect() {
		isSelected = false;
	}
	
	/**
	 * Gives the name of the crew member.
	 * @return the name of the crew member.
	 */
	public String getName() {
		return name;
	}

	public boolean isSelected() {
		return isSelected;
	}

	
	
	
}
