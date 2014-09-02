package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.core.file.FileControllerUtils.getFileAccessController;
import static org.flowerplatform.core.file.FileControllerUtils.getFilePathWithRepo;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.VirtualNodeResourceHandler;
import org.flowerplatform.util.controller.AbstractController;

public class RegexTestFilePropertiesProvider extends AbstractController implements IPropertiesProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		Object file;
		try {
			file = getFileAccessController().getFile(getFilePathWithRepo(node));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		String testFileName = new VirtualNodeResourceHandler().getTypeSpecificPartFromNodeUri(node.getNodeUri());
		testFileName = testFileName.substring(testFileName.lastIndexOf('/') + 1);
		node.getProperties().put(CoreConstants.NAME, testFileName);
		node.getProperties().put(CoreConstants.HAS_CHILDREN, true);
	}
}
