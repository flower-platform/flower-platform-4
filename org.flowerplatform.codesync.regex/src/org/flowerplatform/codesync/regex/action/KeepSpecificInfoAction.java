package org.flowerplatform.codesync.regex.action;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * @author Elena Posea
 */
public class KeepSpecificInfoAction extends RegexAction {
	String keepInfoKey;
	boolean isList;
	boolean isContainment;

	/**
	 * @param keepInfoKey the key to where to keep the info
	 * @param isList whether there should be stored more info for that entry/ a list, or only one
	 * @param isContainment how should I store this info, as children of current node (if false), or as property in hashmap
	 */
	public KeepSpecificInfoAction(String keepInfoKey, boolean isList, boolean isContainment) {
		this.keepInfoKey = keepInfoKey;
		this.isList = isList;
		this.isContainment = isContainment;
	}

	@Override
	public void executeAction(RegexProcessingSession param) {
		Object newInfo = null;
		if (isContainment) {
			Node currentNode = (Node) param.context.get("currentNode");
			newInfo = currentNode;
		} else {
			newInfo = new String(param.getCurrentSubMatchesForCurrentRegex()[0]);
		}
		if (isList) {
			List<Object> currentList = null;
			if (param.context.get(keepInfoKey) == null) {
				currentList = new ArrayList<Object>();
			} else {
				currentList = (List<Object>) param.context.get(keepInfoKey);
			}
			currentList.add(newInfo);
			param.context.put(keepInfoKey, currentList);
		} else {
			param.context.put(keepInfoKey, newInfo);
		}
	}
}
