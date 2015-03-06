package uk.co.plogic.gwt.lib.ui;

import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.events.ActiveUpdateElementEvent;
import uk.co.plogic.gwt.lib.events.GazetteerResultsEvent;
import uk.co.plogic.gwt.lib.events.GazetteerResultsEventHandler;
import uk.co.plogic.gwt.lib.utils.AttributeDictionary;
import uk.co.plogic.gwt.lib.utils.StringUtils;

import com.google.gwt.event.shared.HandlerManager;

/**
 *
 * When results come in from the gazetteer create a bit of HTML and send it to an
 * active element.
 *
 * At present, this class only supports conditional templates in that one template
 * is used if a specific field equals a certain value and the other template if
 * not. The non-conditional version will be simplier.
 *
 * @author si
 *
 */
public class GazetteerResultsAction {

	Logger logger = Logger.getLogger("GazetteerResultsAction");
	HandlerManager eventBus;
	String targetActiveElementId;
	String condition_field;
	String condition_value;
	String templateOnTrue;
	String templateOnFalse;

	public GazetteerResultsAction(final HandlerManager eventBus,
								  final String targetActiveElementId) {

		this.eventBus = eventBus;
		this.targetActiveElementId = targetActiveElementId;

		eventBus.addHandler(GazetteerResultsEvent.TYPE, new GazetteerResultsEventHandler() {

			@Override
			public void onResults(GazetteerResultsEvent e) {
				sendHtmlToTarget(e.getAllGazetteerFields());
			}
		});

	}

	public void setConditionalTemplates(String condition, String templateOnTrue,
										String templateOnFalse) {

		// at present, == is the only operator
		if( ! condition.contains("==") ) {
			logger.warning("Invalid conditional predicate");
			return;
		}

		String[] c = condition.split("==");
		condition_field = c[0];
		condition_value = c[1];

		this.templateOnTrue = templateOnTrue;
		this.templateOnFalse = templateOnFalse;

	}

	private void sendHtmlToTarget(AttributeDictionary ad) {

	    if( ad == null )
	        return;

		String fieldValue;
		if( ad.get(condition_field) != null &&  ad.get(condition_field).equals(condition_value) ) {
			fieldValue = StringUtils.renderHtml(templateOnTrue, ad);
		} else {
			fieldValue = StringUtils.renderHtml(templateOnFalse, ad);
		}

		eventBus.fireEvent(new ActiveUpdateElementEvent(targetActiveElementId, fieldValue));
	}
}
