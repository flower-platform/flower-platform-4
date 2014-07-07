package org.flowerplatform.text;

import static org.flowerplatform.core.CoreConstants.FILE_IS_DIRECTORY;

import org.flowerplatform.core.file.FileSubscribableProvider;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Cristina Constantinescu
 */
public class TextFileSubscribableProvider extends FileSubscribableProvider {

	public TextFileSubscribableProvider(String extension, String scheme, String contentType, boolean insertAtBeginning) {
		super(extension, scheme, contentType, insertAtBeginning);
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		if ((boolean) node.getProperties().get(FILE_IS_DIRECTORY)) {
			return;
		}
		super.populateWithProperties(node, context);
	}
	
}
