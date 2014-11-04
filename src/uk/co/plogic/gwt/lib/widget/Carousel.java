package uk.co.plogic.gwt.lib.widget;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class Carousel extends Composite {

	final AbsolutePanel viewport = new AbsolutePanel();
	FlexTable widgetsTable;
	int width = 100;
	int height = 100;
	int widgetCount = 0;
	int currentWidget = 0;
	static int animationDuration = 350;
	
	class AnimateViewpoint extends Animation {

		double startX; double diff;
		public AnimateViewpoint(int startX, int endX) {
			this.startX = (double) startX;
			diff = endX - startX;
		}

		@Override
		protected void onUpdate(double progress) {
			int currentPos = (int) (startX + (diff * progress));
			viewport.setWidgetPosition(widgetsTable, currentPos, 0);
		}

	}

	public Carousel() {

		FlowPanel holdingPanel = new FlowPanel();
		viewport.setPixelSize(width, height);
		viewport.setStyleName("carousel_viewpoint");

	    widgetsTable = new FlexTable();
	    widgetsTable.setCellSpacing(0);
	    widgetsTable.setCellPadding(0);
	    viewport.add(widgetsTable);
	    viewport.setWidgetPosition(widgetsTable,0,0);

	    Button previous = new Button("Previous");
	    previous.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(currentWidget<1) return;
				AnimateViewpoint av = new AnimateViewpoint( -1*currentWidget*width,
															-1*(currentWidget-1)*width);
				currentWidget--;
				av.run(animationDuration);
			}
	    });
	    Button next = new Button("Next");
	    next.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(currentWidget+1>=widgetCount) return;
				AnimateViewpoint av = new AnimateViewpoint( -1*currentWidget*width,
															-1*(currentWidget+1)*width);
				currentWidget++;
				av.run(animationDuration);
			}
	    });

	    holdingPanel.add(viewport);
	    holdingPanel.add(previous);
	    holdingPanel.add(next);
		initWidget(holdingPanel);
	}

	public void addWidget(Widget w) {
		int colPos = widgetsTable.getRowCount();
		if( colPos > 0 ) colPos = widgetsTable.getCellCount(0);
		widgetsTable.setWidget(0, colPos, w);
		widgetCount++;
	}
}
