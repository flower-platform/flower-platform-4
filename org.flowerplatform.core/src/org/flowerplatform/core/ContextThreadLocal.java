package org.flowerplatform.core;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Claudiu Matei
 *
 */
public class ContextThreadLocal {
	private HttpServletRequest request;
	private String commandTitle;
	private String resource;
	private String lastUpdateIdBeforeCommandExecution;
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getCommandTitle() {
		return commandTitle;
	}

	public void setCommandTitle(String commandTitle) {
		this.commandTitle = commandTitle;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getLastUpdateIdBeforeCommandExecution() {
		return lastUpdateIdBeforeCommandExecution;
	}

	public void setLastUpdateIdBeforeCommandExecution(
			String lastUpdateIdBeforeCommandExecution) {
		this.lastUpdateIdBeforeCommandExecution = lastUpdateIdBeforeCommandExecution;
	}
	
	
}
