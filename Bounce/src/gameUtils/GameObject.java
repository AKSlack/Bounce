package gameUtils;

import entities.AbstractMoveableEntity;

public abstract class GameObject extends AbstractMoveableEntity{

	protected RegularShape shape;
	protected float[] color;

	
	public GameObject(double x, double y, double width, double height) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
	}
	
	public void setShape(double initial, int sides, StateTracker glState) {
		this.shape = new RegularShape(x, y, initial, sides, width, height, glState);
	}
	
	public void setColor(float[] color) {
		this.color = color;
		shape.paintSurface(null, color);
	}
	public void setColor(float red, float green, float blue) {
		float[] newColor = {red, green, blue};
		this.color = newColor;
		shape.paintSurface(null, newColor);
	}
	public float[] getColor() {
		return this.color;
	}

	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
		shape.setLocation(x, y);
	}
	
	public boolean isShowFigure() {
		return shape.isShowFigure();
	}

	public void setShowFigure(boolean showFigure) {
		shape.setShowFigure(showFigure);
	}

	
	public void draw() {
		shape.draw();
	}
	
}
