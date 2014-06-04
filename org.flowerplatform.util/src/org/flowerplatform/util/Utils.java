package org.flowerplatform.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
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
	
	public static URI getUri(String uriAsString) {
		return getUri(uriAsString, false);
	}
	
	public static URI getUriWithoutFragment(String uriAsString) {
		return getUri(uriAsString, true);
	}
	
	public static URI getUriWithoutFragment(URI uri) {
		return getUri(uri.getScheme(), uri.getSchemeSpecificPart(), null);
	}
	
	public static String getUriWithFragment(String baseUriAsString, String fragment) {
		URI baseUri = getUri(baseUriAsString);
		return getString(getUriWithFragment(baseUri, fragment));
	}
	
	public static URI getUriWithFragment(URI baseUri, String fragment) {
		return getUri(baseUri.getScheme(), baseUri.getSchemeSpecificPart(), fragment);
	}
	
	private static URI getUri(String uriAsString, boolean stripFragment) {
		int index = uriAsString.indexOf(":");
		if (index < 0) {
			throw new RuntimeException("Invalid URI: " + uriAsString);
		}
		String scheme = uriAsString.substring(0, index);
		String ssp = uriAsString.substring(index + 1);
		String fragment = null;
		index = ssp.lastIndexOf("#");
		if (index > 0) {
			fragment = ssp.substring(index + 1);
			ssp = ssp.substring(0, index);
		}
		return getUri(scheme, ssp, stripFragment ? null : fragment);
	}
	
	public static URI getUri(String scheme, String ssp, String fragment) {
		try {
			return new URI(scheme, ssp, fragment);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getString(URI uri) {
		try {
			return URLDecoder.decode(uri.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
