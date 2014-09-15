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
package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.mindmap.MindMapConstants.FREEPLANE_PERSISTENCE_NODE_TYPE_KEY;

import java.io.File;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IAddNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.url.UrlManager;
import org.freeplane.features.url.mindmapmode.MFileManager;

/**
 * @author Mariana Gheorghe
 */
public class MindMapFileAddNodeController extends AbstractController implements IAddNodeController {

	private String extension;
	private String rootNodeType;
	
	/**
	 *@author see class
	 **/
	public MindMapFileAddNodeController(String extension) {
		super();
		this.extension = extension;

		// higher order index, to make sure it's invoked after the file was created
		setOrderIndex(10000);
	}
	
	/**
	 *@author see class
	 **/
	public MindMapFileAddNodeController(String extension, String rootNodeType) {
		this(extension);
		this.rootNodeType = rootNodeType;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void addNode(Node node, Node child, ServiceContext<NodeService> context) {
		String filename = (String) child.getPropertyValue(CoreConstants.NAME);
		if (!endsWithExtension(filename)) {
			return;
		}
		
		try {
			MapModel model = Controller.getCurrentModeController().getMapController().newModel();
			if (rootNodeType != null) {
				NodeModel rootNode = model.getRootNode();
				NodeAttributeTableModel attributeTable = (NodeAttributeTableModel) rootNode.getExtension(NodeAttributeTableModel.class);		
				if (attributeTable == null) {
					attributeTable = new NodeAttributeTableModel(rootNode);
					rootNode.addExtension(attributeTable);
				}
				attributeTable.getAttributes().add(new Attribute(FREEPLANE_PERSISTENCE_NODE_TYPE_KEY, rootNodeType));
			}
			((MFileManager) UrlManager.getController()).writeToFile(model, (File) CorePlugin.getInstance().getFileAccessController().getFile(
					FileControllerUtils.getFilePathWithRepo(child)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 *@author see class
	 **/
	protected boolean endsWithExtension(String filename) {
		return filename.endsWith(extension);
	}

}
