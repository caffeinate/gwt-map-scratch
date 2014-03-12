package uk.co.plogic.gwt.lib.map.markers;

import com.google.gwt.event.shared.HandlerManager;

abstract public class AbstractShapeMarker extends AbstractBaseMarker implements ShapeMarker {
	
	protected final HandlerManager eventBus;
	protected String fillColour;
	
	public AbstractShapeMarker(final HandlerManager eventBus, String Id) {
		super(Id);
		this.eventBus = eventBus;
	}
	
	public String getFillColour() { return fillColour; }

}
