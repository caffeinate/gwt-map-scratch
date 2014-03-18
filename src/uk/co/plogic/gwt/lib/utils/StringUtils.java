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

}
