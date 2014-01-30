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
package org.flowerplatform.flexdiagram.renderer.selection {
	
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.geom.Point;
	
	import mx.core.IVisualElement;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ChildAnchorsSelectionRenderer extends StandardAnchorsSelectionRenderer {
		
		override protected function handleTargetMoveResize(event:Event):void {
			var x:int = 0, y:int = 0, crt:IVisualElement = target;
			while (!(crt is DiagramRenderer)) {
				x += crt.x;
				y += crt.y;
				crt = IVisualElement(crt.parent);
			}
			setLayoutBoundsPosition(x, y);
			setLayoutBoundsSize(target.width, target.height);
		}
		
	}
}