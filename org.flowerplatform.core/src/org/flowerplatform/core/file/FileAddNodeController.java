package org.flowerplatform.core.file;

import static org.flowerplatform.core.CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS;
import static org.flowerplatform.core.CoreConstants.FILE_IS_DIRECTORY;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.FILE_SYSTEM_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.NAME;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class FileAddNodeController extends AddNodeController {

	@Override
	public void addNode(Node parentNode, Node child, Node insertBeforeNode, ServiceContext context) {
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
			Node fileParentNode;
			String repositoryPath = new Node(parentNode.getResource()).getIdWithinResource();
			
			if (repositoryPath.replace("//", "/").replace('/', '\\')
					.equals(fileAccessController.getPath(parentFile))) {
				fileParentNode = new Node(FILE_SYSTEM_NODE_TYPE, CoreConstants.SELF_RESOURCE, repositoryPath, null);
			} else {
				fileParentNode = new Node(FILE_NODE_TYPE, parentNode.getResource(), fileAccessController.getPath(parentFile), null);
			}
			CorePlugin.getInstance().getNodeService().addChild(fileParentNode, child, insertBeforeNode, context);
			context.add(DONT_PROCESS_OTHER_CONTROLLERS, true);
			return;
		}

		String name = (String)context.get(NAME);
		Object fileToCreate = fileAccessController.getFile(parentFile, name);
		child.setIdWithinResource(fileAccessController.getPath(fileToCreate));
		boolean isDir = (boolean)context.get(FILE_IS_DIRECTORY);
		
		if (fileAccessController.exists(fileToCreate)) {
			throw new RuntimeException("There is already a file with the same name in this location.");
		} else if (!fileAccessController.createFile(fileToCreate, isDir)) {
			throw new RuntimeException("The filename, directory name, or volume label syntax is incorrect");
		}
		child.getOrPopulateProperties();
	}

}
