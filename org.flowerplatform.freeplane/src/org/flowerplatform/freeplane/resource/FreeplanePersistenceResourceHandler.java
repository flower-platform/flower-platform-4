package org.flowerplatform.freeplane.resource;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.resource.ResourceHandler;
import org.flowerplatform.mindmap.MindMapConstants;
import org.flowerplatform.util.Utils;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.MapWriter.Mode;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.url.UrlManager;
import org.freeplane.features.url.mindmapmode.MFileManager;

/**
 * @author Mariana Gheorghe
 * @author Cristina Constantinescu
 */
public class FreeplanePersistenceResourceHandler extends ResourceHandler {

	@Override
	public String getResourceUri(String nodeUri) {
		String scheme = Utils.getScheme(nodeUri);
		String ssp = Utils.getSchemeSpecificPart(nodeUri);
		return Utils.getUri(scheme, ssp, null);
	}
	
	@Override
	protected Object doLoad(String resourceUri) throws Exception {
		MapModel model = null;
		String path = FileControllerUtils.getFilePath(resourceUri);
		
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
	public Object getResourceData(Object resource, String nodeUri) {
		MapModel model = (MapModel) resource;
		String id = Utils.getFragment(nodeUri);
		if (id == null) {
			return model.getRootNode();
		} else {
			return model.getNodeForID(id);
		}
	}
	
	@Override
	public String getType(Object resourceData, String nodeUri) {
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
		throw new RuntimeException("Node " + nodeUri + " does not have a type!");
	}

	@Override
	protected boolean isDirty(Object resource) {
		MapModel model = (MapModel) resource;
		return !model.isSaved();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void doSave(Object resource) throws Exception {
		MapModel model = (MapModel) resource;
		String path = model.getFile().getAbsolutePath();
		File file = new File(URLDecoder.decode(path, "UTF-8"));
		
		((MFileManager) UrlManager.getController()).writeToFile(model, file);
		model.setSaved(true);
	}

}
