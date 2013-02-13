package breakOutGame;

import pinBallGame.GameWall;
import gameUtils.StateTracker;

public class GameBrick extends GameWall {
	
	protected boolean visible = true;

	public GameBrick(double x, double y, double width, double height, double initial, int sides, StateTracker glState) {
		super(x, y, width, height, initial, sides, glState);		
	}

	@Override
	public void draw() {
		if (this.visible) {
			shape.draw();
		}
	}
			
	public void setVisible(boolean visibility) {
		this.visible = visibility;
	}
	
	public boolean isVisible() {
		return visible;
	}
}
