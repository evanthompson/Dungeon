package dungeonGame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Observable;

public class DungeonGame extends Observable {
	/* 
	Start Menu Options
	-- New Game, Load Game, Exit Game

	InGame Menu Options
	-- Pause / Resume, View statistics, Save & Exit
	
	InGame Environment
	-- key bindings: Movement, Open Menu
	-- Rules: 	Objects cannot overlap
				Pick up loot on contact
				Kill mobs on contact
				Random mob movement
				Contact with exit loads next floor
				Each Floor must be remembered
	 */
	
	public enum Compass { NORTH, WEST, EAST, SOUTH }
	private Hero hero;
	private DungeonFloor level;
	private ArrayList<DungeonFloor> dungeon;
	private int currLevel;
	
	public DungeonGame() {
		currLevel = 0;
		dungeon = new ArrayList<DungeonFloor>();
		dungeon.add(new DungeonFloor(currLevel));
		level = dungeon.get(currLevel);
		
		// Hero Generation
		Point heroStart = level.findFreePoint();
		if(heroStart != null) {
			hero = new Hero(heroStart.x, heroStart.y, 100);
			level.organizeObject(hero);
		}
	}
	
	public void beginGame() {}
	
	public void updateGame() {
		setChanged();
		notifyObservers();
	}
	
	public void move(AnimateObject mover, int xFactor, int yFactor) {
		int xStep = xFactor * mover.getStride();
		int yStep = yFactor * mover.getStride();
		int xCheck = mover.getPos().x + xStep;
		int yCheck = mover.getPos().y + yStep;
		if(xFactor > 0) { xCheck += mover.SIZE; }
		if(yFactor > 0) { yCheck += mover.SIZE; }
		int xpos = mover.getPos().x;
		int ypos = mover.getPos().y;
		
		Point upperPoint = new Point(xCheck + Math.abs(yFactor)*(mover.SIZE-1), 
				yCheck + Math.abs(xFactor)*(mover.SIZE-1));
		for(GameObject obj : level.getObjects()) {
			int objX = obj.getPos().x;
			int objY = obj.getPos().y;
			if(xStep == 0 && yStep == 0) { break; }
			if(level.overlapAt(obj, new Point(xCheck, yCheck)) || level.overlapAt(obj, upperPoint)) {
				// If obj instanceof stair --> go to vertical traversal
				if(obj instanceof Stair) {
					moveFloors((Stair) obj);
				}
				xStep = xFactor * Math.max(0, Math.min(Math.abs(xStep), Math.abs(xpos - objX) - mover.SIZE));
				yStep = yFactor * Math.max(0, Math.min(Math.abs(yStep), Math.abs(ypos - objY) - mover.SIZE));
			}
		}
		mover.setXpos(Math.min(level.getMapWidth() - mover.SIZE, Math.max(0, mover.getPos().x + xStep)));
		mover.setYpos(Math.min(level.getMapHeight() - mover.SIZE, Math.max(0, mover.getPos().y + yStep)));
		int dirNum = (2 * (yFactor + 1)) + xFactor;
		if(dirNum >= 3) dirNum--;
		mover.setDirection(Compass.values()[dirNum]);
		
		updateGame();
	}
	
	public void moveFloors(Stair stairs) {
		if(stairs.getDescent() == true) {
			currLevel++;
			if(dungeon.size() <= currLevel) {
				dungeon.add(new DungeonFloor(currLevel));
			}
			level = dungeon.get(currLevel);
			
		} else {
			if(currLevel > 0) {
				currLevel--;
				level = dungeon.get(currLevel);
			}
		}
	}
	
	public void attack() {
		ArrayList<Mob> targets = new ArrayList<Mob>();
		for(Mob m : level.getEnemies()) {
			if(level.overlapAt(m, hero.getCrosshair())) {
				targets.add(m);
			}
		}
		for(Mob m : targets) {
			m.damage(hero.getStrength() * 10);
			if(m.getCurrHealth() <= 0) {
				System.out.println("hero got " + m.getBooty() + " gil and " + m.getExperience() + " experience.");
				hero.addBooty(m.getBooty());
				hero.addExp(m.getExperience());
				level.removeObject(m);
			}
		}
		updateGame();
	}
	
	// Get Methods
	public Hero getHero() { return hero; }
	public DungeonFloor getFloor() { return level; }
}
