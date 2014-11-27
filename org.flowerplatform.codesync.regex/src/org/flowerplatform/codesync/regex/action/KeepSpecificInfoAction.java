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

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.codesync.regex.CodeSyncRegexConstants;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * Store parsed info in the context under {@link #keepInfoKey}. Info will be either the current node (if {@link #isContainment},
 * and will be added later as a child to a parent node), or matches from the capture groups. Also, info may be collected into
 * a list (if {@link #isList}), or overwrite the previously existing value in the context. 
 * 
 * @author Elena Posea
 * @author Mariana Gheorghe
 */
public class KeepSpecificInfoAction extends RegexAction {
	
	private String keepInfoKey;
	private boolean isList;
	private boolean isContainment;

	public KeepSpecificInfoAction(String keepInfoKey, boolean isList, boolean isContainment) {
		this.keepInfoKey = keepInfoKey;
		this.isList = isList;
		this.isContainment = isContainment;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void executeAction(RegexProcessingSession param) {
		Object newInfo = null;
		if (isContainment) {
			// keep the current node; will be added as a child later
			Node currentNode = (Node) param.context.get(CodeSyncRegexConstants.CURRENT_NODE);
			newInfo = currentNode;
		} else {
			// keep matched strings from capture group
			newInfo = new String(param.getCurrentSubMatchesForCurrentRegex()[0]);
		}
		if (isList) {
			// add it to a list in the context map
			List<Object> currentList = null;
			if (param.context.get(keepInfoKey) == null) {
				currentList = new ArrayList<Object>();
				param.context.put(keepInfoKey, currentList);
			} else {
				currentList = (List<Object>) param.context.get(keepInfoKey);
			}
			currentList.add(newInfo);
		} else {
			// overwrite any existing value in map
			param.context.put(keepInfoKey, newInfo);
		}
	}
}
