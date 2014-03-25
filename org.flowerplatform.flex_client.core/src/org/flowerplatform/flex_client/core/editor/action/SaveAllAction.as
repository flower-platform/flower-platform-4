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
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @see ResourceNodeManager#saveAllAction
	 * @author Cristina Constantinescu
	 */
	public class SaveAllAction extends ActionBase {
		
		public function SaveAllAction() {			
			label = CorePlugin.getInstance().getMessage("saveAll.action.label");
			icon = FlexUtilGlobals.getInstance().createAbsoluteUrl(CorePlugin.getInstance().getResourceUrl("images/disk_multiple.png"));
			parentId = CorePlugin.FILE_MENU_ID;
			enabled = false;
		}
				
		override public function run():void {
			CorePlugin.getInstance().resourceNodesManager.getAllDirtyResourceNodeIds(false, function(dirtyResourceNodeId:String):void {
				// for each dirty resourceNode found -> save it
				CorePlugin.getInstance().serviceLocator.invoke("resourceService.save", [dirtyResourceNodeId]);
			});
		}
				
	}
}
