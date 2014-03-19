package org.flowerplatform.core.file;

import static org.flowerplatform.core.NodePropertiesConstants.FILE_IS_DIRECTORY;
import static org.flowerplatform.core.NodePropertiesConstants.TEXT;

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
			parentFile = fileAccessController.getFile(parentNode.getIdWithinResource());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (!fileAccessController.isDirectory(parentFile)) {
			parentFile = fileAccessController.getParentFile(parentFile);
		}

		String name = (String)child.getProperties().get(TEXT);
		Object fileToCreate = fileAccessController.getFile(parentFile, name);
		child.setIdWithinResource(fileAccessController.getAbsolutePath(fileToCreate));
		boolean isDir = (Boolean) child.getProperties().get(FILE_IS_DIRECTORY);
		if (isDir) {
			if (!fileAccessController.createNewDirectory(fileToCreate)) {
				if (fileAccessController.exists(fileToCreate)) {
					throwException();
				} else {
					throw new RuntimeException("The filename, directory name, or volume label syntax is incorrect");
				}
			}
		} else {
			if (!fileAccessController.createNewFile(fileToCreate)) {
				throwException();
			}
		}
		child.getOrPopulateProperties();
	}

	private void throwException() {
		throw new RuntimeException("There is already a file with the same name in this location.");
	}

}
