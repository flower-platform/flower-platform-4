package org.flowerplatform.core.fileSystem;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class FirstRootChildrendProvider extends ChildrenProvider {

	public FirstRootChildrendProvider() {
		setOrderIndex(200);
	}

	@Override
	public List<Node> getChildren(Node node) {
		List<Node> children = new ArrayList<Node>();
		children.add(new Node("root2", null, "2", null));
		return children;	
	}
	
	@Override
	public boolean hasChildren(Node node) {
		return true;
	}
}
