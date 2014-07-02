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
package org.flowerplatform.text;

import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.text.TextConstants.CONTENT;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.resource.IResourceHandler;
import org.flowerplatform.util.controller.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristina Constantinescu
 */
public class TextResourceHandler extends AbstractController implements IResourceHandler {

	protected final static Logger logger = LoggerFactory.getLogger(TextResourceHandler.class);
	
	@Override
	public String getResourceUri(String nodeUri) {
		return nodeUri;
	}

	@Override
	public Object getRawNodeDataFromResource(String nodeUri, Object resourceData) {
		return resourceData;
	}

	@Override
	public Node createNodeFromRawNodeData(String nodeUri, Object rawNodeData) {
		TextResourceModel model = (TextResourceModel) rawNodeData;
		
		Node node = new Node(nodeUri, FILE_NODE_TYPE);
		node.getProperties().put(CONTENT, model.getResourceContent().toString());
		node.setRawNodeData(model);
		
		return node;
	}

	@Override
	public Object load(String resourceUri) throws Exception {
		TextResourceModel model = new TextResourceModel();
		String path = FileControllerUtils.getFilePathWithRepo(resourceUri);
		model.setResource(CorePlugin.getInstance().getFileAccessController().getFile(path));
		try {
			model.setResourceContent(new StringBuffer(IOUtils.toString(CorePlugin.getInstance().getFileAccessController().getContent(model.getResource()))));
		} catch (IOException e) {
			logger.error("Exception thrown while getting content from resource!");
		}						
		model.setDirty(false);
		return model;
	}

	@Override
	public void save(Object resourceData) throws Exception {
		TextResourceModel model = new TextResourceModel();
		
		CorePlugin.getInstance().getFileAccessController().setContent(model.getResource(), model.getResourceContent().toString());
		model.setDirty(false);
	}

	@Override
	public boolean isDirty(Object resourceData) {	
		return ((TextResourceModel) resourceData).isDirty();
	}

	@Override
	public void unload(Object resourceData) throws Exception {
		// nothing to do
	}

}