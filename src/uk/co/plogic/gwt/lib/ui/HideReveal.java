package uk.co.plogic.gwt.lib.ui;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Element;

import uk.co.plogic.gwt.lib.dom.AttachClickFireEvent;
import uk.co.plogic.gwt.lib.dom.DomElementByAttributeFinder;
import uk.co.plogic.gwt.lib.dom.DomParser;
import uk.co.plogic.gwt.lib.dom.ShowHide;
import uk.co.plogic.gwt.lib.events.ClickFireEvent;
import uk.co.plogic.gwt.lib.events.ClickFireEventHandler;

/**
 * UI Tool to hide and reveal DIVs on click events.
 * @author si
 *
 */
public class HideReveal {

	public HideReveal(HandlerManager eventBus, String hideReveal) {
		
		// TODO - make this more general so it can also use classes

		// target:hide_id:reveal_id
		// target is the menu and it is animated to shrink to nothing
		// the <div> with reveal_id is un-hidden at end of animation period
		// the <div> with hide_id is assumed to be within the target
		
		String[] parts = hideReveal.split(":");
		final String targetDiv = parts[0];
		final String hideDiv = parts[1];
		final String revealDiv = parts[2];
		
		new AttachClickFireEvent(eventBus, parts[1]);
		new AttachClickFireEvent(eventBus, parts[2]);
		final HideRevealAnimation hra = new HideRevealAnimation(targetDiv);
		
		eventBus.addHandler(ClickFireEvent.TYPE, new ClickFireEventHandler() {

			@Override
			public void onClick(ClickFireEvent e) {
				
				if( e.getElement_id().equals(hideDiv)) {
					//new ShowHide(targetDiv).hide();
					hra.hide();
					new ShowHide(revealDiv).show();
				} else if( e.getElement_id().equals(revealDiv)) {
					//new ShowHide(targetDiv).show();
					hra.reveal();
					new ShowHide(revealDiv).hide();
				}
				
			}
		});
	}
	
	public class HideRevealAnimation extends Animation {

		public int animationDuration = 750;
		private Element e;
		private int originalHeight;
		private int originalWidth;
		private boolean direction;

		HideRevealAnimation(String elementID) {
			DomParser domParser = new DomParser();
		    domParser.addHandler(new DomElementByAttributeFinder("id", elementID) {

		        @Override
		        public void onDomElementFound(Element element, String id) {
		        	e = element;
		        	originalHeight = e.getClientHeight();
		        	originalWidth = e.getClientWidth();
		        	//System.out.println(""+originalHeight+" "+originalWidth);
		        }
		    });
		    domParser.parseDom();
		}
		
		public void hide() {
			direction = true;
			run(animationDuration);
		}

		public void reveal() {
			direction = false;
			run(animationDuration);
		}

		@Override
		protected void onUpdate(double progress) {

			if( direction )
				progress = 1-progress;

			if ( !direction && progress > 0.98 )
				// hmmm, I don't like this but scroll bars stick without it.
				e.removeAttribute("style");
			else {
				double newWidth = originalWidth*progress;
				double newHeight = originalHeight*progress;
	
				e.getStyle().setHeight(newHeight, Unit.PX);
				e.getStyle().setWidth(newWidth, Unit.PX);
				e.getStyle().setOverflow(Overflow.HIDDEN);
			}
		}
		
	}

}
