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
package org.flowerplatform.flexdiagram.samples.controller {
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.samples.model.BasicModel;
	import org.flowerplatform.flexdiagram.tool.controller.drag.AbsoluteLayoutChildPlaceHolderDragController;
	import org.flowerplatform.flexdiagram.ui.MoveResizePlaceHolder;
	
	public class BasicModelDragController extends AbsoluteLayoutChildPlaceHolderDragController {
		
		override public function drop(context:DiagramShellContext, model:Object):void {
			var movePlaceHolder:MoveResizePlaceHolder = context.diagramShell.modelToExtraInfoMap[model].movePlaceHolder;
						
			BasicModel(model).x = movePlaceHolder.x;
			BasicModel(model).y = movePlaceHolder.y;
		}
		
	}
}