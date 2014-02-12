package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

public class ClusterSetPointCountEvent  extends GwtEvent<ClusterSetPointCountEventHandler> {

    public static Type<ClusterSetPointCountEventHandler> TYPE =
            new Type<ClusterSetPointCountEventHandler>();

    private int pointCount;

    public ClusterSetPointCountEvent(int pointCount) {
    	this.pointCount = pointCount;
    }

	@Override
	public Type<ClusterSetPointCountEventHandler> getAssociatedType() { return TYPE; }

	@Override
	protected void dispatch(ClusterSetPointCountEventHandler h) { h.onSetPointCount(this); }

	public int getPointCount() {
		return pointCount;
	}

}
