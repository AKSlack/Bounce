package gameUtils;

import static org.lwjgl.opengl.GL11.*;

public class StateTracker {
	
	int currentMatrix;
	float[] currentColor;
	
	String currentImage;
	int currentImageID;
	
	public StateTracker() {
		
	}
	
	public int getCurrentMatrix() {
		return currentMatrix;
	}
	public void setCurrentMatrix(int currentMatrix) {
		if (this.currentMatrix != currentMatrix) {
			this.currentMatrix = currentMatrix;
			glMatrixMode(currentMatrix);
		}
	}
	public float[] getCurrentColor() {
		return currentColor;
	}
	public void setCurrentColor(float[] currentColor) {
		if (this.currentColor != currentColor) {
			this.currentColor = currentColor;
			glColor3f(currentColor[0], currentColor[1], currentColor[2]);
		}
	}
	
	public String getCurrentImage() {
		return currentImage;
	}
	public int getCurrentTextureID() {
		return currentImageID;
	}

	public void setCurrentImage(String currentImage, int currentTextureID) {
		this.currentImage = currentImage; 
		this.currentImageID = currentTextureID;
	}

}
