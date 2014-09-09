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

import java.io.StringReader;
import java.util.Collections;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.freeplane.controller.xml_parser.XmlNodePropertiesParser;
import org.freeplane.features.clipboard.ClipboardController;
import org.freeplane.features.clipboard.MindMapNodesSelection;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.mode.ModeController;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Catalin Burcea
 *
 */
public class MindMapPropertiesProvider1 extends PersistencePropertiesProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		super.populateWithProperties(node, context);

		NodeModel rawNodeData = (NodeModel) node.getRawNodeData();
		final ModeController modeController = Controller.getCurrentModeController();
		final ClipboardController clipboardController = (ClipboardController) modeController.getExtension(ClipboardController.class);
		MindMapNodesSelection data = clipboardController.copy(Collections.singleton(rawNodeData), true);

		try {
			String XMLString = data.getTransferData(MindMapNodesSelection.mindMapNodesFlavor).toString();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			XmlNodePropertiesParser handler = new XmlNodePropertiesParser(node);
			InputSource inputSource = new InputSource(new StringReader(XMLString));
			saxParser.parse(inputSource, handler);
			if (handler.convertAllAttributes_tagProcessorDinamicallyAdded) {
				saxParser.reset();
				inputSource = new InputSource(new StringReader(XMLString));
				saxParser.parse(inputSource, handler);
			}
		} catch (Exception e) {
			new RuntimeException(e);
		}
	}
}
