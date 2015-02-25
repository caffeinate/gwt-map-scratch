package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * An element can signal lock/unlock to stop other elements (espcially the
 * layout) from resizing.
 * This is mainly to stop on-screen keyboards breaking everything.
 * The approach tried before this was to detect device rotate by
 * checking _both_ width and height and only allow resize on rotate. This
 * is still a valid method but doesn't let devices make tiny changes midway
 * through their inital build of the page.
 * @author si
 *
 */
public class LockResizeEvent extends GwtEvent<LockResizeEventHandler> {

    public static Type<LockResizeEventHandler> TYPE = new Type<LockResizeEventHandler>();

    boolean lock;
    public LockResizeEvent(boolean lock) {
        this.lock = lock;
    }

    @Override
    protected void dispatch(LockResizeEventHandler handler) {
        handler.onLock(this);
    }

    @Override
    public Type<LockResizeEventHandler> getAssociatedType() {
        return TYPE;
    }

    public boolean isLocked() {
        return lock;
    }

}
