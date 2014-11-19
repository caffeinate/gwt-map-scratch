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
import com.google.gwt.user.client.ui.Widget;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;

/**
 * 
 * A layout controller to programatically adjust to mobile, fullscreen, iframe,
 * named div and full page layouts.
 * 
 * The 3 ways for this Layout are desktop, mobile landscape and mobile portrait.
 * The info panel is on the left for desktop+landscape but at the bottom for
 * portrait.  
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

	DockLayoutPanel layoutPanel;
	RootLayoutPanel rootPanel;
	Logger logger = Logger.getLogger("ResponsivePlusLayout");
	int windowWidth;
	int windowHeight;

	HTML header;
	HTML footer;
	ResizeLayoutPanel infoPanel;
	HorizontalPanel iconControls;
	HTMLPanel infoPanelContent;
	int infoPanelSize;
	int infoPanelHeight;
	ResponsiveLayoutImageResource images;
	int previousPanelSize;

	// panel content
	Image folderTab; // for map panel when info panel is closed
	FlowPanel mapPanel;
	FlowPanel mapContainer; // this' element is given to GoogleMap.create(...)
	HorizontalPanel mapExtraControlsPanel;
	GoogleMap map;
	
	String responsiveMode = "unknown"; 	// 'mobile_landscape', 'mobile_portrait'
										// and 'full_version'.
										// A String instead of an enum as it makes
										// for a better relationship with
										// user defined variable (responsive_mode) used by 
										// ResponsiveJso.
	ArrayList<ResponsiveElement> responsiveElements = new ArrayList<ResponsiveElement>();
										// @see addResponsiveElement()

	final int PANEL_RESIZE_PIXELS = 150; // size of jump when click the resize arrows
	final int HEADER_HEIGHT_PIXELS = 50;
	final int FOOTER_HEIGHT_PIXELS = 30;
	final double INFO_PANEL_WINDOW_PORTION = 0.4;
	final int MOBILE_THRESHOLD_PIXELS = 720;

	class ResponsiveElement {
		String target_element_id;
		String responsive_mode;
		String add_class;
		String remove_class;
	}

	public ResponsivePlusLayout() {

		windowWidth = Window.getClientWidth();
		windowHeight = Window.getClientHeight();
		rootPanel = RootLayoutPanel.get();

		iconControls = new HorizontalPanel();
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

		mapPanel = new FlowPanel();
		mapPanel.setStyleName("map_canvas");
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
		previousPanelSize = infoPanelSize;
		infoPanelSize = 0;
		resizeInfoPanel();
	}

	public void openPanel() {
		infoPanelSize = previousPanelSize;
		resizeInfoPanel();
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
				
				if( (infoPanelSize-PANEL_RESIZE_PIXELS) < PANEL_RESIZE_PIXELS+50 )
					closePanel();
				else {
					infoPanelSize -= PANEL_RESIZE_PIXELS;
					resizeInfoPanel();
				}
			}
		});
		iconControls.add(shrink);

		Image enlarge = new Image(images.rightArrow());
		enlarge.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				infoPanelSize += PANEL_RESIZE_PIXELS;
				resizeInfoPanel();
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

	    onResize();
	}
	
	private void resizeInfoPanel() {
		LatLng centre = null;
		if( map != null )
			centre = map.getCenter();

		layoutPanel.setWidgetSize(infoPanel, infoPanelSize);
		layoutPanel.animate(250);
		
		final LatLng cc = centre;
        Timer resizeTimer = new Timer() {  
			   @Override
			   public void run() {
				   if( cc != null && map != null ) {
					   map.triggerResize();
					   map.setCenter(cc);
				   }
			   }
         };
         // only after panel has gone
         resizeTimer.schedule(250);
	}

	private void layoutAsDesktop() {
		// 40%
		infoPanelSize = (int) (windowWidth * INFO_PANEL_WINDOW_PORTION);

		int header_height;
		if( isIframed() && ! isFullscreen() )
			header_height = 0;
		else
			header_height = HEADER_HEIGHT_PIXELS;
		
		layoutPanel.addNorth(header, header_height);
		layoutPanel.addSouth(footer, FOOTER_HEIGHT_PIXELS);
		layoutPanel.addWest(infoPanel, infoPanelSize);
		layoutPanel.add(mapPanel);
		
	}
	
	private void layoutAsMobile() {
		// 40%
		if( responsiveMode.equals("mobile_portrait") ) {
			infoPanelSize = (int) (windowHeight * INFO_PANEL_WINDOW_PORTION);
			layoutPanel.addSouth(infoPanel, infoPanelSize);
		} else {
			// landscape
			infoPanelSize = (int) (windowWidth * INFO_PANEL_WINDOW_PORTION);
			layoutPanel.addWest(infoPanel, infoPanelSize);
		}
		layoutPanel.add(mapPanel);
	}

	/**
	 * to be called after window resizes.
	 * 
	 * On change in responsive mode it re-creates the layout panel.
	 * It also hides/reveals responsive parts of the layout.
	 * 
	 */
	public void redraw() {
		
		String previousMode = responsiveMode; 

		if( isMobile() ) {
			
			if( windowHeight > windowWidth )
				responsiveMode = "mobile_portrait";
			else
				responsiveMode = "mobile_landscape";

			iconControls.setVisible(false);

		} else {
			responsiveMode = "full_version";
			iconControls.setVisible(true);
		}

		if( previousMode.equals(responsiveMode) )
			return;

		logger.fine("Switching responsive mode from "+previousMode+" to "+responsiveMode);

		// else re-create layout panel and add it to root
		rootPanel.clear();
		layoutPanel = new DockLayoutPanel(Unit.PX);
		if( responsiveMode.equals("full_version") )
			 layoutAsDesktop();
		else layoutAsMobile();

	    rootPanel.add(layoutPanel);

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
		windowWidth = Window.getClientWidth();
		windowHeight = Window.getClientHeight();
		logger.fine("onResize called: window is "+windowWidth+"x"+windowHeight);
		//Document.get().getElementById("window_size").setInnerHTML("window width:"+windowWidth);
		redraw();
	    rootPanel.onResize();
	}
	
	public boolean isMobile() {
		return windowWidth <= MOBILE_THRESHOLD_PIXELS 
			|| windowHeight <= MOBILE_THRESHOLD_PIXELS;
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
