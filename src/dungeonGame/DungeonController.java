package dungeonGame;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import dungeonGame.DungeonGame.Compass;

public class DungeonController implements KeyListener {

	private DungeonGame game;
	@SuppressWarnings("unused")
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
					switch(Compass.values()[(int)(Math.random() * 3)]) {
					case WEST:	game.move(m, -1, 0); 	break;
					case EAST:	game.move(m, 1, 0);		break;
					case NORTH:	game.move(m, 0, -1);	break;
					case SOUTH:	game.move(m, 0, 1);		break;
					default:	game.move(m, 0, 1);	/* SOUTH */
					}
				}
				shell.getDisplay().timerExec(DELAY, timer);
			}
		};
		Display.getDefault().timerExec(DELAY, timer);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.keyCode) {
		case 97:	game.move(game.getHero(), -1, 0); 	/* WEST */	break;
		case 100:	game.move(game.getHero(), 1, 0);	/* EAST */	break;
		case 119:	game.move(game.getHero(), 0, -1);	/* NORTH */	break;
		case 115:	game.move(game.getHero(), 0, 1);	/* SOUTH */	break;
		case 122:	game.attack();
					break;
		case 120:
			for(GameObject obj : game.getFloor().getObjects()) {
				System.out.print(obj.getClass().getSimpleName() + " ");
			}
			System.out.println();
			break;
		case 99:	System.out.println(e.keyCode);
					break;
		default:	System.out.println("not supported");
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {}
	
}
