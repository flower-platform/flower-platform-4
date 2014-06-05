package org.flowerplatform.freeplane.resource;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.resource.ResourceHandler;
import org.flowerplatform.mindmap.MindMapConstants;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.map.MapWriter.Mode;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.url.UrlManager;
import org.freeplane.features.url.mindmapmode.MFileManager;

/**
 * @author Mariana Gheorghe
 * @author Cristina Constantinescu
 */
public class FreeplanePersistenceResourceHandler extends ResourceHandler {

	@Override
	protected Object doLoad(URI resourceUri) throws Exception {
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
			return model;
		} finally {
			if (urlStreamReader != null) {
				urlStreamReader.close();
			}
		}
	}

	@Override
	public Object getResourceData(Object resource, URI nodeUri) {
		MapModel model = (MapModel) resource;
		String id = nodeUri.getFragment();
		if (id == null) {
			return model.getRootNode();
		} else {
			return model.getNodeForID(id);
		}
	}
	
	@Override
	public String getType(Object resourceData, URI nodeUri) {
		// get type from attributes table
		NodeModel nodeModel = (NodeModel) resourceData;
		NodeAttributeTableModel attributeTable = (NodeAttributeTableModel) nodeModel.getExtension(NodeAttributeTableModel.class);
		if (attributeTable != null) {
			for (Attribute attribute : attributeTable.getAttributes()) {
				if (attribute.getName().equals(MindMapConstants.FREEPLANE_PERSISTENCE_NODE_TYPE_KEY)) {
					return (String) attribute.getValue();
				}
			}
		}
		return MindMapConstants.MINDMAP_NODE_TYPE;
	}

	@Override
	protected boolean isDirty(Object resource) {
		MapModel model = (MapModel) resource;
		return !model.isSaved();
	}

	@Override
	protected void doSave(Object resource) throws Exception {
		MapModel model = (MapModel) resource;
		String path = model.getFile().getAbsolutePath();
		File file = new File(URLDecoder.decode(path, "UTF-8"));
		
		((MFileManager) UrlManager.getController()).writeToFile(model, file);
		model.setSaved(true);
	}

}
