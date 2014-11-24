package org.flowerplatform.codesync.sdiff.controller;

import static org.flowerplatform.codesync.CodeSyncConstants.CODE_SYNC_ICONS;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.CONTAINS_COMMENT;
import static org.flowerplatform.core.CoreConstants.ICONS;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.update.controller.UpdateController;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Elena Posea
 * @author Mariana Gheorghe
 */
public class CanContainCommentPropertyController extends AbstractController implements IPropertySetter, IPropertiesProvider {

	public CanContainCommentPropertyController() {
		// must invoke after the match properties controller
		// so CODE_SYNC_ICONS already 
		setOrderIndex(20000);
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		Boolean containsComment = (Boolean) node.getProperties().get(CONTAINS_COMMENT);
		String icons = (String) node.getProperties().get(CODE_SYNC_ICONS);
		node.getProperties().put(CODE_SYNC_ICONS, getCodeSyncIcons(containsComment, icons));
	}

	@Override
	public void setProperties(Node node, Map<String, Object> properties, ServiceContext<NodeService> context) {
		for (String property : properties.keySet()) {
			if (property.equals(CONTAINS_COMMENT) || property.equals(ICONS)) {
				Boolean containsComment = (Boolean) node.getPropertyValue(CONTAINS_COMMENT);
				String icons = (String) node.getPropertyValue(CODE_SYNC_ICONS);
				ServiceContext<NodeService> newContext = new ServiceContext<NodeService>(context.getService());
				newContext.add(CoreConstants.INVOKE_ONLY_CONTROLLERS_WITH_CLASSES, Collections.singletonList(UpdateController.class));
				context.getService().setProperty(node, CODE_SYNC_ICONS, getCodeSyncIcons(containsComment, icons), newContext);
			}
		}
	}

	private String getCodeSyncIcons(Boolean containsCommentFlag, String icons) {
		boolean containsComment = containsCommentFlag != null && containsCommentFlag; 
		if (icons == null) {
			icons = "";
		}
		
		String icon = ResourcesPlugin.getInstance().getResourceUrl("/images/codesync.sdiff/comment-marker/comments.png");
		if (containsComment) {
			if (icons.indexOf(icon) == -1) { // this node doesn't already contain this icon
				icons = icons + (icons.isEmpty() ? "" : CoreConstants.ICONS_SEPARATOR) + icon;
			}
		} else {
			// remove icon
			int index = icons.indexOf(icon);
			if (index != -1) { // this node contains the icon; remove it
				icons = icons.replaceAll(",?" + Matcher.quoteReplacement(icon), "");
			}
		}
		return icons;
	}

	@Override
	public void unsetProperty(Node node, String property, ServiceContext<NodeService> context) {
		if (property.equals(CONTAINS_COMMENT)) {
			Boolean containsComment = (Boolean) node.getPropertyValue(CONTAINS_COMMENT);
			String icons = (String) node.getPropertyValue(CODE_SYNC_ICONS);
			ServiceContext<NodeService> newContext = new ServiceContext<NodeService>();
			newContext.add(CoreConstants.INVOKE_ONLY_CONTROLLERS_WITH_CLASSES, Collections.singletonList(UpdateController.class));
			context.getService().setProperty(node, CODE_SYNC_ICONS, getCodeSyncIcons(containsComment, icons), newContext);
		}
	}
}
