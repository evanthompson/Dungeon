package dungeonGame;

// Generic object found within the environment of the game board
public abstract class GameObject {
	private int xpos;
	private int ypos;
	protected final int SIZE = 50;
	
	public GameObject() {
		setXpos(0);
		setYpos(0);
	}
	
	public GameObject(int x, int y) {
		setXpos(x);
		setYpos(y);
	}
	
	// Set Methods
	public void setXpos(int x) { xpos = x; }
	public void setYpos(int y) { ypos = y; }
	
	// Get Methods
	public int getXpos() { return xpos; }
	public int getYpos() { return ypos; }
	
	public String toString() {
		return "(" + xpos + "," + ypos + ")";
	}
}