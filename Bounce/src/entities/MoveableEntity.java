package entities;

public interface MoveableEntity extends Entity {
	public double getDX();
	public double getDY();
/*	public double getX0();
	public double getY0();
	public double getHeight0();
	public double getWidth0();
*/	public void setDX(double dx);
	public void setDY(double dy);
//	public void collisionHandler(Entity other);

}
