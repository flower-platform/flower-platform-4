package org.flowerplatform.codesync.template.controller;

import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Mariana Gheorghe
 */
public class GeneratedFileSyncPropertiesProvider extends GeneratedFileSyncPropertiesController implements
		IPropertiesProvider {

	public GeneratedFileSyncPropertiesProvider() {
		setOrderIndex(1000);
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		if (isGeneratedFile(node)) {
			node.getProperties().put(CodeSyncConstants.ADDED, true);
		}
	}

}