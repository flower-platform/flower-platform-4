package org.flowerplatform.codesync.regex.action;

import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * @author Elena Posea
 */
public class ExitStateAction  extends RegexAction{

	@Override
	public void executeAction(RegexProcessingSession param) {
		if(param.currentNestingLevel == param.stateStack.get(0).level){
			param.stateStack.remove(0);
		}
	}

}
