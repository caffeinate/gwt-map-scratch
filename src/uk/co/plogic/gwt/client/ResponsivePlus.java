package uk.co.plogic.gwt.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

public class ResponsivePlus implements EntryPoint {

	protected Logger logger = Logger.getLogger("ResponsivePlus");

	final String DOM_INFO_PANEL = "info_panel";
	final String DOM_MAP_DIV = "map_canvas";
	final int RESPONSIVE_TRIGGER_WIDTH = 720;


	@Override
	public void onModuleLoad() {

		Element infoPanelElement = Document.get().getElementById(DOM_INFO_PANEL);
		Element mapPanelElement = Document.get().getElementById(DOM_MAP_DIV);
		Element headerElement = Document.get().getElementById("header");
		Element footerElement = Document.get().getElementById("footer");

		final SplitLayoutPanel p = new  SplitLayoutPanel();

		final HTML header = new HTML(SafeHtmlUtils.fromTrustedString(headerElement.getInnerHTML()));
		header.setStyleName("header");
		p.addNorth(header, 100);

		final HTML footer = new HTML(SafeHtmlUtils.fromTrustedString(footerElement.getInnerHTML()));
		footer.setStyleName("footer");
		p.addSouth(footer, 100);

		final FlowPanel infoPanel = new FlowPanel();
		HTML infoPanelc = new HTML(SafeHtmlUtils.fromTrustedString(infoPanelElement.getInnerHTML()));
		infoPanel.add(infoPanelc);
		infoPanel.setStyleName("info_panel");
		p.addWest(infoPanel, 250);
		
		HTML mapPanelc = new HTML(SafeHtmlUtils.fromTrustedString(mapPanelElement.getInnerHTML()));
		mapPanelc.setStyleName("map_canvas");
		p.add(mapPanelc);

		RootLayoutPanel rp = RootLayoutPanel.get();
	    rp.add(p);

	    // is mobile?
	    // is iframe?


		  Timer resizeTimer = new Timer() {  
			    @Override
			    public void run() {
			    	
			    	p.setWidgetSize(infoPanel, 120);

			    	//p.setWidgetSize(header, 0);
			    	//p.setWidgetSize(footer, 0);
			    	p.remove(footer);
			    	p.remove(header);

			    	p.animate(450);
			    }
		  };
		  resizeTimer.schedule(4000);


	}

}
