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
	import flash.geom.Rectangle;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.IAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapAbsoluteLayoutRectangleController extends ControllerBase implements IAbsoluteLayoutRectangleController {
		
		public function MindMapAbsoluteLayoutRectangleController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function getBounds(model:Object):Rectangle {
			var modelController:IMindMapModelController = MindMapDiagramShell(diagramShell).getModelController(model);
			return new Rectangle(
				modelController.getX(model), 
				modelController.getY(model), 
				modelController.getWidth(model), 
				modelController.getHeight(model));
		}
		
	}
}