/* license-start
* 
* Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
* Contributors:
*   Crispico - Initial API and implementation
*
* license-end
*/
package org.flowerplatform.flex_client.core.editor.action {
	import avmplus.getQualifiedClassName;
	
	import flash.utils.getDefinitionByName;
	
	import mx.utils.ObjectUtil;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.ui.RichTextWithRendererView;
	import org.flowerplatform.flex_client.core.node.controller.GenericValueProviderFromDescriptor;
	import org.flowerplatform.flex_client.core.node.controller.NodeControllerUtils;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class RenameAction extends DiagramShellAwareActionBase implements IDialogResultHandler {
		
		private var view:RichTextWithRendererView;
		
		public function RenameAction() {			
			label = CorePlugin.getInstance().getMessage("action.rename");
			icon = CorePlugin.getInstance().getResourceUrl("images/edit.png");
			orderIndex = 30;
		}
				
		override public function get visible():Boolean {			
			return selection != null && selection.length == 1 && selection.getItemAt(0) is Node;
		}
		
		public function handleDialogResult(result:Object):void {
			var node:Node = new Node(result.fullNodeId);
			var titleProvider:GenericValueProviderFromDescriptor = NodeControllerUtils.getTitleProvider(diagramShell.registry, node);
			// invoke service method and wait for result to close the rename popup
			CorePlugin.getInstance().serviceLocator.invoke("nodeService.setProperty", [result.fullNodeId, 
				titleProvider.getPropertyNameFromGenericDescriptor(node), result.name], renameSuccessful);
		}
		
		protected function renameSuccessful(data:Object):void {
			if (view != null) {
				FlexUtilGlobals.getInstance().popupHandlerFactory.removePopup(view);
				view = null;
			}
		}
			
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			
			view = new RichTextWithRendererView();
			view.rendererClass = getDefinitionByName(getQualifiedClassName(diagramShell.getRendererForModel(diagramShellContext, node))) as Class;
			view.rendererModel = Node(ObjectUtil.copy(node));
			view.diagramShellContext = diagramShellContext;
			view.setResultHandler(this);
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
				.setViewContent(view)
				.setWidth(500)
				.setHeight(400)
				.setTitle(label)
				.show();			
		}
				
	}
}
