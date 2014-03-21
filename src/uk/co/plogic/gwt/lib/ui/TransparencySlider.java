package uk.co.plogic.gwt.lib.ui;

import java.util.HashSet;

import uk.co.plogic.gwt.lib.events.OverlayOpacityEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEventHandler;
import uk.co.plogic.gwt.lib.widget.Slider;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.kiouri.sliderbar.client.event.BarValueChangedEvent;
import com.kiouri.sliderbar.client.event.BarValueChangedHandler;


public class TransparencySlider {
	
	protected HandlerManager eventBus;
	protected HashSet<String> overlayId = new HashSet<String>();
	protected String panelId; // element in DOM
	protected Panel panel;
	protected FlowPanel sliderPanel;
	
	public TransparencySlider(HandlerManager eventBus, final Element e) {

		this.eventBus = eventBus;

		ElementScrapper es = new ElementScrapper();
		String overlayIds = es.findOverlayId(e, "span", "overlay_id");
		
		if(overlayIds.contains(",")) {
			// comma separated
			for(String oId : overlayIds.split(","))
				overlayId.add(oId);
		} else
			overlayId.add(overlayIds);
		
		
		panelId = e.getId();
		e.removeClassName("hidden");
		panel = RootPanel.get(panelId);
		panel.setVisible(false);

	    
	    eventBus.addHandler(OverlayVisibilityEvent.TYPE, new OverlayVisibilityEventHandler() {

	    	@Override
			public void onOverlayVisibilityChange(OverlayVisibilityEvent e) {
				String visualisationFor = e.getOverlayId();
				if(overlayId.contains(visualisationFor) ) {
					panel.setVisible(e.isVisible());
					if( e.isVisible() )
						addSlider();
				}
			}
	    });
	    
	}
	
	private void addSlider() {
		// the slider only appears if the panel it's added to is visible at the
		// time of adding it

		if(sliderPanel != null)
			// already done
			return;

		final int sliderUnits = 20;
		sliderPanel = new FlowPanel();
		final HTML transparencyLabel = new HTML("0 %");
		transparencyLabel.setStyleName("transparency_slider");
		Slider slider = new Slider(sliderUnits, "90%");
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
		panel.add(sliderPanel);
	}

}
