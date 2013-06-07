package dungeonGame;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import dungeonGame.DungeonGame.GameState;

public class DungeonView implements Observer {
	private ArrayList<Resource> cleanUp;
	private Color dGray, gray, lGray;
	private Image rock, goo, hero, stairsUp, stairsDown;
	private Font titleFont;
	private DungeonGame game;
	
	private Shell shell;
	private Canvas floor;
	private Composite menu, startScreen;
	
	private long aveTime = 0;
	private int timeCounter = 0;
	
	public DungeonView(Shell s) {
		game = new DungeonGame();
		game.addObserver(this);
		this.shell = s;
		
		cleanUp = new ArrayList<Resource>(10);
		makeColors();
		createImages();
		
		shell.setBackground(dGray);
		shell.setLayout(new GridLayout());
		
		initStartScreen();
		
		// Dispose Listener
		shell.addDisposeListener(new DisposeListener () {
			public void widgetDisposed(DisposeEvent e) {
				System.out.println("Disposing Resources");
				for(Resource c : cleanUp) {
					c.dispose();
				}
			}
		});
	}
	
	private void initStartScreen() {
		// Start Screen Initialization
		GridData startData = new GridData();
		startData.horizontalAlignment = SWT.CENTER;
		startData.heightHint = 500;
		startData.widthHint = 700;
		
		startScreen = new Composite(shell, SWT.NONE);
		startScreen.setBackground(dGray);
		startScreen.setLayout(new GridLayout());
		startScreen.setLayoutData(startData);
		
		// Title Initialization
		GridData titleData = new GridData();
		titleData.horizontalAlignment = SWT.CENTER;
		titleData.grabExcessHorizontalSpace = true;
		titleData.verticalAlignment = SWT.CENTER;
		titleData.grabExcessVerticalSpace = true;
		
		Label title = new Label(startScreen, SWT.NONE);
		title.setFont(titleFont);
		title.setForeground(lGray);
		title.setBackground(dGray);
		title.setText("Dugeon Crawler");
		title.setLayoutData(titleData);
		
		// Paint Listener - StartScreen
		startScreen.addListener(SWT.Paint, new Listener () {
			public void handleEvent (Event e) {
				drawMainMenu(e);
			}
		});
	}
	
	private void initActiveGame() {
		if(startScreen != null && !startScreen.isDisposed()) {
			startScreen.dispose();
		}
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		shell.setLayout(gridLayout);
		
		// Floor Initialization
		floor = new Canvas(shell, SWT.NONE);
		floor.setBackground(gray);
		
		GridData floorData = new GridData();
		floorData.horizontalAlignment = SWT.BEGINNING;
		floorData.heightHint = 500;
		floorData.widthHint = 500;
		floor.setLayoutData(floorData);
		
		// Menu Initialization
		menu = new Composite(shell, SWT.NONE);
		menu.setBackground(lGray);

		GridData menuData = new GridData();
		menuData.horizontalAlignment = SWT.END;
		menuData.grabExcessVerticalSpace = true;
		menuData.grabExcessHorizontalSpace = true;
		menuData.minimumHeight = 400;
		menuData.widthHint = 200;
		menu.setLayoutData(menuData);
		
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
		
		shell.pack();
	}
	
