package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.core.NodePropertiesConstants.IS_DIRTY;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.NodePropertiesConstants;
import org.flowerplatform.core.node.NodeService;
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
	public void firstClientSubscribed(String rootNodeId, Map<String, Object> options) throws Exception {
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
		options.put(NodeService.STOP_CONTROLLER_INVOCATION, true);
	}

	@Override
	public void lastClientUnubscribed(String rootNodeId, Map<String, Object> options) {
		Node rootNode = new Node(rootNodeId);
		if (!canHandleResource(rootNode.getIdWithinResource())) {
			return;
		}
		
		logger.debug("Unloaded mindmap {}", rootNode.getIdWithinResource());
		
		CorePlugin.getInstance().getResourceInfoService().unsetRawResourceData(rootNodeId);
		options.put(NodeService.STOP_CONTROLLER_INVOCATION, true);
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void save(String resourceNodeId, Map<String, Object> options) {
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
			options.put(NodeService.STOP_CONTROLLER_INVOCATION, true);
		}
		rawNodeData.setSaved(true);		
	}

	/**
	 * @author Cristina Constantinescu
	 */
	@Override
	public boolean isDirty(String rootNodeId, Map<String, Object> options) {
		Node rootNode = new Node(rootNodeId);
		if (!canHandleResource(rootNode.getIdWithinResource())) {
			return false;
		}
		
		MapModel model = (MapModel) CorePlugin.getInstance().getResourceInfoService().getRawResourceData(rootNode.getFullNodeId());
		
		options.put(NodeService.STOP_CONTROLLER_INVOCATION, true);
		return !model.isSaved();
	}

	private boolean canHandleResource(String path) {
		return path.endsWith(UrlManager.FREEPLANE_FILE_EXTENSION);
	}

}
