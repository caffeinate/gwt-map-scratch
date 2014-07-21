package uk.co.plogic.gwt.lib.widget;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

public class FullScreen extends Composite {

	final Logger logger = Logger.getLogger("FullScreen");
	FlowPanel holdingPanel = new FlowPanel();
	WidgetImageResource images;
	String widgetCopy = "Fullscreen";
	String targetDivId;
	
	public FullScreen(final String targetDivId) {

		this.targetDivId = targetDivId;
		images = GWT.create(WidgetImageResource.class);

		Image i = new Image(images.fullscreen());
		i.setAltText(widgetCopy);
		i.setStyleName("map_canvas_control_icon");
		holdingPanel.add(i);
		i.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				requestFullScreen();
			}
		});

		initWidget(holdingPanel);
	}

	public void requestFullScreen() {		
		logger.fine("going fullscreen");
		JnsiRequestFullscreen();
	}

    private native void JnsiRequestFullscreen() /*-{
    	
    	var element = $doc.documentElement;
		if(element.requestFullscreen) {
			element.requestFullscreen();
		} else if(element.mozRequestFullScreen) {
			element.mozRequestFullScreen();
		} else if(element.webkitRequestFullscreen) {
			element.webkitRequestFullscreen();
		} else if(element.msRequestFullscreen) {
			element.msRequestFullscreen();
		}
	}-*/;

}
