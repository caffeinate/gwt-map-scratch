package uk.co.plogic.gwt.lib.ui;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Element;

import uk.co.plogic.gwt.lib.dom.AttachClickFireEvent;
import uk.co.plogic.gwt.lib.dom.DomElementByAttributeFinder;
import uk.co.plogic.gwt.lib.dom.DomParser;
import uk.co.plogic.gwt.lib.events.ClickFireEvent;
import uk.co.plogic.gwt.lib.events.ClickFireEventHandler;

/**
 * UI Tool to hide and reveal DIVs on click events.
 * @author si
 *
 */
public class HideReveal {

	public HideReveal(DomParser domParser, HandlerManager eventBus, String hideReveal) {
		
		// TODO - make this more general so it can also use classes

		// target:hide_id:reveal_id
		// target is the menu and it is animated to shrink to nothing
		// the <div> with reveal_id is un-hidden at end of animation period
		// the <div> with hide_id is assumed to be within the target
		
		String[] parts = hideReveal.split(":");
		final String targetDiv = parts[0];
		final String hideDiv = parts[1];
		final String revealDiv = parts[2];
		
		new AttachClickFireEvent(domParser, eventBus, hideDiv);
		new AttachClickFireEvent(domParser, eventBus, revealDiv);
		final HideRevealAnimation hra = 
								new HideRevealAnimation(domParser, targetDiv, hideDiv, revealDiv);
		
		eventBus.addHandler(ClickFireEvent.TYPE, new ClickFireEventHandler() {

			@Override
			public void onClick(ClickFireEvent e) {
				
				if( e.getElement_id().equals(hideDiv)) {
					hra.hide();
				} else if( e.getElement_id().equals(revealDiv)) {
					hra.reveal();
				}
				
			}
		});
	}
	
	public class HideRevealAnimation extends Animation {

		public int animationDuration = 750;
		private Element targetElement;
		private Element hideElement;
		private Element revealElement;
		private int originalHeight;
		private int originalWidth;
		private boolean direction;

		HideRevealAnimation(DomParser domParser, String targetDiv, String hideDiv, String revealDiv) {

		    domParser.addHandler(new DomElementByAttributeFinder("id", targetDiv) {
		        @Override
		        public void onDomElementFound(Element element, String id) {
		        	targetElement = element;
		        	originalHeight = targetElement.getClientHeight();
		        	originalWidth = targetElement.getClientWidth();
		        	//System.out.println(""+originalHeight+" "+originalWidth);
		        }
		    });

		    domParser.addHandler(new DomElementByAttributeFinder("id", hideDiv) {
		        @Override
		        public void onDomElementFound(Element element, String id) {
		        	hideElement = element;
		        }
		    });

		    domParser.addHandler(new DomElementByAttributeFinder("id", revealDiv) {
		        @Override
		        public void onDomElementFound(Element element, String id) {
		        	revealElement = element;
		        }
		    });

		    
		}
		
		public void hide() {
			direction = true;
			hideElement.addClassName("hidden");
			run(animationDuration);
		}

		public void reveal() {
			direction = false;
			revealElement.addClassName("hidden");
			targetElement.removeClassName("hidden");
			run(animationDuration);
		}
		
		private void hideComplete() {
			revealElement.removeClassName("hidden");
			targetElement.addClassName("hidden");
			targetElement.removeAttribute("style");
		}
		
		private void revealComplete() {
			hideElement.removeClassName("hidden");
			targetElement.removeAttribute("style");
		}

		@Override
		protected void onUpdate(double progress) {

			if( direction ) {
				progress = 1-progress;
				if( progress < 0.01 ) {
					hideComplete();
					return;
				}
			} else if( progress > 0.99 ) {
				revealComplete();
				return;
			}

			//double newWidth = originalWidth*progress;
			//double newHeight = originalHeight*progress;

			//targetElement.getStyle().setHeight(newHeight, Unit.PX);
			//targetElement.getStyle().setWidth(newWidth, Unit.PX);
			
			//targetElement.getStyle().setHeight(progress*100, Unit.PC);
			//targetElement.getStyle().setWidth(progress*100, Unit.PC);
			int progressPc = (int) (progress*100);
			//System.out.println(""+progressPc);
			targetElement.getStyle().setProperty("height", progressPc+"%");
			targetElement.getStyle().setProperty("width", progressPc+"%");
			targetElement.getStyle().setOverflow(Overflow.HIDDEN);
		}
		
	}

}
