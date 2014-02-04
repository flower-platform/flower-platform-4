package org.flowerplatform.core.mindmap;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.Property;
import org.freeplane.features.cloud.CloudModel;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class FreeplanePropertiesProvider extends PropertiesProvider<NodeModel> {

	@Override
	public void populateWithProperties(Node node, NodeModel rawNodeData) {		
		node.getOrCreateProperties().put("body", rawNodeData.getText());
		node.getOrCreateProperties().put("hasChildren", rawNodeData.hasChildren());
		
		// TODO CC: remove
		if (CloudModel.getModel(rawNodeData) != null) {
			node.getOrCreateProperties().put("could_shape", CloudModel.getModel(rawNodeData).getShape().name());
			node.getOrCreateProperties().put("could_color", CloudModel.getModel(rawNodeData).getColor().toString());			
		}
	}

	@Override
	public List<Property> getPropertiesToDisplay(Node node) {		
		List<Property> properties = new ArrayList<Property>();
		properties.add(new Property().setNameAs("body"));	
		
		// TODO CC: remove
		properties.add(new Property().setNameAs("could_shape").setReadOnlyAs(false));	
		properties.add(new Property().setNameAs("could_color"));
		
		return properties;	
	}
	
}
