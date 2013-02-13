package gameUtils;

import java.awt.Rectangle;

import entities.AbstractMoveableEntity;

public class CollisionLine {
	
	double[] v1 = new double[2];
	double[] v2 = new double[2];
	
	public CollisionLine(double x1, double y1, double x2, double y2) {
		this.v1[0] = x1;
		this.v1[1] = y1;
		this.v2[0] = x2;
		this.v2[1] = y2;
//		System.out.println(x1 + " " + y1 + " " + x2 + " " + y2);
	}
	
/*	public boolean intersects(CollisionLine other) {
		Rectangle collisionArea = new Rectangle();
		collisionArea.setBounds((int) other.v1[0], (int) other.v1[1], (int) (other.v2[0] - other.v1[0]), (int) (other.v2[1] - other.v1[1]));
		double[] vI = getIntersection(other);
		boolean inside = collisionArea.contains(vI[0], vI[1]);
		return inside;
	}
*/	
/*	public double f(double x) {
		double m = (v2[1] - v1[1]) / (v2[0] - v1[0]);
		double b = v1[1] - (m * v1[0]);
		double y = m * x + b;
		return 0;
	}
*/
	public double[] getIntersection(double x0, double y0, double dx, double dy) {
		double[] intersection = {0,0};
		double t;
		double xF;
		double yF;
		
		if (v2[0] == v1[0]) {
			t = (v1[0] - x0) / dx; // dx cannot = 0; ie. cannot be parallel
		} else {
		double m = (v2[1] - v1[1]) / (v2[0] - v1[0]);
		double b = v1[1] - (m * v1[0]);
//		yF = m * xF + b;	
//		y0 + t * dy = m * x0 + t * dx + b;
		t = (m * x0 - y0 + b) / (dy - m * dx);
		}
		
		xF = x0 + t * dx;
		yF = y0 + t * dy;
		
		intersection[0] = xF;
		intersection[1] = yF;
		
/*		this.v1[0] + t * (this.v2[0] - this.v1[0]) = this.v1[0] + s * (this.v2[0] - this.v1[0]);
		this.v1[1] + t * (this.v2[1] - this.v1[1]) = this.v1[1] + s * (this.v2[1] - this.v1[1]);
		
		 t = this.v1[1] + s * (this.v2[1] - this.v1[1]) - this.v1[1]) / (this.v2[1] - this.v1[1]);
	
		double A1 = this.v1[0];
		double A2 = this.v1[1];
		double B1 = this.v2[0];
		double B2 = this.v2[1];
		double C1 = other.v1[0];
		double C2 = other.v1[1];
		double D1 = other.v2[0];
		double D2 = other.v2[1];
			
		this.v1[0] + (this.v1[1] + s * (this.v2[1] - this.v1[1] - this.v1[1]) / (this.v2[1] - this.v1[1])) * (this.v2[0] - this.v1[0]) = this.v1[0] + s * (this.v2[0] - this.v1[0]);
		A1 + (A2 + s * (B2 - A2 - A2) / (B2 - A2)) * (B1 - A1) = A1 + s * (B1 - A1);
		s * (B2 - A2 - A2) / (B2 - A2) - s = -A2;
			
		double s = -1 * A2 / ((B2 - 2 * A2) / (B2 - A2) - 1); 
		double t = A2 + s * (B2 - A2 - A2) / (B2 - A2);

		double xI = A1 + t * (B2 - A1);
		double yI = A2 + t * (B2 - A2);
	
		double m1 = (this.v2[1] - this.v1[1]) / (this.v2[0] - this.v1[0]);
		double b1 = this.v1[1] - m1 * this.v1[0];
		double m2 = (other.v2[1] - other.v1[1]) / (other.v2[0] - other.v1[0]);
		double b2 = this.v2[1] - m2 * this.v2[0];
//		y = m1 * x + b1;
//		y = m2 * x + b2;
		
		double xI = (b2-b1) / (m1 - m2);
		double yI = (m1 * xI) + b1;
		
		intersection[0] = xI;
		intersection[1] = yI;
*/		
		return intersection;
	}

}
