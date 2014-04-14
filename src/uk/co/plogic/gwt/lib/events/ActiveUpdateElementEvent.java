package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

public class ActiveUpdateElementEvent  extends GwtEvent<ActiveUpdateElementEventHandler> {

    public static Type<ActiveUpdateElementEventHandler> TYPE =
            new Type<ActiveUpdateElementEventHandler>();

    private String element_id;
    private String newInnerHtml;

    public ActiveUpdateElementEvent(String element_id, String newInnerHtml) {
    	this.element_id = element_id;
    	this.newInnerHtml = newInnerHtml;
    }

	@Override
	public Type<ActiveUpdateElementEventHandler> getAssociatedType() { return TYPE; }

	@Override
	protected void dispatch(ActiveUpdateElementEventHandler h) { h.onUpdate(this); }

	public String getElement_id() {
		return element_id;
	}

	public String getNewInnerHtml() {
		return newInnerHtml;
	}

}
