package dungeonGame;

import org.eclipse.swt.SWT;
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
	private final int DELAY = 50;
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
				
				game.runGame();
				shell.getDisplay().timerExec(DELAY, timer);
			}
		};
		Display.getDefault().timerExec(DELAY, timer);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(game.isGamePaused()) {
			switch(e.keyCode) {
			case SWT.SPACE:			game.togglePause(); break;
			case SWT.ARROW_UP:		game.traverseMenu(false); break;
			case SWT.ARROW_DOWN:	game.traverseMenu(true); break;
			case SWT.CR:			game.quitGame();
			}
			return;
		}
		
		switch(e.keyCode) {
		case SWT.SPACE:			game.togglePause(); break;
		case SWT.ARROW_LEFT:	game.decideAccel(Compass.WEST, true); break;
		case SWT.ARROW_RIGHT:	game.decideAccel(Compass.EAST, true); break;
		case SWT.ARROW_UP:		game.decideAccel(Compass.NORTH, true); break;
		case SWT.ARROW_DOWN:	game.decideAccel(Compass.SOUTH, true); break;
		
		case 122:	game.attack(); break;
		case 120:	for(GameObject obj : game.getFloor().getObjects()) {
						if(obj instanceof AnimateObject) {
							System.out.print(obj.getClass().getSimpleName() + " ");
						}
					}
					System.out.println();
					break;
		case 99:	System.out.println(e.keyCode); break;
		case 97:	game.decideAccel(Compass.WEST, true); break;
		case 100:	game.decideAccel(Compass.EAST, true); break;
		case 119:	game.decideAccel(Compass.NORTH, true); break;
		case 115:	game.decideAccel(Compass.SOUTH, true); break;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if(game.isGamePaused()) {
			return;
		}
		
		switch(e.keyCode){
		case SWT.ARROW_LEFT:	game.decideAccel(Compass.WEST, false); break;
		case SWT.ARROW_RIGHT:	game.decideAccel(Compass.EAST, false); break;
		case SWT.ARROW_UP:		game.decideAccel(Compass.NORTH, false); break;
		case SWT.ARROW_DOWN:	game.decideAccel(Compass.SOUTH, false); break;
		case 97:	game.decideAccel(Compass.WEST, false); break;
		case 100:	game.decideAccel(Compass.EAST, false); break;
		case 119:	game.decideAccel(Compass.NORTH, false); break;
		case 115:	game.decideAccel(Compass.SOUTH, false); break;
		}
	}
	
}
