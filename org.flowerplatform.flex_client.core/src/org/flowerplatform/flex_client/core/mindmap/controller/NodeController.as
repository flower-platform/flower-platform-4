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
package org.flowerplatform.flex_client.core.mindmap.controller {
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.core.mindmap.MindMapEditorDiagramShell;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeController extends ControllerBase implements IMindMapModelController {
								
		public function getChildren(context:DiagramShellContext, model:Object):IList {			
			return Node(model).children;
		}
					
		public function getExpanded(context:DiagramShellContext, model:Object):Boolean {
			return Node(model).children != null && Node(model).children.length > 0;
		}
		
		public function setExpanded(context:DiagramShellContext, model:Object, value:Boolean):void {
			if (value) {
				MindMapEditorDiagramShell(context.diagramShell).updateProcessor.requestChildren(Node(model));
			} else {				
				MindMapEditorDiagramShell(context.diagramShell).updateProcessor.removeChildren(Node(model));
			}		
		}
		
		public function getSide(context:DiagramShellContext, model:Object):int {
			return Node(model).side;
		}
		
		public function setSide(context:DiagramShellContext, model:Object, value:int):void {
//			Node(model).side = value;
		}

		public function isRoot(context:DiagramShellContext, model:Object):Boolean {			
			return Node(model).parent == null;
		}
	
	}
}
