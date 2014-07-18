package uk.co.plogic.gwt.lib.ui.layout;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
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
	int infoPanelWidth;
	
	Widget map;

	public void setHtml(String headerHtml, String footerHtml, String infoPanelHtml) {

		header = new HTML(SafeHtmlUtils.fromTrustedString(headerHtml));
		header.setStyleName("header");
		
		footer = new HTML(SafeHtmlUtils.fromTrustedString(footerHtml));
		footer.setStyleName("footer");		

		infoPanel = new FlowPanel();

		Button c = new Button("Close");
		c.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				closePanel();
			}
			
		});
		infoPanel.add(c);

		HTML infoPanelc = new HTML(SafeHtmlUtils.fromTrustedString(infoPanelHtml));
		infoPanel.add(infoPanelc);
		infoPanel.setStyleName("info_panel");

	}
	
	public void setMap(Widget map) {
		this.map = map;
	}
	
	public void closePanel() {
		
		layoutPanel.setWidgetSize(infoPanel, 0);
		layoutPanel.animate(250);
		
         Timer resizeTimer = new Timer() {  
			   @Override
			   public void run() {
			   }
         };
         // 2 seconds means 1.75 seconds after panel has gone
         resizeTimer.schedule(2000);
	}
	
	/**
	 * Initial display, should only be called once.
	 */
	public void display() {
	    // is mobile?
	    // is iframe?
		
		// 40%
		infoPanelWidth = (int) (Window.getClientWidth() * 0.4);

		layoutPanel.addNorth(header, 100);
		layoutPanel.addSouth(footer, 100);
		layoutPanel.addWest(infoPanel, infoPanelWidth);
		
		RootLayoutPanel rp = RootLayoutPanel.get();
	    rp.add(layoutPanel);

	}

}
