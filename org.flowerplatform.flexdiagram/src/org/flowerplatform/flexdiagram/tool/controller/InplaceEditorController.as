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
package org.flowerplatform.flexdiagram.tool.controller {
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexutil.controller.AbstractController;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class InplaceEditorController extends AbstractController {
		
		public function InplaceEditorController(orderIndex:int = 0) {
			super(orderIndex);
		}
		
		public function canActivate(context:DiagramShellContext, model:Object):Boolean {
			throw new Error("This method needs to be implemented.");
		}
		
		public function activate(context:DiagramShellContext, model:Object):void {
			throw new Error("This method needs to be implemented.");
		}
		
		public function commit(context:DiagramShellContext, model:Object):void {
			throw new Error("This method needs to be implemented.");
		}
		
		public function abort(context:DiagramShellContext, model:Object):void {
			throw new Error("This method needs to be implemented.");
		}
		
		public function deactivate(context:DiagramShellContext, model:Object):void {
			throw new Error("This method needs to be implemented.");
		}
		
	}
}