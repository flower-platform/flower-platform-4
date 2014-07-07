package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_NAME;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.SKIP_PROVIDER;
import static org.flowerplatform.core.CoreConstants.POPULATE_WITH_PROPERTIES;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cristina Constantinescu
 */
public class VirtualRegexChildrenProvider extends AbstractController implements IChildrenProvider {

	public VirtualRegexChildrenProvider() {
		// invoked before the persistence providers
		setOrderIndex(-10000);
	}
	
	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {	
		if (context.getBooleanValue(SKIP_PROVIDER)) {
			return null;
		}
		
		ServiceContext<NodeService> internalContext = new ServiceContext<>(context.getService());
		internalContext.add(SKIP_PROVIDER, true);
		internalContext.add(POPULATE_WITH_PROPERTIES, true);
				
		String baseUri = "fpp" + ":" + Utils.getSchemeSpecificPart(node.getNodeUri());
		
		List<Node> children = context.getService().getChildren(CorePlugin.getInstance().getResourceService().getResourceNode(baseUri), internalContext);
		List<Node> regexChildren = new ArrayList<Node>();		
		for (Node child : children) {
			if (!((String) child.getPropertyValue(REGEX_NAME)).equals(Utils.getFragment(node.getNodeUri()))) {
				continue;
			}
			regexChildren.add(child);
		}				
		context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
		return regexChildren;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {		
		return true;
	}

}
