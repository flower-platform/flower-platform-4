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
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapRootModelChildrenController extends ControllerBase implements IModelChildrenController {
		
		public function MindMapRootModelChildrenController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function getParent(model:Object):Object {
			return null;
		}
		
		public function getChildren(model:Object):IList {
			// the rootModel keeps all diagram's children in its dynamic object	
			return MindMapDiagramShell(diagramShell).getDynamicObject(diagramShell.rootModel).children;
		}
		
		public function beginListeningForChanges(model:Object):void {
		}
		
		public function endListeningForChanges(model:Object):void {	
		}
		
	}
}