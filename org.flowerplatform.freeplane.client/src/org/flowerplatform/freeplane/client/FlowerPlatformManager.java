/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.freeplane.client;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.flowerplatform.freeplane.controller.xml_parser.XmlWritter;
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
	 * Method which gets the XML content from a given @NodeModel.
	 * @author Valentina Bojan
	 */
	public String loadXmlContentFromNode(NodeModel node) {
		MMapController mapController = (MMapController) Controller.getCurrentModeController().getMapController();
		StringWriter stringWriter = new StringWriter();
		try {
			mapController.getMapWriter().writeNodeAsXml(stringWriter, node, Mode.FILE, false, false, false);
			return stringWriter.toString();
		} catch (final IOException e) {
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
	public void addChildrenToParent(List<Node> children, NodeModel parent) {
		for (Node child : children) {
			if (!child.getType().equalsIgnoreCase(FreeplanePlugin.STYLE_ROOT_NODE)) {
				// create the child node from the xml content
				XmlWritter xmlCreator = new XmlWritter(FreeplanePlugin.getInstance().getXmlConfiguration(), child.getProperties());
				NodeModel childNode = loadNodeFromXmlContent(parent.getMap(), xmlCreator.getXmlContent());

				// if node has children => add a dummy child node
				if ((Boolean) child.getPropertyValue("hasChildren")) {
					childNode.insert(new NodeModel(parent.getMap()));
					childNode.setFolded(true);
				}

				// add the extension for memorize the clientNode
				ClientNodeModel clientNodeModel = new ClientNodeModel();
				clientNodeModel.setNode((ClientNode) child);
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
	
	/**
	 * Method which returns the result of the difference operation
	 * between two Maps of properties.
	 * @author Valentina Bojan
	 */
	public Map<String, Object> compareProperties(Map<String, Object> properties1, Map<String, Object> properties2) {
		Map<String, Object> diffProperties = new HashMap<String, Object>();
		
		for (Entry<String, Object> property : properties2.entrySet()) {
			String propertyKey = property.getKey();
			Object propertyValue = property.getValue();
			if (!properties1.containsKey(propertyKey) || !properties1.get(propertyKey).toString().equals(propertyValue)) {
				diffProperties.put(property.getKey(), property.getValue());
			}
		}
		
		return diffProperties;
	}
}
