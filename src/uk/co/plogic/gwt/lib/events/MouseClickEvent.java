package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

public class MouseClickEvent  extends GwtEvent<MouseClickEventHandler> {

    public static Type<MouseClickEventHandler> TYPE =
            new Type<MouseClickEventHandler>();

    private String classSuffix;

    public MouseClickEvent(String classSuffix) {
    	// this is the _XXXX on the additional class
    	this.classSuffix = classSuffix;
    }

	@Override
	public Type<MouseClickEventHandler> getAssociatedType() { return TYPE; }

	@Override
	protected void dispatch(MouseClickEventHandler h) { h.onMouseClick(this); }

	public String getMouseClick_id() {
		return classSuffix;
	}

}
