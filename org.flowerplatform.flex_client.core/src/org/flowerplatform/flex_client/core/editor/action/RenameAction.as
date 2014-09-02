/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.flex_client.core.editor.action {
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.ui.RichTextWithRendererView;
	import org.flowerplatform.flex_client.core.node.controller.GenericValueProviderFromDescriptor;
	import org.flowerplatform.flex_client.core.node.controller.NodeControllerUtils;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class RenameAction extends DiagramShellAwareActionBase {
		
		public static const ID:String = "org.flowerplatform.flex_client.core.editor.action.RenameAction";
		
		public function RenameAction() {			
			label = Resources.getMessage("mindmap.edit.node.core");
			icon = Resources.editIcon;
			orderIndex = 80;
			
		}
								
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			
			var view:RichTextWithRendererView = new RichTextWithRendererView();			
			view.node = node;
			view.diagramShellContext = diagramShellContext;
			
			var titleProvider:GenericValueProviderFromDescriptor = NodeControllerUtils.getTitleProvider(diagramShellContext.diagramShell.registry, node);
			view.text = String(titleProvider.getValue(node));
			
			view.resultHandler = function(newValue:String):void {				
				// invoke service method and wait for result to close the rename popup
				CorePlugin.getInstance().serviceLocator.invoke("nodeService.setProperty", [node.nodeUri, titleProvider.getPropertyNameFromGenericDescriptor(node), newValue], 
					function(data:Object):void {
						if (view != null) {
							FlexUtilGlobals.getInstance().popupHandlerFactory.removePopup(view);
							view = null;
						};
					});
			};
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
				.setViewContent(view)
				.setWidth(500)
				.setHeight(400)
				.setTitle(label)	
				.setIcon(icon)
				.show();			
		}
				
	}
}
