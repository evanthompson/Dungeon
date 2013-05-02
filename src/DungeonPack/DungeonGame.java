package DungeonPack;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import DungeonPack.DungeonMain.Compass;

public class DungeonGame {
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
	public int UNIT;
	
	public DungeonGame() {
		
		level = new DungeonFloor();
		UNIT = level.UNIT_SIZE;
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
		// TODO : redundant until start menu is created
		
		//updateGame();
	}
	
	public void updateGame() {
		for(Mob m : level.getEnemies()) {
			desiredMove(Compass.values()[(int) (Math.random() * 3)], m);
		}
	}
	
	public void desiredMove(Compass direction, AnimateObject mover) {
		switch(direction) {
			case WEST:	level.moveWest(mover);	break;
			case EAST:	level.moveEast(mover);	break;
			case NORTH:	level.moveNorth(mover);	break;
			case SOUTH:	level.moveSouth(mover);	break;
			default:	System.out.println("invalid direction..");
		}
	}
	
	public boolean checkMove(int newY, int newX) {
		Point topLeft = new Point(newX, newY);
		Point topRight = new Point(newX + UNIT, newY);
		Point bottomLeft = new Point(newX, newY + UNIT);
		Point bottomRight = new Point(newX + UNIT, newY + UNIT);
		
		for(GameObject g : level.getObjects()) {
			Rectangle area = new Rectangle(g.getXpos(), g.getYpos(), UNIT, UNIT);
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
}
