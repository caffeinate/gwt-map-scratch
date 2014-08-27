package uk.co.plogic.gwt.lib.ui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEventHandler;

import com.google.gwt.event.shared.HandlerManager;

/**
 * For a given set of overlays, only allow one to be visible at a time.
 * 
 * This is achieved by watching for events when an overlay is made visible and
 * then sending events to turn all others in set off.
 * 
 * @author si
 *
 */
public class MutuallyExclusiveOverlays {

	final Logger logger = Logger.getLogger("MutuallyExclusiveOverlays");

	/**
	 * 
	 * @param eventBus
	 * @param overlay_set - colon separated serial numbers
	 */
	public MutuallyExclusiveOverlays(final HandlerManager eventBus, String overlaySet) {
		
		String[] o = overlaySet.split(":");
		final Set<String> overlays = new HashSet<String>();
		for(int i=0; i<o.length; i++) {
			logger.fine("exclusion adding:"+o[i]);
			overlays.add(o[i]);
		}
		
        eventBus.addHandler(OverlayVisibilityEvent.TYPE, new OverlayVisibilityEventHandler() {

			@Override
			public void onOverlayVisibilityChange(OverlayVisibilityEvent e) {

				if( ! e.isVisible() )
					// layer is being turned off
					return;
				
				if( overlays.contains(e.getOverlayId()) ) {
					String msg = "Found match for exclusion. On="+e.getOverlayId()+" Off=";
					
					Iterator<String> it = overlays.iterator();
					while( it.hasNext() ) {
						String oId = it.next();
						if( ! e.getOverlayId().equals(oId) ) {
							eventBus.fireEvent(new OverlayVisibilityEvent(false, oId));
							msg += oId+":";
						}
					}
					logger.fine(msg);
				}
			}
		});
	}

}
