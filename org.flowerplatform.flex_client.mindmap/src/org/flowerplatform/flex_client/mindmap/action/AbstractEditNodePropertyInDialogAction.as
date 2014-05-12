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
package org.flowerplatform.flex_client.mindmap.action {
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.ui.RichTextWithRendererView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Sebastian Solomon
	 */
	public class AbstractEditNodePropertyInDialogAction extends ActionBase {
		
		override public function get visible():Boolean {	
			return selection != null && selection.length == 1 && selection.getItemAt(0) is Node && Node(selection.getItemAt(0)).type == "freeplaneNode";
		}
		
		override public function run():void {
			throw new Error("This method needs to be implemented.");
		}
		
		protected function editProperty(property:String, title:String):void {
			var selectedNode:Node = Node(selection.getItemAt(0));
			
			var richTextWithRendererView:RichTextWithRendererView = new RichTextWithRendererView();			
			richTextWithRendererView.icon = icon;
			richTextWithRendererView.node = selectedNode;
			richTextWithRendererView.showRendererArea = false;
			richTextWithRendererView.text = selectedNode.properties[property];
			
			richTextWithRendererView.resultHandler = function(newValue:String):void {				
				// invoke service method and wait for result to close the rename popup
				CorePlugin.getInstance().serviceLocator.invoke("nodeService.setProperty", [selectedNode.fullNodeId, property, newValue], 
					function(result:Object):void {
						FlexUtilGlobals.getInstance().popupHandlerFactory.removePopup(richTextWithRendererView);							
					});
			};
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setTitle(title)
				.setViewContent(richTextWithRendererView)
				.setWidth(500)
				.setHeight(300)
				.show();
		}
		
	}
}
