package org.flowerplatform.core.file;

import static org.flowerplatform.core.NodePropertiesConstants.IS_DIRECTORY;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class FileAddNodeController extends AddNodeController {

	@Override
	public void addNode(Node parentNode, Node child, Node insertBeforeNode) {
		IFileAccessController fileAccessController = CorePlugin.getInstance()
				.getFileAccessController();
		Object parentFile;

		try {
//			if (parentNode.getType().equals(CorePlugin.FILE_SYSTEM_NODE_TYPE)) {
//				parentFile = fileAccessController
//						.getFile("d:\\temp\\fileSystemNode");
//			} else {
//				parentFile = fileAccessController.getFile(parentNode.getIdWithinResource());
//			}
			parentFile = fileAccessController.getFile(parentNode.getIdWithinResource());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (!fileAccessController.isDirectory(parentFile)) {
			parentFile = fileAccessController.getParentFile(parentFile);
		}

		String name = child.getIdWithinResource();
		Object fileToCreate = fileAccessController.getFile(parentFile, name);
		child.setIdWithinResource(fileAccessController.getAbsolutePath(fileToCreate));
		boolean isDir = (Boolean) child.getProperties().get(IS_DIRECTORY);
		if (isDir) {
			fileAccessController.createNewDirectory(fileToCreate);
		} else {
			fileAccessController.createNewFile(fileToCreate);
		}
		child.getOrPopulateProperties();
	}

}
