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
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * @author Cristina Constantinescu
	 * @author Mariana Gheorghe
	 */
	public class ReloadAction extends EditorFrontendAwareAction {
		
		public function ReloadAction() {			
			label = Resources.getMessage("reload.action.label");
			icon = Resources.reloadIcon;
			parentId = CoreConstants.FILE_MENU_ID;
			enabled = false;
			orderIndex = 100;
		}
				
		override public function run():void {
			var resourceSets:Array = CorePlugin.getInstance().resourceNodesManager.nodeRegistryManager.getDirtyResourceSetsFromNodeRegistries([editorFrontend.nodeRegistry]);
			if (resourceSets.length == 1) {
				// single resourceNode to reload -> reload without asking
				CorePlugin.getInstance().serviceLocator.invoke("resourceService.reload", [resourceSets[0]]);
			} else {
				// multiple resourceNodes -> show dialog
				CorePlugin.getInstance().resourceNodesManager.showReloadDialog([editorFrontend.nodeRegistry], resourceSets);
			}
		}			
		
	}
}
