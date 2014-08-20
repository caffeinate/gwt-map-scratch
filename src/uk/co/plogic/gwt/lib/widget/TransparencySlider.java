package uk.co.plogic.gwt.lib.widget;

import java.util.HashSet;

import uk.co.plogic.gwt.lib.events.OverlayOpacityEvent;
import uk.co.plogic.gwt.lib.ui.ElementScrapper;
import uk.co.plogic.gwt.lib.widget.Slider;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.kiouri.sliderbar.client.event.BarValueChangedEvent;
import com.kiouri.sliderbar.client.event.BarValueChangedHandler;


public class TransparencySlider extends Composite {
	
	protected HashSet<String> overlayId = new HashSet<String>();
	protected FlowPanel sliderPanel;
	
	public TransparencySlider(final HandlerManager eventBus, final Element e) {

		ElementScrapper es = new ElementScrapper();
		String overlayIds = es.findOverlayId(e, "span", "overlay_id");
		
		if(overlayIds.contains(",")) {
			// comma separated
			for(String oId : overlayIds.split(","))
				overlayId.add(oId);
		} else
			overlayId.add(overlayIds);
		
		sliderPanel = new FlowPanel();
		sliderPanel.setStyleName("slider_panel");

		final int sliderUnits = 20;
		final HTML transparencyLabel = new HTML("0 %");
		transparencyLabel.setStyleName("transparency_slider");
		Slider slider = new Slider(sliderUnits, "200px");
		slider.addBarValueChangedHandler(new BarValueChangedHandler() {

			@Override
			public void onBarValueChanged(BarValueChangedEvent event) {

				// the slider represents transparency so opacity is the reverse
				double opacity = (sliderUnits-event.getValue()) * 0.05;
				for(String overlayID : overlayId) {
					eventBus.fireEvent(new OverlayOpacityEvent(opacity, overlayID));
				}
				int transparency = (int) ((1-opacity)*100);
				transparencyLabel.setHTML(transparency+"% transparent");
			}
		});

		// 0.8 opacity
		slider.setValue(4);
		sliderPanel.add(slider);
		sliderPanel.add(transparencyLabel);

		initWidget(sliderPanel);
	}

}
