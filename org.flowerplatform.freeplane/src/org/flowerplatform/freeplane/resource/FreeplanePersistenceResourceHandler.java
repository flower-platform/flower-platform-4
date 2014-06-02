package org.flowerplatform.freeplane.resource;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.resource.ResourceHandler;
import org.flowerplatform.mindmap.MindMapConstants;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.MapWriter.Mode;
import org.freeplane.features.mode.Controller;

/**
 * @author Mariana Gheorghe
 */
public class FreeplanePersistenceResourceHandler extends ResourceHandler {

	@Override
	protected Object load(URI resourceUri) throws Exception {
		MapModel model = null;
		String path = resourceUri.getSchemeSpecificPart();
		path = path.substring(path.indexOf(":") + 1);
		
		InputStreamReader urlStreamReader = null;
		try {
			URL url = ((File) CorePlugin.getInstance().getFileAccessController().getFile(path)).toURI().toURL();
			urlStreamReader = new InputStreamReader(url.openStream());
			
			model = new MapModel();			
			model.setURL(url);
				
			Controller.getCurrentModeController().getMapController().getMapReader().createNodeTreeFromXml(model, urlStreamReader, Mode.FILE);
			return model.getRootNode();
		} finally {
			if (urlStreamReader != null) {
				urlStreamReader.close();
			}
		}
	}

	@Override
	protected String getType(Object resource) {
		return MindMapConstants.MINDMAP_NODE_TYPE;
	}

}
