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
	
	public static String getScheme(String uri) {
		int index = uri.indexOf(":");
		if (index < 0) {
			throw new RuntimeException("Invalid URI: " + uri);
		}
		return uri.substring(0, index);
	}
	
	public static String getSchemeSpecificPart(String uri) {
		int index = uri.indexOf(":");
		if (index < 0) {
			throw new RuntimeException("Invalid URI: " + uri);
		}
		String ssp = uri.substring(index + 1);
		index = ssp.lastIndexOf("#");
		if (index < 0) {
			return ssp;
		}
		return ssp.substring(0, index);
	}
	
	public static String getFragment(String uri) {
		int index = uri.lastIndexOf("#");
		if (index < 0) {
			return null;
		}
		return uri.substring(index + 1);
	}
	
	public static String getUri(String scheme, String ssp, String fragment) {
		String uri = scheme + ":" + ssp;
		if (fragment != null) {
			uri += "#" + fragment;
		}
		return uri;
	}
	
}
