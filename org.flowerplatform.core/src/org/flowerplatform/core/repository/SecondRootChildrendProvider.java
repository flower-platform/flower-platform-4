package org.flowerplatform.core.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class SecondRootChildrendProvider extends ChildrenProvider {

	public SecondRootChildrendProvider() {
		setOrderIndex(200);
	}

	@Override
	public List<Node> getChildren(Node node, Map<String, Object> options) {
		List<Node> children = new ArrayList<Node>();
		children.add(new Node("repo", null, "d:/temp/fileSystemNode/repo1", null));
		children.add(new Node("repo", null, "d:/temp/fileSystemNode/repo2", null));
		return children;	
	}
	
	@Override
	public boolean hasChildren(Node node, Map<String, Object> options) {
		return true;
	}
}
