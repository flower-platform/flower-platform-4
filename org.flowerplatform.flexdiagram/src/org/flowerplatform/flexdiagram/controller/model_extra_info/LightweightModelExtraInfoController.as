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
package org.flowerplatform.flexdiagram.controller.model_extra_info {
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class LightweightModelExtraInfoController extends ModelExtraInfoController {
		
		protected static const NO_RENDERER_ASSOCIATED_MARKER:Object = new Object();
		
		override public function getRenderer(context:DiagramShellContext, extraInfo:Object):IVisualElement {
			if (extraInfo == NO_RENDERER_ASSOCIATED_MARKER) {
				return null;
			} else {
				return IVisualElement(extraInfo);
			}
		}
		
		override public function setRenderer(context:DiagramShellContext, model:Object, extraInfo:Object, renderer:IVisualElement):void	{
			if (renderer == null) {
				context.diagramShell.modelToExtraInfoMap[model] = NO_RENDERER_ASSOCIATED_MARKER;	
			} else {
				context.diagramShell.modelToExtraInfoMap[model] = renderer;
			}
		}
		
		override public function createExtraInfo(context:DiagramShellContext, model:Object):Object {
			return NO_RENDERER_ASSOCIATED_MARKER;
		}
						
	}
}