package uk.co.plogic.gwt.lib.ui.layout;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RequiresResize;
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
 * The 3 ways for this Layout are desktop, mobile landscape and mobile portrait
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
public class ResponsivePlusLayout {

	final DockLayoutPanel layoutPanel = new DockLayoutPanel(Unit.PX);
	RootLayoutPanel rootPanel;
	Logger logger = Logger.getLogger("ResponsiveLayout");
	int windowWidth;

	HTML header;
	HTML footer;
	ResizeLayoutPanel infoPanel;
	HorizontalPanel iconControls;
	HTMLPanel infoPanelContent;
	int infoPanelWidth;
	ResponsiveLayoutImageResource images;
	int previousPanelSize;

	// panel content
	Image folderTab; // for map panel when info panel is closed
	FlowPanel mapPanel;
	FlowPanel mapContainer; // this' element is given to GoogleMap.create(...)
	HorizontalPanel mapExtraControlsPanel;
	GoogleMap map;
	
	String responsiveMode = "unknown"; 	// at present there is just 'mobile' and 'full_version'
										// but this could be expanded. A String instead of an
										// enum as it makes for a better relationship with
										// uesr defined variable (responsive_mode) used by 
										// ResponsiveJso.
	ArrayList<ResponsiveElement> responsiveElements = new ArrayList<ResponsiveElement>();
										// @see addResponsiveElement()

	final int PANEL_RESIZE_PIXELS = 150;
	final int HEADER_HEIGHT_PIXELS = 50;
	final int FOOTER_HEIGHT_PIXELS = 30;
	final double INFO_PANEL_WINDOW_PORTION = 0.4;
	final int MOBILE_WIDTH_THRESHOLD = 720;

	class ResponsiveElement {
		String target_element_id;
		String responsive_mode;
		String add_class;
		String remove_class;
	}

	public ResponsivePlusLayout() {

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

		final ResponsivePlusLayout me = this;
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

		//HTML infoPanelContent = new HTML(SafeHtmlUtils.fromTrustedString(infoPanelHtml));
		infoPanelContent = new HTMLPanel(infoPanelHtml);
		infoContent.add(infoPanelContent);

		final HTMLPanel thisInfoPanel = infoPanelContent;
		infoPanel.addResizeHandler(new ResizeHandler(){
            public void onResize(ResizeEvent event){
				int panelWidth = infoPanel.getOffsetWidth();
				if( panelWidth < 22 )
					folderTab.setVisible(true);
				else
					folderTab.setVisible(false);

	            for(int i=0; i<thisInfoPanel.getWidgetCount(); i++) {
	            	Widget w = thisInfoPanel.getWidget(i);
	            	if (w instanceof RequiresResize)
	    	            ((RequiresResize) w).onResize();
	            }
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
	
	/**
	 * wrap (replace) of an element which is within the info panel's
	 * HTML with the given widget.
	 * 
	 * @param elementId
	 * @param w
	 * @return if successful
	 */
	public boolean updateInfoPanelElement(String elementId, Widget w, Boolean replace) {

		if( replace ) infoPanelContent.addAndReplaceElement(w, elementId);
		else		  infoPanelContent.add(w, elementId);
		return true;
	}

	/**
	 * When the responsive mode (i.e. 'mobile'; 'full_version' etc.) changes, update
	 * these HTML elements' style class.
	 * 
	 * If an element's 'responsive_mode' matches the current mode then it's 'add_class'
	 * style will be added and the 'remove_class' style will be removed. And importantly,
	 * vice versa.
	 * 
	 * @param target_element_id
	 * @param responsive_mode
	 * @param add_class
	 * @param remove_class
	 */
	public void addResponsiveElement(String target_element_id, String responsive_mode,
									 String add_class, String remove_class) {
		
		ResponsiveElement re = new ResponsiveElement();
		re.target_element_id = target_element_id;
		re.responsive_mode = responsive_mode;
		re.add_class= add_class;
		re.remove_class = remove_class;
		responsiveElements.add(re);
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
		
		if( isMobile() )
			layoutAsMobile();
		else
			layoutAsDesktop();

		// TODO - named div
		rootPanel = RootLayoutPanel.get();
	    rootPanel.add(layoutPanel);
	    onResize();
	}
	
	private void layoutAsDesktop() {
		// 40%
		infoPanelWidth = (int) (Window.getClientWidth() * INFO_PANEL_WINDOW_PORTION);

		layoutPanel.addNorth(header, HEADER_HEIGHT_PIXELS);
		layoutPanel.addSouth(footer, FOOTER_HEIGHT_PIXELS);
		layoutPanel.addWest(infoPanel, infoPanelWidth);
		layoutPanel.add(mapPanel);
		
	}
	
	private void layoutAsMobile() {
		// 40%
		infoPanelWidth = (int) (Window.getClientHeight() * INFO_PANEL_WINDOW_PORTION);
		infoPanelWidth = 100;
		layoutPanel.addSouth(infoPanel, infoPanelWidth);
		layoutPanel.add(mapPanel);
	}
	
	/**
	 * to be called after window resizes and requests by the user to change
	 * size of panels.
	 * It repositions the map (preserving the centre point) and hides/reveals responsive
	 * parts of the layout.
	 */
	public void redraw() {
		
		String previousResponsiveMode = responsiveMode; 

		if( isMobile() ) {
			responsiveMode = "mobile";

			// for example, hide the map
			//mapPanel.setVisible(false);
			//iconControls.setVisible(false);
			// full width info panel
			layoutPanel.setWidgetSize(infoPanel, windowWidth);
		} else {
			responsiveMode = "full_version";

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
		
		if( previousResponsiveMode.equals(responsiveMode) )
			return;
		
		for( ResponsiveElement re : responsiveElements ) {
			
			Element el = Document.get().getElementById(re.target_element_id);
			if( el == null )
				continue;
			
			if( responsiveMode.equals(re.responsive_mode) ) {
				if( re.remove_class != null )
					el.removeClassName(re.remove_class);

				if( re.add_class != null )
					el.addClassName(re.add_class);
			} else {
				if( re.remove_class != null )
					el.addClassName(re.remove_class);

				if( re.add_class != null )
					el.removeClassName(re.add_class);
			}
		}

	}

	public void onResize() {
		logger.fine("onResize called");
		windowWidth = Window.getClientWidth();
		//Document.get().getElementById("window_size").setInnerHTML("window width:"+windowWidth);
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

	public Widget getInfoPanel() {
		return (Widget) infoPanel;
	}

	/**
	 * browser has full screen mode - not tested
	 * @return
	 */
	public native boolean hasFullscreen() /*-{
		var fullscreenEnabled = $doc.fullscreenEnabled || $doc.mozFullScreenEnabled || $doc.webkitFullscreenEnabled;
		return fullscreenEnabled;
	}-*/;

	public native boolean isFullscreen() /*-{
		var fullscreenElement;
		var fullscreenEnabled = $doc.fullscreenEnabled || $doc.mozFullScreenEnabled || $doc.webkitFullscreenEnabled;
		
		if( ! fullscreenEnabled )
			return false;
		
		if($doc.fullscreenEnabled) fullscreenElement = $doc.fullscreenElement;
		else if($doc.mozFullScreenEnabled) fullscreenElement = $doc.mozFullScreenElement;
		else if($doc.webkitFullscreenEnabled) fullscreenElement = $doc.webkitFullscreenElement;

		return fullscreenElement!=null;
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
