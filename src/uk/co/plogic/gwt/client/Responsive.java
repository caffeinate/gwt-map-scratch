package uk.co.plogic.gwt.client;

import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.jso.PageVariables;
import uk.co.plogic.gwt.lib.jso.ResponsiveJso;
import uk.co.plogic.gwt.lib.map.GoogleMapAdapter;
import uk.co.plogic.gwt.lib.ui.layout.ResponsiveLayout;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;

public class Responsive implements EntryPoint {

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
		
		ResponsiveLayout layout = new ResponsiveLayout();
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

		layout.display();

	}

	private native PageVariables getPageVariables() /*-{
		return $wnd["config"];
	}-*/;

}
