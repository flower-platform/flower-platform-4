package org.flowerplatform.codesync.sdiff.controller;

import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.CAN_CONTAIN_COMMENT;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.IMG_TYPE_COMMENTS;
import static org.flowerplatform.core.CoreConstants.EXECUTE_ONLY_FOR_UPDATER;
import static org.flowerplatform.core.CoreConstants.ICONS;

import org.flowerplatform.codesync.sdiff.CodeSyncSdiffPlugin;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

public class CanContainCommentPropertyController extends AbstractController implements IPropertySetter, IPropertiesProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		Boolean b = (Boolean) node.getProperties().get(CAN_CONTAIN_COMMENT);
		if (b != null && b == true) { // is dirty
			node.getProperties().put(ICONS, CodeSyncSdiffPlugin.getInstance().getImagePath(IMG_TYPE_COMMENTS));
		}
	}

	@Override
	public void setProperty(Node node, String property, Object value, ServiceContext<NodeService> context) {
		if (property.equals(CAN_CONTAIN_COMMENT) && (Boolean) value == true) {
			setIcon(node, context);
		}
	}

	private void setIcon(Node node, ServiceContext<NodeService> context) {
		ServiceContext<NodeService> newContext = new ServiceContext<NodeService>();
		newContext.getContext().put(EXECUTE_ONLY_FOR_UPDATER, true);
		String newIcon = CodeSyncSdiffPlugin.getInstance().getImagePath(IMG_TYPE_COMMENTS);
		String icon = (String) node.getPropertyValue(ICONS);
		if(icon == null) icon = "";
		icon = icon + (!icon.isEmpty() ? CoreConstants.ICONS_SEPARATOR : "") + newIcon;
		context.getService().setProperty(node, ICONS, icon, newContext);
	}
	

	@Override
	public void unsetProperty(Node node, String property, ServiceContext<NodeService> context) {
		if (property.equals(CAN_CONTAIN_COMMENT)) {
			ServiceContext<NodeService> newContext = new ServiceContext<NodeService>();
			newContext.getContext().put(EXECUTE_ONLY_FOR_UPDATER, true);
			context.getService().unsetProperty(node, ICONS, newContext);
		}
	}

}
