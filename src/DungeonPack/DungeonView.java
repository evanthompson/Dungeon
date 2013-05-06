package DungeonPack;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.widgets.Listener;

public class DungeonView implements Observer {

	public DungeonView() {
		
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		//floor.redraw();
		//menu.redraw();
		
	}
	
	public void addController(Listener controller) {
		// button.addActionListener(controller); // etc etc
	}

}
