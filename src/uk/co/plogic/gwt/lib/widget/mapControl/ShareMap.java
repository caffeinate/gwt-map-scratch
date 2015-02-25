package uk.co.plogic.gwt.lib.widget.mapControl;

import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.ui.ShareUrl;
import uk.co.plogic.gwt.lib.widget.WidgetImageResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;

public class ShareMap implements MapControl {

	final Logger logger = Logger.getLogger("ShareMap");
	FocusPanel openPanel = new FocusPanel();
	TextBox urlTextBox;
	ShareUrl urlScraper;
	WidgetImageResource images;
	Image icon;
	String shareCopy = "Share your view of the map";

	public ShareMap(final ShareUrl urlScraper) {
		this.urlScraper = urlScraper;
		images = GWT.create(WidgetImageResource.class);

		icon = new Image(images.share());
		icon.setAltText(shareCopy);
		icon.setTitle(shareCopy);
		icon.setStyleName("map_canvas_control_icon");

		openPanel.setStyleName("map_controls_panel_open");
		openPanel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // anything else in the panel needs to stopPropagation()
                // in their onClicks for this to work.
                closeControl();
            }
        });
		FlowPanel wrap = new FlowPanel();
		wrap.setStyleName("input-group");
		openPanel.add(wrap);

		HTML h = new HTML(shareCopy);
		h.setStyleName("map_controls_copy");
		wrap.add(h);

		urlTextBox = new TextBox();
		urlTextBox.setReadOnly(true);
		urlTextBox.setStyleName("form-control");
		urlTextBox.addStyleName("readonly_control");
		urlTextBox.addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                // stop events bubbling - this allows the MapControl to hide
                // onClick elsewhere in the panel.
                urlTextBox.selectAll();
                event.stopPropagation();
            }
        });
		wrap.add(urlTextBox);
		closeControl();
	}

	public void closeControl() {
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

    @Override
    public boolean isOpen() {
        return openPanel.isAttached();
    }
}
