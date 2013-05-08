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
		switch(e.keyCode) {
		case 97:	game.desiredMove(Compass.WEST, game.getHero()); 
					break;
		case 100:	game.desiredMove(Compass.EAST, game.getHero());
					break;
		case 119:	game.desiredMove(Compass.NORTH, game.getHero());
					break;
		case 115:	game.desiredMove(Compass.SOUTH, game.getHero());
					break;
		case 122:	System.out.println("attacking");
					game.attack();
					break;
		case 120:	System.out.println(e.keyCode);
					break;
		case 99:	System.out.println(e.keyCode);
					break;
		default:	System.out.println("not supported");
		}
		
	}
	@Override
	public void keyReleased(KeyEvent e) {}
	
}
