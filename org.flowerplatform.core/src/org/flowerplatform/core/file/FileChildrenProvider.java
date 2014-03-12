package org.flowerplatform.core.file;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class FileChildrenProvider extends ChildrenProvider {
	private static IFileAccessController fileAccessController = CorePlugin
			.getInstance().getFileAccessController();

	@Override
	public List<Node> getChildren(Node node) {
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
			children.add(getNode(object));
		}
		return children;
	}

	private Node getNode(Object file) {
		Node node = new Node(CorePlugin.FILE_NODE_TYPE, null, fileAccessController.getAbsolutePath(file), null);
		return node;
	}

	@Override
	public boolean hasChildren(Node node) {
		Object file = null;
		try {
			file = fileAccessController.getFile(node.getIdWithinResource());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return fileAccessController.hasChildren(file);
	}

}
