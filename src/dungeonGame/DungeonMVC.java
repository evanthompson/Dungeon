package dungeonGame;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class DungeonMVC {
	
	public DungeonMVC() {
		//DungeonGame game = new DungeonGame();
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Dungeon");
		DungeonView view = new DungeonView(shell);
		//game.addObserver(view); // inside DungeonView Constructor
		
		DungeonController controller = new DungeonController(shell);
		controller.addModel(view.getGame());
		controller.addView(view);
		
		view.addController(controller);
		// Before starting the game looping, have a basic start menu
		// view.loadStartMenu(); --> would show black screen with basic new/load game..
		// could have a START state, and do a check within the game's loop..
		
		controller.startGame();
		
		shell.pack();
		shell.open();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
	
	public static void main(String[] args) {
		new DungeonMVC();
	}

}
