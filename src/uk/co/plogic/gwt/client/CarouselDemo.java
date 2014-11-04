package uk.co.plogic.gwt.client;

import uk.co.plogic.gwt.lib.widget.Carousel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class CarouselDemo implements EntryPoint {


	@Override
	public void onModuleLoad() {

		RootPanel r = RootPanel.get("container");
		Carousel c = new Carousel();
		r.add(c);

		for( String colour : new String [] {"red", "blue", "green"}) {
			FlowPanel tile = new FlowPanel();
			tile.setPixelSize(100, 100);
			tile.setStyleName(colour);
			c.addWidget(tile);
		}

		
	}
	
}
