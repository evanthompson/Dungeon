package dungeonGame;

import java.awt.Point;

import dungeonGame.DungeonGame.Compass;

public class AnimateObject extends GameObject {
	protected int maxHealth;
	protected int currHealth;
	protected int strength;
	protected Compass direction;
	protected int stride;
	protected int reach;
	protected Point crosshair;
	
	// Constructor
	public AnimateObject() {
		super(0, 0);
		maxHealth = currHealth = 1;
		strength = 1;
		stride = 10;
		reach = 20;
		initDirection();
	}
	
	public AnimateObject(int x, int y, int h) {
		super(x, y);
		maxHealth = currHealth = h;
		strength = 1;
		stride = 10;
		reach = 20;
		initDirection();
	}
	
	private void initDirection() {
		// Choose random starting direction
		setDirection(Compass.values()[(int)(Math.random() * 3)]);
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
	public void setDirection(Compass dir) {
		direction = dir;
		setCrosshair(dir);
	}
	public void setCrosshair(Compass dir) {
		switch(dir) {
			case NORTH:	crosshair = new Point(getXpos() + (SIZE / 2), getYpos() - reach); break;
			case EAST:	crosshair = new Point(getXpos() + SIZE + reach, getYpos() + (SIZE / 2)); break;
			case SOUTH:	crosshair = new Point(getXpos() + (SIZE / 2), getYpos() + SIZE + reach); break;
			case WEST:	crosshair = new Point(getXpos() - reach, getYpos() + (SIZE / 2)); break;
			default:	crosshair = new Point(getXpos() + (SIZE / 2), getYpos() + SIZE + reach); // South
		}
	}
	
	public void setMaxHealth(int h) { maxHealth = h; }
	public void setCurrHealth(int h) { currHealth = h; }
	public void setStrength(int s) { strength = s; }
	public void setStride(int s) { stride = s; }
	
	// Get Methods
	public Compass getDirection() { return direction; }
	public Point getCrosshair() { return crosshair; }
	public int getMaxHealth() { return maxHealth; }
	public int getCurrHealth() { return currHealth; }
	public int getStrength() { return strength;	}
	public int getStride() { return stride;	}
}
