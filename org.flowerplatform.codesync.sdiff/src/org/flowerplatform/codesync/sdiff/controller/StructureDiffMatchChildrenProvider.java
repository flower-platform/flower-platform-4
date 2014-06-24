package org.flowerplatform.codesync.sdiff.controller;

import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_FEATURE;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.SKIP_MATCH_CHILDREN_PROVIDER;
import static org.flowerplatform.core.CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS;
import static org.flowerplatform.core.CoreConstants.POPULATE_WITH_PROPERTIES;

import java.util.Iterator;
import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public class StructureDiffMatchChildrenProvider extends AbstractController implements IChildrenProvider {

	public StructureDiffMatchChildrenProvider() {
		// invoked before the persistence providers
		setOrderIndex(-10000);
	}
	
	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		if (context.getBooleanValue(SKIP_MATCH_CHILDREN_PROVIDER)) {
			return null;
		}
		
		ServiceContext<NodeService> internalContext = new ServiceContext<>(context.getService());
		internalContext.add(SKIP_MATCH_CHILDREN_PROVIDER, true);
		internalContext.add(POPULATE_WITH_PROPERTIES, true);
		List<Node> children = context.getService().getChildren(node, internalContext);
		
		// filter out unwanted children
		for (Iterator<Node> iterator = children.iterator(); iterator.hasNext();) {
			Node child = iterator.next();
			Object feature = child.getProperties().get(MATCH_FEATURE);
			if ("operationParameters".equals(feature) ||
					"modifiers".equals(feature) ||
					"superInterfaces".equals(feature)) {
				iterator.remove();
			}
		}
		
		context.getContext().put(DONT_PROCESS_OTHER_CONTROLLERS, true);
		return children;
	}
	
	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		context.getContext().put(DONT_PROCESS_OTHER_CONTROLLERS, true);
		return getChildren(node, context).size() > 0;
	}

}
