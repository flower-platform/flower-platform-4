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
	import flash.events.MouseEvent;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.popup.IMessageBox;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class RenameAction extends ActionBase {
		
		public function RenameAction() {			
			label = CorePlugin.getInstance().getMessage("mindmap.action.rename");
			orderIndex = 30;
		}
		
		protected function askForTextInput(defaultText:String, title:String, button:String, handler:Function):IMessageBox {
			var textArea:Object;
			var name:String = defaultText;
			var messageBox:Object = FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setTitle(title)
				.setText(name)
				.setWidth(300)
				.setHeight(200)
				.setSelectText(true)
				.addButton(button, function(evt:MouseEvent = null):void {
					if (textArea != null) {
						name = textArea.text;
					}
					handler(name);
				})
				.addButton("Cancel");
			if (messageBox.hasOwnProperty("textArea")) {
				textArea = messageBox.textArea;
				if (textArea.hasOwnProperty("editable")) {
					textArea.editable = true;
				}				
			}
			IMessageBox(messageBox).showMessageBox();
			return IMessageBox(messageBox);
		}
		
		override public function get visible():Boolean {			
			return selection != null && selection.length == 1 && selection.getItemAt(0) is Node;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var messageBox:IMessageBox = askForTextInput(node.properties["body"], "Rename", "Rename",
				function(name:String):void {
					CorePlugin.getInstance().mindMapService.setBody(node, name);
				});		
		}
				
	}
}