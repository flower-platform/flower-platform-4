package org.flowerplatform.core;

import javax.servlet.http.HttpServletRequest;

import org.flowerplatform.core.node.update.Command;

/**
 * @author Claudiu Matei
 *
 */
public class ContextThreadLocal {
	
	private HttpServletRequest request;
	
	private Command command;
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	
	
}
