package dungeonGame;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import dungeonGame.DungeonMain.Compass;


public class DungeonController implements KeyListener, PaintListener {

	private DungeonGame game;
	private DungeonView view;
	
	public DungeonController() {}	
	
	
	
	public void addModel(DungeonGame g) {
		this.game = g;
	}

	public void addView(DungeonView v) {
		this.view = v;		
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

	@Override
	public void paintControl(PaintEvent e) {
		
		Display display = view.getDisplay();
		ArrayList<GameObject> objects = game.getFloor().getObjects();
		int unit = DungeonFloor.UNIT_SIZE;
		for(GameObject obj : objects) {
			String life = "";
			if(obj instanceof Mob) {
				e.gc.setBackground(display.getSystemColor(SWT.COLOR_DARK_RED));
				e.gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
				life = ((Mob) obj).getCurrHealth() + " / " + ((Mob) obj).getMaxHealth();
			}
			else if(obj instanceof InanimateObject) {
				e.gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
				life = "*";
			} else {
				e.gc.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
			}
			e.gc.fillRectangle(obj.getXpos(), obj.getYpos(), unit, unit);
			e.gc.drawText(life, obj.getXpos() + 5, obj.getYpos() + 5);
		}
		
		Hero hero = game.getHero();
		e.gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
		e.gc.fillRectangle(hero.getXpos(), hero.getYpos(), unit, unit);
		e.gc.drawText(hero.getXpos() + "," + hero.getYpos(), 10, 10); // from different event
		
		int width = unit / 4;
		int x, y;
		
		switch(hero.getDirection()) {
		case NORTH:	x = (int)(hero.getXpos() + (unit / 2) - (width / 2));
					y = hero.getYpos();
					break;
		case SOUTH:	x = (int)(hero.getXpos() + (unit / 2) - (width / 2));
					y = hero.getYpos() + unit - width;
					break;
		case WEST:	x = hero.getXpos();
					y = (int)(hero.getYpos() + (unit / 2) - (width / 2));
					break;
		case EAST:	x = hero.getXpos() + unit - width;
					y = (int)(hero.getYpos() + (unit / 2) - (width / 2));
					break;
		default:	x = y = 0; break;
		}
		
		e.gc.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
		e.gc.fillOval(x, y, width, width);
		
		display.dispose();
		
	}
	
}
