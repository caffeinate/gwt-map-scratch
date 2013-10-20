package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

public class ClickFireEvent  extends GwtEvent<ClickFireEventHandler> {

    public static Type<ClickFireEventHandler> TYPE =
            new Type<ClickFireEventHandler>();

    private String element_id;

    public ClickFireEvent(String element_id) {
    	this.element_id = element_id;
    }

	@Override
	public Type<ClickFireEventHandler> getAssociatedType() { return TYPE; }

	@Override
	protected void dispatch(ClickFireEventHandler h) { h.onClick(this); }

	public String getElement_id() {
		return element_id;
	}

}
