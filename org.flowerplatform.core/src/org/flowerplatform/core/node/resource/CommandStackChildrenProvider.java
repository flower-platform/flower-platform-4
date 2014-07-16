package org.flowerplatform.core.node.resource;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.update.Command;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;


public class CommandStackChildrenProvider extends AbstractController implements IChildrenProvider {

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		String resourceSet = node.getNodeUri().substring(CoreConstants.COMMAND_STACK_SCHEME.length()+1);
		List<Command> commands = CorePlugin.getInstance().getResourceSetService().getCommands(resourceSet);
		return !commands.isEmpty();
	}

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		String resourceSet = node.getNodeUri().substring(CoreConstants.COMMAND_STACK_SCHEME.length()+1);
		List<Command> commands = CorePlugin.getInstance().getResourceSetService().getCommands(resourceSet);
		ArrayList<Node> children = new ArrayList<>();
		for (Command command : commands) {
			Node childNode = new Node(Utils.getUri(CoreConstants.COMMAND_STACK_SCHEME, resourceSet, command.getId()), CoreConstants.COMMAND_TYPE);
			childNode.getProperties().put(CoreConstants.NAME, command.getTitle());
			childNode.setType(CoreConstants.COMMAND_TYPE);
			children.add(childNode);
		}
		return children;
	}
	
}
