package org.flowerplatform.codesync.sdiff.controller;

import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.CONTAINS_COMMENT;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.IMG_TYPE_COMMENTS;
import static org.flowerplatform.core.CoreConstants.EXECUTE_ONLY_FOR_UPDATER;
import static org.flowerplatform.core.CoreConstants.ICONS;

import java.util.regex.Matcher;

import org.flowerplatform.codesync.sdiff.CodeSyncSdiffPlugin;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

public class CanContainCommentPropertyController extends AbstractController implements IPropertySetter, IPropertiesProvider {

	public CanContainCommentPropertyController() {
			setOrderIndex(10000);
	}
	
	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			node.getProperties().put(ICONS, getIcon(node, context));
	}

	@Override
	public void setProperty(Node node, String property, Object value, ServiceContext<NodeService> context) {
 		if (property.equals(CONTAINS_COMMENT) && (Boolean) value == true) {
			ServiceContext<NodeService> newContext = new ServiceContext<NodeService>(context.getService());
			newContext.getContext().put(EXECUTE_ONLY_FOR_UPDATER, true);
//			node.getProperties().put(ICONS, getIcon(node, context));
			context.getService().setProperty(node, ICONS, getIcon(node, context), newContext);
		}
// 		else{
//	 		if (property.equals(ICONS)) {
//				node.getProperties().put(ICONS, getIcon(node, context));
//	 		}			
//		}
 		
	}

	
	private String getIcon(Node node, ServiceContext<NodeService> context){
		Boolean b = (Boolean) node.getProperties().get(CONTAINS_COMMENT);
		Object obj = node.getProperties().get(ICONS);
		String icon = "";
		if(obj != null) icon = (String) obj;
        // icon = current icon list
		String newIcon = CodeSyncSdiffPlugin.getInstance().getImagePath(IMG_TYPE_COMMENTS);
		// newIcon = the icon that I would like to add
		if (b != null && b == true) {
			if(icon.indexOf(newIcon) == -1){ // this node doesn't already contain this icon
				icon = icon + (!icon.isEmpty() ? CoreConstants.ICONS_SEPARATOR : "") + newIcon;
			}
			return icon;
		}
		else{
			// remove property
			int index = icon.indexOf(newIcon);
			if(index != -1){ // this node contains the icon; remove it
				icon = icon.replaceAll(",?" + Matcher.quoteReplacement(newIcon), "");
			}
			return icon;			
		}
	}
	
//	private void setIcon(Node node, ServiceContext<NodeService> context) {
//		ServiceContext<NodeService> newContext = new ServiceContext<NodeService>(context.getService());
//		newContext.getContext().put(EXECUTE_ONLY_FOR_UPDATER, true);
//		String newIcon = CodeSyncSdiffPlugin.getInstance().getImagePath(IMG_TYPE_COMMENTS);
//		Object obj = node.getProperties().get(ICONS);
//		String icon = "";
//		if (obj != null) icon = (String) obj;
//		icon = icon + (!icon.isEmpty() ? CoreConstants.ICONS_SEPARATOR : "") + newIcon;		
//		context.getService().setProperty(node, ICONS, icon, newContext);
//	}
		
	@Override
	public void unsetProperty(Node node, String property, ServiceContext<NodeService> context) {
		if (property.equals(CONTAINS_COMMENT)) {
			ServiceContext<NodeService> newContext = new ServiceContext<NodeService>();
			newContext.getContext().put(EXECUTE_ONLY_FOR_UPDATER, true);
			context.getService().setProperty(node, ICONS, getIcon(node, context),newContext);
		}
	}

}
