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
package org.flowerplatform.flex_client.mindmap.action {
	
	import flashx.textLayout.conversion.TextConverter;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.AddChildDescriptor;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.ui.RichTextWithRendererView;
	import org.flowerplatform.flex_client.mindmap.MindMapConstants;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;
	
	/**
	 * @author Sebastian Solomon
	 */
	public class EditNodeDetailsInDialogAction extends ActionBase {
		
		public function EditNodeDetailsInDialogAction(descriptor:AddChildDescriptor = null)	{
			super();
			label = Resources.getMessage("edit_node_details_in_dialog_label");
			icon = Resources.editDetailsInDialogActionIcon;
			orderIndex = 85;
		}
		
		override public function get visible():Boolean {	
			return selection != null && selection.length == 1 && selection.getItemAt(0) is Node && Node(selection.getItemAt(0)).type == "freeplaneNode";
		}
		
		override public function run():void {
			var selectedNode:Node = Node(selection.getItemAt(0));
			
			var richTextWithRendererView:RichTextWithRendererView = new RichTextWithRendererView();			
			richTextWithRendererView.icon = icon;
			richTextWithRendererView.node = selectedNode;
			richTextWithRendererView.showRendererArea = false;
			richTextWithRendererView.text = selectedNode.properties[MindMapConstants.NODE_DETAILS];
			
			richTextWithRendererView.resultHandler = function(newValue:String):void {				
				// invoke service method and wait for result to close the rename popup
				CorePlugin.getInstance().serviceLocator.invoke("nodeService.setProperty", [selectedNode.fullNodeId, MindMapConstants.NODE_DETAILS, newValue], 
					function(result:Object):void {
						FlexUtilGlobals.getInstance().popupHandlerFactory.removePopup(richTextWithRendererView);							
					});
			};
						
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setTitle(Resources.getMessage("node_details_title"))
				.setViewContent(richTextWithRendererView)
				.show();
		}
				
	}
}
