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

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.IController;

/**
 * @author Elena Posea
 *
 * @param <SELF_DT>
 * @param <PARENT_DT>
 */
public interface IConfigNodeProcessor<SELF_DT, PARENT_DT> extends IController {
	/**
	 * @param node
	 *            the node that you want to process/ instantiate
	 * @param parentProcessedDataStructure
	 * @param context
	 * @return a reference to the data structure of the instantiated node; if
	 *         this node has children that need to be processed, in with field
	 *         of the instance should I add them?
	 */
	SELF_DT processConfigNode(Node node, PARENT_DT parentProcessedDataStructure, ServiceContext<NodeService> context);

}
