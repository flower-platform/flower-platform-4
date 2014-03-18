package org.flowerplatform.core.fileSystem;

import static org.flowerplatform.core.CorePlugin.REPOSITORY_TYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	public List<Node> getChildren(Node node, Map<String, Object> options) {
		List<Node> children = new ArrayList<Node>();
		children.add(new Node(REPOSITORY_TYPE, null, CorePlugin.FILE_SYSTEM_PATH + "/repo1", null));
		return children;	
	}
	
	@Override
	public boolean hasChildren(Node node, Map<String, Object> options) {
		// TODO Auto-generated method stub
		return true;
	}
}
