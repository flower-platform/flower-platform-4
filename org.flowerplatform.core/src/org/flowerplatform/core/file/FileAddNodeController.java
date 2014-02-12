package org.flowerplatform.core.file;

import java.util.HashMap;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class FileAddNodeController extends AddNodeController {

	@Override
	public void addNode(Node parentNode, Node child) {
		IFileAccessController fileAccessController = CorePlugin.getInstance()
				.getFileAccessController();
		Object parentFile;

		try {
			if (parentNode.getType().equals("fileSystem")) {
				parentFile = fileAccessController
						.getFile("d:\\temp\\fileSystemNode");
			} else {
				parentFile = fileAccessController.getFile(parentNode.getId());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (!fileAccessController.isDirectory(parentFile)) {
			parentFile = fileAccessController.getParentFile(parentFile);
		}

		String name = ((HashMap) (child.getProperties())).get("body")
				.toString();
		Object fileToCreate = fileAccessController.getFile(parentFile, name);
		boolean isDir = (Boolean) ((HashMap) (child.getProperties()))
				.get("isDirectory");
		if (isDir) {
			fileAccessController.createNewDirectory(fileToCreate);
		} else {
			fileAccessController.createNewFile(fileToCreate);
		}
	}

}
