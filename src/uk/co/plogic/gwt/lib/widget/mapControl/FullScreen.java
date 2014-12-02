package uk.co.plogic.gwt.lib.widget.mapControl;

import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.events.MapResizeEvent;
import uk.co.plogic.gwt.lib.events.MapResizeEventHandler;
import uk.co.plogic.gwt.lib.widget.WidgetImageResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

public class FullScreen extends Composite {

	final Logger logger = Logger.getLogger("FullScreen");
	FlowPanel holdingPanel = new FlowPanel();
	String widgetCopyOpen = "Fullscreen";
	String widgetCopyClose = "Exit Fullscreen";
	String targetDivId;
	WidgetImageResource images = GWT.create(WidgetImageResource.class);
	Image panel_image = new Image(images.fullscreen_open());
	boolean currentlyInFullScreen = false;
	HandlerManager eventBus;

	public FullScreen(HandlerManager eventBus, final String targetDivId) {

		this.eventBus = eventBus;
		this.targetDivId = targetDivId;

		eventBus.addHandler(MapResizeEvent.TYPE, new MapResizeEventHandler() {
			@Override
			public void onResizeEvent(MapResizeEvent event) {

				if(currentlyInFullScreen != JnsiIsFullScreen() ) {
					currentlyInFullScreen = JnsiIsFullScreen();
					setPanelContents();
				}
			}
		});

		panel_image.setStyleName("map_canvas_control_icon");
		holdingPanel.add(panel_image);
		setPanelContents();
		panel_image.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				toggleFullScreen();
			}
		});

		initWidget(holdingPanel);
	}

	public void toggleFullScreen() {
		currentlyInFullScreen = !currentlyInFullScreen;

		// TODO - should we check if requestFullScreen was successful?

		if(currentlyInFullScreen)
			 requestFullScreen();
		else requestFullScreenExit();

		setPanelContents();
	}

	public void setPanelContents() {
		if(currentlyInFullScreen) {
			panel_image.setAltText(widgetCopyClose);
			panel_image.setTitle(widgetCopyClose);
			panel_image.setResource(images.fullscreen_close());
		} else {
			panel_image.setAltText(widgetCopyOpen);
			panel_image.setTitle(widgetCopyOpen);
			panel_image.setResource(images.fullscreen_open());
		}
	}

	public void requestFullScreen() {		
		logger.fine("going fullscreen");
		JnsiRequestFullscreen();
	}

	public void requestFullScreenExit() {
		JnsiRequestFullScreenExit();
	}

	private native boolean JnsiIsFullScreen() /*-{
		//return $doc.fullscreenEnabled || $doc.mozFullScreenEnabled || $doc.webkitFullscreenEnabled;
		//document.fullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement;

		if($doc.fullscreenEnabled) {
			return ($doc.fullscreenElement !== null);
		} else if($doc.mozFullScreenEnabled) {
			return ($doc.mozFullScreenElement !== null);;
		} else if($doc.webkitFullscreenEnabled) {
			return ($doc.webkitFullscreenElement !== null);;
		}
		return false;

	}-*/;

	private native void JnsiRequestFullScreenExit() /*-{

		if($doc.exitFullscreen) {
			$doc.exitFullscreen();
		} else if($doc.mozCancelFullScreen) {
		    $doc.mozCancelFullScreen();
		} else if($doc.webkitExitFullscreen) {
			$doc.webkitExitFullscreen();
		}
	}-*/;

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
