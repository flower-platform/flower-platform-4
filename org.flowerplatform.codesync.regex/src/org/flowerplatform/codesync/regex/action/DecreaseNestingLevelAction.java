package org.flowerplatform.codesync.regex.action;

import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * @author Elena Posea
 */
public class DecreaseNestingLevelAction extends RegexAction {

	@Override
	public void executeAction(RegexProcessingSession param) {
		param.currentNestingLevel--;
	}

}
