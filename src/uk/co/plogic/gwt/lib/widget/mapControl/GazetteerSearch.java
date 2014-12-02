package uk.co.plogic.gwt.lib.widget.mapControl;

import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.events.GazetteerResultsEvent;
import uk.co.plogic.gwt.lib.events.GazetteerResultsEventHandler;
import uk.co.plogic.gwt.lib.widget.GazetteerSearchBox;
import uk.co.plogic.gwt.lib.widget.WidgetImageResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;

public class GazetteerSearch implements MapControl {

	final Logger logger = Logger.getLogger("GazetteerSearch");
	FlowPanel openPanel = new FlowPanel();
	GazetteerSearchBox searchBox;
	String url;
	HandlerManager eventBus;
	WidgetImageResource images;
	Image icon;
	String mouseOverCopy = "Search for locations on the map";

	public GazetteerSearch(HandlerManager eventBus, final String url) {
		this.url = url;
		this.eventBus = eventBus;
		images = GWT.create(WidgetImageResource.class);

		icon = new Image(images.magnifying_glass());
		icon.setAltText(mouseOverCopy);
		icon.setTitle(mouseOverCopy);
		icon.setStyleName("map_canvas_control_icon");

		openPanel = new FlowPanel();
		openPanel.setStyleName("share_panel_open");

		searchBox = new GazetteerSearchBox(eventBus, url);
		openPanel.add(searchBox);
		eventBus.addHandler(GazetteerResultsEvent.TYPE, new GazetteerResultsEventHandler() {
			@Override
			public void onResults(GazetteerResultsEvent e) {
				// the user choose something so hide the drop down
				hideExpandedPanel();
			}
		});

		hideExpandedPanel();
	}
	
	private void hideExpandedPanel() {
		//openPanel.setVisible(false);
		openPanel.removeFromParent();
	}

	@Override
	public Panel openControl() {
		//openPanel.setVisible(true);
		return (Panel) openPanel;
	}

	@Override
	public Image getIcon() {
		return icon;
	}
}
