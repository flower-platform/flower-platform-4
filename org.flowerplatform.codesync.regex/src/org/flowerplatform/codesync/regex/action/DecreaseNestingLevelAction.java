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
package org.flowerplatform.codesync.regex.action;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.CURRENT_NESTING_LEVEL;

import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * Decrease nesting level.
 * 
 * @author Elena Posea
 */
public class DecreaseNestingLevelAction extends RegexAction {

	@Override
	public void executeAction(RegexProcessingSession param) {
		int currentNestingLevel = (int) param.context.get(CURRENT_NESTING_LEVEL);
		param.context.put(CURRENT_NESTING_LEVEL, currentNestingLevel - 1);
	}

}
