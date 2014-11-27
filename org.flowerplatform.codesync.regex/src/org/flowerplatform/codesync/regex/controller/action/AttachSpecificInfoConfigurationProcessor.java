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
package org.flowerplatform.codesync.regex.controller.action;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_PROPERTY_INFO_IS_CONTAINMENT;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_PROPERTY_INFO_KEY;

import org.flowerplatform.codesync.regex.action.AttachSpecificInfoAction;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.regex.RegexAction;

/**
 * @author Elena Posea
 */
public class AttachSpecificInfoConfigurationProcessor extends RegexActionConfigurationProcessor  {

	@Override
	protected RegexAction createRegexAction(Node node) {
		String attachInfoKey = (String) node.getPropertyValue(ACTION_PROPERTY_INFO_KEY);
		Boolean isContainment = (Boolean) node.getPropertyValue(ACTION_PROPERTY_INFO_IS_CONTAINMENT);
		return new AttachSpecificInfoAction(attachInfoKey, isContainment);
	}
}
