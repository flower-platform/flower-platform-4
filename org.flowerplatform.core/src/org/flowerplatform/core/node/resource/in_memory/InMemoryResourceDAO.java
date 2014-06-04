package org.flowerplatform.core.node.resource.in_memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.resource.IResourceDAO;
import org.flowerplatform.core.node.update.Command;
import org.flowerplatform.core.node.update.remote.Update;

/**
 * Keeps in-memory maps of {@link ResourceNodeInfo}s and information about subscribed clients (sessions).
 * 
 * @author Mariana Gheorghe
 */
public class InMemoryResourceDAO implements IResourceDAO {

	private Map<String, ResourceNodeInfo> resourceNodeIdToInfo = new HashMap<String, ResourceNodeInfo>();

	private Map<String, SessionInfo> sessionIdToSessionInfo = new HashMap<String, SessionInfo>();

	@Override
	public void sessionSubscribedToResource(String resourceNodeId, String sessionId) {
		if (!getResourceNodeInfoForResourceNodeId(resourceNodeId).getSessions().contains(sessionId)) {
			getResourceNodeInfoForResourceNodeId(resourceNodeId).getSessions().add(sessionId);
		}
		if (!getSessionInfoForSessionId(sessionId).getSubscribedResourceNodeIds().contains(resourceNodeId)) {
			getSessionInfoForSessionId(sessionId).getSubscribedResourceNodeIds().add(resourceNodeId);
		}
	}

	@Override
	public void sessionUnsubscribedFromResource(String resourceNodeId, String sessionId) {
		getResourceNodeInfoForResourceNodeId(resourceNodeId).getSessions().remove(sessionId);
		getSessionInfoForSessionId(sessionId).getSubscribedResourceNodeIds().remove(resourceNodeId);
	}

	@Override
	public void sessionCreated(String sessionId) {
		// nothing to do; session info will be lazy-initialized
	}

	@Override
	public void sessionRemoved(String sessionId) {
		sessionIdToSessionInfo.remove(sessionId);
	}

	@Override
	public List<String> getSubscribedSessions() {
		return new ArrayList<>(sessionIdToSessionInfo.keySet());
	}
	
	@Override
	public Object getSessionProperty(String sessionId, String property) {
		SessionInfo sessionInfo = sessionIdToSessionInfo.get(sessionId);
		if (sessionInfo != null) {
			return sessionInfo.getProperties().get(property);
		}
		return null;
	}

	@Override
	public void updateSessionProperty(String sessionId, String property, Object value) {
		getSessionInfoForSessionId(sessionId).getProperties().put(property, value);
	}

	@Override
	public Object getRawResourceData(String resourceNodeId) {
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
		if (info != null) {
			return info.getRawResourceData();
		}
		return null;
	}

	@Override
	public String getResourceCategory(String resourceNodeId) {
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
		if (info != null) {
			return info.getResourceCategory();
		}
		return null;
	}
	
	@Override
	public void setRawResourceData(String resourceNodeId, Object rawResourceData, String resourceCategory) {
		ResourceNodeInfo resourceNodeInfo = getResourceNodeInfoForResourceNodeId(resourceNodeId);
		resourceNodeInfo.setRawResourceData(rawResourceData);
		resourceNodeInfo.setResourceCategory(resourceCategory);
	}
	
	@Override
	public void unsetRawResourceData(String resourceNodeId) {
		resourceNodeIdToInfo.remove(resourceNodeId);
	}
	
	@Override
	public long getUpdateRequestedTimestamp(String resourceNodeId) {
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
		if (info != null) {
			return info.getUpdateRequestedTimestamp();
		}
		return -1;
	}
	
	@Override
	public List<String> getSessionsSubscribedToResource(String resourceNodeId) {
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
		if (info != null) {
			return info.getSessions();
		}
		return Collections.emptyList();
	}

	@Override
	public List<String> getResourcesSubscribedBySession(String sessionId) {
		SessionInfo sessionInfo = sessionIdToSessionInfo.get(sessionId);
		if (sessionInfo != null) {
			return sessionInfo.getSubscribedResourceNodeIds();
		}
		return Collections.emptyList();
	}

	@Override
	public List<String> getResources() {
		return new ArrayList<String>(resourceNodeIdToInfo.keySet());
	}
	
	@Override
	public void addUpdate(String resourceNodeId, Update update) {
		update.setId(UUID.randomUUID().toString());
		ResourceNodeInfo info = getResourceNodeInfoForResourceNodeId(resourceNodeId);
		info.getUpdates().add(update);
	}

	@Override

