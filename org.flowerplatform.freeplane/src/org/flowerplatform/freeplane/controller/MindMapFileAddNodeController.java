package org.flowerplatform.freeplane.controller;

import java.io.File;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IAddNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.url.UrlManager;
import org.freeplane.features.url.mindmapmode.MFileManager;

/**
 * @author Mariana Gheorghe
 */
public class MindMapFileAddNodeController extends AbstractController implements IAddNodeController {

	public MindMapFileAddNodeController() {
		// higher order index, to make sure it's invoked after the file was created
		setOrderIndex(10000);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void addNode(Node node, Node child, ServiceContext<NodeService> context) {
		String filename = (String) child.getOrPopulateProperties().get(CoreConstants.NAME);
		if (!filename.endsWith(UrlManager.FREEPLANE_FILE_EXTENSION)) {
			return;
		}
		
		try {
			MapModel model = Controller.getCurrentModeController().getMapController().newModel();
			((MFileManager) UrlManager.getController()).writeToFile(model, (File) CorePlugin.getInstance().getFileAccessController().getFile(child.getIdWithinResource()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
