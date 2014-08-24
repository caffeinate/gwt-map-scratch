package uk.co.plogic.gwt.lib.utils;

import com.google.gwt.i18n.client.NumberFormat;

public class StringUtils {

	public StringUtils() {}
	
	public static boolean isAlphaNumericWithHyphensUnderscores(String testString) {
		String t2 = testString.replace("-", "").replace("_", "");
		for(char c : t2.toCharArray()) {
			if( ! Character.isLetterOrDigit(c) )
				return false;
		}
		return true;
	}

	/**
	 * The template contains placeholders in the format {{field_name}} where 'field_name'
	 * is a key in the AttributeDictionary.
	 * 
	 * If the corresponding value is a number, formatting for use with NumberFormat can
	 * be specified. e.g. {{number_of|#}} - meaning no decimal places.
	 * 
	 * @param htmlTemplate
	 * @param values
	 * @return
	 */
	public static String renderHtml(String htmlTemplate, AttributeDictionary values) {

		if( htmlTemplate == null || values == null )
			return null;

		// find all tags, could be done with regex
		int currentPos = 0;
		int tagStart = -1;
		String html = htmlTemplate;
		while( (tagStart = htmlTemplate.indexOf("{{", currentPos) ) > 0 ) {

			int tagEnd = htmlTemplate.indexOf("}}", tagStart);
			String fullTag = htmlTemplate.substring(tagStart, tagEnd+2);
			int tagLen = fullTag.length();

			int formatPos = -1;
			String replacement;
			String fieldName;
			if( (formatPos = fullTag.indexOf("|")) > 0 ) {
				String format = fullTag.substring(formatPos+1, tagLen-2);
				fieldName = fullTag.substring(2, formatPos);
				NumberFormat numberFormat = NumberFormat.getFormat(format);
				replacement = numberFormat.format(values.getDouble(fieldName));
			} else {
				fieldName = fullTag.substring(2, tagLen-2);
				if( values.isType(AttributeDictionary.DataType.dtDouble, fieldName) )
	        		// cast to String
					replacement = ""+values.getDouble(fieldName);
	        	else replacement = values.get(fieldName);
			}
			//System.out.println("Replacing:"+fullTag+" with:"+replacement);
			html = html.replace(fullTag, replacement);
			currentPos=tagEnd+2;
		}

		return html;
	}

}
