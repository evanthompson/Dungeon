package dungeonGame;

// Unique object for each game, spans more than a single floor
public class Hero extends AnimateObject {
	private int requiredExp;
	private int experience;
	private int booty;
	private int cLevel;
	private String name;
	
	public Hero() {
		super(0, 0, 100);
		init();
	}
	
	public Hero(int x, int y, int h) {
		super(x, y, h);
		init();
	}
	
	private void init() {
		name = "anon";
		setExperience(0);
		setBooty(0);
		setLevel(1);
		setRequiredExp(100);
	}
	
	public void addExp(int mobExp) {
		experience += mobExp;
		if(experience >= requiredExp) {
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
		experience -= requiredExp;
		requiredExp += requiredExp;
		setMaxHealth(super.getMaxHealth() + 10);
		setCurrHealth(super.getMaxHealth());
		setStrength(super.getStrength() + 1);
		System.out.println("You reached level " + cLevel);
	}

	// Set Methods
	private void setRequiredExp(int exp) { requiredExp = exp; }
	public void setExperience(int exp) { experience = exp; }
	public void setBooty(int booty) { this.booty = booty; }
	public void setLevel(int lvl) { cLevel = lvl; }
	public void setName(String newName) { name = newName; }
	
	// Get Methods
	public int getlvlExperience() { return requiredExp; }
	public int getExperience() { return experience; }
	public int getBooty() { return booty; }
	public int getLevel() { return cLevel; }
	public String getname() { return name; }
	
}