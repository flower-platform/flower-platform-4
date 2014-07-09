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
package org.flowerplatform.freeplane.resource;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.resource.IResourceHandler;
import org.flowerplatform.mindmap.MindMapConstants;
import org.flowerplatform.util.Utils;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.MapWriter.Mode;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.url.UrlManager;
import org.freeplane.features.url.mindmapmode.MFileManager;

/**
 * @author Mariana Gheorghe
 * @author Cristina Constantinescu
 */
public class FreeplaneMindmapResourceHandler implements IResourceHandler {
	
	/**
	 * Strip the fragment.
	 */
	@Override
	public String getResourceUri(String nodeUri) {
		int index = nodeUri.indexOf("#");
		if (index < 0) {
			return nodeUri;
		}
		return nodeUri.substring(0, index);
	}
	
	@Override
	public Object getRawNodeDataFromResource(String nodeUri, Object resourceData) {
		MapModel mapModel = (MapModel) resourceData;
		String id = Utils.getFragment(nodeUri);
		if (id == null) {
			return mapModel.getRootNode();
		}
		return mapModel.getNodeForID(id);
	}
	
	@Override
	public Node createNodeFromRawNodeData(String nodeUri, Object rawNodeData) {
		NodeModel nodeModel = (NodeModel) rawNodeData;
		String type = getType(nodeUri, nodeModel);
		Node node = new Node(nodeUri, type);
		node.setRawNodeData(nodeModel);
		return node;
	}
	
	protected String getType(String nodeUri, NodeModel nodeModel) {
		return MindMapConstants.MINDMAP_NODE_TYPE;
	}
	
	@Override
	public Object load(String resourceUri) throws Exception {
		MapModel model = null;
		String path = FileControllerUtils.getFilePathWithRepo(resourceUri);
		
		InputStreamReader urlStreamReader = null;
		try {
			URL url = ((File) CorePlugin.getInstance().getFileAccessController().getFile(path)).toURI().toURL();
			urlStreamReader = new InputStreamReader(url.openStream());
			
			model = new MapModel();			
			model.setURL(url);
				
			Controller.getCurrentModeController().getMapController().getMapReader().createNodeTreeFromXml(model, urlStreamReader, Mode.FILE);
			return model;
		} finally {
			if (urlStreamReader != null) {
				urlStreamReader.close();
			}
		}
	}

	@Override
	public boolean isDirty(Object resource) {
		MapModel model = (MapModel) resource;
		return !model.isSaved();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void save(Object resourceData) throws Exception {
		MapModel model = (MapModel) resourceData;
		String path = model.getFile().getAbsolutePath();
		File file = new File(URLDecoder.decode(path, "UTF-8"));
		
		((MFileManager) UrlManager.getController()).writeToFile(model, file);
		model.setSaved(true);
	}

	@Override
	public void unload(Object resourceData) throws Exception {
		// nothing to do
	}

}