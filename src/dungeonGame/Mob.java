package dungeonGame;

// Mob class: things the hero kills for experience and loot
public class Mob extends AnimateObject {
	private int experience;
	private int booty;
	
	public Mob() {
		super(0, 0, 50);
		setExperience((int)(Math.random()*100));
		setBooty((int)(Math.random()*100));
	}
	
	public Mob(int x, int y, int h) {
		super(x, y, h);
		setExperience((int)(Math.random()*100));
		setBooty((int)(Math.random()*100));
	}
	
	// Set Methods
	public void setExperience(int exp) { experience = exp; }
	public void setBooty(int booty) { this.booty = booty; }
	
	// Get Methods
	public int getExperience() { return experience; }
	public int getBooty() { return booty; }
}
