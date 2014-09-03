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
package org.flowerplatform.flex_client.mindmap.action {
	
	import org.flowerplatform.flex_client.core.editor.action.DiagramShellAwareActionBase;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.mindmap.MindMapEditorDiagramShell;
	import org.flowerplatform.flex_client.resources.Resources;
		
	/**
	 * @author Cristina Constantinescu
	 */
	public class RefreshAction extends DiagramShellAwareActionBase {
		
		public static const ID:String = "org.flowerplatform.flex_client.mindmap.action.RefreshAction";
						
		public function RefreshAction() {
			label = Resources.getMessage("mindmap.action.refresh");
			icon = Resources.refreshIcon;
			orderIndex = 200;					
		}
						
		override public function run():void {
			// refresh each node from selection
			for (var i:int = 0; i < selection.length; i++) {
				var obj:Object = selection.getItemAt(i);
				if (obj is Node) {
					MindMapEditorDiagramShell(diagramShell).nodeRegistry.refresh(Node(obj));
				}
			}
		}	
		
	}
}
