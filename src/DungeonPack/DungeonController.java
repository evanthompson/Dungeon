package DungeonPack;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import DungeonPack.DungeonMain.Compass;

public class DungeonController implements Listener {

	private DungeonGame game;
	private DungeonView view;
	
	public DungeonController() {}	
	
	@Override
	public void handleEvent(Event e) {
		if(e.type == SWT.KeyDown) {
			System.out.println("keydown");
		}
		if(e.getClass().equals(KeyEvent.class)) {
			System.out.println("event.equals(KeyEvent)");
		}
		
		if(e.keyCode == 97) {
			game.getFloor().desiredMove(Compass.WEST, game.getHero());
		}
		else if(e.keyCode == 100) {
			game.getFloor().desiredMove(Compass.EAST, game.getHero());
		}
		else if(e.keyCode == 119) {
			game.getFloor().desiredMove(Compass.NORTH, game.getHero());
		}
		else if(e.keyCode == 115) {
			game.getFloor().desiredMove(Compass.SOUTH, game.getHero());
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
		
		view.update(game, 1); // Second parameter needs a real value ***
	}
	
	public void addModel(DungeonGame g) {
		this.game = g;
	}

	public void addView(DungeonView v) {
		this.view = v;		
	}
}
