package org.flowerplatform.freeplane;

import static org.flowerplatform.core.node.controller.ResourceTypeDynamicCategoryProvider.RESOURCE_PATTERN;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import org.flowerplatform.core.node.controller.ResourceTypeDynamicCategoryProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.mindmap.MindMapPlugin;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.MapWriter.Mode;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.url.UrlManager;
import org.freeplane.features.url.mindmapmode.MFileManager;
import org.freeplane.main.headlessmode.HeadlessMModeControllerFactory;

/**
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 */
public class FreeplaneUtils {

	static {
		// configure Freeplane starter
		new FreeplaneHeadlessStarter().createController().setMapViewManager(new HeadlessMapViewController());		
		HeadlessMModeControllerFactory.createModeController();	
	}
	
	public Node getStandardNode(NodeModel nodeModel) {
		String type = null;
		String resource = null;
		
		// get type from attributes table
		NodeAttributeTableModel attributeTable = (NodeAttributeTableModel) nodeModel.getExtension(NodeAttributeTableModel.class);
		if (attributeTable != null) {
			for (Attribute attribute : attributeTable.getAttributes()) {
				if (attribute.getName().equals(MindMapPlugin.FREEPLANE_PERSISTENCE_NODE_TYPE_KEY)) {
					type = (String) attribute.getValue();
					break;
				}
			}
		}
		if (type == null /*|| type.equals(MindMapPlugin.MINDMAP_NODE_TYPE)*/) { 
			// no type provided, maybe this node is provided by a random .mm file, so set type to freeplaneNode
			type = MindMapPlugin.MINDMAP_NODE_TYPE;	
			// TODO CC: temporary code
			resource = "freePlaneMindMap:/" + nodeModel.getMap().getURL().getPath();
		} else {		
			// TODO CC: temporary code
			resource = "freePlanePersistence:/" + nodeModel.getMap().getURL().getPath();
		}
		
		return new Node(type, resource, nodeModel.createID(), nodeModel);
	}
	
	@SuppressWarnings("deprecation")
	public void save(String resource) throws IOException {
//		MapModel newModel = maps.get(resource);
//		((MFileManager) UrlManager.getController()).writeToFile(newModel, newModel.getFile());	
	}
}
