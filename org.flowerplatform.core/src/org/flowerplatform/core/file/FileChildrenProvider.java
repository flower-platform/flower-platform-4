package org.flowerplatform.core.file;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.Pair;

/**
 * @author Sebastian Solomon
 */
public class FileChildrenProvider extends ChildrenProvider {
	private static IFileAccessController fileAccessController = CorePlugin
			.getInstance().getFileAccessController();

	@Override
	public List<Pair<Node, Object>> getChildren(Node node) {
		String path;

		if (node.getType().equals("fileSystem")) {
			path = "d:/temp/fileSystemNode";
		} else {
			path = node.getId();
		}

		Object file = null;
		try {
			file = fileAccessController.getFile(path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Object[] files = fileAccessController.listFiles(file);
		List<Pair<Node, Object>> children = new ArrayList<Pair<Node, Object>>();
		for (Object object : files) {
			children.add(new Pair<Node, Object>(getNode(object), object));
		}
		return children;
	}

	private Node getNode(Object file) {
		Node node = new Node();
		node.setId(fileAccessController.getAbsolutePath(file));
		node.setType("fileNode");
		return node;
	}

}
