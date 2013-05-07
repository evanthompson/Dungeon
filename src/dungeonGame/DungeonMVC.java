package dungeonGame;

public class DungeonMVC {

	public DungeonMVC() {
		DungeonGame game = new DungeonGame();
		DungeonView view = new DungeonView();
		
		game.addObserver(view);
		
		DungeonController controller = new DungeonController();
		controller.addModel(game);
		controller.addView(view);
		game.updateGame();
		
		view.addController(controller);
		
	}
	
	public static void main(String[] args) {
		DungeonMVC newnewGame = new DungeonMVC();
	}

}
