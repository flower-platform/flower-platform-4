package org.flowerplatform.codesync.sdiff.controller;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * 
 * @author Tita Andreea
 *
 */
public class StructureDiffLegendPropertiesProvider extends AbstractController implements IPropertiesProvider {

	public void populateWithProperties(Node node, ServiceContext<NodeService> context){
		node.getProperties().put(CoreConstants.NAME, "legend");
	}
	
}
