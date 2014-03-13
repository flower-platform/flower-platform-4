package org.flowerplatform.util;

import java.util.Map;

/**
 * @author Cristian Spiescu
 */
public class Utils {
	public static <T> T getValueSafe(Map<?, T> map, Object key) {
		if (map == null) {
			return null;
		} else {
			return map.get(key);
		}
	}
	
	public static boolean safeEquals(Object a, Object b) {
		if (a == null && b == null)
			return true;
		else if (a == null || b == null)
			return false;
		else
			return a.equals(b);
	}
	
	public static String defaultIfNull(String str) {
		return defaultIfNull(str, "");
	}
	
	public static String defaultIfNull(String str, String defaultStr) {
		return str == null ? defaultStr : str;
	}
}
