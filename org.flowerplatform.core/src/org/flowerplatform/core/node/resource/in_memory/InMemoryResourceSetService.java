/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.core.node.resource.in_memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.flowerplatform.core.node.resource.ResourceSetService;
import org.flowerplatform.core.node.update.Command;
import org.flowerplatform.core.node.update.remote.Update;

/**
 * @author Mariana Gheorghe
 */
public class InMemoryResourceSetService extends ResourceSetService {

	private Map<String, ResourceSetInfo> resourceSetInfos = new HashMap<String, ResourceSetInfo>();

	@Override
	public String addToResourceSet(String resourceSet, String resourceUri) {
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);
		if (info == null) {
			info = new ResourceSetInfo();
			resourceSetInfos.put(resourceSet, info);
		}
		if (!info.getResourceUris().contains(resourceUri)) {
			info.getResourceUris().add(resourceUri);
		}
		return resourceSet;
	}

	@Override
	public void removeFromResourceSet(String resourceSet, String resourceUri) {
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);
		info.getResourceUris().remove(resourceUri);
		if (info.getResourceUris().isEmpty()) {
			resourceSetInfos.remove(resourceSet);
		}
	}

	@Override
	protected void doReload(String resourceSet) {
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);
		info.getUpdates().clear();
		info.setLoadedTimestamp(new Date().getTime());
	}

	/**
	 * @author Mariana Gheorghe
	 * @author Claudiu Matei
	 */
	@Override
	public void addUpdate(String resourceSet, Update update) {
		logger.debug("Adding update {} for resource set {}", update, resourceSet);
		update.setId(UUID.randomUUID().toString());
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);
		if (info != null) {
			info.getUpdates().add(update);
		}
	}

	@Override
	protected List<Update> getUpdates(String resourceSet) {
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);
		return info.getUpdates();
	}

	@Override
	protected long getLoadedTimestamp(String resourceSet) {
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);
		return info.getLoadedTimestamp();
	}

	@Override
	public List<String> getResourceSets() {
		return new ArrayList<>(resourceSetInfos.keySet());
	}

	@Override
	public List<String> getResourceUris(String resourceSet) {
		ResourceSetInfo resourceSetInfo = resourceSetInfos.get(resourceSet);
		if (resourceSetInfo == null) {
			return Collections.emptyList();
		}
		return resourceSetInfo.getResourceUris();
	}
	
	/**
	 * @author Claudiu Matei
	 */
	@Override
	public List<Update> getUpdates(String resourceSet, String firstUpdateId, String lastUpdateId) {
		List<Update> allUpdates = null;
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);
		if (info != null) {
			allUpdates = info.getUpdates();
		}
		List<Update> updates = new ArrayList<Update>();
		if (allUpdates == null) {
			return updates;
		}
		
		boolean foundLastUpdate = false;
		// iterate updates reversed. Because last element in list is the most recent.
		// Most (99.99%) of the calls will only iterate a few elements at the end of the list
		for (int i = allUpdates.size() - 1; i >= 0; i--) {
			Update update = allUpdates.get(i);			
			if (update.getId().equals(lastUpdateId)) { 
				foundLastUpdate = true;
			}
			if (foundLastUpdate) {
				updates.add(0, update);
			}
			if (update.getId().equals(firstUpdateId)) {
				break;
			}
		}
		return updates;
	}
	
	/**
	 * @author Claudiu Matei
	 */
	@Override
	public Update getLastUpdate(String resourceSet) {
		List<Update> updates = null;
		Update lastUpdate = null;
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);

		if (info == null) {
			return lastUpdate;
		}
		
		updates = info.getUpdates();
		if (updates.size() > 0) {
			lastUpdate = updates.get(updates.size() - 1);
		}
		return lastUpdate;
	}

	/**
	 * @author Claudiu Matei
	 */
	@Override
	public void saveCommand(Command command) {
		command.setId(UUID.randomUUID().toString());
		ResourceSetInfo info = resourceSetInfos.get(command.getResourceSet());
		info.getCommandStack().add(command);
	}

	/**
	 * @author Claudiu Matei
	 * Used only for tests
	 */
	@Override
	public List<Command> getCommands(String resourceSet) {
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);
		if (info == null) {
			return null;
		}
		return info.getCommandStack(); 
	}

	/**
	 * @author Claudiu Matei
	 * @param lastCommandId - if null, returns all commands after firstCommandId
	 */
	@Override
	public List<Command> getCommands(String resourceSet, String firstCommandId, String lastCommandId) {
		List<Command> allCommands = null;
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);
		if (info != null) {
			allCommands = info.getCommandStack();
		}
		List<Command> commands = new ArrayList<Command>();
		if (allCommands == null) {
			return commands;
		}
		
		boolean foundLastCommand = (lastCommandId == null);
		// iterate commands reversed. Because last element in list is the most recent.
		// Most (99.99%) of the calls will only iterate a few elements at the end of the list
		for (int i = allCommands.size() - 1; i >= 0; i--) {
			Command command = allCommands.get(i);			
			if (!foundLastCommand && command.getId().equals(lastCommandId)) { 
				foundLastCommand = true;
			}
			if (foundLastCommand) {
				commands.add(0, command);
			}
			if (command.getId().equals(firstCommandId)) {
				break;
			}
		}
		return commands;
	}
	
	/**
	 * @author Claudiu Matei
	 */
	@Override
	public List<Command> deleteCommandsAfter(String resourceSet, String commandId) {
		List<Command> allCommands = null;
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);
		if (info != null) {
			allCommands = info.getCommandStack();
		}
		List<Command> commands = new ArrayList<Command>();
		if (allCommands == null) {
			return commands;
		}
		
		// iterate commands reversed. Because last element in list is the most recent.
		// Most (99.99%) of the calls will only iterate a few elements at the end of the list
		for (int i = allCommands.size() - 1; i >= 0; i--) {
			Command command = allCommands.get(i);			
			if (command.getId().equals(commandId)) break;
			commands.add(0, command);
			allCommands.remove(i);
		}
		return commands;
	}

	/**
	 * @author Claudiu Matei
	 */
	@Override
	public Command getCommand(String resourceSet, String commandId) {
		List<Command> commands = null;
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);
		Command command = null;
		if (info != null) {
			commands = info.getCommandStack();
		}
		if (commands == null) {
			return command;
		}

		// iterate commands reversed. Because last element in list is the most
		// recent.
		// Most (99.99%) of the calls will only iterate a few elements at the
		// end of the list
		for (int i = commands.size() - 1; i >= 0; i--) {
			Command cmd = commands.get(i);
			if (cmd.getId().equals(commandId)) {
				command = cmd;
				break;
			}
		}
		return command;
	}
	
	/**
	 * @author Claudiu Matei
	 */
	@Override
	public Command getCommandAfter(String resourceSet, String commandId) {
		List<Command> commands = null;
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);
		Command nextCommand = null;
		if (info != null) {
			commands = info.getCommandStack();
		}
		if (commands == null) {
			return nextCommand;
		}

		// iterate commands reversed. Because last element in list is the most
		// recent.
		// Most (99.99%) of the calls will only iterate a few elements at the
		// end of the list
		for (int i = commands.size() - 1; i >= 0; i--) {
			Command command = commands.get(i);
			if (command.getId().equals(commandId)) {
				if (i < commands.size() - 1) {
					nextCommand = commands.get(i + 1);
				}
				break;
			}
		}
		return nextCommand;
	}

	/**
	 * @author Claudiu Matei
	 */
	@Override
	public Command getCommandBefore(String resourceSet, String commandId) {
		List<Command> commands = null;
		Command nextCommand = null;

		ResourceSetInfo info = resourceSetInfos.get(resourceSet);
		if (info != null) {
			commands = info.getCommandStack();
		}
		if (commands == null) {
			return nextCommand;
		}

		// iterate commands reversed. Because last element in list is the most
		// recent.
		// Most (99.99%) of the calls will only iterate a few elements at the
		// end of the list
		for (int i = commands.size() - 1; i >= 0; i--) {
			Command command = commands.get(i);
			if (command.getId().equals(commandId)) {
				if (i > 0) {
					nextCommand = commands.get(i - 1);
				}
				break;
			}
		}
		return nextCommand;
	}

	/**
	 * @author Claudiu Matei
	 */
	@Override
	public Integer compareCommands(String resourceSet, String leftCommandId, String rightCommandId) {
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);

		List<Command> commands = info.getCommandStack();
		// iterate commands reversed. Because last element in list is the most recent.
		// Most (99.99%) of the calls will only iterate a few elements at the end of the list
		Integer leftCommandIndex = null, rightCommandIndex = null;
		for (int i = commands.size() - 1; i >= 0; i--) {
			Command command = commands.get(i);
			if (leftCommandIndex == null && command.getId().equals(leftCommandId)) {
				leftCommandIndex = i;
			}
			if (rightCommandIndex == null && command.getId().equals(rightCommandId)) {
				rightCommandIndex = i;
			}
			if (leftCommandIndex != null && rightCommandIndex != null)
				break;
		}

		if (leftCommandIndex == null || rightCommandIndex == null) {
			return null;
		}
		return leftCommandIndex.compareTo(rightCommandIndex);
	}

	/**
	 * @author Claudiu Matei
	 */
	@Override
	public String getCommandToUndoId(String resourceSet) {
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);
		return info.getCommandToUndoId();
	}
	
	/**
	 * @author Claudiu Matei
	 */
	@Override
	public void setCommandToUndoId(String resourceSet, String commandId) {
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);
		info.setCommandToUndoId(commandId);
	}

	/**
	 * @author Claudiu Matei
	 */
	@Override
	public String getCommandToRedoId(String resourceSet) {
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);
		return info.getCommandToRedoId();
	}
	
	/**
	 * @author Claudiu Matei
	 */
	@Override
	public void setCommandToRedoId(String resourceSet, String commandId) {
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);
		info.setCommandToRedoId(commandId);
	}

	/**
	 * @author Claudiu Matei
	 */
	@Override
	public void clearCommandStack(String resourceSet) {
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);
		info.getCommandStack().clear();
	}
	
}
