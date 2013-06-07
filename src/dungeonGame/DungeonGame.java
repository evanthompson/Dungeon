package dungeonGame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

public class DungeonGame extends Observable {
	/* 
	Start Menu Options
	-- New Game, Load Game, Exit Game

	InGame Menu Options
	-- Pause / Resume, View statistics, Save & Exit
	
	InGame Environment
	-- key bindings: Movement, Open Menu
	-- Rules: 	Objects cannot overlap
				Pick up loot on contact
				Kill mobs on contact
				Random mob movement
				Contact with exit loads next floor
				Each Floor must be remembered
	*/
	
	public enum Compass { NORTH, WEST, EAST, SOUTH }
	public enum GameState { START, PLAY, MAIN, LOAD, SAVE, EXIT }
	
	public Map<Compass, Boolean>	keyFlags;
	public ArrayList<String>		menuOptions;
	
	private ArrayList<DungeonFloor>	dungeon;
	private DungeonFloor			level;
	private SQLManager				saves;
	private Hero					hero;
	
	private GameState				gameState;
	private int						currLevel, menuSelection;
	
	public DungeonGame() {
		currLevel = 0;
		dungeon = new ArrayList<DungeonFloor>();
		dungeon.add(new DungeonFloor(currLevel));
		level = dungeon.get(currLevel);
		
		// KeyFlag initialization and setup
		keyFlags = new TreeMap<Compass, Boolean>();
		for(Compass c : Compass.values()) {
			keyFlags.put(c, false);
		}
		
		// Game menu setup
		menuOptions = new ArrayList<String>();
		menuOptions.add("Load Game");
		menuOptions.add("Save Game");
		menuOptions.add("Quit Game");
		changeState(GameState.START);
		
		// SQL database setup
		saves = new SQLManager();
		
		// Hero Generation
		Point heroStart = level.findFreePoint();
		if(heroStart != null) {
			hero = new Hero(heroStart.x, heroStart.y, 100);
			level.organizeObject(hero);
		}
	}
	
	public void runGame() {
		if(gameState == GameState.EXIT) { return; }
		if(gameState != GameState.PLAY) { return; }
		
		// Hero Movement
		if(hero.getAccel() == true) {
			hero.increaseSpeed(5);
		} else {
			hero.decreaseSpeed(5);
		}
		
		if(keyFlags.get(Compass.NORTH)) { move(hero, 0, -1); }
        if(keyFlags.get(Compass.SOUTH)) { move(hero, 0, 1); }
        if(keyFlags.get(Compass.WEST)) { move(hero, -1, 0); }
        if(keyFlags.get(Compass.EAST)) { move(hero, 1, 0); }
		hero.setCrosshair(hero.getDirection());
		
		// Enemy Movement
		for(Mob m : level.getEnemies()) {
			if(Math.round(Math.random() * 9) >= 7) {
				m.increaseSpeed(3);
				m.setDirection(Compass.values()[(int)(Math.random() * 3)]);
			} else {
				m.decreaseSpeed(5);
			}
			setVelocity(m);
		}
		
		updateGame();
	}
	
	public void updateGame() {
		setChanged();
		notifyObservers();
	}
	
	public void decideAccel(Compass dir, boolean accel) {
		keyFlags.put(dir, accel);
		if(accel) hero.setDirection(dir);
		
		if(keyFlags.containsValue(true)) {
			hero.setAccel(true);
		} else {
			hero.setAccel(false);
		}
	}
	
	public void setVelocity(AnimateObject mover) {
		switch(mover.getDirection()) {
		case WEST:	move(mover, -1, 0);	break;
		case EAST:	move(mover, 1, 0);	break;
		case NORTH:	move(mover, 0, -1);	break;
		case SOUTH:	move(mover, 0, 1);	break;
		}
	}
	
