package org.flowerplatform.core;

import org.flowerplatform.core.node.update.Command;

/**
 * @author Claudiu Matei
 */
public class ContextThreadLocal {
	
	private Command command;
	
	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}
	
}
