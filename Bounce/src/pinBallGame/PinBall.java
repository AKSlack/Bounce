package pinBallGame;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import gameUtils.StateTracker;
import gameUtils.Timer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class PinBall {
	
	StateTracker glState = new StateTracker();
	Timer clock = new Timer();
	
	//create Objects
	private List<GameWall> walls = new ArrayList<GameWall>(4); //ArrayList that holds all created bricks
	private GameBall ball;
	private boolean figureMode = true;

//	boolean keyPressed = false;
	
	public PinBall() {
		float[] backgroundColor = {.1f, .1f, .1f};
		initializeWindow("PinBall", 1024, 768, backgroundColor);
		initializeOpenGL();
		initializeObjects();
//		clock.update();
		
		mainLoop();
		
		Display.destroy();	
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new PinBall();
	}

	private void initializeWindow(String title, int displayW, int displayH, float[] backgroundColor) {
		try {
			Display.setDisplayMode(new DisplayMode(displayW, displayH)); 
			Display.setTitle(title); //sets the title of the Display
			Display.setInitialBackground(backgroundColor[0], backgroundColor[1], backgroundColor[2]); //sets initial background color of window to DARK_GRAY
			Display.setResizable(false); //allows resizing of window
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	private void initializeOpenGL() {
		glState.setCurrentMatrix(GL_PROJECTION); //enter GL_PROJECTION mode
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1); //designate coordinate grid locations
		glState.setCurrentMatrix(GL_MODELVIEW); //enter GL_MODELVIEW mode		
		glEnable(GL_TEXTURE_2D); //enables the use of 2D textures
	}
	
	private void initializeObjects() {
		setupGameEnvironment();
		int ballR = 10;
		ball = new GameBall(Display.getWidth() * .5, Display.getHeight() * .8, ballR, ballR, 0, 4 * ballR, glState);
	}
	
	private void mainLoop() {
		while (!Display.isCloseRequested()) { // This is the Main Loop: Rendering done here
			
			// Entity update for change over time
			int delta = clock.getDelta();
			ball.update(delta);
			
			keyboardHandler();
			ballHandler();
			
			blockUI();
			
			render();
			
			Display.update();
			Display.sync(60);							
			
		}

	}
	
	private void keyboardHandler() {
		while (Keyboard.next()) { //while there are still event keys in the keyboard class				
			switch(Keyboard.getEventCharacter()) {
			case 'g': ball.startBall(.5); break;
			case 'l': setFigureMode(!this.figureMode); break;
			case '': if (ball.getDX() != 0) {
				ball.resetBall();
				break;
			} else {
				Display.destroy(); System.exit(0); break;
			}
			// add further keyboard events
			default : break;
			}
		}
	}
	
	private void render() {

		glClear(GL_COLOR_BUFFER_BIT); //clear the screen 2D

		drawGameEnvironment();
		ball.draw();
		// insert draw methods here
		
		glFlush();
	}

	private void setupGameEnvironment() {
		double pi = Math.PI;
		int wallW = 30;
		float[] wallColor = {0f, 0f, 1f};
		GameWall topWall = new GameWall(Display.getWidth() / 2, wallW / 2, Display.getWidth(), wallW, pi / 4, 4, glState);
//		topWall.setName("topWall");
		topWall.setColor(wallColor);
		walls.add(topWall);
		GameWall rightWall = new GameWall(Display.getWidth() - wallW / 2, Display.getHeight() / 2, wallW, Display.getHeight(), pi / 4, 4, glState);
//		rightWall.setName("rightWall");
		rightWall.setColor(wallColor);
		walls.add(rightWall);
		GameWall botWall = new GameWall(Display.getWidth() / 2, Display.getHeight() - wallW / 2, Display.getWidth(), wallW, pi / 4, 4, glState);
//		botWall.setName("botWall");
		botWall.setColor(wallColor);
		walls.add(botWall);
		GameWall leftWall = new GameWall(wallW / 2, Display.getHeight() / 2, wallW, Display.getHeight(), pi / 4, 4, glState);
//		leftWall.setName("leftWall");
		leftWall.setColor(wallColor);
		walls.add(leftWall);
		GameWall midWall = new GameWall(Display.getWidth() / 2, Display.getHeight() / 2, wallW * 3, wallW * 3, 0, 6, glState);
//		midWall.setName("midWall");
		midWall.setColor(wallColor);
		walls.add(midWall);
		GameWall circle1 = new GameWall(Display.getWidth() * .25, Display.getHeight() * .33, wallW * 2, wallW * 2, 0, 16, glState);
//		circle1.setName("circle1");
		circle1.setColor(wallColor);
		walls.add(circle1);
		GameWall circle2 = new GameWall(Display.getWidth() * .75, Display.getHeight() * .33, wallW * 2, wallW * 2, 0, 16, glState);
//		circle2.setName("circle2");
		circle2.setColor(wallColor);
		walls.add(circle2);
		GameWall rect1 = new GameWall(Display.getWidth() * .25, Display.getHeight() * .7, wallW * 3, wallW * 1, pi / 4, 4, glState);
//		rect1.setName("rect1");
		rect1.setColor(wallColor);
//		rect1.rotateWall(pi * .3);
		walls.add(rect1);
		GameWall rect2 = new GameWall(Display.getWidth() * .75, Display.getHeight() * .7, wallW * 3, wallW * 1, pi / 4, 4, glState);
//		rect2.setName("rect2");
		rect2.setColor(wallColor);
//		rect2.rotateWall(-pi * .3);
		walls.add(rect2);
		GameWall tri1 = new GameWall(Display.getWidth() * .064, Display.getHeight() * .5, wallW * 2, wallW * 2, 0, 3, glState);
//		tri1.setName("tri1");
		tri1.setColor(wallColor);
//		tri1.rotateWall(pi * .3);
		walls.add(tri1);
		GameWall tri2 = new GameWall(Display.getWidth() * .936, Display.getHeight() * .5, wallW * 2, wallW * 2, 0, 3, glState);
//		tri2.setName("tri2");
		tri2.setColor(wallColor);
//		tri2.rotateWall(pi * 1);
		walls.add(tri2);
		
		
		
	}
		
	private void drawGameEnvironment() {
		for (int i = 0; i < walls.size(); i++) {
			walls.get(i).draw();
		}		
	}
	
	private void ballHandler() {
		for (int i = 0; i < walls.size(); i++) {
			GameWall wall = walls.get(i);
//			System.out.println("Contains = " + wall.shape.contains(ball.getX(), wall.getY()));
			
			if (wall.objectCollides(ball)) {
				ball.hitsObject(wall);
			}			
		}				
	}

	private void setFigureMode(boolean figureMode) {
		this.figureMode = figureMode;
		for (int i = 0; i < walls.size(); i++) {
			walls.get(i).setShowFigure(figureMode);
			ball.setShowFigure(figureMode);
		}				
	}
	
	private void blockUI() {
		int speed = 2;
/*		if (keyPressed) {
			walls.get(4).moveWall(0, 0);
			keyPressed = false;
		}
*/		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			walls.get(4).moveWall(0, -speed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			walls.get(4).moveWall(-speed, 0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			walls.get(4).moveWall(0, speed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			walls.get(4).moveWall(speed, 0);
		}
	}
}
