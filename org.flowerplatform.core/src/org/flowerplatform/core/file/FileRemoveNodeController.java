package org.flowerplatform.core.file;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class FileRemoveNodeController extends RemoveNodeController {

	@Override
	public void removeNode(Node node, Node child, ServiceContext context) {
		IFileAccessController fileAccessController = CorePlugin
				.getInstance().getFileAccessController();	
		try {
			fileAccessController.delete(fileAccessController
					.getFile(child.getIdWithinResource()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
