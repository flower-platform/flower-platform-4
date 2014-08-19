package org.flowerplatform.codesync.regex.action;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * @author Elena Posea
 */
public class KeepSpecificInfoAction extends RegexAction {
	String keepInfoKey;
	boolean isList;
	boolean isContainment;

	public KeepSpecificInfoAction(String keepInfoKey, boolean isList, boolean isContainment) {
		this.keepInfoKey = keepInfoKey;
		this.isList = isList;
		this.isContainment = isContainment;
	}

	@Override
	public void executeAction(RegexProcessingSession param) {
		Object newInfo = null;
		if (isContainment) {
			newInfo = param.currentNode;
		} else {
			newInfo = new String(param.getCurrentSubMatchesForCurrentRegex()[0]);
		}
		if (isList) {
			List<Object> currentList = null;
			if (param.specificInfo.get(keepInfoKey) == null) {
				currentList = new ArrayList<Object>();
			} else {
				currentList = (List<Object>) param.specificInfo.get(keepInfoKey);
			}
			currentList.add(newInfo);
			param.specificInfo.put(keepInfoKey, currentList);
		} else {
			param.specificInfo.put(keepInfoKey, newInfo);
		}
	}
}
