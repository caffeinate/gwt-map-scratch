package uk.co.plogic.gwt.lib.widget;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface WidgetImageResource extends ClientBundle {

	@Source("resources/share.png")
	ImageResource share();

	@Source("resources/fullscreen.png")
	ImageResource fullscreen();
}
