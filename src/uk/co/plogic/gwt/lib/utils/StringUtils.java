package uk.co.plogic.gwt.lib.utils;

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
	
	public static String renderHtml(String htmlTemplate, AttributeDictionary values) {

		if( htmlTemplate == null || values == null )
			return null;

		String value;
		String html = htmlTemplate;
		for( String key : values.keySet() ) {

        	if( values.isType(AttributeDictionary.DataType.dtDouble, key) )
        		// cast to String
    			 value = ""+values.getDouble(key);
        	else value = values.get(key);

        	html = html.replace("{{"+key+"}}", value);

		}

		return html;
	}

}
