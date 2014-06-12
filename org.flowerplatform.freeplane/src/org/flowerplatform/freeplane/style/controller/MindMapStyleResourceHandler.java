package org.flowerplatform.freeplane.style.controller;

import static org.flowerplatform.mindmap.MindMapConstants.FREEPLANE_MINDMAP_RESOURCE_KEY;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.flowerplatform.freeplane.resource.FreeplaneMindmapResourceHandler;
import org.flowerplatform.util.Utils;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.styles.MapStyleModel;

/**
 * @author Mariana Gheorghe
 */
public class MindMapStyleResourceHandler extends FreeplaneMindmapResourceHandler {

	@Override
	public Object getRawNodeDataFromResource(String nodeUri, Object resourceData) {
		String ssp = Utils.getSchemeSpecificPart(nodeUri);
		String resourceUri = Utils.getUri(FREEPLANE_MINDMAP_RESOURCE_KEY, ssp, null);
		MapModel model = (MapModel) CorePlugin.getInstance().getResourceService().getResourceData(resourceUri);
		MapModel styleMap = MapStyleModel.getExtension(model).getStyleMap();
		String fragment = Utils.getFragment(nodeUri);
		if (fragment == null) {
			return styleMap.getRootNode();
		}
		return model.getNodeForID(fragment);
	}

	@Override
	public Node createNodeFromRawNodeData(String nodeUri, Object rawNodeData) {
		return super.createNodeFromRawNodeData(nodeUri, rawNodeData);
	}

	@Override
	protected String getType(String nodeUri, NodeModel nodeModel) {
		if (Utils.getFragment(nodeUri) == null) {
			return FreeplanePlugin.STYLE_ROOT_NODE;
		}
		return super.getType(nodeUri, nodeModel);
	}

	@Override
	public Object load(String resourceUri) throws Exception {
		// TODO Auto-generated method stub
		return super.load(resourceUri);
	}

	@Override
	public boolean isDirty(Object resource) {
		return false;
	}

	@Override
	public void save(Object resourceData) throws Exception {
		// TODO Auto-generated method stub
		super.save(resourceData);
	}

	
	
//	@Override
//	public Object getResource(String resourceUri) {
//		String ssp = Utils.getSchemeSpecificPart(resourceUri);
//		resourceUri = Utils.getUri(FREEPLANE_MINDMAP_RESOURCE_KEY, ssp, null);
//		MapModel model = (MapModel) resourceService.getResource(resourceUri);
//		return MapStyleModel.getExtension(model).getStyleMap();
//	}
//
//	@Override
//	public String getType(Object resourceData, String nodeUri) {
//		if (Utils.getFragment(nodeUri) == null) {
//			return FreeplanePlugin.STYLE_ROOT_NODE;
//		}
//		return super.getType(resourceData, nodeUri);
//	}
//
//	@Override
//	protected Object doLoad(String resourceUri) throws Exception {
//		throw new RuntimeException("MindMapStyle is not subscribable");
//	}
//
//	@Override
//	protected boolean isDirty(Object resource) {
//		return false;
//	}
//
//	@Override
//	protected void doSave(Object resource) throws Exception {
//		throw new RuntimeException("MindMapStyle is not subscribable");
//	}

}
