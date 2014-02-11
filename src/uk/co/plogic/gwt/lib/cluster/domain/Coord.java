package uk.co.plogic.gwt.lib.cluster.domain;

public class Coord {

	private double x,y;
	public final static double PRECISION = 0.00000001; // minimum difference between x and y

	public Coord(double x, double y) {
		super();
		setX(x);
		setY(y);
	}	

	public Coord() {
		// gotta trust the use of this constructor sets X & Y
		this(Double.NaN, Double.NaN);
	}

	public boolean isInitialised() {
		return ! (Double.isNaN(x) || Double.isNaN(y));
	}

	public boolean equals( Object arg ) {
		if ( (arg != null) && (arg instanceof Coord) ) {
			
			Coord a = (Coord) arg;

			if( ! a.isInitialised() ) {
				return false;
			}
			
			double q = Math.abs((this.x - a.getX()));
			double r = Math.abs((this.y - a.getY()));

			
			return ! (q > PRECISION || r > PRECISION);

		}
			return false;
	}
	

	public Coord clone() {
		
//		try {
//			return (Coord)super.clone();
//		} catch (CloneNotSupportedException e ) {
//			throw new Error("This should never happen!");
//		}
		
		return new Coord(x, y);
		
	}
	
	public String toString() {
		//return String.format("(%f,%f)", getX(), getY());
		return "("+getX()+","+getY()+")";
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}

	
}
