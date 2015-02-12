package uk.co.plogic.gwt.lib.widget;

import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.events.ClusterChangePointCountEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEventHandler;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.kiouri.sliderbar.client.event.BarValueChangedEvent;
import com.kiouri.sliderbar.client.event.BarValueChangedHandler;

public class ClusterPointsSlider extends Composite {

	final Logger logger = Logger.getLogger("TransparencySlider");
	protected String overlayId;
	final HandlerManager eventBus;

	protected FlowPanel sliderPanel = new FlowPanel();
	protected final HTML transparencyLabel = new HTML("0 %");
	protected final Slider slider = new Slider(9, "90%");

	public ClusterPointsSlider(final HandlerManager eventBus, final Element e) {

		this.eventBus = eventBus;


	      if( e.hasAttribute("data-overlay-id") ) {
	            overlayId = e.getAttribute("data-overlay-id");
	            logger.fine("using data-overlay-id="+overlayId);
	        } else {
	            logger.warning("data-overlay-id attribute is missing");
	        }

		sliderPanel.setStyleName("slider_panel");
		transparencyLabel.setStyleName("slider_label");
		sliderPanel.add(slider);
        sliderPanel.add(transparencyLabel);

        slider.addBarValueChangedHandler(new BarValueChangedHandler() {

            @Override
            public void onBarValueChanged(BarValueChangedEvent event) {
                int scale = event.getValue()+1;
                int requestedPoints = scale*scale*5;
                transparencyLabel.setHTML(""+requestedPoints);
                eventBus.fireEvent(new ClusterChangePointCountEvent(requestedPoints));
            }
        });

        //int sliderPosition = (int) Math.sqrt(clusterPoints.getRequestedNoPoints()/5)-1;
        int sliderPosition = 2;
        slider.setValue(sliderPosition);

		// disable slider when layer isn't visible
		eventBus.addHandler(OverlayVisibilityEvent.TYPE, new OverlayVisibilityEventHandler() {

			@Override
			public void onOverlayVisibilityChange(OverlayVisibilityEvent e) {

				if( overlayId.contains(e.getOverlayId()) ) {
					sliderPanel.setVisible(e.isVisible());
				}
			}
		});

		sliderPanel.add(slider);
		sliderPanel.add(transparencyLabel);
		initWidget(sliderPanel);
	}

}
