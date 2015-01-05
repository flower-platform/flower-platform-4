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
	import flash.geom.Rectangle;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.AbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.mindmap.MultiConnectorModel;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class MultiConnectorAbsoluteLayoutRectangleController extends AbsoluteLayoutRectangleController {
		public function MultiConnectorAbsoluteLayoutRectangleController(orderIndex:int=0) {
			super(orderIndex);
		}
		
		override public function getBounds(context:DiagramShellContext, model:Object):Rectangle {
			var multiConnectorModel:MultiConnectorModel = MultiConnectorModel(model);
			return new Rectangle(multiConnectorModel.x, multiConnectorModel.y, multiConnectorModel.width, multiConnectorModel.height);
		}
		
	}
}