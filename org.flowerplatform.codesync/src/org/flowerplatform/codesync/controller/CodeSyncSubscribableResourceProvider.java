package org.flowerplatform.codesync.controller;

import static org.flowerplatform.core.CoreConstants.SUBSCRIBABLE_RESOURCES;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Pair;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncSubscribableResourceProvider extends AbstractController implements IPropertiesProvider, IChildrenProvider {

	private String resource;
	
	public CodeSyncSubscribableResourceProvider(String resource) {
		super();
		this.resource = resource;
	}
	
	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		String resourceUri = getResourceUri(node);
		String contentType = "mindmap";
		
		@SuppressWarnings("unchecked")
		List<Pair<String, String>> subscribableResources = (List<Pair<String, String>>) 
				node.getProperties().get(SUBSCRIBABLE_RESOURCES);
		if (subscribableResources == null) {
			subscribableResources = new ArrayList<Pair<String, String>>();
			node.getProperties().put(SUBSCRIBABLE_RESOURCES, subscribableResources);
		}
		Pair<String, String> subscribableResource = new Pair<String, String>(resourceUri, contentType);
		subscribableResources.add(0, subscribableResource);
	}

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		Node resourceNode = CorePlugin.getInstance().getResourceService().getNode(getResourceUri(node));
		return context.getService().getChildren(resourceNode, context);
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return true;
	}

	protected String getResourceUri(Node node) {
		String repo = FileControllerUtils.getRepo(node);
		return Utils.getUri("fpp", repo + "|" + resource);
	}
	
}
