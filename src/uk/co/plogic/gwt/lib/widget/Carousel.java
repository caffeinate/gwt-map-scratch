package uk.co.plogic.gwt.lib.widget;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A Widget which holds other widgets which can be rotated through showing
 * one at a time.
 * 
 * @author si
 *
 */
public class Carousel extends Composite {

	final Logger logger = Logger.getLogger("Carousel");
	final AbsolutePanel viewport = new AbsolutePanel();
	int width = 100;
	int height = 100;
	int currentWidget = -1;
	int widgetCount = 0;
	ArrayList<Widget> widgets = new ArrayList<Widget>();
	static int animationDuration = 350;
	
	class AnimateViewpoint extends Animation {

		int direction; Widget w1; Widget w2; double w1_start; double w2_start;

		public AnimateViewpoint(int direction, Widget w1, Widget w2) {
			this.direction = direction;
			this.w1 = w1;
			this.w2 = w2;
			w1_start = viewport.getWidgetLeft(w1);
			w2_start = viewport.getWidgetLeft(w2);
		}

		@Override
		protected void onUpdate(double progress) {
			int currentPos = (int) (w1_start + (width * progress * direction));
			viewport.setWidgetPosition(w1, currentPos, 0);
			currentPos = (int) (w2_start + (width * progress * direction));
			viewport.setWidgetPosition(w2, currentPos, 0);
		}

	}

	public Carousel() {

		FlowPanel holdingPanel = new FlowPanel();
		viewport.setPixelSize(width, height);
		viewport.setStyleName("carousel_viewpoint");

	    Button previous = new Button("Previous");
	    previous.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				moveTo(-1);
			}
	    });
	    Button next = new Button("Next");
	    next.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				moveTo(1);
			}
	    });

	    holdingPanel.add(viewport);
	    holdingPanel.add(previous);
	    holdingPanel.add(next);
		initWidget(holdingPanel);
	}

	public void moveTo(int direction) {
		
		int widgetToShowIndex = currentWidget-direction;
		if( widgetToShowIndex < 0 ) widgetToShowIndex = widgetCount-1;
		if( widgetToShowIndex > widgetCount-1 ) widgetToShowIndex = 0;
		
		// position widgetToShow to one side of viewpoint
		Widget widgetToShow = widgets.get(widgetToShowIndex);
		viewport.setWidgetPosition(widgetToShow, width*direction, 0);
		
		Widget current = widgets.get(currentWidget);
		AnimateViewpoint av = new AnimateViewpoint( direction*-1, widgetToShow, current);
		av.run(animationDuration);
		currentWidget = widgetToShowIndex;
	}

	public void addWidget(Widget w) {
		widgets.add(w);
		if( currentWidget == -1 ) {
			currentWidget = 0;
			viewport.add(w, 0, 0);
		} else {
			// put it somewhere out of sight
			viewport.add(w, 0, height+10);
		}
		widgetCount = widgets.size();
	}
}
