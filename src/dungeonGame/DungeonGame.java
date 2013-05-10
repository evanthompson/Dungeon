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
	
	public DungeonGame() {
		
		level = new DungeonFloor();
		System.out.print("Rocks...");
		for(InanimateObject inan : level.getRocks()) {
			System.out.print(inan.toString());
		}
		System.out.println();
		
		System.out.print("Mobs...");
		for(Mob m : level.getEnemies()) {
			System.out.print(m.toString());
		}
		System.out.println();
		
		// Hero Generation
		hero = level.getHero();
	}
	
	public void beginGame() {}
	
	public void updateGame() {
		setChanged();
		notifyObservers();
	}
	
	public void move(AnimateObject mover, int xFactor, int yFactor) {
		int xStep = xFactor * mover.getStride();
		int yStep = yFactor * mover.getStride();
		int xCheck = mover.getXpos() + xStep;
		int yCheck = mover.getYpos() + yStep;
		if(xFactor > 0) { xCheck += mover.SIZE; }
		if(yFactor > 0) { yCheck += mover.SIZE; }
		
		int xpos = mover.getXpos();
		int ypos = mover.getYpos();
		
		Point upperPoint = new Point(xCheck + Math.abs(yFactor)*(mover.SIZE-1), 
				yCheck + Math.abs(xFactor)*(mover.SIZE-1));
		for(GameObject obj : level.getObjects()) {
			int objX = obj.getXpos();
			int objY = obj.getYpos();
			if(level.overlapAt(obj, new Point(xCheck, yCheck))) {
				xStep = xFactor * Math.max(0, Math.min(Math.abs(xStep), Math.abs(xpos - objX) - mover.SIZE));
				yStep = yFactor * Math.max(0, Math.min(Math.abs(yStep), Math.abs(ypos - objY) - mover.SIZE));
			}
			if(level.overlapAt(obj, upperPoint)) {
				xStep = xFactor * Math.max(0, Math.min(Math.abs(xStep), Math.abs(xpos - objX) - mover.SIZE));
				yStep = yFactor * Math.max(0, Math.min(Math.abs(yStep), Math.abs(ypos - objY) - mover.SIZE));
			}
		}
		mover.setXpos(Math.max(0, mover.getXpos() + xStep));
		mover.setYpos(Math.max(0, mover.getYpos() + yStep));
		int dirNum = (2 * (yFactor + 1)) + xFactor;
		if(dirNum >= 3) dirNum--;
		mover.setDirection(Compass.values()[dirNum]);
		
		updateGame();
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
			System.out.println("Hero hit " + m.getClass().getSimpleName() + " for " + hero.getStrength() * 10);
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
