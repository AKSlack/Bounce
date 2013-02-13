package entities;

//import gameUtils.CollisionLine;

public abstract class AbstractMoveableEntity extends AbstractEntity implements MoveableEntity {

//	protected double x0, y0, width0, height0;
	protected double dx, dy;
//	public CollisionLine moveLine;
	
	public AbstractMoveableEntity(double x, double y, double width, double height) {
		super(x, y, width, height);
		this.dx = 0;
		this.dy = 0;
//		updateVars();
	}
	
	public void update(int delta) {
//		updateVars();
		this.x += delta * dx;
		this.y += delta * dy;
	}
	
	public double getDX() {
		return dx;
	}
	public double getDY() {
		return dy;
	}
	
/*	public double getX0() {
		return x0;
	}
	public double getY0() {
		return y0;
	}
	public double getHeight0() {
		return height0;
	}
	public double getWidth0() {
		return width0;
	}
*/	
	public void setDX(double dx) {
		this.dx = dx;
	}
	public void setDY(double dy) {
		this.dy = dy;
	}
	
/*	public void updateVars() {
//		System.out.println("HERE");
//		this.moveLine = new CollisionLine(this.x0, this.y0, this.x, this.y);
		this.x0 = this.x;
		this.y0 = this.y;
		this.width0 = this.width;
		this.height0 = this.height;
	}
*/	
/*	public void collisionHandler(Entity other) {
		boolean collision = true;
		double ratio = 1;
		while (collision) {
			ratio = ratio - .1;
			this.setX(x0 + (this.dx * ratio));
			this.setY(y0 + (this.dy * ratio));
			collision = this.intersects(other);
		}
		
		 
	}
*/
}
