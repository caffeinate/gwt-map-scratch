package uk.co.plogic.gwt.lib.map.markers;

public interface ShapeMarker extends BaseMarker {

	public double getOpacity();
	public void setOpacity(double opacity);
	
	/**
	 * make the shape appear more clearly on the map.
	 * i.e. could be with opacity or with outline
	 */
	public void highlight();
	
	/**
	 * restore shape to original form
	 */
	public void unhighlight();
}
