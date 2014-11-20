package uk.co.plogic.gwt.lib.ui.layout;

import java.util.ArrayList;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.widget.Carousel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CarouselBasedInfoPanel extends HTMLPanel {

	Logger logger = Logger.getLogger("CarouselBasedInfoPanel");
	String responsiveMode = "unknown";
	ArrayList<Carousel> carousels;
	int currentCarousel = 0;
	VerticalPanel controlPanel;

	public CarouselBasedInfoPanel(SafeHtml safeHtml) {
		super(safeHtml);
		setupControls();
	}

	public void setResponsiveMode(String mode) {
		responsiveMode = mode;
		
		//if(carousels == null)
			loadCarousels();
		
		logger.fine("found "+carousels.size()+" carousels");
		
		if( carousels.size() > 0 && responsiveMode.startsWith("mobile") ) {
			displayCarousel(currentCarousel);
			controlPanel.setVisible(true);
		} else {
			displayAllCarousels();
			controlPanel.setVisible(false);
		}
		
	}
	
	private void displayAllCarousels() {
		for(Carousel c : carousels)
			c.setVisible(true);
	}

	private void displayCarousel(int index) {
		for(int i=0; i<carousels.size(); i++) {
			Carousel c = carousels.get(i);
			if( i == index ) {
					c.setVisible(true);
			} else  c.setVisible(false);
		}
	}

	public void nextCarousel() {
		currentCarousel++;
		if(currentCarousel > carousels.size()-1 ) currentCarousel = 0;
		displayCarousel(currentCarousel);
	}

	public void previousCarousel() {
		currentCarousel--;
		if(currentCarousel < 0 ) currentCarousel = carousels.size()-1;
		displayCarousel(currentCarousel);
	}

	private void loadCarousels() {
		carousels = new ArrayList<Carousel>();
		logger.fine("Info panel has "+getWidgetCount()+" widgets");
		for(int i=0; i<getWidgetCount(); i++) {
			Widget w = getWidget(i);
			if( w instanceof Carousel ) {
				carousels.add((Carousel) w);
			}
		}
	}
	
	private void setupControls() {

		controlPanel = new VerticalPanel();
		
	    Button previous = new Button("<");
	    previous.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				previousCarousel();
			}
	    });
	    Button next = new Button(">");
	    next.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				nextCarousel();
			}
	    });

	    //controlPanel.setStyleName(CAROUSEL_FOOTER_CLASS);
		controlPanel.add(previous);
		controlPanel.add(next);
		controlPanel.setVisible(false);
	}
	
	public Widget getControlPanel() {
		return (Widget) controlPanel;
	}

}
