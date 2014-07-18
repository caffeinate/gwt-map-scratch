package uk.co.plogic.gwt.lib.ui;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * A layout controller to programatically adjust to mobile, fullscreen, iframe and full page layouts.
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

	HTML header;
	HTML footer;
	FlowPanel infoPanel;
	
	Widget map;

	public void setHtml(String headerHtml, String footerHtml, String infoPanelHtml) {

		header = new HTML(SafeHtmlUtils.fromTrustedString(headerHtml));
		header.setStyleName("header");
		
		footer = new HTML(SafeHtmlUtils.fromTrustedString(footerHtml));
		footer.setStyleName("footer");		

		infoPanel = new FlowPanel();
		HTML infoPanelc = new HTML(SafeHtmlUtils.fromTrustedString(infoPanelHtml));
		infoPanel.add(infoPanelc);
		infoPanel.setStyleName("info_panel");

	}
	
	public void setMap(Widget map) {
		this.map = map;
	}
	
	/**
	 * Initial display, should only be called once.
	 */
	public void display() {
	    // is mobile?
	    // is iframe?

		layoutPanel.addNorth(header, 100);
		layoutPanel.addSouth(footer, 100);
		layoutPanel.addWest(infoPanel, 250);
		
		RootLayoutPanel rp = RootLayoutPanel.get();
	    rp.add(layoutPanel);

		  Timer resizeTimer = new Timer() {  
			    @Override
			    public void run() {
			    	
			    	layoutPanel.setWidgetSize(infoPanel, 120);

			    	//p.setWidgetSize(header, 0);
			    	//p.setWidgetSize(footer, 0);
			    	layoutPanel.remove(footer);
			    	layoutPanel.remove(header);

			    	layoutPanel.animate(450);
			    }
		  };
		  resizeTimer.schedule(4000);

	}

}
