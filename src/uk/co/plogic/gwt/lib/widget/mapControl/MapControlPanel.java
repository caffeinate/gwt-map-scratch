package uk.co.plogic.gwt.lib.widget.mapControl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;

public class MapControlPanel extends Composite {

	FlowPanel container;
	HorizontalPanel icons;
	FlowPanel expandedContent;

	public MapControlPanel() {

		container = new FlowPanel();
		container.setStyleName("map_canvas_controls");
		icons = new HorizontalPanel();
		icons.setStyleName("map_canvas_control_icons");
		container.add(icons);
		expandedContent = new FlowPanel();
		container.add(expandedContent);

		initWidget(container);

	}
	
	public void addControl(final MapControl mc) {

		Image icon = mc.getIcon();
		icon.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setExpandedContent((Panel) mc.openControl());
			}
		});
		icons.add(icon);

	}

	private void setExpandedContent(Panel p) {
		expandedContent.clear();
		expandedContent.add(p);
	}

}
