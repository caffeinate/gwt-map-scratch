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
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;

public class GazetteerSearch implements MapControl {

	final Logger logger = Logger.getLogger("GazetteerSearch");
	FocusPanel openPanel = new FocusPanel();
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

		openPanel.setStyleName("map_controls_panel_open");
		openPanel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // anything else in the panel needs to stopPropagation()
                // in their onClicks for this to work.
                closeControl();
            }
		});

		searchBox = new GazetteerSearchBox(eventBus, url);
		openPanel.add(searchBox);
		eventBus.addHandler(GazetteerResultsEvent.TYPE, new GazetteerResultsEventHandler() {
			@Override
			public void onResults(GazetteerResultsEvent e) {
				// the user choose something so hide the drop down
			    closeControl();
			}
		});

		closeControl();
	}

	public void closeControl() {
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

    @Override
    public boolean isOpen() {
        return openPanel.isAttached();
    }
}
