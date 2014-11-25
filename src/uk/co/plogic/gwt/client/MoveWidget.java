package uk.co.plogic.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class MoveWidget implements EntryPoint {

	@Override
	public void onModuleLoad() {

		Element containerElement = Document.get().getElementById("container");
		HTMLPanel hp = HTMLPanel.wrap(containerElement);

	    final FlowPanel panelOne = new FlowPanel();
	    hp.add(panelOne, "one");

	    final FlowPanel panelTwo = new FlowPanel();
	    hp.add(panelTwo, "two");

	    final FlowPanel moveableBlock = new FlowPanel();
	    moveableBlock.setStyleName("movable");
	    panelOne.add(moveableBlock);

	    Button button = new Button("move");
	    hp.addAndReplaceElement(button, "move_button");

	    button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Widget parent = moveableBlock.getParent();
				//moveableBlock.removeFromParent();

				if( parent == panelOne ) {
					//panelOne.remove(moveableBlock);
					panelTwo.add(moveableBlock);
				}
				if( parent == panelTwo ) {
					//panelTwo.remove(moveableBlock);
					panelOne.add(moveableBlock);
				}

			}});
	}

}
