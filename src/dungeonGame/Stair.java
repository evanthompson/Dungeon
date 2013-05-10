package dungeonGame;

public class Stair extends Pathable {

	private boolean descent;
	
	public Stair() {
		super();
		this.descent = true;
	}
	public Stair(int x, int y, boolean goDown) {
		super(x, y);
		this.descent = goDown;
	}
	
	public void setDescent(boolean goDown) {
		descent = goDown;
	}
	
	public boolean getDescent() { return descent; }

}
