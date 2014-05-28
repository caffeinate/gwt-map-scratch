package uk.co.plogic.gwt.lib.ui;

import java.util.HashSet;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEventHandler;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

/**
 * 
 * Set and remove CSS class on given element on hide/reveal of named dataset.
 * 
 * @author si
 *
 */
public class StyledByOverlayVisibility {
	
	protected HandlerManager eventBus;
	protected HashSet<String> overlayId = new HashSet<String>();
	protected String panelId; // element in DOM
	protected Panel panel;
	protected FlowPanel sliderPanel;
	protected Element domElement;
	protected Logger logger = Logger.getLogger("HideRevealWithOverlayVisibility");

	public StyledByOverlayVisibility(HandlerManager eventBus, Element e, final String className) {
		this.eventBus = eventBus;

		ElementScrapper es = new ElementScrapper();
		String overlayIds = es.findOverlayId(e, "span", "overlay_id");
		
		if(overlayIds.contains(",")) {
			// comma separated
			for(String oId : overlayIds.split(",")) {
				overlayId.add(oId);
				logger.finer("HideRevealWithOverlayVisibility found "+oId);
			}
		} else {
			overlayId.add(overlayIds);
			logger.finer("HideRevealWithOverlayVisibility found "+overlayIds);
		}
		
		domElement = e;

//		panelId = e.getId();
//		e.removeClassName("hidden");
//		panel = RootPanel.get(panelId);
//		panel.setVisible(false);

	    
	    eventBus.addHandler(OverlayVisibilityEvent.TYPE, new OverlayVisibilityEventHandler() {

	    	@Override
			public void onOverlayVisibilityChange(OverlayVisibilityEvent e) {
				String visualisationFor = e.getOverlayId();
				if(overlayId.contains(visualisationFor) ) {
					//panel.setVisible(e.isVisible());
					if( e.isVisible() )
						domElement.removeClassName(className);
					else
						domElement.addClassName(className);
				}
			}
	    });

	}

}
