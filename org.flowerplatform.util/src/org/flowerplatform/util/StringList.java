package org.flowerplatform.util;

import java.util.ArrayList;

/**
 * @author Cristina Brinza
 *
 */
public class StringList extends ArrayList<String> {

	private static final long serialVersionUID = 1L;

	/**
	 * @author see class
	 */
	public StringList() {
		super();
	}

	/**
	 * @author see class
	 * 
	 * This exists because a constructor ArrayList(String) is needed in serializing .users file 
	 */
	public StringList(String string) {
		super();
		if (!string.equals("()")) {
			String[] result = (string.substring(1, string.length() - 1)).split(", ");
			for (int index = 0; index < result.length; index++) {
				this.add(result[index]);
			}
		}
	}
}
