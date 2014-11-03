package uk.co.plogic.gwt.lib.widget;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

public class Carousel extends Composite {

	public Carousel() {
		FlowPanel maroonTile = new FlowPanel();
		maroonTile.setPixelSize(100, 100);
		maroonTile.setStyleName("maroon");
		initWidget(maroonTile);
	}
}
