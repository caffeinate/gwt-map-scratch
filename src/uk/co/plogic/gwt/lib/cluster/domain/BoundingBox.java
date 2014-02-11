package uk.co.plogic.gwt.lib.cluster.domain;

public class BoundingBox {

	public static enum SplitOrientation { Horizontal, Vertical };
	Coord bottomLeft, topRight;
	
	public BoundingBox(Coord a, Coord b) {
		contructBoundingBox(a,b);
	}
	
	public BoundingBox() {
		topRight = new Coord();
		bottomLeft = new Coord();
	}
	
	public BoundingBox(double x1, double y1, double x2, double y2) {
		Coord c1 = new Coord(x1,y1);
		Coord c2 = new Coord(x2,y2);
		contructBoundingBox(c1,c2);
	}

	private void contructBoundingBox(Coord a, Coord b) {
		if( a.equals(b) ) {
			throw new IllegalArgumentException("Can't create bounding box with identical coords");
		}
		
		topRight = new Coord();
		bottomLeft = new Coord();

		// normalise
		if( a.getX() > b.getX() ) {
			topRight.setX(a.getX());
			bottomLeft.setX(b.getX());
		} else {
			topRight.setX(b.getX());
			bottomLeft.setX(a.getX());
		}
		
		if( a.getY() > b.getY() ) {
			topRight.setY(a.getY());
			bottomLeft.setY(b.getY());
		} else {
			topRight.setY(b.getY());
			bottomLeft.setY(a.getY());
		}		
	}
	
	public String toString() {
		return "["+bottomLeft.toString()+","+topRight.toString()+"]";
	}

	/**
	 * expand the bounding to include this coord
	 * @param cc
	 */
	public void add(Coord cc) {

		// nothing to do
		if( within(cc) ) { return; }

		double cx = cc.getX();
		double cy = cc.getY();
		
		if( Double.isNaN(topRight.getX()) ) {
			// init all
			topRight.setX(cx);
			topRight.setY(cy);
			bottomLeft.setX(cx);
			bottomLeft.setY(cy);
		}

		if( cx > topRight.getX() ) 	 { topRight.setX(cx); }
		if( cx < bottomLeft.getX() ) { bottomLeft.setX(cx); }
		
		if( cy > topRight.getY() )   { topRight.setY(cy); }
		if( cy < bottomLeft.getY() ) { bottomLeft.setY(cy); }

	}
	
	/**
	 * 
	 * @param p
	 * @return true if p is completely within BB
	 */
	public boolean within(Coord p) {
		double x = p.getX();
		double y = p.getY();
		
		// Note the convention of having => and <=. i.e. boundary condition
		// means a point will be 'within' two nodes
		
		return ( x >= bottomLeft.getX() && x <= topRight.getX()
			  && y >= bottomLeft.getY() && y <= topRight.getY());
	}
	
	public BoundingBox [] split(SplitOrientation split_orientation) {

		BoundingBox bb_a = new BoundingBox();
		BoundingBox bb_b = new BoundingBox();

		double xy;
		Coord xyc = new Coord();

		switch ( split_orientation ) {
		case Horizontal :

			xy = (topRight.getX()-bottomLeft.getX())/2 + bottomLeft.getX();
			xyc.setX(xy);
			xyc.setY(topRight.getY());
			bb_a.add(bottomLeft);
			bb_a.add(xyc);

			xyc.setY(bottomLeft.getY());
			bb_b.add(xyc);
			bb_b.add(topRight);

			break;

		case Vertical :

			xy = (topRight.getY()-bottomLeft.getY())/2 + bottomLeft.getY();
			xyc.setX(topRight.getX());
			xyc.setY(xy);
			bb_a.add(bottomLeft);
			bb_a.add(xyc);

			xyc.setX(bottomLeft.getX());
			bb_b.add(xyc);
			bb_b.add(topRight);

			break;

		default :
			assert false;

		}
		
		
		BoundingBox [] ret = new BoundingBox[2];
		ret[0] = bb_a;
		ret[1] = bb_b;

		return ret;
		
	}
	
	/**
	 * Find overlap between this and given bb
	 * @param bb
	 * @return a new bounding box or null for no intersection
	 */
	public BoundingBox intersection(BoundingBox bb) {
		
		
		
		Coord bb_bottomLeft = bb.getBottomLeft();
		Coord bb_topRight = bb.getTopRight();

		if(    bb_topRight.getX() < bottomLeft.getX()
			|| bb_bottomLeft.getX() > topRight.getX()
			|| bb_topRight.getY() < bottomLeft.getY()
			|| bb_bottomLeft.getY() > topRight.getY()
		  ) {
			return null;
		}
		// from here, there is an intersection
		Coord intersect_bottomLeft = new Coord();
		Coord intersect_topRight = new Coord();

		if( bb_bottomLeft.getX() > bottomLeft.getX() )
			intersect_bottomLeft.setX(bb_bottomLeft.getX());
		else
			intersect_bottomLeft.setX(bottomLeft.getX());
		
		if( bb_topRight.getX() > topRight.getX() )
			intersect_topRight.setX(topRight.getX());
		else
			intersect_topRight.setX(bb_topRight.getX());

		if( bb_bottomLeft.getY() > bottomLeft.getY() )
			intersect_bottomLeft.setY(bb_bottomLeft.getY());
		else
			intersect_bottomLeft.setY(bottomLeft.getY());
		
		if( bb_topRight.getY() > topRight.getY() )
			intersect_topRight.setY(topRight.getY());
		else
			intersect_topRight.setY(bb_topRight.getY());
		
		return new BoundingBox(intersect_bottomLeft, intersect_topRight);
	}
	
	public double getWidth() { return topRight.getX()-bottomLeft.getX();}
	public double getHeight() { return topRight.getY()-bottomLeft.getY();}
	public Coord getTopLeft() { return new Coord(bottomLeft.getX(), topRight.getY()); }
	public Coord getBottomLeft() { return bottomLeft; }
	public Coord getTopRight() { return topRight; }

}
