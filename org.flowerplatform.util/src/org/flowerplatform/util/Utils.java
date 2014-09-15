/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.util;

import java.util.Map;

/**
 * @author Cristian Spiescu
 */
public final class Utils {
	
	private Utils() { }
	
	/**
	 *@author see class
	 **/
	public static <T> T getValueSafe(Map<?, T> map, Object key) {
		if (map == null) {
			return null;
		} else {
			return map.get(key);
		}
	}
	
	/**
	 *@author Mariana Gheorghe
	 **/
	public static boolean safeEquals(Object a, Object b) {
		if (a == null && b == null) {
			return true;
		} else if (a == null || b == null) {
			return false;
		} else {
			return a.equals(b);
		}
	}
	
	/**
	 *@author Mariana Gheorghe
	 **/
	public static String defaultIfNull(String str) {
		return defaultIfNull(str, "");
	}
	
	/**
	 *@author Mariana Gheorghe
	 **/
	public static String defaultIfNull(String str, String defaultStr) {
		return str == null ? defaultStr : str;
	}
	
	/**
	 *@author Mariana Gheorghe
	 **/
	public static String getScheme(String uri) {
		int index = uri.indexOf(":");
		if (index < 0) {
			throw new RuntimeException("Invalid URI: " + uri);
		}
		return uri.substring(0, index);
	}
	
	/**
	 *@author Mariana Gheorghe
	 **/
	public static String getRepo(String uri) {
		int indexStart = uri.indexOf(":");
		int indexEnd;
		if (uri.contains("|")) {
			indexEnd = uri.indexOf("|");
		} else {
			indexEnd = uri.length();
		}
//		int indexEnd = uri.indexOf("|");
//		if (indexStart < 0 || indexEnd < 0) {
//			throw new RuntimeException("Invalid URI: " + uri);
//		}
		return uri.substring(indexStart + 1, indexEnd);
	}
	
	/**
	 *@author Mariana Gheorghe
	 **/
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
	
	/**
	 *@author Mariana Gheorghe
	 **/
	public static String getFragment(String uri) {
		int index = uri.lastIndexOf("#");
		if (index < 0) {
			return null;
		}
		return uri.substring(index + 1);
	}
	
	/**
	 *@author Mariana Gheorghe
	 **/
	public static String getUri(String scheme, String ssp) {
		return getUri(scheme, ssp, null);
	}
	
	/**
	 *@author Mariana Gheorghe
	 **/
	public static String getUri(String scheme, String ssp, String fragment) {
		String uri = scheme + ":" + ssp;
		if (fragment != null) {
			uri += "#" + fragment;
		}
		return uri;
	}
}
