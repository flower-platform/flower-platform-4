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
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class SaveAllAction extends ActionBase {
		
		public function SaveAllAction() {			
			label = CorePlugin.getInstance().getMessage("saveAll.action.label");
			icon = CorePlugin.getInstance().getResourceUrl("images/disk_multiple.png");
		}
				
		override public function run():void {
			var resourceNodes:ArrayCollection = CorePlugin.getInstance().resourceNodesManager.rootNodeIdToEditors.getRootNodeIds();
			for (var i:int = 0; i < resourceNodes.length; i++) {
				CorePlugin.getInstance().serviceLocator.invoke("nodeService.saveResource", [resourceNodes.getItemAt(i)]);				
			}			
		}
				
	}
}
