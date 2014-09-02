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
	
	import flash.ui.Keyboard;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.shortcut.Shortcut;
	
	/**
	 * @see ResourceNodeManager#saveAllAction
	 * @author Cristina Constantinescu
	 */
	public class SaveAllAction extends ActionBase {
		
		public static const ID:String = "org.flowerplatform.flex_client.core.editor.action.SaveAllAction";
		
		public function SaveAllAction() {			
			label = Resources.getMessage("saveAll.action.label");
			icon = Resources.saveAllIcon;
			parentId = CoreConstants.FILE_MENU_ID;
			enabled = false;
			orderIndex = 120;
			
			FlexUtilGlobals.getInstance().keyBindings.registerBinding(new Shortcut(true, true, false, Keyboard.S), id); // Ctrl + Shift + S
		}
				
		override public function run():void {
			CorePlugin.getInstance().nodeRegistryManager.resourceOperationsManager.saveAll();
		}
				
	}
}
