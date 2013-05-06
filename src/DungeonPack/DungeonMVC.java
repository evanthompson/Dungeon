package DungeonPack;

public class DungeonMVC {

	public DungeonMVC() {
		DungeonGame game = new DungeonGame();
		DungeonView view = new DungeonView();
		
		game.addObserver(view);
		
		DungeonController controller = new DungeonController();
		controller.addModel(game);
		controller.addView(view);
		
		view.addController(controller);
		
	}
	
	public static void main(String[] args) {
		DungeonMVC dungeonGame = new DungeonMVC();
	}

}
