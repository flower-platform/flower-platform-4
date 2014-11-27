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
package org.flowerplatform.core.config_processor;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.TypeDescriptor;

/**
 * Add the type descriptor to the category configured in the config node.
 * 
 * @author Mariana Gheorghe
 */
public class ContainingCategoryConfigNodeProcessor extends AbstractController implements
		IConfigNodeProcessor<Void, TypeDescriptor> {

	@Override
	public Void processConfigNode(Node node, TypeDescriptor type, ServiceContext<NodeService> context) {
		String category = (String) node.getPropertyValue(CoreConstants.NAME);
		type.addCategory(category);
		return null;
	}

}
