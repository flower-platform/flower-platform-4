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
package org.flowerplatform.flex_client.core.mindmap.action {
	import avmplus.getQualifiedClassName;
	
	import flash.events.MouseEvent;
	import flash.utils.getDefinitionByName;
	
	import mx.core.ClassFactory;
	import mx.core.IVisualElement;
	import mx.utils.ObjectUtil;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.NodePropertiesConstants;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flex_client.core.mindmap.ui.RichTextWithRendererView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;
	import org.flowerplatform.flexutil.popup.IMessageBox;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class RenameAction extends DiagramShellAwareActionBase implements IDialogResultHandler {
		
		public function RenameAction() {			
			label = CorePlugin.getInstance().getMessage("mindmap.action.rename");
			icon = CorePlugin.getInstance().getResourceUrl("images/edit.png");
			orderIndex = 30;
		}
				
		override public function get visible():Boolean {			
			return selection != null && selection.length == 1 && selection.getItemAt(0) is Node;
		}
		
		public function handleDialogResult(result:Object):void {
			CorePlugin.getInstance().serviceLocator.invoke("nodeService.setProperty", [result.fullNodeId, NodePropertiesConstants.TEXT, result.name]);
		}
			
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			
			var view:RichTextWithRendererView = new RichTextWithRendererView();
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
