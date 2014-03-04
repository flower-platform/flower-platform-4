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
package org.flowerplatform.flexdiagram.controller.model_extra_info {
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexutil.controller.AbstractController;

	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public class ModelExtraInfoController extends AbstractController {
		
		public static const TYPE:String = "ModelExtraInfoController";
		
		public function getRenderer(context:DiagramShellContext, extraInfo:Object):IVisualElement {
			throw new Error("This method needs to be implemented.");
		}
		
		public function setRenderer(context:DiagramShellContext, model:Object, extraInfo:Object, renderer:IVisualElement):void {
			throw new Error("This method needs to be implemented.");
		}
		
		public function createExtraInfo(context:DiagramShellContext, model:Object):Object {
			throw new Error("This method needs to be implemented.");
		}
		
	}
}