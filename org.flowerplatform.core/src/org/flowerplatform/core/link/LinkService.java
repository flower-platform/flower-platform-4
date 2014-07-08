package org.flowerplatform.core.link;

import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;

public abstract class LinkService {

	public List<String> getReverseLinks(String nodeUri) {
		Node node = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		return getReverseLinks(node);
	}
	
	public abstract List<String> getReverseLinks(Node node);

	public void addReverseLink(String nodeUri, String linkUri) {
		Node node = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		addReverseLink(node, linkUri);
	}

	public abstract void addReverseLink(Node node, String linkUri);

	public abstract void removeReverseLinks(String resourceUri);
	
}
