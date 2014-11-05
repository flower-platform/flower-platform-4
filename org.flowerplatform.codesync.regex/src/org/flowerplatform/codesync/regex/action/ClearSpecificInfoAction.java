package org.flowerplatform.codesync.regex.action;

import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * Clear the value stored in the context under {@link #clearInfoKey}.
 * 
 * @author Elena Posea
 */
public class ClearSpecificInfoAction extends RegexAction {

	private String clearInfoKey;
	
	public ClearSpecificInfoAction(String clearInfoKey) {
		this.clearInfoKey = clearInfoKey;
	}

	@Override
	public void executeAction(RegexProcessingSession param) {
		param.context.remove(clearInfoKey);
	}

}
