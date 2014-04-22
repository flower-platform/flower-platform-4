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
	public class EditNodeDetailsInDialogAction extends ActionBase implements IDialogResultHandler {
		
		private var selectedNode:Node;
			
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
			selectedNode = Node(selection.getItemAt(0));
			var nodeDetails:String = selectedNode.properties[MindMapConstants.NODE_DETAILS];
			
			var richTextWithRendererView:RichTextWithRendererView = new RichTextWithRendererView();
			richTextWithRendererView.setResultHandler(this);
			richTextWithRendererView.icon = Resources.editDetailsInDialogActionIcon;
			if (nodeDetails != null) {
				nodeDetails = Utils.getCompatibleHTMLText(nodeDetails);
				// if text contains html tag, display it as html, otherwise plain text
				richTextWithRendererView.textEditor.textFlow = TextConverter.importToFlow(nodeDetails , Utils.isHTMLText(nodeDetails) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT);
			}
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setTitle(Resources.getMessage("node_details_title"))
				.setViewContent(richTextWithRendererView)
				.show();
		}
		
		public function handleDialogResult(result:Object):void {
			CorePlugin.getInstance().serviceLocator.invoke("nodeService.setProperty", [selectedNode.fullNodeId, 
				MindMapConstants.NODE_DETAILS, result]);
		}
		
	}
}
