package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

public class MouseOutEvent  extends GwtEvent<MouseOutEventHandler> {

    public static Type<MouseOutEventHandler> TYPE =
            new Type<MouseOutEventHandler>();

    private String classSuffix;

    public MouseOutEvent(String classSuffix) {
    	// this is the _XXXX on the additional class
    	this.classSuffix = classSuffix;
    }

	@Override
	public Type<MouseOutEventHandler> getAssociatedType() { return TYPE; }

	@Override
	protected void dispatch(MouseOutEventHandler h) { h.onMouseOut(this); }

	public String getMouseOut_id() {
		return classSuffix;
	}

}
