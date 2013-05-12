package dungeonGame;

import java.awt.Point;

// Generic object found within the environment of the game board
public abstract class GameObject {
	private Point position;
	protected final int SIZE = 50;
	
	public GameObject() {
		position = new Point(0, 0);
	}
	
	public GameObject(int x, int y) {
		position = new Point(x, y);
	}
	
	public GameObject(Point p) {
		position = p;
	}
	
	// Set Methods
	public void setPosition(Point p) {
		position = p;
	}
	public void setXpos(int x) { position.x = x; }
	public void setYpos(int y) { position.y = y; }
	
	// Get Methods
	public Point getPos() { return position; }
	
	public String toString() {
		return "(" + position.x + "," + position.y + ")";
	}
}