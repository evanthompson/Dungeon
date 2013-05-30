package dungeonGame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class DungeonMVC {
	
	public DungeonMVC() {
		//DungeonGame game = new DungeonGame();
		Display display = new Display();
		Shell shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setText("Dungeon");
		DungeonView view = new DungeonView(shell);
		//game.addObserver(view); // inside DungeonView Constructor
		
		DungeonController controller = new DungeonController(shell);
		controller.addModel(view.getGame());
		controller.addView(view);
		
		view.addController(controller);		
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
