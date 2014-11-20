package uk.co.plogic.gwt.lib.widget;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * SuperCarousel is a collection of Carousels.
 * 
 * It is used to simplify the UI for mobile devices by combining all the pages
 * in a collection of carousels into one.
 * 
 * @author si
 *
 */
public class SuperCarousel extends Carousel {
	ArrayList<Carousel> carousels;
	//AbsolutePanel parent;

	public SuperCarousel() {
		super();
		holdingPanel.setSize("100%", "100%");
	}
	
	/**
	 * initiate with data from all the carousels that this
	 * will take over from.
	 * @param carousel_list
	 */
	public void load(ArrayList<Carousel> carousel_list) {

		carousels = carousel_list;
	}
	
	public void display() {

		// copy all pages into this
		for(Carousel c : carousels) {
			
//			if(parent == null )
//				parent = (AbsolutePanel) c.getParent();
			c.removeFromParent();
			c.setVisible(true);
			c.setFooterVisibility(false);
			c.setSizing(this);
			c.setPixelAdjustments(footerOffset*-1, 0);
			addWidget(c.holdingPanel.getElement().getId(), c, c.holdingPanel.getElement());
		}
	}
	
//	public void undisplay() {
//		for(Carousel c : carousels)
//			parent.add(c);
//	}

	@Override
	public void onResize() {
		
		if(carousels==null)
			// not yet loaded
			return;
		
		super.onResize();

	    visibleWidgetsCount = 0;
		for(Carousel c : carousels) {
			c.onResize();
			visibleWidgetsCount += c.visibleWidgetsCount;
		}
		updateControls();
	}

}
