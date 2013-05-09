package dungeonGame;

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
	
	public enum Compass { NORTH, SOUTH, EAST, WEST }
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
	
	public void desiredMove(Compass direction, AnimateObject mover) {
		switch(direction) {
			case WEST:	level.moveWest(mover);	break;
			case EAST:	level.moveEast(mover);	break;
			case NORTH:	level.moveNorth(mover);	break;
			case SOUTH:	level.moveSouth(mover);	break;
			default: System.out.println("invalid direction..");
		}
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
