package uk.co.plogic.gwt.lib.widget.mapControl;

import java.util.ArrayList;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.events.OverlayLoadingEvent;
import uk.co.plogic.gwt.lib.events.OverlayLoadingEventHandler;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEventHandler;
import uk.co.plogic.gwt.lib.widget.WidgetImageResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * Not a true mapControl as it doesn't implement MapControl.
 *
 * It isn't controlled by the user clicking but does use the mapcontrol panel
 * to display info on overlays that are loading
 *
 * @author si
 *
 */
public class OverlayLoading {

	final Logger logger = Logger.getLogger("OverlayLoading");
	String loadingCopy = "Map data is loading";
	String targetDivId;
	WidgetImageResource images = GWT.create(WidgetImageResource.class);
	Image loadingIcon;
	HandlerManager eventBus;
	private MapControlPanel mapControlPanel;
	private HorizontalPanel loadingPanel;
	private ArrayList<String> overlayIdsLoading;
	final static int delayDuration = 1000; // wait before showing loading panel
    private Timer delayTimer;

	public OverlayLoading(HandlerManager eventBus, MapControlPanel mapControlPanel) {

		this.eventBus = eventBus;
		this.mapControlPanel = mapControlPanel;
		overlayIdsLoading = new ArrayList<String>();

		loadingIcon = new Image(images.loading_menu());
		loadingIcon.setStyleName("map_canvas_control_icon");

		delayTimer = new Timer() {
            @Override
            public void run() {
                doPanelUpdate();
            }
		};


		eventBus.addHandler(OverlayLoadingEvent.TYPE, new OverlayLoadingEventHandler() {
            @Override
            public void onOverlayLoading(OverlayLoadingEvent e) {

                logger.fine("loading event ["+e.getOverlayId()+"] "+e.isLoading());

                if( e.isLoading() && ! overlayIdsLoading.contains(e.getOverlayId()) ) {
                    overlayIdsLoading.add(e.getOverlayId());
                    logger.fine("loading ["+e.getOverlayId()+"]");
                }

                if( ! e.isLoading() && overlayIdsLoading.contains(e.getOverlayId()) ) {
                    overlayIdsLoading.remove(e.getOverlayId());
                    logger.fine("finished loading ["+e.getOverlayId()+"]");
                }

                requestPanelUpdate();
            }
		});

		eventBus.addHandler(OverlayVisibilityEvent.TYPE, new OverlayVisibilityEventHandler() {
            @Override
            public void onOverlayVisibilityChange(OverlayVisibilityEvent e) {

                // hide before it has finished loading
                if( ! e.isVisible() && overlayIdsLoading.contains(e.getOverlayId()) ) {
                    overlayIdsLoading.remove(e.getOverlayId());
                    requestPanelUpdate();
                }
            }
		});

	}

	private void requestPanelUpdate() {

	    if( overlayIdsLoading.size() == 0 )
	        hideOverlayLoading();
	    else {
	        delayTimer.cancel();
	        delayTimer.schedule(delayDuration);
	    }
	}

	private void doPanelUpdate() {

	    if( overlayIdsLoading.size() > 0 )
	         showOverlayLoading();
	    else hideOverlayLoading();

	}

	public void showOverlayLoading() {

	    if( loadingPanel == null ) {
	        loadingPanel = new HorizontalPanel();
	        loadingPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    } else
	        loadingPanel.clear();

	    loadingPanel.add(loadingIcon);
	    HTML copy = new HTML(loadingCopy);
	    copy.setStyleName("map_controls_copy_wide");
	    loadingPanel.add(copy);

	    if( mapControlPanel != null )
	        mapControlPanel.setExpandedContent(loadingPanel);
	}

	public void hideOverlayLoading() {
	    if( loadingPanel != null )
	        loadingPanel.clear();
	}

}
