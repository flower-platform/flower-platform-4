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
	import org.flowerplatform.flex_client.core.mindmap.MindMapEditorDiagramShell;
	import org.flowerplatform.flex_client.core.mindmap.MindMapEditorFrontend;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexutil.action.ActionBase;
		
	/**
	 * @author Cristina Constantinescu
	 */
	public class RefreshAction extends DiagramShellAwareActionBase {
						
		public function RefreshAction() {
			label = CorePlugin.getInstance().getMessage("mindmap.action.refresh");
			orderIndex = 40;					
		}
		
		override public function get visible():Boolean {			
			return selection != null;
		}
		
		override public function run():void {
			// refresh each node from selection
			for (var i:int = 0; i < selection.length; i++) {
				var obj:Object = selection.getItemAt(i);
				if (obj is Node) {
					MindMapEditorDiagramShell(diagramShell).updateProcessor.checkForNodeUpdates(Node(obj));
				}
			}			
		}

	}
}
