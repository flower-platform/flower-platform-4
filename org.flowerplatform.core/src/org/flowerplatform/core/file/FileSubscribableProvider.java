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
package org.flowerplatform.core.file;

import static org.flowerplatform.core.CoreConstants.AUTO_SUBSCRIBE_ON_EXPAND;
import static org.flowerplatform.core.CoreConstants.SUBSCRIBABLE_RESOURCES;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Pair;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public class FileSubscribableProvider extends AbstractController implements IPropertiesProvider {

	private String extension;
	
	private String scheme;
	
	private String contentType;
	
	private boolean insertAtBeginning;
	
	public FileSubscribableProvider(String extension, String scheme, String contentType, boolean insertAtBeginning) {
		super();
		this.extension = extension;
		this.scheme = scheme;
		this.contentType = contentType;
		this.insertAtBeginning = insertAtBeginning;
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		if (!extension.equals(CoreConstants.ALL) && !node.getNodeUri().endsWith(extension)) {
			return;
		}
		
		@SuppressWarnings("unchecked")
		List<Pair<String, String>> subscribableResources = (List<Pair<String, String>>) 
				node.getProperties().get(SUBSCRIBABLE_RESOURCES);
		if (subscribableResources == null) {
			subscribableResources = new ArrayList<Pair<String, String>>();
			node.getProperties().put(SUBSCRIBABLE_RESOURCES, subscribableResources);
		}
		String resourceUri = Utils.getUri(scheme, Utils.getSchemeSpecificPart(node.getNodeUri()), null);
		Pair<String, String> subscribableResource = new Pair<String, String>(resourceUri, contentType);
		if (insertAtBeginning) {
			subscribableResources.add(0, subscribableResource);
		} else {
			subscribableResources.add(subscribableResource);
		}
		node.getProperties().put(AUTO_SUBSCRIBE_ON_EXPAND, true);
	}
	
}