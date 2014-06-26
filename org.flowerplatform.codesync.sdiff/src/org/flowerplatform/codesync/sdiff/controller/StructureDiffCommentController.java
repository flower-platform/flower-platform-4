package org.flowerplatform.codesync.sdiff.controller;

import static org.flowerplatform.mindmap.MindMapConstants.COLOR_BACKGROUND;

import org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public class StructureDiffCommentController extends AbstractController implements IPropertiesProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		node.getProperties().put(COLOR_BACKGROUND, CodeSyncSdiffConstants.MATCH_COLOR_COMMENT);
	}

}
