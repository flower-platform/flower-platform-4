package org.flowerplatform.codesync.controller;

import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncConfigDirsKeyPropertyProvider extends AbstractController implements IPropertiesProvider {

	public CodeSyncConfigDirsKeyPropertyProvider() {
		// invoke after persistence controller
		setOrderIndex(10000);
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		String configDirs = (String) node.getProperties().get(CodeSyncConstants.CODE_SYNC_CONFIG_PROPERTY_DIRS);
		if (configDirs != null) {
			String path = CodeSyncPlugin.getInstance().getCodeSyncOperationsService().getPathFromResourceNode(node, null);
			node.getProperties().put(CodeSyncConstants.CODE_SYNC_CONFIG_PROPERTY_DIRS_KEY,
					CodeSyncPlugin.getInstance().getCodeSyncOperationsService().getCodeSyncConfigDirsKey(configDirs, path));
		}
	}

}