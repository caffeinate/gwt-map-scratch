package uk.co.plogic.gwt.client;

import java.util.ArrayList;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.dom.DomElementByClassNameFinder;
import uk.co.plogic.gwt.lib.dom.DomParser;
import uk.co.plogic.gwt.lib.jso.PageVariables;
import uk.co.plogic.gwt.lib.jso.ResponsiveJso;
import uk.co.plogic.gwt.lib.map.GoogleMapAdapter;
import uk.co.plogic.gwt.lib.ui.layout.ResponsivePlusLayout;
import uk.co.plogic.gwt.lib.ui.layout.ResponsiveSizing;
import uk.co.plogic.gwt.lib.widget.Carousel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.maps.gwt.client.GoogleMap;

public class ResponsivePlus implements EntryPoint {

	protected Logger logger = Logger.getLogger("ResponsivePlus");
	protected GoogleMap gMap;
	protected GoogleMapAdapter gma;
	private HandlerManager eventBus;
	protected PageVariables pv;
	protected ResponsivePlusLayout layout;

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

		layout = new ResponsivePlusLayout();
		layout.setHtml(	headerElement.getInnerHTML(),
						footerElement.getInnerHTML(),
						infoPanelElement.getInnerHTML());

//	    gma = new GoogleMapAdapter(eventBus, layout.getMapContainerPanel());
//	    gma.fitBounds(	pv.getDoubleVariable("LAT_A"), pv.getDoubleVariable("LNG_A"),
//						pv.getDoubleVariable("LAT_B"), pv.getDoubleVariable("LNG_B"));
//		layout.setMap(gma);

//		JsArray<ResponsiveJso> rej = pv.getResponsiveElements();
//		for(int i=0; i< rej.length(); i++){
//			ResponsiveJso re = rej.get(i);
//			layout.addResponsiveElement(re.getTargetElementId(), re.getResponsiveMode(),
//										re.getAddClass(), re.getRemoveClass());
//		}
		layout.initialBuild();

//		ResponsiveSizing rs = new ResponsiveSizing(layout.getInfoPanel());
//		rs.setScaleFactor(1, 0.333);
//		rs.setPixelAdjustments(-30, -30);
//		for(String c_name : new String [] {	"example_carousel_1",
//											"example_carousel_2",
//											"example_carousel_3"}) {
//
//			Carousel c = generateExampleCarousel(c_name);
//			c.setSizing(rs);
//			layout.updateInfoPanelElement(c_name, c, false);
//		}


		domManipulation();
		layout.onResize();

	}

	private void domManipulation() {

	    DomParser domParser = new DomParser();
        final ArrayList<Element> carouselElements = new ArrayList<Element>();
        domParser.addHandler(new DomElementByClassNameFinder(Carousel.CAROUSEL_CLASS) {
            @Override
            public void onDomElementFound(Element element, String id) {
                logger.fine("found carousel class for id:"+id);
                carouselElements.add(element);
            }
        });
        domParser.parseDom();


        Widget infoPanel = layout.getInfoPanel();
        for(Element e : carouselElements) {
            // Carousel removes header and page items from this element
            // anything else will be left
            logger.fine("found carousel:"+e.getId());
            Carousel c = new Carousel(e);
            c.setParentWidget(infoPanel);
            layout.updateInfoPanelElement(e.getId(), c, false);
        }

    }

	private native PageVariables getPageVariables() /*-{
		return $wnd["config"];
	}-*/;

}
