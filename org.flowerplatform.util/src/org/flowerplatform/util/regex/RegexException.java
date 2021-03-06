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
package org.flowerplatform.util.regex;

/**
 * @see RegexProcessingSession
 * @author Cristina Constantinescu
 */
public class RegexException extends Exception {

	private static final long serialVersionUID = 1L;
	/**
	 *@author Cristina Constantinescu
	 **/
	public RegexException(String log) {
		super(log);		
	}
			
}