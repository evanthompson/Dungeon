package DungeonPack;

import java.util.ArrayList;

import DungeonPack.DungeonMain.Compass;

// Contains floor related information, including:
// dimensions of map, rock and mob lists
public class DungeonFloor {
	public final int 	MAP_HEIGHT = 500;
	public final int 	MAP_WIDTH = 500;
	public final int	UNIT_SIZE = 50;
	
	private ArrayList<GameObject> objects; // keeps track of all 'units' on THIS floor
	private ArrayList<InanimateObject> rocks;
	private ArrayList<Mob> enemies;
	private Hero hero;
	
	// Constructor - initiate lists and place objects
	public DungeonFloor() {
		objects = new ArrayList<GameObject>(20);
		rocks = new ArrayList<InanimateObject>(10);
		enemies = new ArrayList<Mob>(10);
		
		place(InanimateObject.class, 2);	// Place 10 rocks
		place(Mob.class, 2);	// Place two mobs
		place(Hero.class, 1);
	}
	
	// Creates multiple objects of a certain type and places them
	// randomly within the bounds of the board.
	public void place(Class<? extends GameObject> objectType, int amount) {
		int randomY, randomX;
		int multiplier = (int)(MAP_HEIGHT / UNIT_SIZE);
		
		for(int i = 0; i < amount; i++) {
			
			while(true) {
				randomY = Math.min((int)(Math.random() * multiplier) * UNIT_SIZE, MAP_HEIGHT - UNIT_SIZE);
				randomX = Math.min((int)(Math.random() * multiplier) * UNIT_SIZE, MAP_WIDTH - UNIT_SIZE);
				if(checkPosition(randomY, randomX) == false) {
					break;
				}
			}
			
			GameObject newObject;
			if(objectType == InanimateObject.class) {
				//System.out.println("objectType is Inanimate");
				newObject = new InanimateObject(randomX, randomY);
				rocks.add((InanimateObject) newObject);
				objects.add(newObject);
			}
			else if(objectType == Mob.class) {
				//System.out.println("objectType is a Mob");
				newObject = new Mob(randomX, randomY, 50);
				enemies.add((Mob) newObject);
				objects.add(newObject);
			}
			else if(objectType == Hero.class) {
				System.out.println("Placed the hero on this floor");
				newObject = new Hero(randomX, randomY);
				objects.add(newObject);
				hero = (Hero) newObject;
			}
			else { System.out.println("Invalid gameObject type passed to DungeonFloor.place(...)"); }
			
			/*** Alternate implementation for initialization of dynamic types ***
			try {
				newObject = (GameObject) Class.forName(objectType.getName()).newInstance();
				System.out.println("created new GameObject of Type: " + newObject.getClass().getSimpleName());
				newObject.setXpos(randomX);
				newObject.setYpos(randomY);
				objects.add(newObject);
				
				if(newObject instanceof Mob) {
					enemies.add((Mob) newObject);
				}
				if(newObject instanceof InanimateObject) {
					rocks.add((InanimateObject) newObject);
				}
				
			} catch (InstantiationException e) {
				System.out.println("exception 1 " + e);
			} catch (IllegalAccessException e) {
				System.out.println("exception 2 " + e);
			} catch (ClassNotFoundException e) {
				System.out.println("exception 3 " + e);
			}
			*/
			
		}
	}
	
	public void moveWest(AnimateObject mover) {
		int lower = mover.getYpos();
		int upper = lower + UNIT_SIZE;
		int step = 10;
		
		// Check all objects for a possible collision with hero
		for(GameObject g : getObjects()) {
			// Is there horizontal overlap?
			if((mover.getXpos() - step) <= g.getXpos() + UNIT_SIZE) {
				// Is there vertical overlap?
				if((lower >= g.getYpos() && lower < g.getYpos() + UNIT_SIZE) || 
						(upper > g.getYpos() && upper <= g.getYpos() + UNIT_SIZE)) {
					// there should be a collision -> move as close as possible
					step = Math.min(step, Math.abs(mover.getXpos() - (g.getXpos() + UNIT_SIZE)));
					if(step == 0) { break; }
				}
			}
		}
		mover.setXpos(Math.max(0, mover.getXpos() - step));
		mover.setDirection(Compass.WEST);
	}
	
