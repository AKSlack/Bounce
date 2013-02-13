package pinBallGame;

import gameUtils.GameObject;
import gameUtils.StateTracker;
import gameUtils.Vector;

public class GameWall extends GameObject {
	
	protected String name;

	public String toString() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GameWall(double x, double y, double width, double height, double initial, int sides, StateTracker glState) {
		super(x, y, width, height);
		
		setShape(initial, sides, glState);
		setColor(1f, 1f, 1f);
	}
	
	public boolean objectCollides(GameBall ball) {
		if (shape.contains(ball.getX(), ball.getY())) {
//			System.out.println("Inside");
			return true;
		}
		return false;
	}
	
	public double[] getCollision(Vector vector) {
		double[] collisionInfo = shape.getCollisionInfo(vector);
		return collisionInfo;
	}
	
	public void moveWall(double dx, double dy) {
		shape.updateLocation(dx, dy);
	}
	
	public void rotateWall(double angle) {
		shape.updateRotation(angle);
	}
}
