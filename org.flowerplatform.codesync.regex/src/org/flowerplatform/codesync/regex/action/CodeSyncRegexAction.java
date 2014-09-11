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
package org.flowerplatform.codesync.regex.action;

import org.flowerplatform.codesync.regex.CodeSyncRegexPlugin;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * @author Cristina Constantinescu
 */
public abstract class CodeSyncRegexAction extends RegexAction {

	@Override
	public void run() {
		CodeSyncRegexPlugin.getInstance().addRegexAction(this);
	}

	/**
	 *@author see class
	 **/
	public static class IfFindThisSkip extends CodeSyncRegexAction {

		@Override
		public void executeAction(RegexProcessingSession param) {
			// do nothing			
		}
	}	
	
}