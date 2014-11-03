package org.flowerplatform.codesync.regex.action;

import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * @author Elena Posea
 */
public class ClearSpecificInfoAction   extends RegexAction {

	String clearInfoKey;
	
	/**
	 * @param clearInfoKey the key for the info that you want to remove
	 */
	public ClearSpecificInfoAction(String clearInfoKey) {
		this.clearInfoKey = clearInfoKey;
	}

	@Override
	public void executeAction(RegexProcessingSession param) {
		param.context.remove(clearInfoKey);
	}

}
