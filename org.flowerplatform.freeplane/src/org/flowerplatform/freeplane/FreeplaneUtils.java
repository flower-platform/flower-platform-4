package org.flowerplatform.freeplane;

import static org.flowerplatform.core.node.controller.ResourceTypeDynamicCategoryProvider.RESOURCE_PATTERN;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import org.flowerplatform.core.NodePropertiesConstants;
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

	// TODO CC: temporary code
	public static Map<String, MapModel> maps = new HashMap<String, MapModel>();
				
	static {
		// configure Freeplane starter
		new FreeplaneHeadlessStarter().createController().setMapViewManager(new HeadlessMapViewController());		
		HeadlessMModeControllerFactory.createModeController();	
	}
	
	public NodeModel getNodeModel(Node node) {		
		if (node.getIdWithinResource() == null) {
			if (!maps.containsKey(node.getResource())) {
				try {
					load(node.getResource());
				} catch (Exception e) {	
					throw new RuntimeException(e);
				}
			}
			return maps.get(node.getResource()).getRootNode();
		} 
		return maps.get(node.getResource()).getNodeForID(node.getIdWithinResource());		
	}
	
	public Node getStandardNode(NodeModel nodeModel) {
		String type = null;
		String resource = null;
		
		// get type from attributes table
		NodeAttributeTableModel attributeTable = (NodeAttributeTableModel) nodeModel.getExtension(NodeAttributeTableModel.class);
		if (attributeTable != null) {
			for (Attribute attribute : attributeTable.getAttributes()) {
				if (attribute.getName().equals(NodePropertiesConstants.TYPE)) {
					type = (String) attribute.getValue();
					break;
				}
			}
		}
		if (type == null) { 
			// no type provided, maybe this node is provided by a random .mm file, so set type to freeplaneNode
			type = MindMapPlugin.MINDMAP_NODE_TYPE;			
		}
		
		// TODO CC: temporary code
		resource = "mm:/" + nodeModel.getMap().getURL().getPath();
			
		
		return new Node(type, resource, nodeModel.createID(), nodeModel);
	}
	
	/**
	 * @param path matches {@link ResourceTypeDynamicCategoryProvider#RESOURCE_PATTERN} (e.g. mm://path-to-resource)
	 * @throws Exception
	 */
	public void load(String path) throws Exception {
		URL url = null;
		Matcher matcher = RESOURCE_PATTERN.matcher(path);
		if (matcher.find()) {
			url = new File(matcher.group(2)).toURI().toURL();
		}
		
		InputStreamReader urlStreamReader = null;
		try {
			urlStreamReader = new InputStreamReader(url.openStream());
			
			MapModel newModel = new MapModel();			
			newModel.setURL(url);
				
			Controller.getCurrentModeController().getMapController().getMapReader().createNodeTreeFromXml(newModel, urlStreamReader, Mode.FILE);		
			maps.put(path, newModel);
		} finally {
			if (urlStreamReader != null) {
				urlStreamReader.close();
			}
		}
	}
		
	@SuppressWarnings("deprecation")
	public void save(String resource) throws IOException {
		MapModel newModel = maps.get(resource);
		((MFileManager) UrlManager.getController()).writeToFile(newModel, newModel.getFile());	
	}
}
