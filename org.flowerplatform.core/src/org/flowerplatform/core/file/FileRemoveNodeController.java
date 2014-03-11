package org.flowerplatform.core.file;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class FileRemoveNodeController extends RemoveNodeController {
	private IFileAccessController fileAccessController = CorePlugin
			.getInstance().getFileAccessController();

	@Override
	public void removeNode(Node node, Node child) {

		try {
			fileAccessController.deleteFolderContent(fileAccessController
					.getFile(child.getIdWithinResource()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
