package uk.co.plogic.gwt.lib.ui.activatedElements;

import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.dom.DomElementByAttributeFinder;
import uk.co.plogic.gwt.lib.dom.DomParser;
import uk.co.plogic.gwt.lib.ui.ShareUrl;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;


/**
 * 
 * Take an element like this-
 * <div id="share_map" class="map_canvas_overlay_top" alt="Share map">
 * 		<img src="media/images/share.png" style="width:16px; height:18px;"></img>
 * </div>
 * from the DOM and make it into a share widget.
 * 
 * Assumes bootstrap for the Ok button and uses .share_panel_open and .map_canvas_overlay_top
 * classes.
 * 
 * @author si
 *
 */
@Deprecated
public class ShareMap {

	final Logger logger = Logger.getLogger("ShareMap");
	boolean panelOpen = false;
	FlowPanel holdingPanel;
	FlowPanel closedPanel;
	FlowPanel openPanel;
	TextBox urlTextBox;
	ShareUrl urlScraper;

	/**
	 * 
	 * @param className 
	 */
	public ShareMap(DomParser domParser, final ShareUrl urlScraper, final String elementID) {

		this.urlScraper = urlScraper;
	    domParser.addHandler(new DomElementByAttributeFinder("id", elementID) {

	        @Override
	        public void onDomElementFound(Element element, String id) {
	        	setup(element);
	        	hideSharePanel();
	        }
	    });
	}

	private void setup(Element element) {

		RootPanel panel = RootPanel.get(element.getId());
		String originalTagContents = element.getInnerHTML();
		element.setInnerHTML("");

		holdingPanel = new FlowPanel();
		panel.add(holdingPanel);

		closedPanel = new FlowPanel();

		if( originalTagContents != null ) {
			HTML h = new HTML(originalTagContents);
			closedPanel.add(h);
			h.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					showSharePanel();
				}
			});
		}

		openPanel = new FlowPanel();
		openPanel.setStyleName("share_panel_open");

		HTML h = new HTML("Share your current view of the map.");
		openPanel.add(h);

		urlTextBox = new TextBox();
		urlTextBox.setReadOnly(true);
		openPanel.add(urlTextBox);

		//<button type="button" class="btn btn-primary btn-xs">Extra small button</button>
		Button bt = new Button("Ok");
		bt.setStyleName("btn");
		bt.addStyleName("btn-primary");
		bt.addStyleName("btn-xs");
		bt.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hideSharePanel();
			}
		});
		openPanel.add(bt);

	}

	public void showSharePanel() {
		panelOpen = true;
		
		holdingPanel.clear();

		String url = urlScraper.getUrlforShares();
		urlTextBox.setVisibleLength(url.length()+1);
		urlTextBox.setText(url);

		holdingPanel.add(openPanel);

		urlTextBox.selectAll();

	}

	public void hideSharePanel() {
		panelOpen = false;

		holdingPanel.clear();
		holdingPanel.add(closedPanel);
	}

}
