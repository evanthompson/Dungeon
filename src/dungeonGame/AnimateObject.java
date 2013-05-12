package dungeonGame;

import java.awt.Point;

import dungeonGame.DungeonGame.Compass;

public class AnimateObject extends GameObject {
	protected Compass direction;
	protected Point crosshair;
	
	protected int maxHealth, currHealth;
	protected int strength, stride, reach;
	
	// Constructor
	public AnimateObject() {
		super();
		maxHealth = currHealth = 1;
		setStats();
		initDirection();
	}
	
	public AnimateObject(int x, int y, int h) {
		super(x, y);
		maxHealth = currHealth = h;
		setStats();
		initDirection();
	}
	
	private void setStats() {
		strength = 1;
		stride = 10;
		reach = 20;
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
		if(crosshair == null) {
			crosshair = new Point(0, 0);
		}
		switch(dir) {
			case NORTH:	crosshair.x = getPos().x + (SIZE / 2);
						crosshair.y = getPos().y - reach; break;
			case EAST:	crosshair.x = getPos().x + SIZE + reach;
						crosshair.y = getPos().y + (SIZE / 2); break;
			case WEST:	crosshair.x = getPos().x - reach;
						crosshair.y = getPos().y + (SIZE / 2); break;
			case SOUTH:	crosshair.x = getPos().x + (SIZE / 2);
						crosshair.y = getPos().y + SIZE + reach; break;
			default:	crosshair.x = getPos().x + (SIZE / 2);
						crosshair.y = getPos().y + SIZE + reach; break; // South
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
