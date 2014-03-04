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
package org.flowerplatform.flexdiagram.mindmap.controller {
	import mx.collections.IList;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.model_children.ModelChildrenController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapRootModelChildrenController extends ModelChildrenController {
		
		public function MindMapRootModelChildrenController(orderIndex:int = 0) {
			super(orderIndex);
		}
		
		override public function getParent(context:DiagramShellContext, model:Object):Object {
			return null;
		}
		
		override public function getChildren(context:DiagramShellContext, model:Object):IList {
			// the rootModel keeps all diagram's children in its dynamic object	
			return context.diagramShell.getDynamicObject(context, context.diagramShell.rootModel).children;
		}
		
		override public function beginListeningForChanges(context:DiagramShellContext, model:Object):void {
		}
		
		override public function endListeningForChanges(context:DiagramShellContext, model:Object):void {	
		}
		
	}
}