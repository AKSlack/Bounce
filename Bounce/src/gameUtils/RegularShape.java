package gameUtils;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

public class RegularShape {
	protected List<Vertex> vertices;
	protected List<Side> sides;
	public boolean placingEndPoint = true;
	public boolean selected = false;
	protected float[] color;
//	protected Texture texture;
	protected int drawShape = GL_TRIANGLE_FAN;
	static double pi = Math.PI;
	double initial;
	double arc;
	StateTracker glState;
	boolean showFigure = true;
	
	public boolean isShowFigure() {
		return showFigure;
	}

	public void setShowFigure(boolean showFigure) {
		this.showFigure = showFigure;
	}

	double orient = 0;
	Vertex position = new Vertex(0, 0, color); // in world dimensions
	double[] scale = {1 , 1};
	
	public class Vertex {
		
		public double x, y;
		public float[] color;
		
		public Vertex(double x, double y, float[] color) {
			this.x = x;
			this.y = y;
			this.color = color;
		}
		
		public void setXY(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}
	
	public class Side {
		double[] v1 = new double[2];
		double[] v2 = new double[2];
		double collisionT;
		protected String name;
		float[] color = {1f, 1f, 1f};
		
		
		public Side(double x1, double y1, double x2, double y2, String name) {
			if (x1 < x2) {
				this.v1[0] = x1;
				this.v1[1] = y1;
				this.v2[0] = x2;
				this.v2[1] = y2;				
			} else if (x1 > x2) {
				this.v1[0] = x2;
				this.v1[1] = y2;
				this.v2[0] = x1;
				this.v2[1] = y1;
			} else if (x1 == x2) {
				if (y1 < y2) {
					this.v1[0] = x1;
					this.v1[1] = y1;
					this.v2[0] = x2;
					this.v2[1] = y2;				
				} else {
					this.v1[0] = x2;
					this.v1[1] = y2;
					this.v2[0] = x1;
					this.v2[1] = y1;
				}
				
			}
			
			this.name = name;
		}
				
		public String toString() {
			return this.name;
		}
		
		public boolean intersects(Vector vector) { // vector expected to be in shape dimensions
			double[] vI = getIntersection(vector);
			
			boolean yInOrder = false;
			if (v1[1] < v2[1]) {
				yInOrder = true;
			}
			
			boolean inside = false;
			if (vI[0] >= v1[0] && vI[0] <= v2[0] /*&& vI[1] >= v1[1] && vI[1] <= v2[1]*/) { // verifies intersection is within line segment
				if (yInOrder && vI[1] >= v1[1] && vI[1] <= v2[1]) {
					inside = true;
				}
				if (!yInOrder && vI[1] <= v1[1] && vI[1] >= v2[1]) {
					inside = true;
				}
			}
			
			collisionT = vI[2];
			return inside;
		}
		
		public double[] getIntersection(Vector vector) { // vector expected to be in shape dimensions
			
			double x1 = v1[0];
			double y1 = v1[1];
			double x2 = v2[0];
			double y2 = v2[1];
			
			double x0 = vector.getX();
			double y0 = vector.getY();
			double dx = vector.getDx();
			double dy = vector.getDy();
			
			double t;
			double xF;
			double yF;
			
			if (x2 == x1) {
				t = (x1 - x0) / dx; // dx cannot = 0; ie. cannot be parallel
			} else {
				double m = (y2 - y1) / (x2 - x1);
				double b = y1 - (m * x1);
				t = (m * x0 - y0 + b) / (dy - m * dx); // cannot be parallel
			}
			
			xF = x0 + t * dx;
			yF = y0 + t * dy;
			
			double[] intersection = {xF, yF, t};			
			
			return intersection;
		}
				
		public double getOffset() {
			double m = (v2[1] - v1[1]) / (v2[0] - v1[0]);
			double dirAngle = Math.atan(m);
			if (dirAngle < 0) {
				dirAngle = dirAngle + Math.PI;
			}
			return dirAngle;
		}
		
		public void draw() {
			glBegin(GL_LINES);
			glVertex2d(v1[0], v1[1]);
			glVertex2d(v2[0], v2[1]);
		}
	}
	
	public RegularShape (double x, double y, double initial, int sides, double width, double height, StateTracker glState) {
		this.glState = glState;
		vertices = new ArrayList<Vertex>(sides + 2);
		this.sides = new ArrayList<Side>(sides);
		this.color = new float[] {1, 1, 1};
		position.x = x;
		position.y = y;
		setShape(initial, sides);
		populate();
		this.scale[0] = width;
		this.scale[1] = height;
		colorSurface();
	}

	protected void setShape(double initial, int sides) {
		this.initial = initial;
		this.arc = 2 * pi / (double) sides;
	
	}
	
	protected void colorSurface() {
		float ratio = 1.0f / vertices.size();
		for (float i = 0.0f; i < vertices.size(); i++) {
			vertices.get((int) i).color = new float[] {color[0] * i * ratio, color[1] * i * ratio, color[2] * i * ratio};
		}
	}

	public void paintSurface(String texture, float[] color) {
		this.color = color;
		if (texture == null) {
			colorSurface();
		}
	}
		
