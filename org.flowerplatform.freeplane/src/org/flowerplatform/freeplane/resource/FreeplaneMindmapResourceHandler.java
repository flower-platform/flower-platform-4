package org.flowerplatform.freeplane.resource;

import static org.flowerplatform.core.CoreConstants.SUBSCRIBABLE_RESOURCES;
import static org.flowerplatform.mindmap.MindMapConstants.FREEPLANE_MINDMAP_RESOURCE_KEY;
import static org.flowerplatform.mindmap.MindMapConstants.MINDMAP_CONTENT_TYPE;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.mindmap.MindMapConstants;
import org.flowerplatform.util.Pair;
import org.flowerplatform.util.Utils;
import org.freeplane.features.url.UrlManager;

/**
 * @author Mariana Gheorghe
 * @author Cristina Constantinescu
 */
public class FreeplaneMindmapResourceHandler extends FreeplanePersistenceResourceHandler implements IPropertiesProvider {

	@Override
	public String getType(Object resourceData, String nodeUri) {
		return MindMapConstants.MINDMAP_NODE_TYPE;
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		if (CoreConstants.FILE_NODE_TYPE.equals(node.getType())) {
			if (node.getNodeUri().endsWith(UrlManager.FREEPLANE_FILE_EXTENSION)) {
				// set subscribable resources
				@SuppressWarnings("unchecked")
				List<Pair<String, String>> subscribableResources = (List<Pair<String, String>>) 
						node.getProperties().get(SUBSCRIBABLE_RESOURCES);
				if (subscribableResources == null) {
					subscribableResources = new ArrayList<Pair<String, String>>();
					node.getProperties().put(SUBSCRIBABLE_RESOURCES, subscribableResources);
				}
				
				String resourceUri = Utils.getUri(FREEPLANE_MINDMAP_RESOURCE_KEY, node.getSchemeSpecificPart(), null);
				Pair<String, String> subscribableResource = new Pair<String, String>(resourceUri, MINDMAP_CONTENT_TYPE);
				subscribableResources.add(0, subscribableResource);
			}
		}
	}

}