	public List<Update> getUpdates(String resourceNodeId, long timestampOfLastRequest, long timestampOfThisRequest) {
		List<Update> updates = null;
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
		if (info != null) {
			updates = info.getUpdates();
			info.setUpdateRequestedTimestamp(timestampOfThisRequest);
		}
		List<Update> updatesAddedAfterLastRequest = new ArrayList<Update>();
		if (updates == null) {
			return updatesAddedAfterLastRequest;
		}
		
		if (timestampOfLastRequest > 0 && 
			info.getLoadedTimestamp() > timestampOfLastRequest + Integer.valueOf(CorePlugin.getInstance().getFlowerProperties().getProperty(IResourceDAO.PROP_RESOURCE_UPDATES_MARGIN))) {
			// if resource was reloaded after -> tell client to perform full refresh
			return null;
		}
		
		// iterate updates reversed. Because last element in list is the most recent.
		// Most (99.99%) of the calls will only iterate a few elements at the end of the list
		for (int i = updates.size() - 1; i >= 0; i--) {
			Update update = updates.get(i);			
			if (update.getTimestamp() < timestampOfLastRequest) { 
				// an update was registered before timestampOfLastRequest
				break;
			}
			updatesAddedAfterLastRequest.add(0, update);
		}
		return updatesAddedAfterLastRequest;
	}

	/**
	 * @author Claudiu Matei
	 */
	@Override
	public List<Update> getUpdates(String resourceNodeId, String firstUpdateId, String lastUpdateId) {
		List<Update> allUpdates = null;
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
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
			if (update.getId().equals(firstUpdateId)) break;
		}
		return updates;
	}
	
	/**
	 * @author Claudiu Matei
	 */
	@Override
	public Update getLastUpdate(String resourceNodeId) {
		List<Update> updates = null;
		Update lastUpdate = null;
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
		if (info != null) {
			updates = info.getUpdates();
			if (updates.size() > 0) {
				lastUpdate = updates.get(updates.size() - 1);
			}
		}
		return lastUpdate;
	}

	/**
	 * @author Claudiu Matei
	 */
	@Override
	public void addCommand(Command command) {
		command.setId(UUID.randomUUID().toString());
		ResourceNodeInfo info = getResourceNodeInfoForResourceNodeId(command.getResource());
		info.getCommandStack().add(command);
	}

	/**
	 * @author Claudiu Matei
	 */
	@Override
	public List<Command> getCommands(String resourceNodeId) {
		ResourceNodeInfo info = getResourceNodeInfoForResourceNodeId(resourceNodeId);
		return info.getCommandStack(); 
	}

	/**
	 * @author Claudiu Matei
	 */
	@Override
	public List<Command> getCommands(String resourceNodeId, String firstCommandId, String lastCommandId) {
		List<Command> allCommands = null;
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
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
			if (command.getId().equals(firstCommandId)) break;
		}
		return commands;
	}
	
	/**
	 * @author Claudiu Matei
	 */
	@Override
	public List<Command> getCommandsAfter(String resourceNodeId, String commandId) {
		return getCommands(resourceNodeId, commandId, null);
	}

	/**
	 * @author Claudiu Matei
	 */
	@Override
	public List<Command> deleteCommandsAfter(String resourceNodeId, String commandId) {
		List<Command> allCommands = null;
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
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
	public Command getCommand(String resourceNodeId, String commandId) {
		List<Command> commands = null;
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
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
	public Command getCommandAfter(String resourceNodeId, String commandId) {
		List<Command> commands = null;
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
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
	public Command getCommandBefore(String resourceNodeId, String commandId) {
		List<Command> commands = null;
		Command nextCommand = null;

		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
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
	public Integer compareCommands(String resourceNodeId, String leftCommandId, String rightCommandId) {
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);

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
	public String getCommandToUndoId(String resourceNodeId) {
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
		return info.getCommandToUndoId();
	}
	
	/**
	 * @author Claudiu Matei
	 */
	@Override
	public void setCommandToUndoId(String resourceNodeId, String commandId) {
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
		info.setCommandToUndoId(commandId);
	}

	/**
	 * @author Claudiu Matei
	 */
	@Override
	public String getCommandToRedoId(String resourceNodeId) {
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
		return info.getCommandToRedoId();
	}
	
	/**
	 * @author Claudiu Matei
	 */
	@Override
	public void setCommandToRedoId(String resourceNodeId, String commandId) {
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
		info.setCommandToRedoId(commandId);
	}

	/**
	 * Lazy-init mechanism. Called from methods that need to add/update info.
	 * Getters should instead check if the info exists, to avoid memory leaks.
	 */
	private ResourceNodeInfo getResourceNodeInfoForResourceNodeId(String resourceNodeId) {
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
		if (info == null) {
			info = new ResourceNodeInfo();
			resourceNodeIdToInfo.put(resourceNodeId, info);
		}
		return info;
	}
	
	/**
	 * Lazy-init mechanism. Called from methods that need to add/update info.
	 * Getters should instead check if the info exists, to avoid memory leaks.
	 */
	private SessionInfo getSessionInfoForSessionId(String sessionId) {
		SessionInfo info = sessionIdToSessionInfo.get(sessionId);
		if (info == null) {
			info = new SessionInfo();
			sessionIdToSessionInfo.put(sessionId, info);
		}
		return info;
	}

}
