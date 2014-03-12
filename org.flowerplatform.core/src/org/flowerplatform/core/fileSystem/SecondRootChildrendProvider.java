package org.flowerplatform.core.fileSystem;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CorePlugin;
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
	public List<Node> getChildren(Node node) {
		List<Node> children = new ArrayList<Node>();
		children.add(new Node("repo", null, CorePlugin.FILE_SYSTEM_PATH + "/repo1", null));
		children.add(new Node("repo", null, CorePlugin.FILE_SYSTEM_PATH + "/repo2", null));
		return children;	
	}
	
	@Override
	public boolean hasChildren(Node node) {
		// TODO Auto-generated method stub
		return true;
	}
}
