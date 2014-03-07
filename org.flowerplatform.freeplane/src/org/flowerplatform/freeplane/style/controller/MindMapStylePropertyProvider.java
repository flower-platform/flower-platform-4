package org.flowerplatform.freeplane.style.controller;

import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MIN_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MAX_WIDTH;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.StylePropertyProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.nodestyle.NodeSizeModel;
import org.freeplane.features.styles.MapStyleModel;

/**
 * @author Sebastian Solomon
 */
public class MindMapStylePropertyProvider extends StylePropertyProvider {

	@Override
	public Object getStylePropertyValue(Node node, String property) {
		String styleName = (String)node.getProperties().get("styleName");
		Object stylePropertyValue = getNodeSizePropertyFromStyle(property, styleName);
		
		if (stylePropertyValue != null && (int)stylePropertyValue != NodeSizeModel.NOT_SET) {
			setDefaultValue(node, property, stylePropertyValue);
			return stylePropertyValue;
		} else {
			Object defaultStylePropertyValue = getNodeSizePropertyFromStyle(property, "Default");
			setDefaultValue(node, property, defaultStylePropertyValue);
			return defaultStylePropertyValue;
		}
			
	}
	
	private Object getNodeSizePropertyFromStyle(String property, String styleName) {
		NodeModel nodeModel = FreeplanePlugin.getInstance().getFreeplaneUtils().getNodeModel("ID_85319927");
		final MapModel map = nodeModel.getMap();
		final MapModel styleMap = MapStyleModel.getExtension(map).getStyleMap();
		
		if(styleMap == null){
			return null;
		}
		
		List<Node> children = new ArrayList<Node>();
		Enumeration<NodeModel> enumeration = styleMap.getRootNode().children();
		
		while(enumeration.hasMoreElements()){
			for (NodeModel styleNodeModel : enumeration.nextElement().getChildren()) {
				children.add(FreeplanePlugin.getInstance().getFreeplaneUtils().getStandardNode(styleNodeModel));
				if (styleNodeModel.getText().equals(styleName) ) {
					if (property.equals(MIN_WIDTH)) {
						return ((NodeSizeModel)styleNodeModel.getExtensions().get(NodeSizeModel.class)).getMinNodeWidth();
					} 
					if (property.equals(MAX_WIDTH)) {
						return ((NodeSizeModel)styleNodeModel.getExtensions().get(NodeSizeModel.class)).getMaxNodeWidth();
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Sets PropertyDescriptor defaultValue.
	 * 
	 * @param node
	 * @param propertyName
	 * @param defaultValue
	 * @see PropertyDescriptor
	 */
	private void setDefaultValue(Node node, String propertyName, Object defaultValue) {
		List<PropertyDescriptor> descriptorList = CorePlugin.getInstance().getNodeService().getPropertyDescriptors(node);
		for (PropertyDescriptor propDescriptor : descriptorList) {
			if (propDescriptor.getName().equals(propertyName)) {
				propDescriptor.setDefaultValue(defaultValue);
			}
		}
	}

}
