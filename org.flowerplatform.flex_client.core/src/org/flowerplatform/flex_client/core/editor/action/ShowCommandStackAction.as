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
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	
	/**
	 * @author Cristina Constantinescu
	 * @author Mariana Gheorghe
	 */
	public class ShowCommandStackAction extends EditorFrontendAwareAction {
		
		public function ShowCommandStackAction() {			
//			label = Resources.getMessage("reload.action.label");
			label = "Show command stack";
			icon = Resources.reloadIcon;
			parentId = CoreConstants.DEBUG;
			enabled = true;
			orderIndex = 1000;
		}
				
		override public function run():void {
			trace("*** "+CorePlugin.getInstance().resourceNodesManager.nodeRegistryManager);
			var resourceSets:Array = CorePlugin.getInstance().resourceNodesManager.nodeRegistryManager.getResourceSetsFromNodeRegistries([editorFrontend.nodeRegistry]);
			var node:Node = new Node(CoreConstants.COMMAND_STACK+":"+resourceSets[0]);
			CorePlugin.getInstance().openEditor(node);
			
//			if (resourceSets.length == 1) {
//				// single resourceNode to reload -> reload without asking
//				CorePlugin.getInstance().serviceLocator.invoke("resourceService.reload", [resourceSets[0]]);
//			} else {
//				// multiple resourceNodes -> show dialog
//				CorePlugin.getInstance().resourceNodesManager.showReloadDialog([editorFrontend.nodeRegistry], resourceSets);
//			}
		}			
		
	}
}
