package DungeonPack;

import java.util.Observable;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import DungeonPack.DungeonMain.Compass;

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
	
	
	private Hero hero;
	private DungeonFloor level;
	private GameThread mobThread;
	
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
		
		// Thread Generation
		mobThread = new GameThread(level);
	}
	
	public void beginGame() {
		// TODO : redundant until start menu is created
		
		//updateGame();
	}
	
	public void updateGame() {
		
	}
	
	// TODO: Method is currently redundant. Moved down to DungeonFloor
	public void desiredMove(Compass direction, AnimateObject mover) {
		switch(direction) {
			case WEST:	level.moveWest(mover);	break;
			case EAST:	level.moveEast(mover);	break;
			case NORTH:	level.moveNorth(mover);	break;
			case SOUTH:	level.moveSouth(mover);	break;
			default: System.out.println("invalid direction..");
		}
	}
	
	
	// TODO: Method is currently redundant.
	public boolean checkMove(int newY, int newX) {
		int unit = DungeonFloor.UNIT_SIZE;
		Point topLeft = new Point(newX, newY);
		Point topRight = new Point(newX + unit, newY);
		Point bottomLeft = new Point(newX, newY + unit);
		Point bottomRight = new Point(newX + unit, newY + unit);
		
		for(GameObject g : level.getObjects()) {
			Rectangle area = new Rectangle(g.getXpos(), g.getYpos(), unit, unit);
			if(area.contains(bottomRight) || area.contains(bottomLeft) ||
					area.contains(topRight) || area.contains(topLeft)) {
				System.out.println("Collision Detected..");
				return false;
			}
		}
		return true;
	}
	
	public void attack() {
		
	}
	
	// Get Methods
	public Hero getHero() { return hero; }
	public DungeonFloor getFloor() { return level; }
	public GameThread getThread() { return mobThread; }
}
