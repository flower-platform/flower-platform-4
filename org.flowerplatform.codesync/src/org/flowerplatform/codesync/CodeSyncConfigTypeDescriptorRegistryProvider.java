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
package org.flowerplatform.codesync;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.resource.IResourceHandler;
import org.flowerplatform.core.node.resource.in_memory.InMemoryResourceService;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.ITypeDescriptorRegistryProvider;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncConfigTypeDescriptorRegistryProvider implements ITypeDescriptorRegistryProvider {

	@Override
	public TypeDescriptorRegistry getTypeDescriptorRegistry(Object model) {
		if (model instanceof Node) {
			String nodeUri = ((Node) model).getNodeUri();
			
			// get resource uri
			IResourceHandler handler = CorePlugin.getInstance().getResourceService()
					.getResourceHandler(Utils.getScheme(nodeUri));
			String resourceUri = handler.getResourceUri(nodeUri);
			
			if (resourceUri != null) {
				InMemoryResourceService service = (InMemoryResourceService) CorePlugin.getInstance().getResourceService();
				String configDirsKey = service.getConfigDirsKey(resourceUri);
				if (configDirsKey != null) {
					return CodeSyncPlugin.getInstance().getCodeSyncOperationsService()
							.getOrLoadCodeSyncConfig(configDirsKey);
				}
			}
		}
		return null;
	}

}
