/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.core.node.resource;

import static org.flowerplatform.core.CoreConstants.NODE_IS_RESOURCE_NODE;
import static org.flowerplatform.core.CoreConstants.RESOURCE_SET;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.ContextThreadLocal;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.FlowerProperties;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPersistenceController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.update.Command;
import org.flowerplatform.core.node.update.controller.UpdateController;
import org.flowerplatform.core.node.update.remote.ChildrenUpdate;
import org.flowerplatform.core.node.update.remote.PropertyUpdate;
import org.flowerplatform.core.node.update.remote.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mariana Gheorghe
 */
public abstract class ResourceSetService {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ResourceSetService.class);
	
	static final String PROP_RESOURCE_UPDATES_MARGIN = "resourceUpdatesMargin"; 
	static final String PROP_DEFAULT_PROP_RESOURCE_UPDATES_MARGIN = "0"; 
	
	/**
	 *@author see class
	 **/
	public ResourceSetService() {
		CorePlugin.getInstance().getFlowerProperties().addProperty(new FlowerProperties
				.AddIntegerProperty(PROP_RESOURCE_UPDATES_MARGIN, PROP_DEFAULT_PROP_RESOURCE_UPDATES_MARGIN));
	}
	
	/**
	 *@author see class
	 **/
	public abstract String addToResourceSet(String resourceSet, String resourceUri);
	
	/**
	 *@author see class
	 **/
	public abstract void removeFromResourceSet(String resourceSet, String resourceUri);
	
	/**
	 *@author see class
	 **/
	public void save(String resourceSet, ServiceContext<ResourceSetService> context) {
		LOGGER.debug("Save resource set {}", resourceSet);
		ServiceContext<ResourceService> resourceServiceContext = new ServiceContext<ResourceService>();
		resourceServiceContext.setContext(context.getContext());
		for (String resourceUri : getResourceUris(resourceSet)) {
			CorePlugin.getInstance().getResourceService().save(resourceUri, resourceServiceContext);
		}
	}
	
	/**
	 *@author see class
	 **/
	public void reload(String resourceSet, ServiceContext<ResourceSetService> context) {
		LOGGER.debug("Reload resource set {}", resourceSet);
		doReload(resourceSet);
		ServiceContext<ResourceService> resourceServiceContext = new ServiceContext<ResourceService>();
		resourceServiceContext.setContext(context.getContext());
		for (String resourceUri : getResourceUris(resourceSet)) {
			CorePlugin.getInstance().getResourceService().reload(resourceUri, resourceServiceContext);
		}
	}

	/**
	 *@author see class
	 **/
	protected abstract void doReload(String resourceSet);
	
	/**
	 *@author see class
	 **/
	public abstract void addUpdate(String resourceSet, Update update);
	
	/**
	 * @author Cristina Constantinescu
	 */
	public void addUpdate(Node node, Update update, ServiceContext<?> context) {
		String resourceSet = null;
		Node resourceNode;
		if (context.getBooleanValue(NODE_IS_RESOURCE_NODE)) {
			resourceNode = node;
		} else {
			resourceNode = CorePlugin.getInstance().getResourceService().getResourceNode(node.getNodeUri());
			
		}
		if (resourceNode == null) {
			return;
		}
		resourceSet = (String) resourceNode.getPropertyValue(RESOURCE_SET);
		if (resourceSet == null) {
			resourceSet = resourceNode.getNodeUri();
		}		
		addUpdate(resourceSet, update);		
	}
	
	/**
	 *@author see class
	 **/
	public List<Update> getUpdates(String resourceSet, long timestampOfLastRequest) {
		List<Update> updates = getUpdates(resourceSet);
		List<Update> updatesAddedAfterLastRequest = new ArrayList<Update>();
		if (updates == null) {
			return updatesAddedAfterLastRequest;
		}
		
		if (timestampOfLastRequest > 0 
				&& getLoadedTimestamp(resourceSet) > timestampOfLastRequest + Integer
				.valueOf(CorePlugin.getInstance().getFlowerProperties().getProperty(PROP_RESOURCE_UPDATES_MARGIN))) {
			// if resource was reloaded after -> tell client to perform full refresh
			return null;
		}
		
		// iterate updates reversed. Because last element in list is the most recent.
		// Most (99.99%) of the calls will only iterate a few elements at the end of the list
		for (int i = updates.size() - 1; i >= 0; i--) {
			Update update = updates.get(i);			
			if (update.getTimestamp() <= timestampOfLastRequest) { 
				// an update was registered before timestampOfLastRequest
				break;
			}
			updatesAddedAfterLastRequest.add(update);
		}
		return updatesAddedAfterLastRequest;
	}
	
	/**
	 *@author see class
	 **/
	protected abstract List<Update> getUpdates(String resourceSet);
	
	/**
	 *@author see class
	 **/
	protected abstract long getLoadedTimestamp(String resourceSet);
	
	/**
	 *@author see class
	 **/
	public abstract List<String> getResourceSets();
	
	/**
	 *@author see class
	 **/
	public abstract List<String> getResourceUris(String resourceSet);

	/**
	 * @author Claudiu Matei 
	 */
	public String getResourceSet(String nodeUri) {
		Node resourceNode = CorePlugin.getInstance().getResourceService().getResourceNode(nodeUri);
		ServiceContext<NodeService> context = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
		String resourceSet = (String) resourceNode.getOrPopulateProperties(context).get(CoreConstants.RESOURCE_SET);
		if (resourceSet == null) {
			resourceSet = resourceNode.getNodeUri();
		}
		return resourceSet;
	}
	
	/**
	 * @author Claudiu Matei 
	 */
	public void startCommand(String resourceSet, String commandTitle) {
		CorePlugin.getInstance().getLockManager().lock(resourceSet);
		Command command = new Command();
		command.setResourceSet(resourceSet);
		command.setTitle(commandTitle);
		Update lastUpdate = getLastUpdate(resourceSet);
		if (lastUpdate != null) {
			// i.e. the resource has just been opened and this is the first command
			command.setLastUpdateIdBeforeCommandExecution(lastUpdate.getId());
		}
		
		ContextThreadLocal context = CorePlugin.getInstance().getContextThreadLocal().get();
		context.setCommand(command);
	}

	/**
	 * @author Claudiu Matei 
	 */
	public void addCommand(Command command) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("For resource = {} adding command = {}", command.getResourceSet(), command);
		}
		
		Node commandStackNode = CorePlugin.getInstance().getCommandStackResourceHandler().createCommandStackNode(command.getResourceSet()); 
		String commandToUndoId = getCommandToUndoId(command.getResourceSet());

		List<Command> removedCommands = deleteCommandsAfter(command.getResourceSet(), commandToUndoId);
		ServiceContext<NodeService> context = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
		for (Command cmd : removedCommands) {
			Node node = CorePlugin.getInstance().getCommandStackResourceHandler().createCommandNode(cmd.getResourceSet(), cmd.getId());
			// doesn't do anything, except adding a notification
			CorePlugin.getInstance().getNodeService().removeChild(commandStackNode, node, context);
		}
		
		command.setLastUpdateIdAfterCommandExecution(getLastUpdate(command.getResourceSet()).getId());
		saveCommand(command);
		Node newCommandNode = CorePlugin.getInstance().getCommandStackResourceHandler().createCommandNode(command.getResourceSet(), command.getId());
		newCommandNode.getProperties().put(CoreConstants.NAME, command.getTitle());
		// doesn't do anything, except adding a notification
		CorePlugin.getInstance().getNodeService().addChild(commandStackNode, newCommandNode, new ServiceContext<NodeService>());

		setCommandToUndoId(command.getResourceSet(), command.getId());
		setCommandToRedoId(command.getResourceSet(), null);
	}

	/**
	 * @author Claudiu Matei 
	 */
	public List<Update> getCommandUpdates(Command command) {
		return getUpdates(command.getResourceSet(), command.getLastUpdateIdBeforeCommandExecution(), command.getLastUpdateIdAfterCommandExecution());
	}

	/**
	 * Clears command stack pointers and removes all commands from the command stack. 
	 * Invoked from tests only.
	 * 
	 * @author Claudiu Matei 
	 */
	public void resetCommandStack(String resourceSet) {
		clearCommandStack(resourceSet);
		setCommandToUndoId(resourceSet, null);
		setCommandToRedoId(resourceSet, null);
		NodeService nodeService = CorePlugin.getInstance().getNodeService();
		ServiceContext<NodeService> context = new ServiceContext<NodeService>();
		
		Node commandStackNode = CorePlugin.getInstance().getCommandStackResourceHandler().createCommandStackNode(resourceSet);
		List<Node> commandNodes = nodeService.getChildren(commandStackNode, context);
		for (Node node : commandNodes) {
			nodeService.removeChild(commandStackNode, node, context);
		}
	}
	
	/**
	 * @author Claudiu Matei 
	 */
	public void undo(String resourceSet, String commandId) {
		try {
			CorePlugin.getInstance().getLockManager().lock(resourceSet);
			String commandToUndoId = getCommandToUndoId(resourceSet);
			Command command = getCommand(resourceSet, commandId);
			Integer comp = compareCommands(resourceSet, commandId, commandToUndoId);
			if (command == null) {
				throw new IllegalArgumentException(String.format("For resource %s command %s doesn't exist. Current command to undo is: %s", resourceSet, commandId,
						commandToUndoId));
			} else if (comp == null || comp > 0) {
				throw new IllegalArgumentException(String.format("For resource %s command %s has already been undone. Current command to undo is: %s", resourceSet, commandId,
						commandToUndoId));
			} else {
				List<Command> commands = getCommands(resourceSet, commandId, commandToUndoId);
				for (int i = commands.size() - 1; i >= 0; i--) {
					Command cmd = commands.get(i);
					List<Update> updates = getUpdates(resourceSet, cmd.getLastUpdateIdBeforeCommandExecution(), cmd.getLastUpdateIdAfterCommandExecution());
					for (int k = updates.size() - 1; k >= (cmd.getLastUpdateIdBeforeCommandExecution() == null ? 0 : 1); k--) {
						Update update = updates.get(k);
						undoUpdate(update);
					}
				}
				Command previousCommand = getCommandBefore(resourceSet, commandId);
				setCommandToUndoId(resourceSet, (previousCommand != null ? previousCommand.getId() : null));
				setCommandToRedoId(command.getResourceSet(), commandId);

			}
		} finally {
			CorePlugin.getInstance().getLockManager().unlock(resourceSet);
		}
	}
	
	/**
	 * @author Claudiu Matei 
	 */
	private void undoUpdate(Update update) {
		if (update instanceof PropertyUpdate) {
			undoPropertyUpdate((PropertyUpdate) update);
		} else if (update instanceof ChildrenUpdate) {
			undoChildrenUpdate((ChildrenUpdate) update);
		}
	}
	
	/**
	 * @author Claudiu Matei 
	 */
	private void undoPropertyUpdate(PropertyUpdate update) {
		Node node = CorePlugin.getInstance().getResourceService().getNode(update.getFullNodeId());

		ServiceContext<NodeService> context = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
		List<Class<?>> controllerList = new ArrayList<Class<?>>();
		controllerList.add(IPersistenceController.class);
		controllerList.add(UpdateController.class);
		context.add(CoreConstants.INVOKE_ONLY_CONTROLLERS_WITH_CLASSES, controllerList);
		
		if (update.getHasOldValue()) {
			CorePlugin.getInstance().getNodeService().setProperty(node, update.getKey(), update.getOldValue(), context);
		} else {
			CorePlugin.getInstance().getNodeService().unsetProperty(node, update.getKey(), context);
		}
	}

	/**
	 * @author Claudiu Matei 
	 */
	private void undoChildrenUpdate(ChildrenUpdate childrenUpdate) {
		Node node = CorePlugin.getInstance().getResourceService().getNode(childrenUpdate.getFullNodeId());
		Node child = childrenUpdate.getTargetNode();
		NodeService nodeService = CorePlugin.getInstance().getNodeService();

		ServiceContext<NodeService> context = new ServiceContext<NodeService>(nodeService);
		List<Class<?>> controllerList = new ArrayList<Class<?>>();
		controllerList.add(IPersistenceController.class);
		controllerList.add(UpdateController.class);
		context.add(CoreConstants.INVOKE_ONLY_CONTROLLERS_WITH_CLASSES, controllerList);

		switch (childrenUpdate.getType()) {
		case CoreConstants.UPDATE_CHILD_ADDED:
			nodeService.removeChild(node, child, context);
			break;
		case CoreConstants.UPDATE_CHILD_REMOVED:
			List<ChildrenUpdate> removedNodes = childrenUpdate.getRemovedNodes();
			for (int i = 0; i < removedNodes.size(); i++) {
				ChildrenUpdate update = removedNodes.get(i);
				Node removedNode = update.getTargetNode();
				Node removedNodeParent = CorePlugin.getInstance().getResourceService().getNode(update.getFullNodeId());
				if (i == 0) {
					context.add(CoreConstants.INSERT_BEFORE_FULL_NODE_ID, childrenUpdate.getFullTargetNodeAddedBeforeId());
					nodeService.addChild(removedNodeParent, removedNode, context);
					context.add(CoreConstants.INSERT_BEFORE_FULL_NODE_ID, null);
				} else {
					nodeService.addChild(removedNodeParent, removedNode, context);
				}
				Map<String, Object> properties = removedNode.getProperties();
				nodeService.setProperties(removedNode, properties, context);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * @author Claudiu Matei 
	 */
	public void redo(String resourceSet, String commandId) {
		try {
			CorePlugin.getInstance().getLockManager().lock(resourceSet);
			String commandToRedoId = getCommandToRedoId(resourceSet);
			Command command = getCommand(resourceSet, commandId);
			Integer comp = compareCommands(resourceSet, commandId, commandToRedoId);
			if (command == null) {
				throw new IllegalArgumentException(String.format("For resource %s command %s doesn't exist. Current command to redo is: %s",
						resourceSet, commandId,	commandToRedoId));
			} else if (comp == null || comp < 0) {
				throw new IllegalArgumentException(String.format("For resource %s command %s has already been redone. Current command to redo is: %s", 
						resourceSet, commandId, commandToRedoId));
			} else {
				List<Command> commands = getCommands(resourceSet, commandToRedoId, commandId);
				for (int i = 0; i < commands.size(); i++) {
					Command cmd = commands.get(i);
					List<Update> updates = getUpdates(resourceSet, cmd.getLastUpdateIdBeforeCommandExecution(), cmd.getLastUpdateIdAfterCommandExecution());
					for (int k = (cmd.getLastUpdateIdBeforeCommandExecution() == null ? 0 : 1); k < updates.size(); k++) {
						Update update = updates.get(k);
						redoUpdate(update);
					}
				}
				Command nextCommand = getCommandAfter(resourceSet, commandId);
				setCommandToRedoId(resourceSet, (nextCommand != null ? nextCommand.getId() : null));
				setCommandToUndoId(resourceSet, commandId);
			}
		} finally {
			CorePlugin.getInstance().getLockManager().unlock(resourceSet);
		}
	}

	/**
	 * @author Claudiu Matei 
	 */
	private void redoUpdate(Update update) {
		if (update instanceof PropertyUpdate) {
			redoPropertyUpdate((PropertyUpdate) update);
		}
		if (update instanceof ChildrenUpdate) {
			redoChildrenUpdate((ChildrenUpdate) update);
		}
	}
	
	/**
	 * @author Claudiu Matei 
	 */
	private void redoPropertyUpdate(PropertyUpdate update) {
		Node node = CorePlugin.getInstance().getResourceService().getNode(update.getFullNodeId());
		NodeService nodeService = CorePlugin.getInstance().getNodeService();

		ServiceContext<NodeService> context = new ServiceContext<NodeService>(nodeService);
		List<Class<?>> controllerList = new ArrayList<Class<?>>();
		controllerList.add(IPersistenceController.class);
		controllerList.add(UpdateController.class);
		context.add(CoreConstants.INVOKE_ONLY_CONTROLLERS_WITH_CLASSES, controllerList);
		if (update.getIsUnset()) {
			nodeService.unsetProperty(node, update.getKey(), context); 
		} else {
			nodeService.setProperty(node, update.getKey(), update.getValue(), context);
		}
	}

	/**
	 * @author Claudiu Matei 
	 */
	private void redoChildrenUpdate(ChildrenUpdate update) {
		Node node = CorePlugin.getInstance().getResourceService().getNode(update.getFullNodeId());
		NodeService nodeService = CorePlugin.getInstance().getNodeService();
		ServiceContext<NodeService> context = new ServiceContext<NodeService>(nodeService);
		
		List<Class<?>> controllerList = new ArrayList<Class<?>>();
		controllerList.add(IPersistenceController.class);
		controllerList.add(UpdateController.class);
		context.add(CoreConstants.INVOKE_ONLY_CONTROLLERS_WITH_CLASSES, controllerList);

		switch (update.getType()) {
		case CoreConstants.UPDATE_CHILD_ADDED:
			CorePlugin.getInstance().getNodeService().addChild(node, update.getTargetNode(), context);
			break;
		case CoreConstants.UPDATE_CHILD_REMOVED:
			Node targetNode =  CorePlugin.getInstance().getResourceService().getNode(update.getTargetNode().getNodeUri());
			CorePlugin.getInstance().getNodeService().removeChild(node, targetNode, context);
			break;
		default:
			break;
		}
	}
	
	/**
	 * @author Claudiu Matei
	 */
	protected abstract List<Update> getUpdates(String resourceNodeId, String firstUpdateId, String lastUpdateId);
	
	/**
	 * @author Claudiu Matei
	 */
	protected abstract Update getLastUpdate(String resourceNodeId);

	/**
	 * @author Claudiu Matei
	 */
	protected abstract void saveCommand(Command command);
	
	/**
	 * Used only for tests.
	 * 
	 * @author Claudiu Matei
	 */
	public abstract List<Command> getCommands(String resourceNodeId);

	/**
	 * @author Claudiu Matei
	 */
	protected abstract List<Command> getCommands(String resourceNodeId, String firstCommandId, String lastCommandId);

	/**
	 * @author Claudiu Matei
	 */
	protected abstract List<Command> deleteCommandsAfter(String resourceNodeId, String commandId);
	
	/**
	 * @author Claudiu Matei
	 */
	protected abstract Command getCommand(String resourceNodeId, String commandId);
	
	/**
	 * @author Claudiu Matei
	 */
	protected abstract Command getCommandAfter(String resourceNodeId, String commandId);
	
	/**
	 * @author Claudiu Matei
	 */
	protected abstract Command getCommandBefore(String resourceNodeId, String commandId);
	
	/**
	 * @author Claudiu Matei
	 */
	protected abstract Integer compareCommands(String resourceNodeId, String leftCommandId, String rightCommandId);
	
	/**
	 * @author Claudiu Matei
	 */
	public abstract String getCommandToUndoId(String resourceNodeId);

	/**
	 * @author Claudiu Matei
	 */
	protected abstract void setCommandToUndoId(String resourceNodeId, String commandId);

	/**
	 * @author Claudiu Matei
	 */
	public abstract String getCommandToRedoId(String resourceNodeId);

	/**
	 * @author Claudiu Matei
	 */
	protected abstract void setCommandToRedoId(String resourceNodeId, String commandId);

	/**
	 * @author Claudiu Matei
	 */
	protected abstract void clearCommandStack(String resourceNodeId);
	
}
