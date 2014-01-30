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
	import mx.core.FlexGlobals;
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class LightweightModelExtraInfoController extends ControllerBase implements IModelExtraInfoController {
		
		protected static const NO_RENDERER_ASSOCIATED_MARKER:Object = new Object();
		
		public function LightweightModelExtraInfoController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function getRenderer(extraInfo:Object):IVisualElement {
			if (extraInfo == NO_RENDERER_ASSOCIATED_MARKER) {
				return null;
			} else {
				return IVisualElement(extraInfo);
			}
		}
		
		public function setRenderer(model:Object, extraInfo:Object, renderer:IVisualElement):void	{
			if (renderer == null) {
				diagramShell.modelToExtraInfoMap[model] = NO_RENDERER_ASSOCIATED_MARKER;	
			} else {
				diagramShell.modelToExtraInfoMap[model] = renderer;
			}
		}
		
		public function createExtraInfo(model:Object):Object {
			return NO_RENDERER_ASSOCIATED_MARKER;
		}
						
	}
}