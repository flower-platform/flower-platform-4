package org.flowerplatform.core.file;

import static org.flowerplatform.core.CoreConstants.SUBSCRIBABLE_RESOURCES;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Pair;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public class FileSubscribableProvider extends AbstractController implements IPropertiesProvider {

	private String extension;
	
	private String scheme;
	
	private String contentType;
	
	private boolean insertAtBeginning;
	
	public FileSubscribableProvider(String extension, String scheme, String contentType, boolean insertAtBeginning) {
		super();
		this.extension = extension;
		this.scheme = scheme;
		this.contentType = contentType;
		this.insertAtBeginning = insertAtBeginning;
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		if (!node.getNodeUri().endsWith(extension)) {
			return;
		}
		
		@SuppressWarnings("unchecked")
		List<Pair<String, String>> subscribableResources = (List<Pair<String, String>>) 
				node.getProperties().get(SUBSCRIBABLE_RESOURCES);
		if (subscribableResources == null) {
			subscribableResources = new ArrayList<Pair<String, String>>();
			node.getProperties().put(SUBSCRIBABLE_RESOURCES, subscribableResources);
		}
		String resourceUri = Utils.getUri(scheme, Utils.getSchemeSpecificPart(node.getNodeUri()), null);
		Pair<String, String> subscribableResource = new Pair<String, String>(resourceUri, contentType);
		if (insertAtBeginning) {
			subscribableResources.add(0, subscribableResource);
		} else {
			subscribableResources.add(subscribableResource);
		}
	}
	
}
