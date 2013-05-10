package dungeonGame;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class DungeonView implements Observer {
	private ArrayList<Color> cleanUp;
	private Color dGray, gray, lGray;
	private Image rock, scaledRock, goo, scaledGoo, hero, scaledHero, floorTexture;
	private DungeonGame newGame;
	
	private Shell shell;
	private Canvas floor;
	private Composite menu;
	
	public DungeonView(Shell s) {
		newGame = new DungeonGame();
		newGame.addObserver(this);
		this.shell = s;
		
		cleanUp = new ArrayList<Color>(10);
		makeColors();
		createImages();
		
		GridLayout layout;
		GridData data;
		layout = new GridLayout();
		layout.numColumns = 2;
		shell.setBackground(dGray);
		shell.setLayout(layout);
		
		// Floor Initialization
		floor = new Canvas(shell, SWT.NONE);
		floor.setBackground(gray);
		//floorTexture = new Image(shell.getDisplay(), DungeonView.class.getResourceAsStream("images/premade_tile_single.png"));
		//floor.setBackgroundImage(floorTexture);
		
		
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
		
		// Paint Listener ///////////
		floor.addListener(SWT.Paint, new Listener () {
			public void handleEvent(Event event) {
				ArrayList<GameObject> objects = newGame.getFloor().getObjects();
				int unit = newGame.getFloor().UNIT_SIZE;
				for(GameObject obj : objects) {
					String life = "";
					if(obj instanceof Mob) {
						event.gc.drawImage(scaledGoo, obj.getXpos(), obj.getYpos());
						life = ((Mob) obj).getCurrHealth() + " / " + ((Mob) obj).getMaxHealth();
						event.gc.setForeground(event.display.getSystemColor(SWT.COLOR_WHITE));
						event.gc.drawText(life, obj.getXpos() + 5, obj.getYpos() + 5, true);
					}
					else if(obj instanceof Obstacle) {
						event.gc.drawImage(scaledRock, obj.getXpos(), obj.getYpos());
						life = "";
					} else if(!(obj instanceof Hero)) {
						event.gc.setBackground(event.display.getSystemColor(SWT.COLOR_YELLOW));
						event.gc.fillRectangle(obj.getXpos(), obj.getYpos(), unit, unit);
					}
				}
				
				// Drawing Hero
				Hero hero = newGame.getHero();
				event.gc.drawImage(scaledHero, hero.getXpos(), hero.getYpos());
				
				// Drawing Hero range indicator
				int width = unit / 4;
				event.gc.setBackground(event.display.getSystemColor(SWT.COLOR_YELLOW));
				event.gc.fillOval(hero.getCrosshair().x - (width / 2), hero.getCrosshair().y  - (width / 2), width, width);
				
			}
		});
		
		menu.addListener(SWT.Paint, new Listener () {
			public void handleEvent (Event event) {
				Hero hero = newGame.getHero();
				event.gc.drawText(hero.getXpos() + "," + hero.getYpos(), 10, 10);
				event.gc.drawText("Experience: " + hero.getExperience(), 10, 30);
				event.gc.drawText("Money: " + hero.getBooty(), 10, 50);
			}
		});
		
		// Dispose Listener
		shell.addDisposeListener(new DisposeListener () {
			public void widgetDisposed(DisposeEvent e) {
				for(Color c : cleanUp) {
					c.dispose();
				}
				//floorTexture.dispose();
				rock.dispose();
				scaledRock.dispose();
			}
		});
		
	}
	
	public void makeColors() {
		dGray = new Color(shell.getDisplay(), 30, 30, 30);
		gray = new Color(shell.getDisplay(), 150, 150, 150);
		lGray = new Color(shell.getDisplay(), 200, 200, 200);
		
		cleanUp.add(dGray);
		cleanUp.add(gray);
		cleanUp.add(lGray);
	}
	
	public void createImages() {
		rock = new Image(shell.getDisplay(), getClass().getResourceAsStream("images/rock_base.png"));
		ImageData rockData = rock.getImageData();
		RGB rockRgb = rockData.palette.getRGB(rockData.getPixel(0, 0));
		rockData.transparentPixel = rockData.palette.getPixel(rockRgb);
		scaledRock = new Image(shell.getDisplay(), rockData.scaledTo(50,50));
		
		goo = new Image(shell.getDisplay(), getClass().getResourceAsStream("images/goo_1_1.png"));
		ImageData gooData = goo.getImageData();
		RGB gooRgb = gooData.palette.getRGB(gooData.getPixel(0, 0));
		gooData.transparentPixel = gooData.palette.getPixel(gooRgb);
		scaledGoo = new Image(shell.getDisplay(), gooData.scaledTo(50,50));
		
		hero = new Image(shell.getDisplay(), getClass().getResourceAsStream("images/hero_1.png"));
		ImageData heroData = hero.getImageData();
		RGB heroRgb = rockData.palette.getRGB(heroData.getPixel(0, 0));
		heroData.transparentPixel = rockData.palette.getPixel(heroRgb);
		scaledHero = new Image(shell.getDisplay(), heroData.scaledTo(50,50));
	}
	
	@Override
	public void update(Observable obs, Object obj) {
		floor.redraw();
		menu.redraw();
	}
	
	public void addController(DungeonController controller) {
		shell.addKeyListener(controller);
	}
	
	public DungeonGame getGame() { return newGame; }

}
