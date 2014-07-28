package org.flowerplatform.core.node.resource;

import static org.flowerplatform.core.CoreConstants.VIRTUAL_NODE_SCHEME;
import static org.flowerplatform.core.CoreUtils.createNodeUriWithRepo;

import java.util.HashSet;
import java.util.Set;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.resources.ResourcesPlugin;

/**
 * Used for virtual nodes, i.e. nodes that are not contained in a subscribable
 * resource. The node URI for a virtual node is <tt>virtual:repository|type@typeSpecificPart</tt>.
 * 
 * <p>
 * Usage: this handler is a singleton. To register a new virtual node type,
 * get the handler instance ({@link CorePlugin#getVirtualNodeResourceHandler()})
 * and add it via {@link #addVirtualNodeType(String)}.
 * 
 * @author Mariana Gheorghe
 */
public class VirtualNodeResourceHandler implements IResourceHandler {

	private Set<String> virtualNodeTypes = new HashSet<String>();
	
	public void addVirtualNodeType(String type) {
		virtualNodeTypes.add(type);
	}
	
	public String createVirtualNodeUri(String repo, String type, String typeSpecificPart) {
		return createNodeUriWithRepo(VIRTUAL_NODE_SCHEME, repo, type
				+ (typeSpecificPart == null ? "" : "@" + typeSpecificPart));
	}
	
	public String getTypeFromNodeUri(String nodeUri) {
		String ssp = CoreUtils.getSchemeSpecificPartWithoutRepo(nodeUri);
		int index = ssp.indexOf("@");
		if (index < 0) {
			return ssp;
		}
		return ssp.substring(0, index);
	}
	
	public String getTypeSpecificPartFromNodeUri(String nodeUri) {
		String ssp = CoreUtils.getSchemeSpecificPartWithoutRepo(nodeUri);
		int index = ssp.indexOf("@");
		if (index < 0) {
			return null;
		}
		return ssp.substring(index + 1);
	}
	
	@Override
	public String getResourceUri(String nodeUri) {
		return null;
	}
	
	@Override
	public Object getRawNodeDataFromResource(String nodeUri, Object resourceData) {
		return null;
	}

	@Override
	public Node createNodeFromRawNodeData(String nodeUri, Object rawNodeData) {
		String type = getTypeFromNodeUri(nodeUri);
		if (!virtualNodeTypes.contains(type)) {
			throw new RuntimeException(ResourcesPlugin.getInstance().getMessage(
					"virtual.error.unknownUri", type, nodeUri));
		}
		return new Node(nodeUri, type); 
	}

	@Override
	public Object load(String resourceUri) throws Exception {
		return null;
	}

	@Override
	public void save(Object resourceData) throws Exception {
		// nothing to do
	}

	@Override
	public boolean isDirty(Object resourceData) {
		return false;
	}

	@Override
	public void unload(Object resourceData) throws Exception {
		// nothing to do
	}

}
