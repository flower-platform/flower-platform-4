package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.core.ServiceContext.DONT_PROCESS_OTHER_CONTROLLERS;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.resource.ResourceAccessController;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.MapWriter.Mode;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.url.UrlManager;
import org.freeplane.features.url.mindmapmode.MFileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mariana Gheorghe
 */
public class FreeplaneResourceAccessController extends ResourceAccessController {

	private String resourceCategory;
	
	private final static Logger logger = LoggerFactory.getLogger(FreeplaneResourceAccessController.class);
	
	public FreeplaneResourceAccessController(String resourceCategory) {
		this.resourceCategory = resourceCategory;
	}
	
	@Override
	public void firstClientSubscribed(String rootNodeId, ServiceContext context) throws Exception {
		Node rootNode = new Node(rootNodeId);
		if (!canHandleResource(rootNode.getIdWithinResource())) {
			return;
		}
		
		MapModel model = null;
		URL url = new File(rootNode.getIdWithinResource()).toURI().toURL();
		InputStreamReader urlStreamReader = null;
		try {
			urlStreamReader = new InputStreamReader(url.openStream());
			
			model = new MapModel();			
			model.setURL(url);
				
			Controller.getCurrentModeController().getMapController().getMapReader().createNodeTreeFromXml(model, urlStreamReader, Mode.FILE);		
		} finally {
			if (urlStreamReader != null) {
				urlStreamReader.close();
			}
		}
		
		logger.debug("Loaded mindmap {}", rootNode.getIdWithinResource());
		
		CorePlugin.getInstance().getResourceInfoService().setRawResourceData(rootNodeId, model, resourceCategory);
		context.put(DONT_PROCESS_OTHER_CONTROLLERS, true);
	}

	@Override
	public void lastClientUnubscribed(String rootNodeId, ServiceContext context) {
		Node rootNode = new Node(rootNodeId);
		if (!canHandleResource(rootNode.getIdWithinResource())) {
			return;
		}
		
		logger.debug("Unloaded mindmap {}", rootNode.getIdWithinResource());
		
		CorePlugin.getInstance().getResourceInfoService().unsetRawResourceData(rootNodeId);
		context.put(DONT_PROCESS_OTHER_CONTROLLERS, true);
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void save(String resourceNodeId, ServiceContext context) {
		Node rootNode = new Node(resourceNodeId);
		if (!canHandleResource(rootNode.getIdWithinResource())) {
			return;
		}
		
		MapModel rawNodeData = (MapModel) CorePlugin.getInstance().getResourceInfoService().getRawResourceData(rootNode.getFullNodeId());
		
		try {
			((MFileManager) UrlManager.getController()).writeToFile(rawNodeData, rawNodeData.getFile());
		} catch (Exception e) {
			return;
		} finally {
			context.put(DONT_PROCESS_OTHER_CONTROLLERS, true);
		}
		rawNodeData.setSaved(true);		
	}

	/**
	 * @author Cristina Constantinescu
	 */
	@Override
	public boolean isDirty(String rootNodeId, ServiceContext context) {
		Node rootNode = new Node(rootNodeId);
		if (!canHandleResource(rootNode.getIdWithinResource())) {
			return false;
		}
		
		MapModel model = (MapModel) CorePlugin.getInstance().getResourceInfoService().getRawResourceData(rootNode.getFullNodeId());
		
		context.put(DONT_PROCESS_OTHER_CONTROLLERS, true);
		return !model.isSaved();
	}

	private boolean canHandleResource(String path) {
		return path.endsWith(UrlManager.FREEPLANE_FILE_EXTENSION);
	}

}
