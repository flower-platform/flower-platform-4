package org.flowerplatform.codesync.regex.action;

import java.util.List;

import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

public class CreateNodeAction extends RegexAction{
	
	private String type;
	List<String> properties;
	
	public CreateNodeAction(String type, List<String> properties) {
		this.properties = properties;
		this.type = type;
	}

	@Override
	public void executeAction(RegexProcessingSession param) {
		
	}

}
