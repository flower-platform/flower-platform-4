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
package org.flowerplatform.flex_client.core.editor.action {
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class RemoveNodeAction extends ActionBase {
		
		public static const ID:String = "org.flowerplatform.flex_client.core.editor.action.RemoveNodeAction";
				
		public function RemoveNodeAction() {
			super();
			label = Resources.getMessage("action.remove");	
			icon = Resources.deleteIcon;
			orderIndex = 50;
			
		}
		
		override public function run():void {
			CorePlugin.getInstance().serviceLocator.invoke("nodeService.removeChild", [Node(selection.getItemAt(0)).parent.nodeUri, Node(selection.getItemAt(0)).nodeUri]);
		}
	}
}
