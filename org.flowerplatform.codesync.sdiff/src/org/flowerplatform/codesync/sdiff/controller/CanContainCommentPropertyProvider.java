package org.flowerplatform.codesync.sdiff.controller;

import static org.flowerplatform.codesync.CodeSyncConstants.CODESYNC_ICONS;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.ALREADY_BEEN_IN_THIS_SETTER;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.CONTAINS_COMMENT;
import static org.flowerplatform.core.CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS;
import static org.flowerplatform.core.CoreConstants.EXECUTE_ONLY_FOR_UPDATER;

import java.util.Map;
import java.util.regex.Matcher;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Elena Posea
 */
public class CanContainCommentPropertyProvider extends AbstractController implements IPropertySetter, IPropertiesProvider {

	/**
	 * Order index has to be higher than CanContainCommentAddNodeListener's
	 * order index. First add the node, then set/provide properties. 
	 * Order index must also be higher than StructureDiffMatchPropertiesProvider's order index. 
	 * For nodes of type Match, you should first invoke StructureDifffMatchPropertiesProvider, 
	 * then this provider. It should also be higher than StructureDiffCommentController.
	 */
	public CanContainCommentPropertyProvider() {
		setOrderIndex(11000);
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		node.getProperties().put(CODESYNC_ICONS, getCodeSyncIcon(node, context));
	}

	@Override
	public void setProperties(Node node, Map<String, Object> properties, ServiceContext<NodeService> context) {
		if (context.get(ALREADY_BEEN_IN_THIS_SETTER) != null) {
			return;
		}
		for (String property : properties.keySet()) {
			if (property.equals(CONTAINS_COMMENT)) {
				Object value = properties.get(property);
				if (value != null && (boolean) value) {
					continue;
				}
				ServiceContext<NodeService> newContext = new ServiceContext<NodeService>(context.getService());
				newContext.getContext().put(EXECUTE_ONLY_FOR_UPDATER, true);
				newContext.getContext().put(ALREADY_BEEN_IN_THIS_SETTER, true);
				// here I set only the codesync icons, that are not to be persisted;
				// in order not to cycle/infinitely recourse in this setProperty
				// function, I use the ALREADY_BEEN_IN_THIS_SETTER flag, in context
				context.getService().setProperty(node, CODESYNC_ICONS, getCodeSyncIcon(node, context), newContext);
			} else if (property.equals(CODESYNC_ICONS)) {
				ServiceContext<NodeService> newContext = new ServiceContext<NodeService>(context.getService());
				// otherwise, the next controller for MATCH is
				// CanContainCommentPropertyProvider + Updater (we don't want this)
				context.getContext().put(DONT_PROCESS_OTHER_CONTROLLERS, true);
				newContext.getContext().put(ALREADY_BEEN_IN_THIS_SETTER, true);
				context.getService().setProperty(node, CODESYNC_ICONS, getCodeSyncIcon(node, context), newContext);
			}
		}
	}

	private String getCodeSyncIcon(Node node, ServiceContext<NodeService> context) {
		Boolean containsCommentFlag = (Boolean) node.getProperties().get(CONTAINS_COMMENT);
		Object codesyncListOfIcons = node.getProperties().get(CODESYNC_ICONS);
		String icon = "";
		if (codesyncListOfIcons != null) {
			icon = (String) codesyncListOfIcons;
		}
		// icon = current icon list
		String newIcon = ResourcesPlugin.getInstance().getResourceUrl("/images/codesync.sdiff/comment-marker/comments.png");
		// newIcon = the icon that I would like to add
		if (containsCommentFlag != null && containsCommentFlag) {
			if (icon.indexOf(newIcon) == -1) { // this node doesn't already contain this icon
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
			newContext.getContext().put(ALREADY_BEEN_IN_THIS_SETTER, true);
			context.getService().setProperty(node, CODESYNC_ICONS, getCodeSyncIcon(node, context), newContext);
		}
	}
}
