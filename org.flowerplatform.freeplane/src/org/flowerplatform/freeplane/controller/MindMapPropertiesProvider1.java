/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.freeplane.controller;

import java.util.Collections;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPersistenceController;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.flowerplatform.freeplane.controller.xml_parser.XmlParser;
import org.flowerplatform.util.controller.AbstractController;
import org.freeplane.features.clipboard.ClipboardController;
import org.freeplane.features.clipboard.MindMapNodesSelection;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.mode.ModeController;

/**
 * @author Catalin Burcea
 * @author Valentina Bojan
 */
public class MindMapPropertiesProvider1 extends AbstractController implements IPropertiesProvider, IPersistenceController {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {

		NodeModel rawNodeData = (NodeModel) node.getRawNodeData();
		final ModeController modeController = Controller.getCurrentModeController();
		final ClipboardController clipboardController = (ClipboardController) modeController.getExtension(ClipboardController.class);
		MindMapNodesSelection data = clipboardController.copy(Collections.singleton(rawNodeData), true);

		try {
			String xmlString = data.getTransferData(MindMapNodesSelection.mindMapNodesFlavor).toString();
			XmlParser handler = new XmlParser(FreeplanePlugin.getInstance().getXmlConfiguration(), node);
			handler.parseXML(xmlString);
		} catch (Exception e) {
			new RuntimeException(e);
		}
	}
}
