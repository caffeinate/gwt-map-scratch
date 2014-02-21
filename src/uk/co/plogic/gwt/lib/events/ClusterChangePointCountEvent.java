package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

public class ClusterChangePointCountEvent  extends GwtEvent<ClusterChangePointCountEventHandler> {

    public static Type<ClusterChangePointCountEventHandler> TYPE =
            new Type<ClusterChangePointCountEventHandler>();

    private int pointCount;

    public ClusterChangePointCountEvent(int pointCount) {
    	this.pointCount = pointCount;
    }

	@Override
	public Type<ClusterChangePointCountEventHandler> getAssociatedType() { return TYPE; }

	@Override
	protected void dispatch(ClusterChangePointCountEventHandler h) { h.onPointCountChanged(this); }

	public int getPointCount() {
		return pointCount;
	}

}