	public void move(AnimateObject mover, int xFactor, int yFactor) {
		int xDelta = xFactor * mover.getSpeed();
		int yDelta = yFactor * mover.getSpeed();
		if(xDelta == 0 && yDelta == 0) {
			return;
		}
		
		int xBefore = mover.getPos().x;
		int yBefore = mover.getPos().y;
		int xAfter = xBefore + xDelta;
		int yAfter = yBefore + yDelta;
		if(xFactor > 0) { xAfter += mover.SIZE; }
		if(yFactor > 0) { yAfter += mover.SIZE; }
		
		if((xBefore <= 0 && xFactor < 0) || (xBefore + mover.SIZE >= level.MAP_WIDTH - 1 && xFactor > 0)) {
			mover.setSpeed(0);
		}
		if((yBefore <= 0 && yFactor < 0) || (yBefore + mover.SIZE >= level.MAP_HEIGHT - 1 && yFactor > 0)) {
			mover.setSpeed(0);
		}
		
		Point upperPoint = new Point(xAfter + Math.abs(yFactor)*(mover.SIZE-1), 
				yAfter + Math.abs(xFactor)*(mover.SIZE-1));
		GameObject stairs = null;
		for(GameObject obj : level.getObjects()) {
			
			int objX = obj.getPos().x;
			int objY = obj.getPos().y;
			if(level.overlapAt(obj, new Point(xAfter, yAfter)) || level.overlapAt(obj, upperPoint)) {
				if(obj instanceof Stair && mover instanceof Hero) {
					stairs = obj;
				}
				xDelta = xFactor * Math.max(0, Math.min(Math.abs(xDelta), Math.abs(xBefore - objX) - mover.SIZE));
				yDelta = yFactor * Math.max(0, Math.min(Math.abs(yDelta), Math.abs(yBefore - objY) - mover.SIZE));
				
				if(xDelta == 0 && yDelta == 0) {
					mover.setSpeed(0);
					break;
				}
			}
		}
		mover.setXpos(Math.min(level.getMapWidth() - mover.SIZE, Math.max(0, mover.getPos().x + xDelta)));
		mover.setYpos(Math.min(level.getMapHeight() - mover.SIZE, Math.max(0, mover.getPos().y + yDelta)));
		moveFloors((Stair) stairs);
	}
	
	public void moveFloors(Stair stairs) {
		if(stairs == null) return;
		
		level.removeObject(hero);
		if(stairs.getDescent() == true) {
			currLevel++;
			if(dungeon.size() <= currLevel) {
				dungeon.add(new DungeonFloor(currLevel));
			}
			level = dungeon.get(currLevel);
			
		} else {
			if(currLevel > 0) {
				currLevel--;
				level = dungeon.get(currLevel);
			}
		}
		level.organizeObject(hero);
	}
	
	public void attack() {
		ArrayList<GameObject> targets = new ArrayList<GameObject>();
		for(Mob m : level.getEnemies()) {
			if(level.overlapAt(m, hero.getCrosshair())) {
				m.damage(hero.getStrength() * 10);
				if(m.getCurrHealth() <= 0) {
					System.out.println("hero got " + m.getBooty() + " gil and " + m.getExperience() + " experience.");
					hero.addBooty(m.getBooty());
					hero.addExp(m.getExperience());
					targets.add(m);
				}
			}
		}
		if(targets.size() > 0)
			level.removeObjects(targets);
		
		updateGame();
	}
	
	//////////////////////////////////////////
	// Menu related Methods
	
	public void menuDecision() {
		if(gameState == GameState.LOAD) {
			loadGame();
		} else if(gameState == GameState.SAVE) {
			saveGame();
		} else {
			
			if(menuSelection == 0) {
				changeState(GameState.LOAD);
			} else if(menuSelection == 1) {
				if(gameState == GameState.START) {
					changeState(GameState.PLAY);
				} else {
					changeState(GameState.SAVE);
				}
			} else if(menuSelection == 2) {
				gameState = GameState.EXIT;
			}
			
		}
		updateGame();
	}
	
	public void togglePause() {
		if(gameState != GameState.PLAY) {
			changeState(GameState.PLAY);
		} else {
			changeState(GameState.MAIN);
		}
		updateGame();
	}
	
	public void traverseMenu(boolean nextItem) {
		int listLength = menuOptions.size();
		if(gameState == GameState.LOAD || gameState == GameState.SAVE) {
			listLength = getSaves().getTableRows("heros").size();
		}
		for(int i = 0; i < listLength; i++) {
			if(i == menuSelection) {
				if(nextItem && (i + 1 < listLength)) {
					menuSelection++;
				} else if((!nextItem) && (i - 1 >= 0)) {
					menuSelection--;
				}
				break;
			}
		}
		updateGame();
	}
	
	public void changeState(GameState newState) {
		gameState = newState;
		menuSelection = 0;
	}
	
	public void loadGame() {		
		ArrayList<Object> heroStats = saves.getTableRows("heros").get(menuSelection);
		hero.reset();
		hero.setPosition(hero.getPos());
		hero.setName((String) heroStats.get(0));
		hero.addExp((Integer) heroStats.get(1));
		hero.addBooty((Integer) heroStats.get(2));
		
		changeState(GameState.MAIN);
		updateGame();
	}
	
	public void saveGame() {
		int totalExp = hero.getExperience() + ((int) Math.pow(2, hero.getLevel() - 2) * 100);
		if(hero.getLevel() == 1) { totalExp = hero.getExperience(); }
		saves.updateTable("heros", menuSelection + 1, hero.getname(), totalExp, hero.getBooty());
		
		changeState(GameState.MAIN);
		updateGame();
	}
	
	// Get Methods
	public Hero getHero() { return hero; }
	public DungeonFloor getFloor() { return level; }
	public GameState getGameState() { return gameState; }
	public int getMenuSelection() { return menuSelection; }
	public SQLManager getSaves() { return saves; }
}
