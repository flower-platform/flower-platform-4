/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flex_client.core.link {
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.BasicEditorDescriptor;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	
	public class LinkHandler implements ILinkHandler {
		
		public var viewId:String;
		
		public function LinkHandler(viewId:String) {
			this.viewId = viewId;
		}
		
		public function handleLink(command:String, parameters:String):void {
			if (command == CoreConstants.OPEN_RESOURCES) {
				// parameters format = file1,file2,file3|selectResourceAtIndex=1
				var files:String = parameters;
				
				// TODO MG: deactivated because this is a full node id = type|resource|id
//				var index:String;
//				if (parameters.lastIndexOf("|") != -1) { // index exists
//					files = parameters.split("|")[0];
//					index = parameters.split("|")[1];	
//					if (index.match(SELECT_RESOURCE_AT_INDEX + "=[0-9]")) {
//						index = index.substring(index.lastIndexOf("=") + 1);
//					}					
//				}
				
				for each (var file:String in files.split(CoreConstants.OPEN_RESOURCES_SEPARATOR)) {
					file = decodeURI(file);
					var editorDescriptor:BasicEditorDescriptor = CorePlugin.getInstance().contentTypeRegistry["mindmap"];
					editorDescriptor.openEditor(file);
//					CorePlugin.getInstance().serviceLocator.invoke("nodeService.getNode", [file], 
//						function(node:Node):void {
//							CorePlugin.getInstance().openEditor(node);
//						});
				}
			}
		}
	}
}
