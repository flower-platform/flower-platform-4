package org.flowerplatform.core.repository;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Sebastian Solomon
 */
public class RepositoryPropertiesProvider extends AbstractController implements IPropertiesProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {		
		IFileAccessController fileAccessController = CorePlugin.getInstance().getFileAccessController();
		Object file;
		try {
			file = fileAccessController.getFile(Utils.getFragment(node.getNodeUri()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		node.getProperties().put(CoreConstants.NAME, fileAccessController.getName(file));
	}
	
}