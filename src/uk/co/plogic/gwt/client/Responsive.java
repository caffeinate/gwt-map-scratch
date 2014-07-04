package uk.co.plogic.gwt.client;

import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.dom.DomElementByAttributeFinder;
import uk.co.plogic.gwt.lib.dom.DomParser;
import uk.co.plogic.gwt.lib.jso.PageVariables;
import uk.co.plogic.gwt.lib.map.GoogleMapAdapter;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.maps.gwt.client.GoogleMap;

public class Responsive implements EntryPoint {

	
	protected Logger logger = Logger.getLogger("Responsive");
	protected GoogleMap gMap;
	protected GoogleMapAdapter gma;
	private HandlerManager eventBus;
	protected PageVariables pv;
	protected RootPanel rootPanel;
	protected RootPanel infoPanel;
	protected RootPanel mapPanel;
	
	final String DOM_INFO_PANEL = "info_panel";
	final String DOM_MAP_DIV = "map_canvas";
	final int RESPONSIVE_TRIGGER_WIDTH = 700;
	
	// tmp vars
	Element infoPanelx;

	@Override
	public void onModuleLoad() {
		eventBus = new HandlerManager(null);
		pv = getPageVariables();

	    gma = new GoogleMapAdapter(eventBus, DOM_MAP_DIV);
	    gma.fitBounds(	pv.getDoubleVariable("LAT_A"), pv.getDoubleVariable("LNG_A"),
						pv.getDoubleVariable("LAT_B"), pv.getDoubleVariable("LNG_B"));
		
		infoPanel = RootPanel.get(DOM_INFO_PANEL);
		mapPanel = RootPanel.get(DOM_MAP_DIV);

		DomParser domParser = new DomParser();
		domParser.addHandler(new DomElementByAttributeFinder("id", "header") {
	        @Override
	        public void onDomElementFound(final Element element, String id) {
	        	infoPanelx = element;
	        }
	    });
		domParser.parseDom();
		

		Window.addResizeHandler(new ResizeHandler() {

			  Timer resizeTimer = new Timer() {  
			    @Override
			    public void run() {
			    	int width = Window.getClientWidth();
					logger.info("Root panel width:"+width);
					redraw( width < RESPONSIVE_TRIGGER_WIDTH );
					infoPanelx.setInnerHTML("window width is :"+width);
			    }
			  };

			  @Override
			  public void onResize(ResizeEvent event) {
			    resizeTimer.cancel();
			    resizeTimer.schedule(200);
			  }
		});
		
		// initial layout
		int width = Window.getClientWidth();
		infoPanelx.setInnerHTML("window width is :"+width);
		redraw( Window.getClientWidth() < RESPONSIVE_TRIGGER_WIDTH);

		
	}

	/**
	 * 
	 * @param reducedLayout - true for mobile devices
	 */
	private void redraw(boolean reducedLayout){
		if( reducedLayout ) {
			mapPanel.setVisible(false);
			infoPanel.addStyleName("mobile_view");
		} else {
			mapPanel.setVisible(true);
			infoPanel.removeStyleName("mobile_view");

			// bind Google map to DOM on demand
			if( gMap == null ) {
				logger.info("Binding map now");
				gMap = gma.create();
			}
		}
	}
	
	private native PageVariables getPageVariables() /*-{
		return $wnd["config"];
	}-*/;
}