	public void moveEast(AnimateObject mover) {
		int lower = mover.getYpos();
		int upper = lower + UNIT_SIZE;
		int step = 10;
		
		for(GameObject g : getObjects()) {
			// Is there horizontal overlap?
			if((mover.getXpos() + UNIT_SIZE + step) >= g.getXpos()) {
				// Is there vertical overlap?
				if((lower >= g.getYpos() && lower < g.getYpos() + UNIT_SIZE) || 
						(upper > g.getYpos() && upper <= g.getYpos() + UNIT_SIZE)) {
					// there should be a collision -> move as close as possible
					step = Math.min(step, Math.abs((mover.getXpos() + UNIT_SIZE) - g.getXpos()));
					if(step == 0) { break; }
				}
			}
		}
		mover.setXpos(Math.min(MAP_WIDTH - UNIT_SIZE, mover.getXpos() + step));
		mover.setDirection(Compass.EAST);
	}
	
	public void moveNorth(AnimateObject mover) {
		int lower = mover.getXpos();
		int upper = lower + UNIT_SIZE;
		int step = 10;
		
		for(GameObject g : getObjects()) {
			// Is there vertical overlap?
			if((mover.getYpos() - step) <= g.getYpos() + UNIT_SIZE) {
				// Is there vertical overlap?
				if((lower >= g.getXpos() && lower < g.getXpos() + UNIT_SIZE) || 
						(upper > g.getXpos() && upper <= g.getXpos() + UNIT_SIZE)) {
					// there should be a collision -> move as close as possible
					step = Math.min(step, Math.abs(mover.getYpos() - (g.getYpos() + UNIT_SIZE)));
					if(step == 0) { break; }
				}
			}
		}
		mover.setYpos(Math.max(0, mover.getYpos() - step));
		mover.setDirection(Compass.NORTH);
	}
	
	public void moveSouth(AnimateObject mover) {
		int lower = mover.getXpos();
		int upper = lower + UNIT_SIZE;
		int step = 10;
		
		for(GameObject g : getObjects()) {
			// Is there vertical overlap?
			if((mover.getYpos() + UNIT_SIZE + step) >= g.getYpos()) {
				// Is there vertical overlap?
				if((lower >= g.getXpos() && lower < g.getXpos() + UNIT_SIZE) || 
						(upper > g.getXpos() && upper <= g.getXpos() + UNIT_SIZE)) {
					// there should be a collision -> move as close as possible
					step = Math.min(step, Math.abs((mover.getYpos() + UNIT_SIZE) - g.getYpos()));
					if(step == 0) { break; }
				}
			}
		}
		mover.setYpos(Math.min(MAP_HEIGHT - UNIT_SIZE, mover.getYpos() + step));
		mover.setDirection(Compass.SOUTH);
	}
	
	
	// Searches within the list of objects for an object with position
	// matching the x/y passed as parameters.
	public boolean checkPosition(int y, int x) {
		for(int i = 0; i < objects.size(); i++) {
			if(objects.get(i).getXpos() == x && objects.get(i).getYpos() == y) {
				System.out.println("overlap at: " + objects.get(i).toString());
				return true;
			}
		}
		
		return false;
	}
	
	// Removes mob from both lists in which it is contained.
	// This method is used for when a mob dies.
	public void removeObject(GameObject waste) {
		for(GameObject w : objects) {
			if(waste.equals(w)) {
				System.out.println("Object: " + waste.toString() + " is being removed..");
				if(objects.remove(w)) { System.out.println("Object removed from 'objects'"); }
				if(enemies.remove(w)) { System.out.println("Object removed from 'enemies'"); }
				if(rocks.remove(w)) { System.out.println("Object removed from 'rocks'"); }
			}
		}
	}
	
	// Get Methods
	public ArrayList<InanimateObject> getRocks() { return rocks; }
	public ArrayList<Mob> getEnemies() { return enemies; }
	public ArrayList<GameObject> getObjects() { return objects; }
	public Hero getHero() { return hero; }
}
