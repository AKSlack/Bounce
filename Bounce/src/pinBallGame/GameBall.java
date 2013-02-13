package pinBallGame;

import java.util.Random;

import org.lwjgl.opengl.Display;

import gameUtils.GameObject;
import gameUtils.StateTracker;
import gameUtils.Vector;

public class GameBall extends GameObject {
	
	double velocity;
	double initial;
	int sides;
	StateTracker glState;
	
	public GameBall(double x, double y, double width, double height, double initial, int sides, StateTracker glState) {
		super(x, y, width, height);
		
		this.initial = initial;
		this.sides = sides;
		this.glState = glState;
		
		setShape(initial, sides, glState);
		setColor(1f, 1f, 1f);

		// TODO Auto-generated constructor stub
	}
	
	public void startBall(double ballVelocity) {
		Random randomGenerator = new Random();
		this.setVelocity(ballVelocity);
		this.setDir(randomGenerator.nextFloat() * 2 * Math.PI);
	}
	
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}
	
	public double getVelocity() {
		return velocity;
	}

	public void setDir(double dirAngle) {
		// negative added to println simply to adj for normal coord system appearance
//		System.out.println("Set Angle to: " + -dirAngle / (2 * Math.PI) * 360);
		double dirX = Math.cos(dirAngle);
		double dirY = Math.sin(dirAngle);

		this.setDX(dirX * this.velocity);
		this.setDY(dirY * this.velocity);
//		System.out.println("Velocity = " + Math.pow((Math.pow(this.getDX(), 2) + Math.pow(this.getDY(), 2)), .5));
	}
	
	public double getDir() {
//		System.out.println("dy = " + dy + ", dx = " + dx);
		double dirAngle = Math.atan2(dy, dx);
		if (dirAngle < 0) {
			dirAngle = dirAngle + 2 * Math.PI;
		}
//		System.out.println("returned dir Angle = " + dirAngle / (2 * Math.PI) * 360);
		return dirAngle;
	}

	public Vector getMovementVector() {
		Vector v = new Vector(this.x - this.dx, this.y - this.dy, this.dx, this.dy); // subtract one frame so vector exists outside figure
		return v;
	}	
	
	public void hitsObject(GameWall wall) {
		double[] collisionInfo = wall.getCollision(this.getMovementVector());
//		System.out.println("of wall " + wall.toString());
//		System.out.println("at t = " + collisionInfo[2]);
		this.setDir(bounceBall(this.getDir(), collisionInfo[2]));
		this.setLocation(collisionInfo[0] - this.dx, collisionInfo[1] - this.dy);
	}
	
	private double bounceBall(double d0, double offset) {
		double dF = 2 * offset - d0;
		if (dF < 0) {
			dF = dF + 2 * Math.PI;
		}
		return dF;
	}

	public void resetBall() {
		this.setLocation(Display.getWidth() / 2 + 100, Display.getHeight() / 2 + 100);
		this.setDX(0);
		this.setDY(0);
		System.out.println("Ball Reset");
	}
	
	public void updatePosition(int delta) {
		this.x += delta * dx;
		this.y += delta * dy;
		shape.updateLocation(delta * dx, delta * dy);		
	}
	
	public void update(int delta) {
		updatePosition(delta);
	}


}
