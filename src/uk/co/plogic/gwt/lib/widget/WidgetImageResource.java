package uk.co.plogic.gwt.lib.widget;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface WidgetImageResource extends ClientBundle {

	@Source("resources/share.png")
	ImageResource share();

	@Source("resources/fullscreen_open.png")
	ImageResource fullscreen_open();

	@Source("resources/fullscreen_close.png")
	ImageResource fullscreen_close();
	
	// https://openclipart.org/detail/188650/search-ideogram-by-libberry-188650
	@Source("resources/magnifying_glass.png")
	ImageResource magnifying_glass();

}
