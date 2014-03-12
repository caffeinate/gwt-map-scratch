package uk.co.plogic.gwt.lib.ui;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerManager;

import uk.co.plogic.gwt.lib.dom.DomParser;

/**
 * UI Tool to hide and reveal DIVs on click events.
 * @author si
 *
 */
public class Disclose extends HideReveal {

	public Disclose(DomParser domParser, HandlerManager eventBus, String hideReveal) {
		super(domParser, eventBus, hideReveal);
	}

	protected HideRevealAnimation getAnimator(DomParser domParser, String targetDiv, String hideDiv, String revealDiv) {
		return new DiscloseAnimation(domParser, targetDiv, hideDiv, revealDiv);
	}

	public class DiscloseAnimation extends HideRevealAnimation {

		DiscloseAnimation(DomParser domParser, String targetDiv, String hideDiv, String revealDiv) {
			super(domParser, targetDiv, hideDiv, revealDiv);
		}

		public void hide() {
			direction = true;
			hideElement.addClassName("hidden");
			revealElement.removeClassName("hidden");
			originalHeight = targetElement.getScrollHeight();
			run(animationDuration);
		}

		public void reveal() {
			direction = false;
			revealElement.addClassName("hidden");
			hideElement.removeClassName("hidden");
			targetElement.removeClassName("hidden");
			originalHeight = targetElement.getScrollHeight();
			run(animationDuration);
		}
		
		protected void hideComplete() {
			targetElement.addClassName("hidden");
			targetElement.removeAttribute("style");
		}
		
		protected void revealComplete() {
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
			} else {
				if( progress > 0.99 ) {
					revealComplete();
					return;
				}
			}

			int newHeight = (int) (originalHeight*progress);
			targetElement.getStyle().setHeight(newHeight, Unit.PX);
//			targetElement.getStyle().setProperty("width", "100%");
			targetElement.getStyle().setOverflow(Overflow.HIDDEN);
			//System.out.println("update: "+targetElement.getScrollHeight()+" "+targetElement.getOffsetHeight());
		}
		
	}

}
