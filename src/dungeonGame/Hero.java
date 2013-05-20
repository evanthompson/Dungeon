package dungeonGame;

// Unique object for each game, spans more than a single floor
public class Hero extends AnimateObject {
	private int lvlExperience;
	private int experience;
	private int booty;
	private int cLevel;
	
	public Hero() {
		super(0, 0, 100);
		init();
	}
	
	public Hero(int x, int y, int h) {
		super(x, y, h);
		init();
	}
	
	private void init() {
		setExperience(0);
		setBooty(0);
		setLevel(1);
		setLvlExperience(100);
	}
	
	public void addExp(int mobExp) {
		experience += mobExp;
		if(experience >= lvlExperience) {
			lvlUp();
		}
	}
	public void addBooty(int mobBooty) {
		booty += mobBooty;
	}
	
	// Increase experience required for next level
	// Subtracts current level's experience from new level's
	// Increments hero's level by 1
	private void lvlUp() {
		cLevel++;
		experience -= lvlExperience;
		lvlExperience += lvlExperience;
		setMaxHealth(super.getMaxHealth() + 10);
		setCurrHealth(super.getMaxHealth());
		setStrength(super.getStrength() + 1);
		System.out.println("You reached level " + cLevel);
	}

	// Set Methods
	private void setLvlExperience(int exp) { lvlExperience = exp; }
	private void setExperience(int exp) { experience = exp; }
	private void setBooty(int booty) { this.booty = booty; }
	private void setLevel(int lvl) { cLevel = lvl;	}
	
	// Get Methods
	public int getlvlExperience() { return lvlExperience; }
	public int getExperience() { return experience; }
	public int getBooty() { return booty; }
	public int getLevel() { return cLevel; }
	
}