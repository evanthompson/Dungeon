package dungeonGame;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import dungeonGame.DungeonGame.GameState;

public class DungeonView implements Observer {
	private ArrayList<Resource> cleanUp;
	private Color dGray, gray, lGray;
	private Image rock, goo, hero, stairsUp, stairsDown;
	private DungeonGame game;
	
	private Shell shell;
	private Canvas floor;
	private Composite menu;
	
	public DungeonView(Shell s) {
		game = new DungeonGame();
		game.addObserver(this);
		this.shell = s;
		
		cleanUp = new ArrayList<Resource>(10);
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
		
		// Paint Listener - Floor
		floor.addListener(SWT.Paint, new Listener () {
			public void handleEvent(Event e) {
				drawObjects(e);
				drawHero(e);
				drawPauseScreen(e);
			}
		});
		// Paint Listener - Menu
		menu.addListener(SWT.Paint, new Listener () {
			public void handleEvent (Event e) {
				drawSideMenu(e);
			}
		});
		
		// Dispose Listener
		shell.addDisposeListener(new DisposeListener () {
			public void widgetDisposed(DisposeEvent e) {
				for(Resource c : cleanUp) {
					c.dispose();
				}
			}
		});
		
	}
	
	public void drawObjects(Event e) {
		ArrayList<GameObject> objects = game.getFloor().getObjects();
		for(GameObject obj : objects) {
			int size = obj.SIZE;
			if(obj instanceof Mob) {
				String life = ((Mob) obj).getCurrHealth() + " / " + ((Mob) obj).getMaxHealth();
				e.gc.drawImage(goo, obj.getPos().x, obj.getPos().y);
				e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_WHITE));
				e.gc.drawText(life, obj.getPos().x + 5, obj.getPos().y + 5, true);
			}
			else if(obj instanceof Obstacle) {
				e.gc.drawImage(rock, obj.getPos().x, obj.getPos().y);
			} 
			else if(obj instanceof Stair) {
				if(((Stair) obj).getDescent() == true) {
					e.gc.drawImage(stairsDown, obj.getPos().x, obj.getPos().y);
				} else { e.gc.drawImage(stairsUp, obj.getPos().x, obj.getPos().y); }
			}
			else if(!(obj instanceof Hero)) {
				e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_YELLOW));
				e.gc.fillRectangle(obj.getPos().x, obj.getPos().y, size, size);
			}
		}
	}
	
	public void drawHero(Event e) {
		// Drawing Hero
		Hero heroObj = game.getHero();
		e.gc.drawImage(hero, heroObj.getPos().x, heroObj.getPos().y);
		
		// Drawing Hero range indicator
		int size = heroObj.SIZE;
		int width = size / 4;
		e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_YELLOW));
		e.gc.fillOval(heroObj.getCrosshair().x - (width / 2), heroObj.getCrosshair().y  - (width / 2), width, width);
	}
	
	public void drawPauseScreen(Event e) {
		if(game.getGameState() != GameState.PLAY) {
			e.gc.setBackground(dGray);
			e.gc.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
			e.gc.drawText("Game Paused", 200, 10, false);
			
			int firstRow = 30;
			int rowHeight = 20;
			
			if(game.getGameState() == GameState.LOAD) {
				drawLoadingMenu(e);
			} else if(game.getGameState() == GameState.SAVE) {
				drawLoadingMenu(e);
			} else {
				for(int i = 0; i < game.menuOptions.size(); i++) {
					boolean isTransparent = true;
					if(i == game.getMenuSelection()) {
						isTransparent = false;
					}
					e.gc.drawText(game.menuOptions.get(i), 200, firstRow + (rowHeight*i), isTransparent);
				}
			}
		}
	}
	
	public void drawLoadingMenu(Event e) {
		ArrayList< ArrayList<Object> > heroList = game.getSaves().getTableRows("heros");
		if(heroList == null || heroList.isEmpty()) {
			System.out.println("heroList is null or empty");
			return;
		}
		int firstRow = 30;
		int rowHeight = 20;
		for(int i = 0; i < heroList.size(); i++) {
			ArrayList<Object> list = heroList.get(i);
			boolean isTransparent = true;
			String hero = list.get(0) + ": " + list.get(1) + ", " + list.get(2) + "g.";
			if(list.get(0).equals("")) { hero = "ANON" + hero; }
			
			if(game.getMenuSelection() == i) {
				isTransparent = false;
			}
			e.gc.drawText(hero, 200, firstRow + (rowHeight*i), isTransparent);
		}
	}
	
	public void drawSideMenu(Event e) {
		Hero hero = game.getHero();
		int firstRow = 10;
		int rowHeight = 20;
		e.gc.drawText(hero.getPos().x + "," + hero.getPos().y, 10, firstRow);
		e.gc.drawText("Speed: " + hero.getSpeed(), 10, firstRow += rowHeight);
		e.gc.drawText("keyFlags: " + game.keyFlags.values() , 10, firstRow += rowHeight);
		e.gc.drawText("Experience: " + hero.getExperience(), 10, firstRow += rowHeight);
		e.gc.drawText("Money: " + hero.getBooty(), 10, firstRow += rowHeight);
	}
	
	public void makeColors() {
		addResource(dGray = new Color(shell.getDisplay(), 30, 30, 30));
		addResource(gray = new Color(shell.getDisplay(), 150, 150, 150));
		addResource(lGray = new Color(shell.getDisplay(), 200, 200, 200));
	}
	
	public void createImages() {
		rock = new Image(shell.getDisplay(), getClass().getResourceAsStream("images/rock_base_2.png"));
		ImageData rockData = rock.getImageData();
		RGB rockRgb = rockData.palette.getRGB(rockData.getPixel(0, 0));
		rockData.transparentPixel = rockData.palette.getPixel(rockRgb);
		addResource(rock = new Image(shell.getDisplay(), rockData.scaledTo(50,50)));
		
		goo = new Image(shell.getDisplay(), getClass().getResourceAsStream("images/goo_1_1.png"));
		ImageData gooData = goo.getImageData();
		RGB gooRgb = gooData.palette.getRGB(gooData.getPixel(0, 0));
		gooData.transparentPixel = gooData.palette.getPixel(gooRgb);
		addResource(goo = new Image(shell.getDisplay(), gooData.scaledTo(50,50)));
		
		hero = new Image(shell.getDisplay(), getClass().getResourceAsStream("images/hero_1.png"));
		ImageData heroData = hero.getImageData();
		RGB heroRgb = rockData.palette.getRGB(heroData.getPixel(0, 0));
		heroData.transparentPixel = rockData.palette.getPixel(heroRgb);
		addResource(hero = new Image(shell.getDisplay(), heroData.scaledTo(50,50)));
		
		stairsUp = new Image(shell.getDisplay(), getClass().getResourceAsStream("images/stair_up.png"));
		ImageData stairsUpData = stairsUp.getImageData();
		RGB stairsUpRgb = stairsUpData.palette.getRGB(stairsUpData.getPixel(0, 0));
		stairsUpData.transparentPixel = stairsUpData.palette.getPixel(stairsUpRgb);
		addResource(stairsUp = new Image(shell.getDisplay(), stairsUpData.scaledTo(50,50)));
		
		stairsDown = new Image(shell.getDisplay(), getClass().getResourceAsStream("images/stairs_down.png"));
		ImageData stairsDownData = stairsDown.getImageData();
		RGB stairsDownRgb = stairsDownData.palette.getRGB(stairsDownData.getPixel(0, 0));
		stairsDownData.transparentPixel = stairsDownData.palette.getPixel(stairsDownRgb);
		addResource(stairsDown = new Image(shell.getDisplay(), stairsDownData.scaledTo(50,50)));
	}
	
	public void addResource(Resource r) {
		cleanUp.add(r);
	}
	
	@Override
	public void update(Observable obs, Object obj) {
		if(shell.isDisposed()) {
			System.out.println("Update: shell is disposed!");
			return;
		}
		floor.redraw();
		menu.redraw();
	}
	
	public void addController(DungeonController controller) {
		shell.addKeyListener(controller);
	}
	
	public DungeonGame getGame() { return game; }

}
