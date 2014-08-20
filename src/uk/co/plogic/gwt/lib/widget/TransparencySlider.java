package uk.co.plogic.gwt.lib.widget;

import java.util.HashSet;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.events.OverlayOpacityEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEventHandler;
import uk.co.plogic.gwt.lib.ui.ElementScrapper;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.SliderBar;

public class TransparencySlider extends Composite {
	
	final Logger logger = Logger.getLogger("TransparencySlider");
	protected HashSet<String> overlayId = new HashSet<String>();
	final HandlerManager eventBus;

	protected FlowPanel sliderPanel = new FlowPanel();
	protected final HTML transparencyLabel = new HTML("0 %");
	protected final SliderBar slider = new SliderBar(0.0, 1.0);

	public TransparencySlider(final HandlerManager eventBus, final Element e) {

		this.eventBus = eventBus;
		ElementScrapper es = new ElementScrapper();
		String overlayIds = es.findOverlayId(e, "span", "overlay_id");
		
		if(overlayIds.contains(",")) {
			// comma separated
			for(String oId : overlayIds.split(","))
				overlayId.add(oId);
		} else
			overlayId.add(overlayIds);
		

		sliderPanel.setStyleName("slider_panel");
		transparencyLabel.setStyleName("slider_label");
		slider.setStepSize(0.05);
		slider.setCurrentValue(0.8);
		slider.setEnabled(false);
		
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void onChange(Widget sender) {
				fireSliderChange();
			}
		});


		// disable slider when layer isn't visible
		eventBus.addHandler(OverlayVisibilityEvent.TYPE, new OverlayVisibilityEventHandler() {

			@Override
			public void onOverlayVisibilityChange(OverlayVisibilityEvent e) {
				
				if( overlayId.contains(e.getOverlayId()) ) {
					slider.setEnabled(e.isVisible());
				}
			}
		});

		
		sliderPanel.add(slider);
		sliderPanel.add(transparencyLabel);
		fireSliderChange();
		initWidget(sliderPanel);
	}

	public void fireSliderChange() {
		double opacity = slider.getCurrentValue();
		for(String overlayID : overlayId) {
			eventBus.fireEvent(new OverlayOpacityEvent(opacity, overlayID));
		}
		int transparency = (int) ((1-opacity)*100);
		transparencyLabel.setHTML(transparency+"% transparent");
	}

}
