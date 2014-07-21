package uk.co.plogic.gwt.lib.ui.layout;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;

/**
 * 
 * A layout controller to programatically adjust to mobile, fullscreen, iframe, named div and full page layouts.
 * 
 * There are four parts-
 * - infoPanel - Adjustable side panel
 * - header - HTML - hides when in iframe or fullscreen
 * - footer - HTML
 * - map - requires a Google map
 * 
 * @author si
 *
 */
public class ResponsiveLayout {

	final SplitLayoutPanel layoutPanel = new  SplitLayoutPanel();
	RootLayoutPanel rootPanel;
	Logger logger = Logger.getLogger("ResponsiveLayout");
	int windowWidth;

	HTML header;
	HTML footer;
	ResizeLayoutPanel infoPanel;
	HorizontalPanel iconControls;
	int infoPanelWidth;
	ResponsiveLayoutImageResource images;
	int previousPanelSize;

	// panel content
	Image folderTab; // for map panel when info panel is closed
	FlowPanel mapPanel;
	FlowPanel mapContainer; // this' element is given to GoogleMap.create(...)
	HorizontalPanel mapExtraControlsPanel;
	GoogleMap map;

	final int PANEL_RESIZE_PIXELS = 150;
	final int HEADER_HEIGHT_PIXELS = 50;
	final int FOOTER_HEIGHT_PIXELS = 30;
	final double INFO_PANEL_WINDOW_PORTION = 0.4;
	final int MOBILE_WIDTH_THRESHOLD = 720;

