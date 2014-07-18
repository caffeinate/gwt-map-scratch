package uk.co.plogic.gwt.client;

import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.ui.layout.ResponsiveLayout;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTML;

public class ResponsivePlus implements EntryPoint {

	protected Logger logger = Logger.getLogger("ResponsivePlus");

	final String DOM_INFO_PANEL_ID = "info_panel";
	final String DOM_HEADER_ID = "header";
	final String DOM_FOOTER_ID = "footer";
	final int RESPONSIVE_TRIGGER_WIDTH = 720;


	@Override
	public void onModuleLoad() {

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


		HTML mapPanelc = new HTML("The Map X");
		mapPanelc.setStyleName("map_canvas");
		layout.setMap(mapPanelc);

		layout.display();

	}

}
