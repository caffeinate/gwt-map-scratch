package uk.co.plogic.gwt.lib.utils;

import java.util.logging.Logger;

import com.google.gwt.i18n.client.NumberFormat;

public class StringUtils {

	static final Logger logger = Logger.getLogger("StringUtils");

	public StringUtils() {}

	public static boolean legalIdString(String testString) {
		String t2 = testString.replace("-", "")
		                      .replace("_", "")
		                      .replace(":", "")
		                      .replace(".", "");
		for(char c : t2.toCharArray()) {
			if( ! Character.isLetterOrDigit(c) )
				return false;
		}
		return true;
	}

	/**
	 * The template contains placeholders in the format [[field_name]] where 'field_name'
	 * is a key in the AttributeDictionary.
	 *
	 * If the corresponding value is a number, formatting for use with NumberFormat can
	 * be specified. e.g. [[number_of|#]] - meaning no decimal places.
	 *
	 * @param htmlTemplate
	 * @param values
	 * @return
	 */
	public static String renderHtml(String htmlTemplate, AttributeDictionary values) {

		if( htmlTemplate == null || values == null )
			return htmlTemplate;

		// find all tags, could be done with regex
		int currentPos = 0;
		int tagStart = -1;
		String html = htmlTemplate;
		while( (tagStart = htmlTemplate.indexOf("[[", currentPos) ) >= 0 ) {

			int tagEnd = htmlTemplate.indexOf("]]", tagStart);
			String fullTag = htmlTemplate.substring(tagStart, tagEnd+2);
			int tagLen = fullTag.length();

			int formatPos = -1;
			String replacement;
			String fieldName;
			if( (formatPos = fullTag.indexOf("|")) > 0 ) {
				String format = fullTag.substring(formatPos+1, tagLen-2);
				fieldName = fullTag.substring(2, formatPos);
				NumberFormat numberFormat = NumberFormat.getFormat(format);

				if( values.get(fieldName) == null )
					logger.warning("Field not found:"+fieldName);
				else if( ! values.isType(AttributeDictionary.DataType.dtDouble, fieldName) )
					logger.warning("Not a number for number formatted field:"+fieldName);

				replacement = numberFormat.format(values.getDouble(fieldName));
				logger.finer("Formatted field:"+fieldName+" as "+format);
			} else {
				fieldName = fullTag.substring(2, tagLen-2);
				if( values.isType(AttributeDictionary.DataType.dtDouble, fieldName) ) {
	        		// cast to String
					replacement = ""+values.getDouble(fieldName);
					logger.finer("field:"+fieldName+" is number and replacement found.");
				} else if( values.isType(AttributeDictionary.DataType.dtString, fieldName) ) {
					replacement = values.get(fieldName);
					logger.finer("field:"+fieldName+" is string and replacement found.");
				}
	        	else {
	        		replacement = "";
	        		logger.finer("field:"+fieldName+" no replacement found.");
	        	}
			}
			//System.out.println("Replacing:"+fullTag+" with:"+replacement);
			html = html.replace(fullTag, replacement);
			currentPos=tagEnd+2;
		}

		return html;
	}

}
