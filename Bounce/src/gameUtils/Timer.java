package gameUtils;

import org.lwjgl.Sys;

public class Timer {
	
	private long lastFrame;
	
	private long getTime() {
		return (Sys.getTime() * 1000 / Sys.getTimerResolution()); //Returns time in milliseconds
	}

	public void update() {
		lastFrame = getTime();
	}
	
	public int getDelta() {
		long currentTime = getTime();
		int delta = (int) (currentTime - lastFrame);
		update();
		return delta;
	}
		
	public Timer() {
		update();
	}
}
