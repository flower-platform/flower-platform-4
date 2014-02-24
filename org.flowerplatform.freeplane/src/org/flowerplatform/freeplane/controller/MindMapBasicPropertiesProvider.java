package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.core.NodePropertiesConstants.HAS_CHILDREN;
import static org.flowerplatform.core.NodePropertiesConstants.TEXT;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.DEFAULT_MAX_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.DEFAULT_MIN_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.ICONS;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MAX_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MIN_WIDTH;

import java.util.List;

import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.mindmap.MindMapPlugin;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.icon.MindIcon;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.nodestyle.NodeSizeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapBasicPropertiesProvider extends PropertiesProvider {

	@Override
	public void populateWithProperties(Node node) {
		node.getProperties().put(TEXT, ((NodeModel) node.getOrRetrieveRawNodeData()).getText());
		// TODO CC: temporary code
		node.getProperties().put(HAS_CHILDREN, ((NodeModel) node.getOrRetrieveRawNodeData()).hasChildren());
		
		// properties are populated from the attributes table
		NodeAttributeTableModel attributeTable = NodeAttributeTableModel.getModel(((NodeModel) node.getOrRetrieveRawNodeData()));
		if (attributeTable != null) {
			for (Attribute attribute : attributeTable.getAttributes()) {
				node.getProperties().put(attribute.getName(), attribute.getValue());
			}
		}
		
		NodeModel rawNodeData = ((NodeModel) node.getOrRetrieveRawNodeData());
		NodeSizeModel nodeSizeModel = NodeSizeModel.getModel(rawNodeData);
				
		if (nodeSizeModel != null && nodeSizeModel.getMinNodeWidth() != NodeSizeModel.NOT_SET) { // property set by user, use it
			node.getProperties().put(MIN_WIDTH, nodeSizeModel.getMinNodeWidth());
		} else { // otherwise, use default value
			node.getProperties().put(MIN_WIDTH, DEFAULT_MIN_WIDTH);
		}
		if (nodeSizeModel != null && nodeSizeModel.getMaxNodeWidth() != NodeSizeModel.NOT_SET) { // property set by user, use it
			node.getProperties().put(MAX_WIDTH, nodeSizeModel.getMaxNodeWidth());
		} else { // otherwise, use default value
			node.getProperties().put(MAX_WIDTH, DEFAULT_MAX_WIDTH);
		}
		
		List<MindIcon> icons = rawNodeData.getIcons();
		if (icons != null) {
			StringBuilder sb = new StringBuilder();
			for (MindIcon icon : icons) {
				sb.append(MindMapPlugin.getInstance().getResourceUrl(icon.getPath()));
				sb.append("|");
			}
			if (sb.length() > 0) { // remove last "|"
				node.getProperties().put(ICONS, sb.substring(0, sb.length() - 1));
			}
		}
		
	}

}
