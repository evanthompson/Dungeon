package DungeonPack;

import DungeonPack.DungeonMain.Compass;

/*
 * I need to use this thread to independently modify the positions of every
 * non-player character on the field, while leaving the player free to control
 * his hero.
 * 
 */
public class GameThread extends Thread {
	
	private DungeonFloor level;
	private boolean running = true;
	
	public GameThread(DungeonFloor floor) {
		super("gameThread");
		System.out.println("Constructing new GameThread...");
		level = floor;
		start();
	}
	
	public void run() {
		while(running) {
			for(Mob m : level.getEnemies()) {
				level.desiredMove(Compass.values()[(int) (Math.random() * 3)], m);
			}
			try { Thread.sleep(1000); } catch (InterruptedException e) { }
		}
		System.out.println("Exiting thread...");
	}
	
	public void killProc() {
		running = false;
	}
}
