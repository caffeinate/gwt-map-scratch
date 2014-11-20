package uk.co.plogic.gwt.client;

import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.jso.PageVariables;
import uk.co.plogic.gwt.lib.jso.ResponsiveJso;
import uk.co.plogic.gwt.lib.map.GoogleMapAdapter;
import uk.co.plogic.gwt.lib.ui.layout.ResponsivePlusLayout;
import uk.co.plogic.gwt.lib.widget.Carousel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HTML;
import com.google.maps.gwt.client.GoogleMap;

public class ResponsivePlus implements EntryPoint {

	protected Logger logger = Logger.getLogger("ResponsivePlus");
	protected GoogleMap gMap;
	protected GoogleMapAdapter gma;
	private HandlerManager eventBus;
	protected PageVariables pv;

	final String DOM_INFO_PANEL_ID = "info_panel";
	final String DOM_HEADER_ID = "header";
	final String DOM_FOOTER_ID = "footer";

	@Override
	public void onModuleLoad() {
		eventBus = new HandlerManager(null);
		pv = getPageVariables();

		Element infoPanelElement = Document.get().getElementById(DOM_INFO_PANEL_ID);
		Element headerElement = Document.get().getElementById(DOM_HEADER_ID);
		Element footerElement = Document.get().getElementById(DOM_FOOTER_ID);

		// remove from DOM so that DomParser only handles items once and it allows
		// ids to be reused.
		infoPanelElement.removeFromParent();
		headerElement.removeFromParent();
		footerElement.removeFromParent();
		
		ResponsivePlusLayout layout = new ResponsivePlusLayout();
		layout.setHtml(	headerElement.getInnerHTML(),
						footerElement.getInnerHTML(),
						infoPanelElement.getInnerHTML());


	    gma = new GoogleMapAdapter(eventBus, layout.getMapContainerElement());
	    gma.fitBounds(	pv.getDoubleVariable("LAT_A"), pv.getDoubleVariable("LNG_A"),
						pv.getDoubleVariable("LAT_B"), pv.getDoubleVariable("LNG_B"));
		layout.setMap(gma.create());
		
		JsArray<ResponsiveJso> rej = pv.getResponsiveElements();
		for(int i=0; i< rej.length(); i++){
			ResponsiveJso re = rej.get(i);
			layout.addResponsiveElement(re.getTargetElementId(), re.getResponsiveMode(),
										re.getAddClass(), re.getRemoveClass());
		}
		
		
		for(String c_name : new String [] {	"example_carousel_1",
											"example_carousel_2",
											"example_carousel_3"}) {
	    layout.updateInfoPanelElement(c_name,
	    							  generateExampleCarousel(c_name),
	    							  true);
		}
		layout.display();

	}

	private Carousel generateExampleCarousel(String carouselName) {
	    Carousel c = new Carousel();
	    c.setSizing(200, 200);

	    // add pages
	    final HTML h1 = new HTML("I'm "+carouselName+" h1");
	    h1.setStyleName("orange");
	    h1.addStyleName("my-carousel-page");
	    c.addWidget("h1", h1, null);
	    HTML h2 = new HTML("I'm "+carouselName+" h2");
	    h2.setStyleName("blue");
	    h2.addStyleName("my-carousel-page");
	    c.addWidget("h1", h2, null);
	    HTML h3 = new HTML("I'm "+carouselName+" h3");
	    h3.setStyleName("green");
	    h3.addStyleName("my-carousel-page");
	    c.addWidget("h1", h3, null);
	    
	    return c;
	}
	
	private native PageVariables getPageVariables() /*-{
		return $wnd["config"];
	}-*/;

}