	public ResponsiveLayout() {

		iconControls = new HorizontalPanel();
		mapPanel = new FlowPanel();
		mapPanel.setStyleName("map_canvas");
		images = GWT.create(ResponsiveLayoutImageResource.class);

		folderTab = new Image(images.tab());
		folderTab.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				openPanel();
			}
			
		});
		folderTab.setVisible(false);
		folderTab.setStyleName("folder_tab");
		mapPanel.add(folderTab);
		mapContainer = new FlowPanel();
		mapContainer.setStyleName("map_canvas");
		mapPanel.add(mapContainer);

		final ResponsiveLayout me = this;
		Window.addResizeHandler(new ResizeHandler() {

		  Timer resizeTimer = new Timer() {  
			  @Override
			  public void run() { me.onResize(); }
		  };

		  @Override
		  public void onResize(ResizeEvent event) {
			  resizeTimer.cancel();
			  resizeTimer.schedule(200);
		  }
		});
	}

	public Element getMapContainerElement() {
		return mapContainer.getElement();
	}

	public void setHtml(String headerHtml, String footerHtml, String infoPanelHtml) {

		header = new HTML(SafeHtmlUtils.fromTrustedString(headerHtml));
		header.setStyleName("header");
		
		footer = new HTML(SafeHtmlUtils.fromTrustedString(footerHtml));
		footer.setStyleName("footer");		

		infoPanel = new ResizeLayoutPanel();
		FlowPanel infoContent = new FlowPanel();
		infoPanel.add(infoContent);
		infoContent.setStyleName("info_panel");

		iconControls.setStyleName("info_panel_controls");
		infoContent.add(iconControls);

		HTML infoPanelContent = new HTML(SafeHtmlUtils.fromTrustedString(infoPanelHtml));
		infoContent.add(infoPanelContent);

		infoPanel.addResizeHandler(new ResizeHandler(){
            public void onResize(ResizeEvent event){
				int panelWidth = infoPanel.getOffsetWidth();
				if( panelWidth < 22 )
					folderTab.setVisible(true);
				else
					folderTab.setVisible(false);
            }
        });

	}
	
	public void setMap(GoogleMap googleMap) {
		map = googleMap;
	}

	public void addMapControl(Widget c) {
		if( mapExtraControlsPanel == null ) {
			// init
			mapExtraControlsPanel = new HorizontalPanel();
			mapExtraControlsPanel.setStyleName("map_canvas_controls");
			mapPanel.add(mapExtraControlsPanel);
		}
		mapExtraControlsPanel.add(c);
	}

	public void closePanel() {
		
		previousPanelSize = infoPanelWidth;
		infoPanelWidth = 0;
		layoutPanel.setWidgetSize(infoPanel, 0);
		layoutPanel.animate(250);

        Timer resizeTimer = new Timer() {  
			   @Override
			   public void run() {
				   redraw();
			   }
         };
         // only after panel has gone
         resizeTimer.schedule(250);
	}

	public void openPanel() {

		infoPanelWidth = previousPanelSize;
		layoutPanel.setWidgetSize(infoPanel, infoPanelWidth);
		layoutPanel.animate(250);
		
        Timer resizeTimer = new Timer() {  
			   @Override
			   public void run() {
				   redraw();
			   }
         };
         resizeTimer.schedule(250);

	}

	/**
	 * Initial display, should only be called once.
	 */
	public void display() {

		// general layout setup
		Image shrink = new Image(images.leftArrow());
		shrink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				if( (infoPanelWidth-PANEL_RESIZE_PIXELS) < PANEL_RESIZE_PIXELS+50 )
					closePanel();
				else
					infoPanelWidth -= PANEL_RESIZE_PIXELS;
				redraw();
			}
		});
		iconControls.add(shrink);

		Image enlarge = new Image(images.rightArrow());
		enlarge.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				infoPanelWidth += PANEL_RESIZE_PIXELS;
				redraw();
			}
		});
		iconControls.add(enlarge);

		Image close = new Image(images.cross());
		close.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				closePanel();
			}
		});
		iconControls.add(close);

		// image that appears when info panel is hidden

		// 40%
		infoPanelWidth = (int) (Window.getClientWidth() * INFO_PANEL_WINDOW_PORTION);

		layoutPanel.addNorth(header, HEADER_HEIGHT_PIXELS);
		layoutPanel.addSouth(footer, FOOTER_HEIGHT_PIXELS);
		layoutPanel.addWest(infoPanel, infoPanelWidth);
		layoutPanel.add(mapPanel);

		// TODO - named div
		rootPanel = RootLayoutPanel.get();
	    rootPanel.add(layoutPanel);
	    onResize();
	}
	
	/**
	 * to be called after window resizes and requests by the user to change
	 * size of panels.
	 * It repositions the map (preserving the centre point) and hides/reveals responsive
	 * parts of the layout.
	 */
	public void redraw() {

		if( isMobile() ) {
			// for example, hide the map
			mapPanel.setVisible(false);
			iconControls.setVisible(false);
			infoPanel.addStyleName("mobile_view");
			// full width info panel
			layoutPanel.setWidgetSize(infoPanel, windowWidth);
		} else {

			if( isIframed() && ! isFullscreen() )
				layoutPanel.setWidgetSize(header, 0);
			else
				layoutPanel.setWidgetSize(header, HEADER_HEIGHT_PIXELS);

			mapPanel.setVisible(true);
			iconControls.setVisible(true);
			infoPanel.removeStyleName("mobile_view");

			LatLng centre = null;
			if( map != null )
				centre = map.getCenter();

			// resize
			layoutPanel.setWidgetSize(infoPanel, infoPanelWidth);
			map.triggerResize();

			//re-centre
			if( centre != null )
				map.setCenter(centre);

		}

	}
	
	public void onResize() {
		logger.fine("onResize called");
		windowWidth = Window.getClientWidth();
		redraw();
	    rootPanel.onResize();
	}
	
	public boolean isMobile() {
		logger.fine("window width is "+windowWidth);
		return windowWidth <= MOBILE_WIDTH_THRESHOLD;
	}

	public boolean isIframed() {
		String baseUrl = getParentUrl();
	    String ourUrl = Window.Location.getHref();
	    return ! baseUrl.equals(ourUrl);
	}

	public native boolean isFullscreen() /*-{
		var fullscreenEnabled = $doc.fullscreenEnabled || $doc.mozFullScreenEnabled || $doc.webkitFullscreenEnabled;
		return fullscreenEnabled;
	}-*/;

	/**
	 * 
	 * @return url of parent frame. This will only work (i.e. security exception)
	 * when the site fully occupies the browser or is an iframe from the same
	 * domain as the parent.
	 */
    private static final native String getParentUrl() /*-{
		try {
			return $wnd.parent.location.href;
		} catch(e) {
			return "";
		}
	}-*/;

}
