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
	public class EditNoteInDialogAction extends ActionBase implements IDialogResultHandler {
		
		private var selectedNode:Node;
			
		public function EditNoteInDialogAction(descriptor:AddChildDescriptor = null)	{
			super();
				
			label = Resources.getMessage("edit_note_in_dialog_label");
			icon = Resources.mindmap_knotesIcon;
			orderIndex = 90;
		}
		
		override public function get visible():Boolean {	
			return selection != null && selection.length == 1 && selection.getItemAt(0) is Node && Node(selection.getItemAt(0)).type == "freeplaneNode";
		}
		
		override public function run():void {
			selectedNode = Node(selection.getItemAt(0));
			var note:String = selectedNode.properties.note;
			
			var richTextWithRendererView:RichTextWithRendererView = new RichTextWithRendererView();
			richTextWithRendererView.setResultHandler(this);
			if (note != null) {
				note = Utils.getCompatibleHTMLText(note);
				// if text contains html tag, display it as html, otherwise plain text
				richTextWithRendererView.textEditor.textFlow = TextConverter.importToFlow(note , Utils.isHTMLText(note) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT);
			}
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setTitle(Resources.getMessage("note_title"))
				.setViewContent(richTextWithRendererView)
				.show();
		}
		
		public function handleDialogResult(result:Object):void {
			CorePlugin.getInstance().serviceLocator.invoke("nodeService.setProperty", [selectedNode.fullNodeId, 
				MindMapConstants.NOTE, result]);
		}
		
	}
}