	private void drawObjects(Event e) {
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
	
	private void drawHero(Event e) {
		// Drawing Hero
		Hero heroObj = game.getHero();
		e.gc.drawImage(hero, heroObj.getPos().x, heroObj.getPos().y);
		
		// Drawing Hero range indicator
		int size = heroObj.SIZE;
		int width = size / 4;
		e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_YELLOW));
		e.gc.fillOval(heroObj.getCrosshair().x - (width / 2), heroObj.getCrosshair().y  - (width / 2), width, width);
	}
	
	private void drawPauseScreen(Event e) {
		if(game.getGameState() != GameState.PLAY) {
			e.gc.setBackground(dGray);
			e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_WHITE));
			e.gc.drawText("Game Paused", 200, 10, false);
			
			switch(game.getGameState()) {
			case LOAD:	drawLoadingMenu(e);	break;
			case SAVE:	drawLoadingMenu(e);	break;
			default:	drawMainMenu(e);
			}
		}
	}
	
	/*
	private void drawMenuHelper(Event e, int xStart, int yStart, int width) {
		ArrayList<?> baseList;
		if(game.getGameState() == GameState.MAIN) {
			baseList = game.menuOptions;
		} else {
			baseList = game.getSaves().getTableRows("heros");
		}
		
		if(baseList == null) {
			System.out.println("baseList is null");
			return;
		}
		int listSize = baseList.size();
		
		int rowSpace = 20;
		e.gc.setBackground(dGray);
		e.gc.fillRoundRectangle(xStart - 20, yStart, width, rowSpace * listSize + 30, 10, 10);
		e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_WHITE));
		e.gc.drawRoundRectangle(xStart - 20, yStart, width, rowSpace * listSize + 30, 10, 10);
		
		for(int i = 0; i < listSize; i++) {
			if(i == game.getMenuSelection()) {
				e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_YELLOW));
			} else {
				e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_WHITE));
			}
			
			if(game.getGameState() != GameState.MAIN) {
				ArrayList<Object> list = (ArrayList<Object>) baseList.get(i);
				String record = "Name:" + list.get(0) + " Exp:" + list.get(1) + " $" + list.get(2);
				e.gc.drawText(record, xStart, yStart += (rowSpace));
			} else {
				if(game.getGameState() == GameState.START && i == 1) {
					e.gc.drawText("New Game", xStart, yStart += (rowSpace));
				} else {
					e.gc.drawText((String) baseList.get(i), xStart, yStart += (rowSpace));
				}
			}
		}
	}
	*/
	
	private void drawMainMenu(Event e) {
		int xStart = 200;
		int yStart = 30;
		int rowSpace = 20;
		e.gc.setBackground(dGray);
		e.gc.fillRoundRectangle(xStart - 20, yStart, 100, rowSpace * game.menuOptions.size() + 30, 10, 10);
		e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_WHITE));
		e.gc.drawRoundRectangle(xStart - 20, yStart, 100, rowSpace * game.menuOptions.size() + 30, 10, 10);
		
		for(int i = 0; i < game.menuOptions.size(); i++) {
			if(i == game.getMenuSelection()) {
				e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_YELLOW));
			} else {
				e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_WHITE));
			}
			
			if(game.getGameState() == GameState.START && i == 1) {
				e.gc.drawText("New Game", xStart, yStart += (rowSpace));
			} else {
				e.gc.drawText(game.menuOptions.get(i), xStart, yStart += (rowSpace));
			}
		}
	}
	
	private void drawLoadingMenu(Event e) {
		ArrayList< ArrayList<Object> > heroList = game.getSaves().getTableRows("heros");
		if(heroList == null || heroList.isEmpty()) {
			System.out.println("heroList is null or empty");
			return;
		}
		int xStart = 200;
		int yStart = 70;
		int rowSpace = 20;
		e.gc.setBackground(dGray);
		e.gc.fillRoundRectangle(xStart - 20, yStart, 170, rowSpace * heroList.size() + 30, 10, 10);
		e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_WHITE));
		e.gc.drawRoundRectangle(xStart - 20, yStart, 170, rowSpace * heroList.size() + 30, 10, 10);
		
		for(int i = 0; i < heroList.size(); i++) {
			ArrayList<Object> list = heroList.get(i);
			String hero = "Name:" + list.get(0) + " Exp:" + list.get(1) + " $" + list.get(2);
			
			if(i == game.getMenuSelection()) {
				e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_YELLOW));
			} else {
				e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_WHITE));
			}
			e.gc.drawText(hero, xStart, yStart += (rowSpace));
		}
	}
	
	private void drawSideMenu(Event e) {
		Hero hero = game.getHero();
		int firstRow = 10;
		int rowHeight = 20;
		e.gc.drawText(hero.getPos().x + "," + hero.getPos().y, 10, firstRow);
		e.gc.drawText("Speed: " + hero.getSpeed(), 10, firstRow += rowHeight);
		e.gc.drawText("keyFlags: " + game.keyFlags.values() , 10, firstRow += rowHeight);
		e.gc.drawText("Experience: " + hero.getExperience(), 10, firstRow += rowHeight);
		e.gc.drawText("Money: " + hero.getBooty(), 10, firstRow += rowHeight);
	}
	
	private void makeColors() {
		addResource(dGray = new Color(shell.getDisplay(), 30, 30, 30));
		addResource(gray = new Color(shell.getDisplay(), 150, 150, 150));
		addResource(lGray = new Color(shell.getDisplay(), 200, 200, 200));
		
		String path = System.getProperty("user.dir") + "/src/dungeonGame/";
		if(shell.getDisplay().loadFont(path + "Baumans-Regular.ttf")) {
			addResource(titleFont = new Font(shell.getDisplay(), "Baumans", 24, SWT.BOLD));
        } else { System.out.println("Baumans did not load"); }
	}
	
	private void createImages() {
		addResource(rock = constructImage("images/rock_base_2.png"));
		addResource(goo = constructImage("images/goo_1_1.png"));
		addResource(hero = constructImage("images/hero_1.png"));
		addResource(stairsUp = constructImage("images/stair_up.png"));
		addResource(stairsDown = constructImage("images/stairs_down.png"));
	}
	
	private Image constructImage(String filePath) {
		Image image = new Image(shell.getDisplay(), getClass().getResourceAsStream(filePath));
		ImageData imageData = image.getImageData();
		RGB imageRgb = imageData.palette.getRGB(imageData.getPixel(0, 0));
		imageData.transparentPixel = imageData.palette.getPixel(imageRgb);
		
		image.dispose();
		return new Image(shell.getDisplay(), imageData.scaledTo(50,50));
	}
	
	private void addResource(Resource r) {
		cleanUp.add(r);
	}
	
	@Override
	public void update(Observable obs, Object obj) {
		if(shell.isDisposed()) {
			System.out.println("Update: shell is disposed!");
			return;
		}
		if(game.getGameState() == GameState.EXIT) {
			shell.dispose();
			return;
		}
		
		if(startScreen != null && game.getGameState() == GameState.START) {
			startScreen.redraw();
		} else {
			if(floor == null || menu == null) {
				System.out.println("floor/menu is Null! Initiate active game.");
				initActiveGame();
			}
			
			// Performance testing code
			long start = System.nanoTime();
			floor.redraw();
			if(timeCounter < 30000) {
				long delta = System.nanoTime() - start;
				aveTime = ((aveTime * timeCounter) + delta) / ++timeCounter;
			} // End of performance testing code
			
			menu.redraw();
		}
	}
	
	public void addController(DungeonController controller) {
		shell.addKeyListener(controller);
	}
	
	public DungeonGame getGame() { return game; }
	public ArrayList<Resource> getResources() { return cleanUp; }
	
	public void printAveTimer() {
		System.out.println("redraw() = " + aveTime + "ns, with a sample size of " + timeCounter);
	}
	
}
