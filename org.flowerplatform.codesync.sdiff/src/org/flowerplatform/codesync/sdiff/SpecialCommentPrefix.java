/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.codesync.sdiff;

/**
 * 
 * @author Elena Posea
 */
public class SpecialCommentPrefix {
	public String specialPrefixText;
	public boolean isRegex;

	/**
	 * @param specialPrefixText the special text that you want to look for in your source code
	 * @param isRegex specifies whether the specialPrefixText should be treated as regex or as plain text  
	 */
	public SpecialCommentPrefix(String specialPrefixText, boolean isRegex) {
		super();
		this.specialPrefixText = specialPrefixText;
		this.isRegex = isRegex;
	}

}