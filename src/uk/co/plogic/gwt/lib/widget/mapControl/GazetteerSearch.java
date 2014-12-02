package uk.co.plogic.gwt.lib.widget.mapControl;

import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.events.GazetteerResultsEvent;
import uk.co.plogic.gwt.lib.events.GazetteerResultsEventHandler;
import uk.co.plogic.gwt.lib.widget.GazetteerSearchBox;
import uk.co.plogic.gwt.lib.widget.WidgetImageResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

public class GazetteerSearch extends Composite {

	final Logger logger = Logger.getLogger("ShareMap");
	boolean panelOpen = false;
	FlowPanel holdingPanel = new FlowPanel();
	FlowPanel closedPanel = new FlowPanel();
	FlowPanel openPanel = new FlowPanel();
	GazetteerSearchBox searchBox;
	String url;
	HandlerManager eventBus;
	WidgetImageResource images;
	String mouseOverCopy = "Share your current view of the map";

	public GazetteerSearch(HandlerManager eventBus, final String url) {
		this.url = url;
		this.eventBus = eventBus;
		images = GWT.create(WidgetImageResource.class);

		Image i = new Image(images.magnifying_glass());
		i.setAltText(mouseOverCopy);
		i.setTitle(mouseOverCopy);
		i.setStyleName("map_canvas_control_icon");
		closedPanel.add(i);
		i.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showExpandedPanel();
			}
		});

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
		initWidget(holdingPanel);
	}
	
	private void showExpandedPanel() {
		panelOpen = true;
		holdingPanel.clear();
		holdingPanel.add(openPanel);
	}

	private void hideExpandedPanel() {
		panelOpen = false;
		holdingPanel.clear();
		holdingPanel.add(closedPanel);
	}

}
