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
	import org.flowerplatform.flex_client.core.editor.BasicEditorDescriptor;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 */
	public class OpenAction extends ActionBase {
		
		public var contentType:String;
		public static const ID:String = "org.flowerplatform.flex_client.core.editor.action.OpenAction";
		
		public function OpenAction(contentType:String = null) {
			super();					
			
			this.contentType = contentType;
			if (contentType != null) {
				var editorDescriptor:BasicEditorDescriptor = CorePlugin.getInstance().contentTypeRegistry[contentType];
				label = editorDescriptor.getEditorName();
				icon = editorDescriptor.getIcon();
				parentId = OpenWithEditorComposedAction.ID;
			} else {
				orderIndex = 20;
				label = Resources.getMessage("editor.action.open");
				icon = Resources.openIcon;
			}
		}
		
		override public function get visible():Boolean {			
			return selection != null && selection.length == 1 && selection.getItemAt(0) is Node;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));									
			CorePlugin.getInstance().openEditor(node, contentType);
		}
		
	}
}