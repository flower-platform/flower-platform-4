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
package org.flowerplatform.flexdiagram.controller.renderer {
	import mx.core.IVisualElement;
	
	/**
	 * @author Cristian Spiescu
	 */
	public interface IRendererController {
		/**
		 * This usually returns the Class of the renderer. For Sequential Layout mechanism the method
		 * MUST return the Class of the renderer as key.
		 */
		function geUniqueKeyForRendererToRecycle(model:Object):Object;
		function createRenderer(model:Object):IVisualElement;
		function associatedModelToRenderer(model:Object, renderer:IVisualElement):void;
		function unassociatedModelFromRenderer(model:Object, renderer:IVisualElement, modelIsDisposed:Boolean):void;
	}
}