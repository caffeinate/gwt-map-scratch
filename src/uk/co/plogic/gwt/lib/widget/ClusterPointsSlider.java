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

	final Logger logger = Logger.getLogger("ClusterPointsSlider");
	protected String overlayId;
	final HandlerManager eventBus;
	private int requestedNoPoints = 45; // TODO - this should be fed in by an
	                                    // event or similar

	protected FlowPanel sliderPanel = new FlowPanel();
	protected final HTML pointsVisibleLabel = new HTML("0");
	protected Slider slider;

	public ClusterPointsSlider(final HandlerManager eventBus, final Element e) {

		this.eventBus = eventBus;

        if( e.hasAttribute("data-overlay-id") ) {
            overlayId = e.getAttribute("data-overlay-id");
            logger.fine("using data-overlay-id="+overlayId);
        } else {
            logger.warning("data-overlay-id attribute is missing");
        }

		sliderPanel.setStyleName("slider_panel");
		pointsVisibleLabel.setStyleName("slider_label");


		// disable slider when layer isn't visible

		eventBus.addHandler(OverlayVisibilityEvent.TYPE, new OverlayVisibilityEventHandler() {

			@Override
			public void onOverlayVisibilityChange(OverlayVisibilityEvent e) {
				if( overlayId.equals(e.getOverlayId()) ) {
					setVisible(e.isVisible());
				}
			}
		});

		initWidget(sliderPanel);
	}

	@Override
	public void setVisible(boolean show) {

	    sliderPanel.setVisible(show);
	    if( show && slider == null ) {
	        buildSlider();
	    }

	}

	private void buildSlider() {

	    slider = new Slider(9, "90%");
	    sliderPanel.add(slider);
        sliderPanel.add(pointsVisibleLabel);

        slider.addBarValueChangedHandler(new BarValueChangedHandler() {

            @Override
            public void onBarValueChanged(BarValueChangedEvent event) {
                int scale = event.getValue()+1;
                int requestedPoints = scale*scale*5;
                pointsVisibleLabel.setHTML(""+requestedPoints);
                eventBus.fireEvent(new ClusterChangePointCountEvent(requestedPoints));
            }
        });

        //int sliderPosition = (int) Math.sqrt(clusterPoints.getRequestedNoPoints()/5)-1;
        int sliderPosition = (int) Math.sqrt(requestedNoPoints/5)-1;
        logger.finer(overlayId+" slider at "+sliderPosition+" for "+requestedNoPoints+" points");
        slider.setValue(sliderPosition);
	}

}
