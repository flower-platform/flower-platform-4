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
package org.flowerplatform.flexdiagram.mindmap.controller {
	import mx.collections.ArrayList;
	import mx.collections.IList;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexutil.controller.AbstractController;

	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapModelController extends AbstractController {
		
		public function getChildren(context:DiagramShellContext, model:Object):IList {
			throw new Error("This method needs to be implemented.");
		}

		public function getExpanded(context:DiagramShellContext, model:Object):Boolean {
			throw new Error("This method needs to be implemented.");
		}
		
		public function setExpanded(context:DiagramShellContext, model:Object, value:Boolean):void {
			throw new Error("This method needs to be implemented.");
		}
		
		public function getSide(context:DiagramShellContext, model:Object):int {
			throw new Error("This method needs to be implemented.");
		}
		
		public function setSide(context:DiagramShellContext, model:Object, value:int):void {
			throw new Error("This method needs to be implemented.");
		}
		
		public function isRoot(context:DiagramShellContext, model:Object):Boolean {
			throw new Error("This method needs to be implemented.");
		}
		
	}
}