	public boolean contains(double x, double y) {
		// draw line and check for intersection with a side
		// then check distance to side vs distance to given point
		// may need to narrow to one point, if two+ intersections determined
		
		double xP = (x - this.position.x) / scale[0]; // puts coord in ref to object and scales down
		double yP = (y - this.position.y) / scale[1];

		if (orient != 0) {
			double currentAngle = Math.atan2(xP, yP);
			double newAngle = currentAngle + orient;
			xP = Math.cos(newAngle);
			yP = Math.sin(newAngle);
		}

		double givenDistance = Math.pow(Math.pow(xP, 2) + Math.pow(yP, 2), .5);
		
		if (givenDistance <= 1) {
//			System.out.println("Distance < 1");

			Vector vector = new Vector(0, 0, xP, yP); //uses point coord as dx and dy

			ArrayList<Side> sideOptions = findCollisionSides(vector); // creates array of sides which center vector collides with
			Side intsct = new Side(0, 0, 0, 0, "noName");
			for (int i = 0; i < sideOptions.size(); i++) {
//				System.out.println("pos Collision at time = " + sideOptions.get(i).collisionT);
				if (sideOptions.get(i).collisionT >= 0) {
					intsct = sideOptions.get(i);
				}
			}
			double[] intersection = intsct.getIntersection(vector);

			double insideDistance = Math.pow(Math.pow(intersection[0], 2) + Math.pow(intersection[1], 2), .5);

			if (givenDistance < insideDistance) {
				return true;
			}
		}

		return false;
	}

	public double[] getCollisionInfo(Vector vector) {
		// returns a double array that contains the coord of collision and the offset of the collision line
		double xP = (vector.getX() - this.position.x) / scale[0]; // puts coord in ref to object and scales down
		double yP = (vector.getY() - this.position.y) / scale[1];
		double dx = vector.getDx() / scale[0];
		double dy = vector.getDy() / scale[1];
		Vector scaledV = new Vector(xP, yP, dx, dy);
		
		ArrayList<Side> sideOptions = findCollisionSides(scaledV);
		Side intsct = sideOptions.get(0);
	
		if (sideOptions.size() > 1) {
			for (int i = 1; i < sideOptions.size(); i++) {
				if (sideOptions.get(i).collisionT < intsct.collisionT) {
					intsct = sideOptions.get(i);
				}
			}
		}

		double[] collisionPos = intsct.getIntersection(scaledV);
		double xIntsct = collisionPos[0] * scale[0] + this.position.x; // returns coord to world dimensions
		double yIntsct = collisionPos[1] * scale[1] + this.position.y;
		double offset = intsct.getOffset();
//		System.out.println("Side " + intsct.toString());
		
		double[] collisionInfo = {xIntsct, yIntsct, offset};
		
		return collisionInfo;
	}
	
	protected ArrayList<Side> findCollisionSides(Vector vector) {
		// returns an ArrayList of all the sides with potential collisions
		
		//Error when returns empty ArrayList....
		
		ArrayList<Side> intsctSides = new ArrayList<Side>();
		for (int s = 0; s < sides.size(); s++) {
			if (sides.get(s).intersects(vector)) {
				intsctSides.add(sides.get(s));
//				System.out.println("Collision time = " + sides.get(s).collisionT);
			}
		}
		return intsctSides;
	}
	
	public void setLocation(double x, double y) {
		this.position.setXY(x, y);
	}
	
	public void updateScale(double dx, double dy) {
		scale[0] += dx * 2;
		scale[1] += dy * 2;
	}
	
	public void updateLocation(double dx, double dy) { //update location of shape
		position.x += dx;
		position.y += dy;
	}
		
	public void updateRotation(double newAngle) {
		orient += newAngle * 360 / (2 * pi);		
	}
	
	public void spin(int delta) {
		double speed = .2;
		orient += delta * speed;
	}
	
	protected void populate() {
//		System.out.println("New Figure");
		vertices.add(new Vertex(0, 0, color));
		for (double angle = initial; angle < 2 * pi + arc; angle = angle + arc) {
			Vertex v = new Vertex(Math.cos(angle), Math.sin(angle), color);
//			System.out.println("Vertex at " + v.x + ", " + v.y);
			vertices.add(v);
		}
		for (int i = 2; i < vertices.size(); i++) {
			Side s = new Side(vertices.get(i-1).x, vertices.get(i-1).y, vertices.get(i).x, vertices.get(i).y, "Side" + (i - 1));
//			System.out.println("Vertex at " + s.v1[0] + ", " + s.v1[1] + " and " + s.v2[0] + ", " + s.v2[1]);
			sides.add(s);
		}		
	}
			
	public void draw() { // draw function for a triangle
	
		glState.setCurrentMatrix(GL_MODELVIEW); // sets current matrix to modify
		glLoadIdentity(); // sets matrix to identity

		glTranslated(position.x, position.y, 0); // moves current matrix from (0, 0) to given position
		glRotated(orient, 0, 0, 1); // rotates current matrix around z axis: angle "orient" in rad; +- 1 for rotational direction
		glScaled(scale[0], scale[1], 1); // scales x and y boundaries to desired size

		if (showFigure) {
			glBegin(drawShape);
			for (Vertex v : vertices) { //for each Vertex in vertices
				glState.setCurrentColor(v.color);
				glVertex2d(v.x, v.y);
			}
		} else {
			for (Side s : sides) { //for each Vertex in vertices
				glState.setCurrentColor(s.color);
//				System.out.println("Draw side " + s.name);
				s.draw();
			}
		}
		glEnd();
	}
}
