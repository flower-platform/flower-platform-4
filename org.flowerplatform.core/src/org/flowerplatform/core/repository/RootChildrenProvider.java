package org.flowerplatform.core.repository;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class RootChildrenProvider extends ChildrenProvider {

	public RootChildrenProvider() {
		setOrderIndex(200);
	}

	@Override
	public List<Node> getChildren(Node node, ServiceContext context) {
		List<Node> children = new ArrayList<Node>();
		children.add(new Node(CoreConstants.REPOSITORY_TYPE, null, CoreConstants.FILE_SYSTEM_PATH + "/repo1", null));
		return children;	
	}
	
	@Override
	public boolean hasChildren(Node node, ServiceContext context) {
		return true;
	}
}
