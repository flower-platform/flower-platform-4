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
package org.flowerplatform.flex_client.codesync.action {
	
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.codesync.CodeSyncConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.action.DiagramShellAwareActionBase;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class SynchronizeAction extends DiagramShellAwareActionBase {
		
		public static const ID:String = "org.flowerplatform.flex_client.codesync.action.SynchronizeAction";
		
		public function SynchronizeAction() {
			super();
			label = Resources.getMessage("codesync.action.synchronize");
			icon = Resources.synchronizeIcon;
			orderIndex = 400;
		}
		
//		override public function get visible():Boolean {
//			if (selection != null && selection.length == 1 && selection.getItemAt(0) is Node) { 
//				var categorysList:IList = CorePlugin.getInstance().nodeTypeDescriptorRegistry.getExpectedTypeDescriptor(Node(selection.getItemAt(0)).type).categories;
//				for (var i:int=0; i < categorysList.length; i++) {
//					if (categorysList.getItemAt(i) == CodeSyncConstants.CATEGORY_CODESYNC) {
//						return true;
//					}
//				}
//			}
//			return false;
//		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			CorePlugin.getInstance().serviceLocator.invoke("codeSyncOperationsService.synchronize", [node.nodeUri]);
		}
		
	}
}
