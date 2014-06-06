package org.flowerplatform.freeplane.style.controller;

import org.flowerplatform.freeplane.FreeplanePlugin;
import org.flowerplatform.freeplane.resource.FreeplanePersistenceResourceHandler;
import org.flowerplatform.util.Utils;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.styles.MapStyleModel;

/**
 * @author Mariana Gheorghe
 */
public class MindMapStyleResourceHandler extends FreeplanePersistenceResourceHandler {

	@Override
	public Object getResource(String resourceUri) {
		String scheme = "fpp";
		String ssp = Utils.getSchemeSpecificPart(resourceUri);
		resourceUri = Utils.getUri(scheme, ssp, null);
		MapModel model = (MapModel) resourceService.getResource(resourceUri);
		return MapStyleModel.getExtension(model).getStyleMap();
	}

	@Override
	public String getType(Object resourceData, String nodeUri) {
		if (Utils.getFragment(nodeUri) == null) {
			return FreeplanePlugin.STYLE_ROOT_NODE;
		}
		return super.getType(resourceData, nodeUri);
	}

	@Override
	protected Object doLoad(String resourceUri) throws Exception {
		throw new RuntimeException("MindMapStyle is not subscribable");
	}

	@Override
	protected boolean isDirty(Object resource) {
		return false;
	}

	@Override
	protected void doSave(Object resource) throws Exception {
		throw new RuntimeException("MindMapStyle is not subscribable");
	}

}
