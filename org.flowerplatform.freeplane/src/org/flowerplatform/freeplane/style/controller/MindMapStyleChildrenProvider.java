package org.flowerplatform.freeplane.style.controller;

import static org.flowerplatform.freeplane.FreeplanePlugin.MIND_MAP_STYLE;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.styles.MapStyleModel;


/**
 * @author Sebastian Solomon
 */
public class MindMapStyleChildrenProvider extends ChildrenProvider {

	@Override
	public List<Node> getChildren(Node node, ServiceContext serviceContext) {
		// TODO replace with getRoot
//		NodeModel nodeModel = FreeplanePlugin.getInstance().getFreeplaneUtils().getNodeModel("ID_85319927");
		NodeModel nodeModel = ((MapModel) CorePlugin.getInstance().getResourceService()
					.getRawResourceData(node.getFullNodeId())).getRootNode();
		
		final MapModel map = nodeModel.getMap();
		final MapStyleModel mapStyleModel = MapStyleModel.getExtension(map);
		final MapModel styleMap = mapStyleModel.getStyleMap();
		
		if(styleMap == null){
			// no style found in map
			return null;
		}
		
		List<Node> children = new ArrayList<Node>();
		Enumeration<NodeModel> enumeration = styleMap.getRootNode().children();
		
		while(enumeration.hasMoreElements()){
			for (NodeModel styleNodeModel : enumeration.nextElement().getChildren()) {
				children.add(FreeplanePlugin.getInstance().getFreeplaneUtils().getStandardNode(styleNodeModel, node.getResource()));
			}
		}
		
		return children;
	}
	
	
	@Override
	public boolean hasChildren(Node node, ServiceContext serviceContext) {
		if (node.getType().equals(MIND_MAP_STYLE)) {
			return false;
		}
		return true;
	}

}
