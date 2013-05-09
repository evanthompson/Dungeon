package dungeonGame;

import dungeonGame.DungeonGame.Compass;

public class AnimateObject extends GameObject {
	private int maxHealth;
	private int currHealth;
	private int strength;
	private Compass direction;
	private int stride;
	
	// Constructor
	public AnimateObject() {
		super(0, 0);
		maxHealth = currHealth = 1;
		strength = 0;
		stride = 10;
		initDirection();
	}
	
	public AnimateObject(int x, int y, int h) {
		super(x, y);
		maxHealth = currHealth = h;
		strength = 1;
		stride = 10;
		initDirection();
	}
	
	private void initDirection() {
		// Choose random starting direction
		// Used for when combat is implemented
		int i = (int)(Math.random() * 3);
		switch(i) {
			case 0:	direction = Compass.NORTH; break;
			case 1:	direction = Compass.SOUTH; break;
			case 2:	direction = Compass.EAST; break;
			case 3:	direction = Compass.WEST; break;
			default: System.out.println(this.getClass().getName() + 
					"start direction was not chosen..");
		}
	}
	
	public void addToMaxHealth(int h) {
		maxHealth += h;
	}
	public void subToMaxHealth(int h) {
		maxHealth -= h;
	}
	
	public void heal(int h) {
		if(currHealth + h > maxHealth) {
			currHealth = maxHealth;
		} else {
			currHealth += h;
		}
	}
	public void damage(int h) {
		if(currHealth - h < 0) {
			currHealth = 0;
		} else {
			currHealth -= h;
		}
	}
	
	// Set Methods
	public void setDirection(Compass dir) { direction = dir; }
	public void setMaxHealth(int h) { maxHealth = h; }
	public void setCurrHealth(int h) { currHealth = h; }
	public void setStrength(int s) { strength = s; }
	public void setStride(int s) { stride = s; }
	
	// Get Methods
	public Compass getDirection() { return direction; }
	public int getMaxHealth() { return maxHealth; }
	public int getCurrHealth() { return currHealth; }
	public int getStrength() { return strength;	}
	public int getStride() { return stride;	}
}
