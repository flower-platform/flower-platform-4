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
package org.flowerplatform.flex_client.mindmap.controller {
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.mindmap.MindMapEditorDiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelController;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeController extends MindMapModelController {
								
		override public function getChildren(context:DiagramShellContext, model:Object):IList {			
			return Node(model).children;
		}
					
		override public function getExpanded(context:DiagramShellContext, model:Object):Boolean {
			return Node(model).children != null && Node(model).children.length > 0;
		}
		
		override public function setExpanded(context:DiagramShellContext, model:Object, value:Boolean):void {
			if (value) {
				MindMapEditorDiagramShell(context.diagramShell).updateProcessor.requestChildren(context, Node(model));
			} else {				
				MindMapEditorDiagramShell(context.diagramShell).updateProcessor.removeChildren(context, Node(model));
			}		
		}
		
		override public function getSide(context:DiagramShellContext, model:Object):int {
			return Node(model).side;
		}
		
		override public function setSide(context:DiagramShellContext, model:Object, value:int):void {
//			Node(model).side = value;
		}

		override public function isRoot(context:DiagramShellContext, model:Object):Boolean {			
			return Node(model).parent == null;
		}
	
	}
}
