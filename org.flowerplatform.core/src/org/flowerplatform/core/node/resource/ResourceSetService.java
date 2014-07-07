package org.flowerplatform.core.node.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.ContextThreadLocal;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.FlowerProperties;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.update.Command;
import org.flowerplatform.core.node.update.remote.ChildrenUpdate;
import org.flowerplatform.core.node.update.remote.PropertyUpdate;
import org.flowerplatform.core.node.update.remote.Update;
import org.flowerplatform.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mariana Gheorghe
 */
public abstract class ResourceSetService {

	protected final static Logger logger = LoggerFactory.getLogger(ResourceSetService.class);
	
	static final String PROP_RESOURCE_UPDATES_MARGIN = "resourceUpdatesMargin"; 
	static final String PROP_DEFAULT_PROP_RESOURCE_UPDATES_MARGIN = "0"; 
	
	public ResourceSetService() {
		CorePlugin.getInstance().getFlowerProperties().addProperty(new FlowerProperties.AddIntegerProperty(PROP_RESOURCE_UPDATES_MARGIN, PROP_DEFAULT_PROP_RESOURCE_UPDATES_MARGIN));
	}
	
	public abstract String addToResourceSet(String resourceSet, String resourceUri);
	
	public abstract void removeFromResourceSet(String resourceSet, String resourceUri);
	
	public void save(String resourceSet, ServiceContext<ResourceSetService> context) {
		logger.debug("Save resource set {}", resourceSet);
		ServiceContext<ResourceService> resourceServiceContext = new ServiceContext<ResourceService>();
		resourceServiceContext.setContext(context.getContext());
		for (String resourceUri : getResourceUris(resourceSet)) {
			CorePlugin.getInstance().getResourceService().save(resourceUri, resourceServiceContext);
		}
	}
	
	public void reload(String resourceSet, ServiceContext<ResourceSetService> context) {
		logger.debug("Reload resource set {}", resourceSet);
		doReload(resourceSet);
		ServiceContext<ResourceService> resourceServiceContext = new ServiceContext<ResourceService>();
		resourceServiceContext.setContext(context.getContext());
		for (String resourceUri : getResourceUris(resourceSet)) {
			CorePlugin.getInstance().getResourceService().reload(resourceUri, resourceServiceContext);
		}
	}

	protected abstract void doReload(String resourceSet);
	
	public abstract void addUpdate(String resourceSet, Update update);
	
