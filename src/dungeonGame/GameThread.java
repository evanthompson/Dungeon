/**
 * ## This Class is currently Redundant ##
 * 
 * I need to use this thread to independently modify the positions of every
 * non-player character on the field, while leaving the player free to control
 * his hero.
 * 
 */

package dungeonGame;

import dungeonGame.DungeonMain.Compass;


public class GameThread extends Thread {
	
	private DungeonGame game;
	private boolean running = true;
	
	public GameThread(DungeonGame game) {
		super("gameThread");
		System.out.println("Constructing new GameThread...");
		this.game = game;
		start();
	}
	
	public void run() {
		while(running) {
			for(Mob m : game.getFloor().getEnemies()) {
				game.desiredMove(Compass.values()[(int) (Math.random() * 3)], m);
			}
			try { Thread.sleep(1000); } catch (InterruptedException e) { }
		}
		System.out.println("Exiting thread...");
	}
	
	public void killProc() {
		running = false;
	}
	
}
