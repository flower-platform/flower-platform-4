package org.flowerplatform.core.file;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class FileChildrenProvider extends ChildrenProvider {
	private static IFileAccessController fileAccessController = CorePlugin
			.getInstance().getFileAccessController();

	@Override
	public List<Node> getChildren(Node node, ServiceContext context) {
		String path;
		path = node.getIdWithinResource();

		Object file = null;
		try {
			file = fileAccessController.getFile(path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Object[] files = fileAccessController.listFiles(file);
		List<Node> children = new ArrayList<Node>();
		for (Object object : files) {
			if (".metadata".equals(fileAccessController.getName(object)) && path == null) {
				// TODO CC: temporary code; don't show .metadata directory from workspace
				continue;
			}
			children.add(new Node(CoreConstants.FILE_NODE_TYPE, 
					node.getType().equals(CoreConstants.FILE_SYSTEM_NODE_TYPE) ? node.getFullNodeId() : node.getResource(),
					fileAccessController.getPath(object), null));
		}
		return children;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext context) {
		Object file = null;
		try {
			file = fileAccessController.getFile(node.getIdWithinResource());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return fileAccessController.hasChildren(file);
	}

}
