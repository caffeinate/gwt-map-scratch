package uk.co.plogic.gwt.lib.ui.dataVisualisation;

import uk.co.plogic.gwt.lib.events.ActiveUpdateElementEvent;
import uk.co.plogic.gwt.lib.events.DataVisualisationEvent;
import uk.co.plogic.gwt.lib.events.DataVisualisationEventHandler;
import uk.co.plogic.gwt.lib.map.overlay.OverlayHasMarkers;
import uk.co.plogic.gwt.lib.utils.AttributeDictionary;
import uk.co.plogic.gwt.lib.utils.StringUtils;

import com.google.gwt.event.shared.HandlerManager;

/**
 * This visualisation requires an activatedElement
 * @author si
 *
 */
public class HtmlVisualisation {
	
	public HtmlVisualisation(final HandlerManager eventBus,
							 final String overlayId,
							 final String htmlTemplate,
							 final String targetActiveElementId) {
		
		eventBus.addHandler(DataVisualisationEvent.TYPE, new DataVisualisationEventHandler() {

			@Override
			public void onDataAvailableEvent(DataVisualisationEvent e) {
				String visualisationFor = e.getOverlay().getOverlayId(); 
				if(overlayId != null && overlayId.equals(visualisationFor)
				   && e.hasMarker() ) {

					OverlayHasMarkers overlay = (OverlayHasMarkers) e.getOverlay();
					AttributeDictionary d = overlay.getMarkerAttributes(e.getMarkerId());
					if( d != null ) {
						String builtHtml = StringUtils.renderHtml(htmlTemplate, d);
						eventBus.fireEvent(new ActiveUpdateElementEvent(targetActiveElementId, builtHtml));
					}
				}
			}
			
		});

	}
}
