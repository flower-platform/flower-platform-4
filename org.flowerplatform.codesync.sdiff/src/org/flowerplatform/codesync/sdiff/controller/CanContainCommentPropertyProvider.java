package org.flowerplatform.codesync.sdiff.controller;

import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.CONTAINS_COMMENT;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.IMG_TYPE_COMMENTS;
import static org.flowerplatform.core.CoreConstants.CODESYNC_ICONS;
import static org.flowerplatform.core.CoreConstants.EXECUTE_ONLY_FOR_UPDATER;
import static org.flowerplatform.core.CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS;

import java.util.regex.Matcher;

import org.flowerplatform.codesync.sdiff.CodeSyncSdiffPlugin;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

public class CanContainCommentPropertyProvider extends AbstractController implements IPropertySetter, IPropertiesProvider {

	public CanContainCommentPropertyProvider() {
		setOrderIndex(10001);
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		node.getProperties().put(CODESYNC_ICONS, getCodeSyncIcon(node, context));
	}

	@Override
	public void setProperty(Node node, String property, Object value, ServiceContext<NodeService> context) {
 		if(context.get("myHardcodedFlag") != null) return;
		if ((property.equals(CONTAINS_COMMENT) && (Boolean) value == true)) {
			ServiceContext<NodeService> newContext = new ServiceContext<NodeService>(context.getService());
			newContext.getContext().put(EXECUTE_ONLY_FOR_UPDATER, true);
			newContext.getContext().put("myHardcodedFlag", true);
			context.getService().setProperty(node, CODESYNC_ICONS, getCodeSyncIcon(node, context), newContext);
		} else{
			 if(property.equals(CODESYNC_ICONS)){
				ServiceContext<NodeService> newContext = new ServiceContext<NodeService>(context.getService());
				// otherwise, the next controller for MATCH is CanContainCommentPropertyProvider + Updater (we don't want this)
				context.getContext().put(DONT_PROCESS_OTHER_CONTROLLERS, true);
				newContext.getContext().put("myHardcodedFlag", true);
				context.getService().setProperty(node, CODESYNC_ICONS, getCodeSyncIcon(node, context), newContext);	 
			 }
		}	
	}

	private String getCodeSyncIcon(Node node, ServiceContext<NodeService> context) {
		Boolean b = (Boolean) node.getProperties().get(CONTAINS_COMMENT);
		Object obj = node.getProperties().get(CODESYNC_ICONS);
		String icon = "";
		if (obj != null)
			icon = (String) obj;
		// icon = current icon list
		String newIcon = CodeSyncSdiffPlugin.getInstance().getImagePath(IMG_TYPE_COMMENTS);
		// newIcon = the icon that I would like to add
		if (b != null && b == true) {
			if (icon.indexOf(newIcon) == -1) { // this node doesn't already
												// contain this icon
				icon = icon + (!icon.isEmpty() ? CoreConstants.ICONS_SEPARATOR : "") + newIcon;
			}
			return icon;
		} else {
			// remove property
			int index = icon.indexOf(newIcon);
			if (index != -1) { // this node contains the icon; remove it
				icon = icon.replaceAll(",?" + Matcher.quoteReplacement(newIcon), "");
			}
			return icon;
		}
	}

	@Override
	public void unsetProperty(Node node, String property, ServiceContext<NodeService> context) {
		if (property.equals(CONTAINS_COMMENT)) {
			ServiceContext<NodeService> newContext = new ServiceContext<NodeService>();
			newContext.getContext().put(EXECUTE_ONLY_FOR_UPDATER, true);
			newContext.getContext().put("myHardcodedFlag", true);
			context.getService().setProperty(node, CODESYNC_ICONS, getCodeSyncIcon(node, context), newContext);
		}
	}

}
