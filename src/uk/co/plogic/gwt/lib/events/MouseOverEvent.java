package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

public class MouseOverEvent  extends GwtEvent<MouseOverEventHandler> {

    public static Type<MouseOverEventHandler> TYPE =
            new Type<MouseOverEventHandler>();

    private String classSuffix;

    public MouseOverEvent(String classSuffix) {
    	// this is the _XXXX on the additional class
    	this.classSuffix = classSuffix;
    }

	@Override
	public Type<MouseOverEventHandler> getAssociatedType() { return TYPE; }

	@Override
	protected void dispatch(MouseOverEventHandler h) { h.onMouseOver(this); }

	public String getMouseOver_id() {
		return classSuffix;
	}

}
