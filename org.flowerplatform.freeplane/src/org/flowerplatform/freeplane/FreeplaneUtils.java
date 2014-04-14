package org.flowerplatform.freeplane;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.mindmap.MindMapConstants;
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
		String resourceCategory = CorePlugin.getInstance().getResourceService().getResourceCategory(resource);
		
		String type = null;
		if (MindMapConstants.FREEPLANE_MINDMAP_CATEGORY.equals(resourceCategory)) {
			type = MindMapConstants.MINDMAP_NODE_TYPE;	
		} else if (MindMapConstants.FREEPLANE_PERSISTENCE_CATEGORY.equals(resourceCategory)) {
			// get type from attributes table
			NodeAttributeTableModel attributeTable = (NodeAttributeTableModel) nodeModel.getExtension(NodeAttributeTableModel.class);
			if (attributeTable != null) {
				for (Attribute attribute : attributeTable.getAttributes()) {
					if (attribute.getName().equals(MindMapConstants.FREEPLANE_PERSISTENCE_NODE_TYPE_KEY)) {
						type = (String) attribute.getValue();
						break;
					}
				}
			}
		}
		return new Node(type, resource, nodeModel.createID(), nodeModel);
	}

}
