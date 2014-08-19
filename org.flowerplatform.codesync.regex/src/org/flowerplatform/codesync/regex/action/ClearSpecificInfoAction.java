package org.flowerplatform.codesync.regex.action;

import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

public class ClearSpecificInfoAction   extends RegexAction{

	String clearInfoKey;
	
	public ClearSpecificInfoAction(String clearInfoKey) {
		this.clearInfoKey = clearInfoKey;
	}

	@Override
	public void executeAction(RegexProcessingSession param) {
		param.specificInfo.remove(clearInfoKey);
	}

}
