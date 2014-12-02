package uk.co.plogic.gwt.lib.widget.mapControl;

import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.ui.ShareUrl;
import uk.co.plogic.gwt.lib.widget.WidgetImageResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;

public class ShareMap extends Composite {

	final Logger logger = Logger.getLogger("ShareMap");
	boolean panelOpen = false;
	FlowPanel holdingPanel = new FlowPanel();
	FlowPanel closedPanel = new FlowPanel();
	FlowPanel openPanel = new FlowPanel();
	TextBox urlTextBox;
	ShareUrl urlScraper;
	WidgetImageResource images;
	String shareCopy = "Share your current view of the map";

	public ShareMap(final ShareUrl urlScraper) {
		this.urlScraper = urlScraper;
		images = GWT.create(WidgetImageResource.class);

		Image i = new Image(images.share());
		i.setAltText(shareCopy);
		i.setTitle(shareCopy);
		i.setStyleName("map_canvas_control_icon");
		closedPanel.add(i);
		i.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showSharePanel();
			}
		});

		openPanel = new FlowPanel();
		openPanel.setStyleName("share_panel_open");

		HTML h = new HTML(shareCopy);
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
		hideSharePanel();
		initWidget(holdingPanel);
	}
	
	private void showSharePanel() {
		panelOpen = true;
		
		holdingPanel.clear();

		String url = urlScraper.getUrlforShares();
		urlTextBox.setVisibleLength(url.length()+1);
		urlTextBox.setText(url);

		holdingPanel.add(openPanel);

		urlTextBox.selectAll();

	}

	private void hideSharePanel() {
		panelOpen = false;

		holdingPanel.clear();
		holdingPanel.add(closedPanel);
	}

}
