package org.flowerplatform.freeplane.controller;

import java.io.File;
import java.io.IOException;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.url.UrlManager;
import org.freeplane.features.url.mindmapmode.MFileManager;

/**
 * @author Mariana Gheorghe
 */
public class MindMapFileAddNodeController extends AddNodeController {

	public MindMapFileAddNodeController() {
		// higher order index, to make sure it's invoked after the file was created
		setOrderIndex(10000);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void addNode(Node node, Node child, Node insertBeforeNode, ServiceContext context) {
		String filename = (String) child.getOrPopulateProperties().get(CoreConstants.NAME);
		if (!filename.endsWith(UrlManager.FREEPLANE_FILE_EXTENSION)) {
			return;
		}
		
		try {
			MapModel model = Controller.getCurrentModeController().getMapController().newModel();
			((MFileManager) UrlManager.getController()).writeToFile(model, new File (child.getIdWithinResource()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}