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
	private Hero hero;
	
	// Constructor - initiate lists and place objects
	public DungeonFloor() {
		objects = new ArrayList<GameObject>(20);
		rocks = new ArrayList<Obstacle>();
		enemies = new ArrayList<Mob>();
		
		//place(Stair.class, 2);
		place(Obstacle.class, 6);
		place(Mob.class, 2);
	}
	
	// Creates multiple objects of a certain type and places them
	// randomly within the bounds of the board.
	public void place(Class<? extends GameObject> objectType, int amount) {
		int randomY, randomX;
		int multiplier = (int)(MAP_HEIGHT / UNIT_SIZE);
		
		for(int i = 0; i < amount; i++) {
			
			int count = 0;
			do {
				randomY = Math.min((int)(Math.random() * multiplier) * UNIT_SIZE, MAP_HEIGHT - UNIT_SIZE);
				randomX = Math.min((int)(Math.random() * multiplier) * UNIT_SIZE, MAP_WIDTH - UNIT_SIZE);
				if(!anyOverlapAt(new Point(randomX, randomY))) {
					break;
				}
				count++;
			} while(count < 15);
			
			GameObject newObject;
			if(objectType == Obstacle.class) {
				newObject = new Obstacle(randomX, randomY);
				rocks.add((Obstacle) newObject);
				objects.add(newObject);
			}
			else if(objectType == Mob.class) {
				newObject = new Mob(randomX, randomY, 50);
				enemies.add((Mob) newObject);
				objects.add(newObject);
			}
			else if(objectType == Hero.class) {
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
				if(newObject instanceof Obstacle) {
					rocks.add((Obstacle) newObject);
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
		
	public boolean overlapAt(GameObject obj, Point p) {
		Rectangle rect = new Rectangle(obj.getXpos(), obj.getYpos(), UNIT_SIZE, UNIT_SIZE);
		if(rect.contains(p)) {
			return true;
		}
		return false;
	}
	
	// Runs the Point p against all object, checking for overlap
	public boolean anyOverlapAt(Point p) {
		Rectangle rect;
		for(GameObject obj : objects) {
			rect = new Rectangle(obj.getXpos(), obj.getYpos(), UNIT_SIZE, UNIT_SIZE);
			if(rect.contains(p)) {
				System.out.println("overlap at: " + obj.toString());
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
				System.out.print(waste.getClass().getSimpleName() + " is being removed from..");
				if(enemies.remove(w)) { System.out.print(" Enemies"); }
				if(rocks.remove(w)) { System.out.print(" Rocks"); }
				if(objects.remove(w)) { System.out.print(" GameObjects"); }
				System.out.println();
			}
		}
	}
	
	// Get Methods
	public ArrayList<Obstacle> getRocks() { return rocks; }
	public ArrayList<Mob> getEnemies() { return enemies; }
	public ArrayList<GameObject> getObjects() { return objects; }
	public Hero getHero() { return hero; }
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
