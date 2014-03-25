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
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @see ResourceNodeManager#saveAction
	 * @author Cristina Constantinescu
	 */
	public class SaveAction extends ActionBase {
		
		public var editorFrontend:EditorFrontend;
		
		public function SaveAction() {			
			label = CorePlugin.getInstance().getMessage("save.action.label");
			icon = FlexUtilGlobals.getInstance().createAbsoluteUrl(CorePlugin.getInstance().getResourceUrl("images/disk.png"));
			parentId = CorePlugin.FILE_MENU_ID;
			enabled = false;
		}
				
		override public function run():void {			
			var dirtyResourceNodeIds:Array = CorePlugin.getInstance().resourceNodesManager.getEditorsDirtyResourceNodeIds([editorFrontend]);
			if (dirtyResourceNodeIds.length == 1) { 
				// single resourceNode to save -> save without asking
				CorePlugin.getInstance().serviceLocator.invoke("resourceService.save", [dirtyResourceNodeIds[0]]);
			} else { 
				// multiple resourceNodes to save -> show dialog
				CorePlugin.getInstance().resourceNodesManager.showSaveDialogIfDirtyStateOrCloseEditors([editorFrontend], dirtyResourceNodeIds, function():void {});
			}
		}
				
	}
}
