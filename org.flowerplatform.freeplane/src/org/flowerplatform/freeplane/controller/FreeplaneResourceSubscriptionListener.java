package org.flowerplatform.freeplane.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.resource.ResourceSubscriptionListener;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.MapWriter.Mode;
import org.freeplane.features.mode.Controller;
import org.freeplane.n3.nanoxml.XMLException;

/**
 * @author Mariana Gheorghe
 */
public class FreeplaneResourceSubscriptionListener extends ResourceSubscriptionListener {

	@Override
	public void firstClientSubscribed(String rootNodeId, Map<String, Object> options) {
		Node rootNode = new Node(rootNodeId);
		if (!canHandleResource(rootNode.getIdWithinResource())) {
			return;
		}
		
		MapModel model = null;
		try {
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
		} catch (IOException | XMLException e) {
			throw new RuntimeException(e);
		}
		
		CorePlugin.getInstance().getResourceInfoService().setRawResourceData(rootNodeId, model);
		options.put(NodeService.STOP_CONTROLLER_INVOCATION, true);
	}

	@Override
	public void lastClientUnubscribed(String rootNodeId, Map<String, Object> options) {
		Node rootNode = new Node(rootNodeId);
		if (!canHandleResource(rootNode.getIdWithinResource())) {
			return;
		}
		
		CorePlugin.getInstance().getResourceInfoService().setRawResourceData(rootNodeId, null);
		options.put(NodeService.STOP_CONTROLLER_INVOCATION, true);
	}
	
	private boolean canHandleResource(String path) {
		return path.endsWith(".mm"); // TODO find extensions from freeplane
	}

}
