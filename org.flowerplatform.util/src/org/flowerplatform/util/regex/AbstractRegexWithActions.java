/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.util.regex;

import java.util.regex.Pattern;

/**
 * @author Cristina Constantinescu
 */
public abstract class AbstractRegexWithActions {

	protected String name;
	protected String regex;
	protected int numberOfCaptureGroups = -1;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
		this.numberOfCaptureGroups = Pattern.compile(regex).matcher("").groupCount();
	}

	public int getNumberOfCaptureGroups() {
		return numberOfCaptureGroups;
	}

	@Override
	public String toString() {
		return "RegexWithAction [name="
				+ name + ", regex=" + regex
				+ ", numberOfCaptureGroups=" + numberOfCaptureGroups + "]";
	}
	
	public abstract void executeAction(RegexProcessingSession session);
	
}
