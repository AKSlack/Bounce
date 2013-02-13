package breakOutGame;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glOrtho;
import gameUtils.StateTracker;
import gameUtils.Timer;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import pinBallGame.GameBall;
import pinBallGame.GameWall;

public class BreakOut {

	StateTracker glState = new StateTracker();
	Timer clock = new Timer();
	
	//create Objects
	private List<GameWall> walls = new ArrayList<GameWall>(4); //ArrayList that holds all created bricks
	private List<GameBrick> bricks = new ArrayList<GameBrick>(); //ArrayList that holds all created bricks
	private GameBall ball;
	private GameWall paddle;
	private boolean figureMode = true;

	//BreakOut run block
	public BreakOut() {
		float[] backgroundColor = {.1f, .1f, .1f};
		initializeWindow("BreakOut", 1024, 768, backgroundColor);
		initializeOpenGL();
		initializeObjects();
		
		mainLoop();
		
		Display.destroy();	
	}

	public static void main(String[] args) {
		new BreakOut();

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
		ball = new GameBall(Display.getWidth() / 2, Display.getHeight() / 2, ballR, ballR, 0, 4 * ballR, glState);
		ball.setColor(1, 0, 0);
		int paddleW = 80;
		paddle = new GameWall(Display.getWidth() / 2, Display.getHeight() - ballR, paddleW, ballR, Math.PI / 4, 4, glState);
		paddle.setColor(0, 1, 0);
	}
	
	private void mainLoop() {
		while (!Display.isCloseRequested()) { // This is the Main Loop: Rendering done here
			
			// Entity update for change over time
			int delta = clock.getDelta();
			ball.update(delta);
			paddle.update(delta);
		
			keyboardHandler();
			ballHandler();
			
			paddleUI();
			
			render();
			
			Display.update();
			Display.sync(60);									
		}
	}
	
	private void keyboardHandler() {
		while (Keyboard.next()) { //while there are still event keys in the keyboard class				
			switch(Keyboard.getEventCharacter()) {
			case 'g': ball.startBall(.3); break;
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
		paddle.draw();
		ball.draw();
		// insert draw methods here
		
		glFlush();
	}

	private void setupGameEnvironment() {
		int wallW = 30;
		float[] wallColor = {0, 0, 1};
		GameWall topWall = new GameWall(Display.getWidth() / 2, wallW / 2, Display.getWidth(), wallW, Math.PI / 4, 4, glState);
		topWall.setName("topWall");
		topWall.setColor(wallColor);
		walls.add(topWall);
		GameWall rightWall = new GameWall(Display.getWidth() - wallW / 2, Display.getHeight() / 2, wallW, Display.getHeight(), Math.PI / 4, 4, glState);
		rightWall.setName("rightWall");
		rightWall.setColor(wallColor);
		walls.add(rightWall);
		GameWall leftWall = new GameWall(wallW / 2, Display.getHeight() / 2, wallW, Display.getHeight(), Math.PI / 4, 4, glState);
		leftWall.setName("leftWall");
		leftWall.setColor(wallColor);
		walls.add(leftWall);
		
		populateBricks(wallW);
	}
	
	private void populateBricks(int wallW) {
		int coverageX = 21;
		int coverageY = 8;
		double brickW = 30;
		double brickH = 15;
		float[] brickColor = {0, 1, 1};
		double spacing = ((Display.getWidth() - (2.0 * wallW)) - (coverageX * brickW)) / (coverageX + 1.0);
		for (int y = 0; y < coverageY; y++) {
			for (int x = 0; x < coverageX; x++) {
				GameBrick brick = new GameBrick(wallW + spacing + (brickW * .5) + ((brickW + spacing) * x), wallW + spacing + (brickH * .5) + ((brickH + spacing) * y), brickW, brickH, Math.PI / 4, 4, glState);				
				brick.setColor(brickColor);
				bricks.add(brick);
			}
		}
	}

	
	private void drawGameEnvironment() {
		for (int i = 0; i < walls.size(); i++) {
			walls.get(i).draw();
		}
		for (int i = 0; i < bricks.size(); i++) {
			bricks.get(i).draw();
		}
	}
	
	private void ballHandler() {
		for (int i = 0; i < walls.size(); i++) {
			GameWall wall = walls.get(i);			
			if (wall.objectCollides(ball)) {
				ball.hitsObject(wall);
			}			
		}
		
		if (paddle.objectCollides(ball)) {
			ball.hitsObject(paddle);
		}			

		for (int i = 0; i < bricks.size(); i++) {
			GameBrick brick = bricks.get(i);
			if (brick.isVisible() && brick.objectCollides(ball)) {
				ball.hitsObject(brick);
				brick.setVisible(false);
			}			
		}
		
		if (ball.getY() > Display.getHeight()) {
			ball.resetBall();
		}
	}

	private void setFigureMode(boolean figureMode) {
		this.figureMode = figureMode;
		for (int i = 0; i < walls.size(); i++) {
			walls.get(i).setShowFigure(figureMode);
		}
		for (int i = 0; i < bricks.size(); i++) {
			bricks.get(i).setShowFigure(figureMode);
		}
		paddle.setShowFigure(figureMode);
		ball.setShowFigure(figureMode);

	}
	
	private void paddleUI() {
		int speed = 8;
/*		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			paddle.moveWall(0, -speed);
		}
*/		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			paddle.moveWall(-speed, 0);
		}
/*		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			paddle.moveWall(0, speed);
		}
*/		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			paddle.moveWall(speed, 0);
		}
	}

}
