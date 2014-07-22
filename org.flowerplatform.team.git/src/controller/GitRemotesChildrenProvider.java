package controller;

import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

public class GitRemotesChildrenProvider extends AbstractController implements IChildrenProvider {

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		// TODO EC: Returns gitRemote type children
		// must get them from somewhere
		return null;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		// TODO Auto-generated method stub
		return false;
	}

}
