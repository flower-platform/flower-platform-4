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
package org.flowerplatform.flexdiagram.controller {
	import flash.geom.Rectangle;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexutil.controller.AbstractController;

	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public class AbsoluteLayoutRectangleController extends AbstractController {
		
		public function AbsoluteLayoutRectangleController(orderIndex:int = 0) {
			super(orderIndex);
		}
		
		public function getBounds(context:DiagramShellContext, model:Object):Rectangle {
			throw new Error("This method needs to be implemented.");
		}
		
	}
}