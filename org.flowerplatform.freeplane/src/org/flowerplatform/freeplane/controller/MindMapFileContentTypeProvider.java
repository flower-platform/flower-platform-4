package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.core.CoreConstants.CONTENT_TYPE;
import static org.flowerplatform.core.CoreConstants.HIDE_ROOT_NODE;
import static org.flowerplatform.mindmap.MindMapConstants.MINDMAP_CONTENT_TYPE;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.file.FilePropertiesProvider;
import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.url.UrlManager;

/**
 * Sets the <code>CONTENT_TYPE</code> property to {@link #MINDMAP_CONTENT_TYPE}
 * if the node is a file with the .mm extension. Needs to be invoked <b>after</b>
 * the {@link FilePropertiesProvider} responsible with setting the <code>NAME</code>
 * and <code>IS_DIRECTORY</code> properties for the node.
 * 
 * @author Mariana Gheorghe
 */
public class MindMapFileContentTypeProvider extends PropertiesProvider {

	
	public MindMapFileContentTypeProvider() {
		super();
		
		// must be invoked after the file properties providers
		// so the name and is_directory flag are already set
		setOrderIndex(10000);
	}
	
	@Override
	public void populateWithProperties(Node node, ServiceContext context) {
		boolean isDirectory = Boolean.parseBoolean((String) node.getProperties().get(CoreConstants.FILE_IS_DIRECTORY));
		if (!isDirectory) {
			String path = (String) node.getProperties().get(CoreConstants.NAME);
			if (path.endsWith(UrlManager.FREEPLANE_FILE_EXTENSION)) {
				// mindmap files should be open in a new editor, and hide the root
				node.getProperties().put(CONTENT_TYPE, MINDMAP_CONTENT_TYPE);
				node.getProperties().put(HIDE_ROOT_NODE, true);
			}
		}
	}

}
