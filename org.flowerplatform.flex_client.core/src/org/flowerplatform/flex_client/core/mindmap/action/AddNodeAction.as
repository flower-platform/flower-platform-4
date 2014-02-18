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
package org.flowerplatform.flex_client.core.mindmap.action {
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class AddNodeAction extends ActionBase {
		
		public function AddNodeAction()	{
			super();
			label = CorePlugin.getInstance().getMessage("mindmap.action.add");	
			icon = CorePlugin.getInstance().getResourceUrl("images/add.png");
			orderIndex = 10;
		}
		
		override public function get visible():Boolean {			
			return selection != null && selection.length == 1 && selection.getItemAt(0) is Node;
		}
		
		override public function run():void {
			// TODO MG: replace with specific actions later
			var properties:Object = new Object();
			properties.type = "freeplaneNode";
			// TODO CC: temporary code
			properties.resource = "mm://path_to_resource";
			
			CorePlugin.getInstance().serviceLocator.invoke("nodeService.addChild", [Node(selection.getItemAt(0)).fullNodeId, properties, null]);		
		}
		
	}
}
