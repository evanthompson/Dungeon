package dungeonGame;

import java.util.Observable;
import org.eclipse.swt.widgets.Display;

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
	private Runnable runnable;
	
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
	
	public void beginGame() {
		runnable = new Runnable() {
			public void run() {
				for(Mob m : getFloor().getEnemies()) {
					desiredMove(Compass.values()[(int) (Math.random() * 3)], m);
				}
				Display.getDefault().timerExec(1000, runnable);
			}
		};
		Display.getDefault().timerExec(1000, runnable);
	}
	
	public void updateGame() {
		setChanged();
		notifyObservers();
	}
	
	// TODO: Method is currently redundant. Moved down to DungeonFloor
	public synchronized void desiredMove(Compass direction, AnimateObject mover) {
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
		
	}
	
	// Get Methods
	public Hero getHero() { return hero; }
	public DungeonFloor getFloor() { return level; }
}
