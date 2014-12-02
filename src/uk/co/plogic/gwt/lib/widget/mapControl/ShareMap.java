package uk.co.plogic.gwt.lib.widget.mapControl;

import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.ui.ShareUrl;
import uk.co.plogic.gwt.lib.widget.WidgetImageResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;

public class ShareMap implements MapControl {

	final Logger logger = Logger.getLogger("ShareMap");
	FlowPanel openPanel = new FlowPanel();
	TextBox urlTextBox;
	ShareUrl urlScraper;
	WidgetImageResource images;
	Image icon;
	String shareCopy = "Share your current view of the map";

	public ShareMap(final ShareUrl urlScraper) {
		this.urlScraper = urlScraper;
		images = GWT.create(WidgetImageResource.class);

		icon = new Image(images.share());
		icon.setAltText(shareCopy);
		icon.setTitle(shareCopy);
		icon.setStyleName("map_canvas_control_icon");

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
	}

	private void hideSharePanel() {
		openPanel.removeFromParent();
	}

	@Override
	public Panel openControl() {
		String url = urlScraper.getUrlforShares();
		urlTextBox.setVisibleLength(url.length()+1);
		urlTextBox.setText(url);
		return (Panel) openPanel;
	}

	@Override
	public Image getIcon() {
		return icon;
	}
}
