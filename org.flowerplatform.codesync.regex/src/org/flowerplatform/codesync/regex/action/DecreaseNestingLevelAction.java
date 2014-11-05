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
