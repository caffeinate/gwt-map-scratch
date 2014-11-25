package uk.co.plogic.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class MoveWidget implements EntryPoint {

	@Override
	public void onModuleLoad() {

		Element containerElement = Document.get().getElementById("container");
		HTMLPanel hp = HTMLPanel.wrap(containerElement);
		
		Element e = hp.getElementById("some_copy");
		String copy = e.getInnerHTML();
		e.removeFromParent();
		

	    final FlowPanel panelOne = new FlowPanel();
	    hp.add(panelOne, "one");

	    final FlowPanel panelTwo = new FlowPanel();
	    hp.add(panelTwo, "two");

	    final FocusPanel moveableBlock = new FocusPanel();
	    moveableBlock.setStyleName("movable");
	    moveableBlock.add(new HTML(copy));
	    panelOne.add(moveableBlock);
	    moveableBlock.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("click handler is working");
			}
		});

	    final FocusPanel green = new FocusPanel();
	    green.add(new HTML("Green Panel"));
	    green.setStyleName("green");
	    panelOne.add(green);

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