	public List<Update> getUpdates(String resourceSet, long timestampOfLastRequest) {
		List<Update> updates = getUpdates(resourceSet);
		List<Update> updatesAddedAfterLastRequest = new ArrayList<Update>();
		if (updates == null) {
			return updatesAddedAfterLastRequest;
		}
		
		if (timestampOfLastRequest > 0 && 
			getLoadedTimestamp(resourceSet) > timestampOfLastRequest + Integer.valueOf(CorePlugin.getInstance().getFlowerProperties().getProperty(PROP_RESOURCE_UPDATES_MARGIN))) {
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
	
	protected abstract List<Update> getUpdates(String resourceSet);
	
	protected abstract long getLoadedTimestamp(String resourceSet);
	
	public abstract List<String> getResourceSets();
	
	public abstract List<String> getResourceUris(String resourceSet);

	/**
	 * @author Claudiu Matei 
	 */
	public String getResourceSet(String nodeUri) {
		Node resourceNode = CorePlugin.getInstance().getResourceService().getResourceNode(nodeUri);
		String resourceSet = (String)resourceNode.getOrPopulateProperties().get(CoreConstants.RESOURCE_SET);
		if (resourceSet == null) resourceSet = resourceNode.getNodeUri();
		return resourceSet;
	}
	
	/**
	 * @author Claudiu Matei 
	 */
	public void startCommand(String resourceSet, String commandTitle) {
		CorePlugin.getInstance().getLockManager().lock(resourceSet);
		Command command=new Command();
		command.setResourceSet(resourceSet);
		command.setTitle(commandTitle);
		Update lastUpdate = getLastUpdate(resourceSet);
		if (lastUpdate != null) command.setLastUpdateIdBeforeCommandExecution(lastUpdate.getId());
		
		ContextThreadLocal context = CorePlugin.getInstance().getContextThreadLocal().get();
		context.setCommand(command);
	}
	
	/**
	 * @author Claudiu Matei 
	 */
	public void addCommand(Command command) {
		if (logger.isDebugEnabled()) {
			logger.debug("For resource = {} adding command = {}", command.getResourceSet(), command);
		}
		
		
		String commandStackUri = Utils.getUri(CoreConstants.COMMAND_STACK_SCHEME, command.getResourceSet());
		Node commandStackNode = new Node(commandStackUri, CoreConstants.COMMAND_STACK_TYPE);
		String commandToUndoId = getCommandToUndoId(command.getResourceSet());

		List<Command> removedCommands = deleteCommandsAfter(command.getResourceSet(), commandToUndoId);
		ServiceContext<NodeService> context = new ServiceContext<NodeService>();
		for (Command cmd : removedCommands) {
			Node node = new Node(Utils.getUri(CoreConstants.COMMAND_STACK_SCHEME, command.getResourceSet(), cmd.getId()), CoreConstants.COMMAND_TYPE);
			CorePlugin.getInstance().getNodeService().removeChild(commandStackNode, node, context);
		}
		
		command.setLastUpdateId(getLastUpdate(command.getResourceSet()).getId());
		saveCommand(command);
		Node newCommandNode = new Node(Utils.getUri(CoreConstants.COMMAND_STACK_SCHEME, command.getResourceSet(), command.getId()), CoreConstants.COMMAND_TYPE);
		newCommandNode.getProperties().put(CoreConstants.NAME, command.getTitle());
		CorePlugin.getInstance().getNodeService().addChild(commandStackNode, newCommandNode, new ServiceContext<NodeService>());

		setCommandToUndoId(command.getResourceSet(), command.getId());
		setCommandToRedoId(command.getResourceSet(), null);
	}

	/**
	 * @author Claudiu Matei 
	 */
	public List<Update> getCommandUpdates(Command command) {
		return getUpdates(command.getResourceSet(), command.getLastUpdateIdBeforeCommandExecution(), command.getLastUpdateId());
	}

	/**
	 * @author Claudiu Matei 
	 */
	public void resetCommandStack(String resourceSet) {
		clearCommandStack(resourceSet);
		setCommandToUndoId(resourceSet, null);
		setCommandToRedoId(resourceSet, null);
		NodeService nodeService = CorePlugin.getInstance().getNodeService();
		ServiceContext<NodeService> context=new ServiceContext<NodeService>();
		
		String commandStackUri = Utils.getUri(CoreConstants.COMMAND_STACK_SCHEME, resourceSet);
		Node commandStackNode = CorePlugin.getInstance().getResourceService().getNode(commandStackUri);
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
					List<Update> updates = getUpdates(resourceSet, cmd.getLastUpdateIdBeforeCommandExecution(), cmd.getLastUpdateId());
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
			undoPropertyUpdate((PropertyUpdate)update);
		}
		else if (update instanceof ChildrenUpdate) {
			undoChildrenUpdate((ChildrenUpdate)update);
		}
	}
	
	/**
	 * @author Claudiu Matei 
	 */
	private void undoPropertyUpdate(PropertyUpdate update) {
		Node node = CorePlugin.getInstance().getResourceService().getNode(update.getFullNodeId());
		ServiceContext<NodeService> context = new ServiceContext<NodeService>();
		if (update.getHasOldValue()) {
			CorePlugin.getInstance().getNodeService().setProperty(node, update.getKey(), update.getOldValue(), context);
		}
		else {
			CorePlugin.getInstance().getNodeService().unsetProperty(node, update.getKey(), context);
		}
	}

	/**
	 * @author Claudiu Matei 
	 */
	private void undoChildrenUpdate(ChildrenUpdate update) {
		Node node = CorePlugin.getInstance().getResourceService().getNode(update.getFullNodeId());
		Node child=update.getTargetNode();
		NodeService nodeService = CorePlugin.getInstance().getNodeService();
		ServiceContext<NodeService> context = new ServiceContext<NodeService>(nodeService);
		switch (update.getType()) {
		case CoreConstants.UPDATE_CHILD_ADDED:
			nodeService.removeChild(node, child, context);
			break;
		case CoreConstants.UPDATE_CHILD_REMOVED:
			context.add(CoreConstants.INSERT_BEFORE_FULL_NODE_ID, update.getFullTargetNodeAddedBeforeId());
			nodeService.addChild(node, child, context);
			Map<String, Object> properties = child.getProperties();
			for (String prop : properties.keySet()) {
				nodeService.setProperty(child, prop, properties.get(prop), context);
			}
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
			Command command=getCommand(resourceSet, commandId);
			Integer comp = compareCommands(resourceSet, commandId, commandToRedoId);
			if (command == null) {
				throw new IllegalArgumentException(String.format("For resource %s command %s doesn't exist. Current command to redo is: %s", resourceSet, commandId,	commandToRedoId));
			} else if (comp == null || comp < 0) {
				throw new IllegalArgumentException(String.format("For resource %s command %s has already been redone. Current command to redo is: %s", resourceSet, commandId, commandToRedoId));
			} else {
				List<Command> commands = getCommands(resourceSet, commandToRedoId, commandId);
				for (int i = 0; i < commands.size(); i++) {
					Command cmd = commands.get(i);
					List<Update> updates = getUpdates(resourceSet, cmd.getLastUpdateIdBeforeCommandExecution(), cmd.getLastUpdateId());
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
			redoPropertyUpdate((PropertyUpdate)update);
		}
		if (update instanceof ChildrenUpdate) {
			redoChildrenUpdate((ChildrenUpdate)update);
		}
	}
	
	/**
	 * @author Claudiu Matei 
	 */
	private void redoPropertyUpdate(PropertyUpdate update) {
		Node node = CorePlugin.getInstance().getResourceService().getNode(update.getFullNodeId());
		ServiceContext<NodeService> context = new ServiceContext<NodeService>();
		CorePlugin.getInstance().getNodeService().setProperty(node, update.getKey(), update.getValue(), context);
	}

	/**
	 * @author Claudiu Matei 
	 */
	private void redoChildrenUpdate(ChildrenUpdate update) {
		Node node = CorePlugin.getInstance().getResourceService().getNode(update.getFullNodeId());
		ServiceContext<NodeService> context = new ServiceContext<NodeService>();
		switch (update.getType()) {
		case CoreConstants.UPDATE_CHILD_ADDED:
			CorePlugin.getInstance().getNodeService().addChild(node, update.getTargetNode(), context);
			break;
		case CoreConstants.UPDATE_CHILD_REMOVED:
			CorePlugin.getInstance().getNodeService().removeChild(node, update.getTargetNode(), context);
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
	protected abstract List<Command> getCommandsAfter(String resourceNodeId, String commandId);

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
