package org.flowerplatform.freeplane.client;

import java.io.StringReader;
import java.util.List;

import org.flowerplatform.freeplane.FreeplanePlugin;
import org.flowerplatform.freeplane.controller.xml_parser.XmlNodePropertiesCreator;
import org.flowerplatform.js_client.java.node.ClientNode;
import org.freeplane.core.extension.IExtension;
import org.freeplane.features.map.INodeView;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.MapWriter.Mode;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.map.mindmapmode.MMapController;
import org.freeplane.features.mode.Controller;

/**
 * @author Valentina Bojan
 */
public class FlowerPlatformManager implements IExtension {
	
	/**
	 * @author Valentina Bojan
	 */
	public FlowerPlatformManager() {
		super();
		Controller controller = Controller.getCurrentController();
		controller.addAction(new FlowerPlatformSubscribe());
	}
	
	/**
	 * @author Valentina Bojan
	 */
	public static FlowerPlatformManager getController() {
		Controller controller = Controller.getCurrentController();
		return (FlowerPlatformManager) controller.getExtension(FlowerPlatformManager.class);
	}

	/**
	 * @author Valentina Bojan
	 */
	public static void install() {
		Controller controller = Controller.getCurrentController();
		controller.addExtension(FlowerPlatformManager.class, new FlowerPlatformManager());
	}

	/**
	 * Method which creates a new @NodeModel based on a given 
	 * XML file content and adds the new node in  a certain map.
	 * @author Valentina Bojan
	 */
	public NodeModel loadNodeFromXmlContent(MapModel map, String xmlContent) {
		MMapController mapController = (MMapController) Controller.getCurrentModeController().getMapController();
		StringReader reader = new StringReader(xmlContent);

		try {
			return mapController.getMapReader().createNodeTreeFromXml(map, reader, Mode.FILE);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * Method which adds a list of children to their @NodeModel parent. Every child
	 * is created based on the corresponding XML file content. If the child has the
	 * flag hasChildren marked as true, then the child receives a dummy child node,
	 * so that the @NodeView knows about this flag.
	 * @author Valentina Bojan
	 */
	public void addChildrenToParent(List<ClientNode> children, NodeModel parent) {
		for (ClientNode child : children) {
			if (!child.getType().equalsIgnoreCase(FreeplanePlugin.STYLE_ROOT_NODE)) {
				// create the child node from the xml content
				XmlNodePropertiesCreator xmlCreator = new XmlNodePropertiesCreator(child);
				NodeModel childNode = loadNodeFromXmlContent(parent.getMap(), xmlCreator.getXmlContent());

				// if node has children => add a dummy child node
				if ((Boolean) child.getPropertyValue("hasChildren")) {
					childNode.insert(new NodeModel(parent.getMap()));
					childNode.setFolded(true);
				}

				// add the extension for memorize the clientNode
				ClientNodeModel clientNodeModel = new ClientNodeModel();
				clientNodeModel.setNode(child);
				childNode.addExtension(clientNodeModel);

				// add the child to its parent node
				parent.insert(childNode);
			}
		}
	}
	
	/**
	 * Method which adds a certian @INodeView for all the children of a given @NodeModel.
	 * @author Valentina Bojan
	 */
	public void addViewerToChildren(NodeModel parent, INodeView viewer) {
		for (NodeModel childNode : parent.getChildren()) {
			childNode.addViewer(viewer);
		}
	}
	
	/**
	 * Method which removes all the children of a given @NodeModel.
	 * @author Valentina Bojan
	 */
	public void removeAllChildren(NodeModel node) {
		int index = node.getChildCount();
		while (index > 0) {
			node.remove(--index);
		}
	}
}
