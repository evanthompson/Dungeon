package dungeonGame;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class DungeonView implements Observer {
	private ArrayList<Color> cleanUp;
	private Color dGray, gray, lGray;
	
	private Display display;
	private Shell shell;
	private Composite floor;
	private Composite menu;
	
	public DungeonView() {
		cleanUp = new ArrayList<Color>(10);
		
		display = new Display();
		shell = new Shell(display);
		shell.setText("Dungeon");
		
		makeColors(display);
		
		GridLayout layout;
		GridData data;
		layout = new GridLayout();
		layout.numColumns = 2;
		shell.setBackground(dGray);
		shell.setLayout(layout);
		
		// Floor Initialization
		floor = new Composite(shell, SWT.NONE);
		floor.setBackground(lGray);
		
		data = new GridData();
		data.horizontalAlignment = SWT.BEGINNING;
		data.heightHint = 500;
		data.widthHint = 500;
		floor.setLayoutData(data);
		
		// Menu Initialization
		menu = new Composite(shell, SWT.NONE);
		menu.setBackground(lGray);
		data = new GridData();
		data.horizontalAlignment = SWT.END;
		data.grabExcessVerticalSpace = true;
		data.grabExcessHorizontalSpace = true;
		data.minimumHeight = 400;
		data.widthHint = 200;
		menu.setLayoutData(data);
		
		/** Currently, might be covered by the other PaintEvent
		menu.addListener(SWT.Paint, new Listener () {
			public void handleEvent (Event event) {
				Hero hero = newGame.getHero();
				event.gc.drawText(hero.getXpos() + "," + hero.getYpos(), 10, 10);
			}
		});
		**/
		
		
		// Dispose Listener
		shell.addDisposeListener(new DisposeListener () {
			public void widgetDisposed(DisposeEvent e) {
				for(Color c : cleanUp) {
					c.dispose();
				}
			}
		});
		
		shell.pack();
		shell.open();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
	
	@Override
	public void update(Observable obs, Object game) {
	}
	
	public void addController(DungeonController controller) {
		//shell.addKeyListener((KeyListener) controller);
		floor.addPaintListener((PaintListener) controller);
		menu.addPaintListener((PaintListener) controller);
	}
	
	public void makeColors(Display display) {
		dGray = new Color(display, 30, 30, 30);
		gray = new Color(display, 150, 150, 150);
		lGray = new Color(display, 200, 200, 200);
		
		cleanUp.add(dGray);
		cleanUp.add(gray);
		cleanUp.add(lGray);
	}
	
	public Display getDisplay() { return display; }

}
