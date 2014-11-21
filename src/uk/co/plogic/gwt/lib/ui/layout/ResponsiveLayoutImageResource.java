package uk.co.plogic.gwt.lib.ui.layout;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ResponsiveLayoutImageResource extends ClientBundle {

	@Source("resources/tab_expand.png")
	ImageResource tab();

	@Source("resources/tab_expand_horizontal.png")
	ImageResource tab_horizontal();

	@Source("resources/tab_expand_vertical.png")
	ImageResource tab_vertical();

	//https://openclipart.org/detail/33655/tango-style-multiply-by-warszawianka
	@Source("resources/cross.png")
	ImageResource cross();
	
	//https://openclipart.org/detail/30679/tango-blue-go-previous-by-warszawianka
	@Source("resources/left_arrow.png")
	ImageResource leftArrow();
	
	//https://openclipart.org/detail/30667/tango-blue-go-next-by-warszawianka
	@Source("resources/right_arrow.png")
	ImageResource rightArrow();
	
	//https://openclipart.org/detail/35503/tango-emblem-important-by-warszawianka
	@Source("resources/dot.png")
	ImageResource dot();

	@Source("resources/dot_selected.png")
	ImageResource dot_selected();
	
}
