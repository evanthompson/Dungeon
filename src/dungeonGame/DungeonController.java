package dungeonGame;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import dungeonGame.DungeonGame.Compass;

public class DungeonController implements KeyListener {

	private DungeonGame game;
	private DungeonView view;
	private Runnable timer;
	private final int DELAY = 1000;
	private Shell shell;
	
	public DungeonController(Shell s) {
		shell = s;
	}
	
	public void addModel(DungeonGame g) {
		this.game = g;
	}

	public void addView(DungeonView v) {
		this.view = v;		
	}

	public void startGame() {
		timer = new Runnable() {
			public void run() {
				if(shell.isDisposed()) {
					System.out.println("Runnable: shell is disposed!");
					return;
				}
				for(Mob m : game.getFloor().getEnemies()) {
					game.desiredMove(Compass.values()[(int) (Math.random() * 3)], m);
				}
				shell.getDisplay().timerExec(DELAY, timer);
			}
		};
		Display.getDefault().timerExec(DELAY, timer);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.keyCode == 97) {
			game.desiredMove(Compass.WEST, game.getHero());
		}
		else if(e.keyCode == 100) {
			game.desiredMove(Compass.EAST, game.getHero());
		}
		else if(e.keyCode == 119) {
			game.desiredMove(Compass.NORTH, game.getHero());
		}
		else if(e.keyCode == 115) {
			game.desiredMove(Compass.SOUTH, game.getHero());
		}
		else if(e.keyCode == 122) { // Z
			System.out.println("attacking");
			game.attack();
		}
		else if(e.keyCode == 120) { // X
			System.out.println(e.keyCode);
		}
		else if(e.keyCode == 99) { // C
			System.out.println(e.keyCode);
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {}
	
}
