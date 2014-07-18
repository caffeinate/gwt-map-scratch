package uk.co.plogic.gwt.lib.ui.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * A layout controller to programatically adjust to mobile, fullscreen, iframe, named div and full page layouts.
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
	HorizontalPanel iconControls;
	int infoPanelWidth;
	ResponsiveLayoutImageResource images;
	int previousPanelSize;

	// panel content
	Image folderTab; // for map panel when info panel is closed
	FlowPanel mapPanel;
	Widget map;

	final int PANEL_RESIZE_PIXELS = 150;
	final int HEADER_HEIGHT_PIXELS = 50;
	final int FOOTER_HEIGHT_PIXELS = 50;
	final double INFO_PANEL_WINDOW_PORTION = 0.4;

	public ResponsiveLayout() {
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
		
		mapPanel.add(folderTab);

	}

	public void setHtml(String headerHtml, String footerHtml, String infoPanelHtml) {

		header = new HTML(SafeHtmlUtils.fromTrustedString(headerHtml));
		header.setStyleName("header");
		
		footer = new HTML(SafeHtmlUtils.fromTrustedString(footerHtml));
		footer.setStyleName("footer");		

		infoPanel = new FlowPanel();
		iconControls.setStyleName("info_panel_controls");
		infoPanel.add(iconControls);

		HTML infoPanelContent = new HTML(SafeHtmlUtils.fromTrustedString(infoPanelHtml));
		infoPanel.add(infoPanelContent);
		infoPanel.setStyleName("info_panel");

	}
	
	public void setMap(Widget map) {
		this.map = map;
		mapPanel.add(map);
	}
	
	public void closePanel() {
		
		previousPanelSize = infoPanelWidth;
		infoPanelWidth = 0;
		layoutPanel.setWidgetSize(infoPanel, 0);
		layoutPanel.animate(250);

        Timer resizeTimer = new Timer() {  
			   @Override
			   public void run() {
//				   layoutPanel.remove(infoPanel);
//				   layoutPanel.addWest(folderTab, 100);
//				   
//				   infoPanel.clear();
//				   infoPanel.add(folderTab);
//				   layoutPanel.setWidgetSize(infoPanel, 50);
				   
				   folderTab.setVisible(true);
				   redraw();
			   }
         };
         // only after panel has gone
         resizeTimer.schedule(250);
	}

	public void openPanel() {

		infoPanelWidth = previousPanelSize;
		folderTab.setVisible(false);
		
//		infoPanel.clear();
//		infoPanel.add(infoPanelContent);
		layoutPanel.setWidgetSize(infoPanel, infoPanelWidth);

//		layoutPanel.remove(folderTab);
//		layoutPanel.addWest(infoPanel, infoPanelWidth);
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
	    // is mobile?
	    // is iframe?

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

		// 40%
		infoPanelWidth = (int) (Window.getClientWidth() * INFO_PANEL_WINDOW_PORTION);

		layoutPanel.addNorth(header, HEADER_HEIGHT_PIXELS);
		layoutPanel.addSouth(footer, FOOTER_HEIGHT_PIXELS);
		layoutPanel.addWest(infoPanel, infoPanelWidth);
		layoutPanel.add(mapPanel);

		// TODO - named div
		RootLayoutPanel rp = RootLayoutPanel.get();
	    rp.add(layoutPanel);

	}
	
	/**
	 * to be called after window resizes and requests by the user to change
	 * size of panels.
	 * It repositions the map (preserving the centre point) and hides/reveals responsive
	 * parts of the layout.
	 */
	public void redraw() {
		layoutPanel.setWidgetSize(infoPanel, infoPanelWidth);
	}

}
