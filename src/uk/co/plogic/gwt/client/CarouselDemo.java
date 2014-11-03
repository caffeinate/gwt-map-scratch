package uk.co.plogic.gwt.client;

import uk.co.plogic.gwt.lib.widget.Carousel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class CarouselDemo implements EntryPoint {


	@Override
	public void onModuleLoad() {

		RootPanel r = RootPanel.get("container");

		Carousel c = new Carousel();
		r.add(c);
		
	}
	
}
