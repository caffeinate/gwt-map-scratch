package uk.co.plogic.gwt.client;

import uk.co.plogic.gwt.lib.ui.layout.ResponsiveSizing;
import uk.co.plogic.gwt.lib.widget.Carousel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class MoveWidget implements EntryPoint {

	String copy;
	final FlowPanel panelOne = new FlowPanel();
	final FlowPanel panelTwo = new FlowPanel();
	
	@Override
	public void onModuleLoad() {

		Element containerElement = Document.get().getElementById("container");
		final HTMLPanel hp = HTMLPanel.wrap(containerElement);
		
		Element e = hp.getElementById("some_copy");
		copy = e.getInnerHTML();
		e.removeFromParent();
		
	    hp.add(panelOne, "one");
	    panelOne.setSize("100%", "100%");

	    hp.add(panelTwo, "two");
	    panelTwo.setSize("100%", "100%");

		final Widget moveableBlock = getMovable("carousel"); //"simple"
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

	/**
	 * Two types of widget, a simple FocusPanel or a Carousel
	 * 
	 * @param mode
	 * @return
	 */
	public Widget getMovable(String mode) {

	    if( mode.equals("simple")) {

	    	final FocusPanel moveableBlock = new FocusPanel();
		    moveableBlock.setStyleName("movable");
		    moveableBlock.add(new HTML(copy));
		    moveableBlock.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Window.alert("click handler is working");
				}
			});
		    return (Widget) moveableBlock;

	    } else {
	    	final Carousel moveableBlock = new Carousel();
	    	ResponsiveSizing rs = new ResponsiveSizing(panelOne);
	    	moveableBlock.setSizing(rs);

	    	moveableBlock.addWidget(new HTML("Page 1"), null, null);
	    	moveableBlock.addWidget(new HTML("Page 2"), null, null);
	    	return (Widget) moveableBlock;
	    }

	}
	


}
