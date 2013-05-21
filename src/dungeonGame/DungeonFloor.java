package dungeonGame;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

// Contains floor related information, including:
// dimensions of map, rock and mob lists
public class DungeonFloor {
	public final int 	MAP_HEIGHT = 500;
	public final int 	MAP_WIDTH = 500;
	public final int	UNIT_SIZE = 50;
	
	private ArrayList<GameObject> objects; // keeps track of all 'units' on THIS floor
	private ArrayList<Obstacle> rocks;
	private ArrayList<Mob> enemies;
	private ArrayList<Stair> stairs;
	
	private int level;
	
	// Constructor - initiate lists and place objects
	public DungeonFloor(int level) {
		this.level = level;
		objects = new ArrayList<GameObject>(20);
		rocks = new ArrayList<Obstacle>();
		enemies = new ArrayList<Mob>();
		stairs = new ArrayList<Stair>(5);
		
		populate();
	}
	
	public void populate() {
		if(level > 0) {
			placeObjects(Stair.class, 2);
			stairs.get(1).setDescent(false);
		} else {
			placeObjects(Stair.class, 1);
		}
		stairs.get(0).setDescent(true);
		
		placeObjects(Obstacle.class, 6);
		placeObjects(Mob.class, 2);
	}
	
	public void placeObjects(Class<? extends GameObject> objectType, int amount) {
		Point newPoint;
		for(int i = 0; i < amount; i++) {
			newPoint = findFreePoint();
			if(newPoint == null) {
				System.out.println("findFreePoint returned null..");
				return;
			}
			
			GameObject newObject;
			try {
				newObject = (GameObject) Class.forName(objectType.getName()).newInstance();
				newObject.setPosition(newPoint);
				organizeObject(newObject);
				
			} catch (InstantiationException e) {
				System.out.println("InstantiationException");
			} catch (IllegalAccessException e) {
				System.out.println("IllegalAccessException");
			} catch (ClassNotFoundException e) {
				System.out.println("ClassNotFoundException");
			}
		}
	}
	
	// If successful, returns a Point. Otherwise, returns null.
	public Point findFreePoint() {
		int randomY, randomX;
		int multiplier = (int)(MAP_HEIGHT / UNIT_SIZE);

		int count = 0;
		while(count < 15) {
			
			randomY = Math.min((int)(Math.random() * multiplier) * UNIT_SIZE, MAP_HEIGHT - UNIT_SIZE);
			randomX = Math.min((int)(Math.random() * multiplier) * UNIT_SIZE, MAP_WIDTH - UNIT_SIZE);
			if(!anyOverlapAt(new Point(randomX, randomY))) {
				return new Point(randomX, randomY);
			}
			count++;
		}
		return null;
	}
	
	// Adds the given object to all relevant lists
	public void organizeObject(GameObject obj) {
		objects.add(obj);
		
		if(obj instanceof Mob) { enemies.add((Mob) obj); }
		if(obj instanceof Obstacle) { rocks.add((Obstacle) obj); }
		if(obj instanceof Stair) { stairs.add((Stair) obj); }
	}
		
	public boolean overlapAt(GameObject obj, Point p) {
		Rectangle rect = new Rectangle(obj.getPos().x, obj.getPos().y, UNIT_SIZE, UNIT_SIZE);
		if(rect.contains(p)) {
			return true;
		}
		return false;
	}
	
	// Runs the Point p against all object, checking for overlap
	public boolean anyOverlapAt(Point p) {
		Rectangle rect;
		for(GameObject obj : objects) {
			rect = new Rectangle(obj.getPos().x, obj.getPos().y, UNIT_SIZE, UNIT_SIZE);
			if(rect.contains(p)) {
				return true;
			}
		}
		return false;
	}
	
	// Removes mob from both lists in which it is contained.
	// This method is used for when a mob dies.
	public void removeObject(GameObject waste) {
		ArrayList<GameObject> forRemoval = new ArrayList<GameObject>();
		for(GameObject obj : objects) {
			if(waste.equals(obj)) {
				forRemoval.add(obj);
			}
		}
		removeObjects(forRemoval);
	}
	
	public void removeObjects(ArrayList<GameObject> list) {
		System.out.print(list.get(0).getClass().getSimpleName() + " is being removed from..");
		if(enemies.removeAll(list)) { System.out.print(" Enemies"); }
		if(rocks.removeAll(list)) { System.out.print(" Rocks"); }
		if(objects.removeAll(list)) { System.out.print(" GameObjects"); }
		System.out.println();
	}
	
	// Get Methods
	public ArrayList<Obstacle> getRocks() { return rocks; }
	public ArrayList<Mob> getEnemies() { return enemies; }
	public ArrayList<GameObject> getObjects() { return objects; }
	public ArrayList<Stair> getStairs() { return stairs; }
	public int getMapHeight() { return MAP_HEIGHT; }
	public int getMapWidth() { return MAP_WIDTH; }
	
	// Prints
	public void printObjects() {
		System.out.print("Rocks...");
		for(Obstacle obst : getRocks()) {
			System.out.print(obst.toString());
		}
		System.out.println();
		
		System.out.print("Mobs...");
		for(Mob m : getEnemies()) {
			System.out.print(m.toString());
		}
		System.out.println();
	}
}
