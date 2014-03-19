package org.flowerplatform.freeplane;

import java.io.IOException;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.mindmap.MindMapPlugin;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.NodeModel;
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
	
	public Node getStandardNode(NodeModel nodeModel, String resource) {
		String resourceCategory = CorePlugin.getInstance().getResourceInfoService().getResourceCategory(resource);
		
		String type = null;
		if (MindMapPlugin.FREEPLANE_MINDMAP_CATEGORY.equals(resourceCategory)) {
			type = MindMapPlugin.MINDMAP_NODE_TYPE;	
		} else if (MindMapPlugin.FREEPLANE_PERSISTENCE_CATEGORY.equals(resourceCategory)) {
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
		}
		return new Node(type, resource, nodeModel.createID(), nodeModel);
	}
	
	@SuppressWarnings("deprecation")
	public void save(String resource) throws IOException {
//		MapModel newModel = maps.get(resource);
//		((MFileManager) UrlManager.getController()).writeToFile(newModel, newModel.getFile());	
	}
}
