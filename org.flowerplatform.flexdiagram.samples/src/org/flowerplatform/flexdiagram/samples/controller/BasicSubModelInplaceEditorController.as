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
package org.flowerplatform.flexdiagram.samples.controller
{
	import flash.display.DisplayObject;
	import flash.geom.Rectangle;
	
	import mx.core.IDataRenderer;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.samples.model.BasicSubModel;
	import org.flowerplatform.flexdiagram.tool.controller.IInplaceEditorController;
	
	import spark.components.TextInput;
	import spark.components.supportClasses.StyleableTextField;
	
	public class BasicSubModelInplaceEditorController extends ControllerBase implements IInplaceEditorController {
		
		public function canActivate(context:DiagramShellContext, model:Object):Boolean	{		
			return true;
		}
		
		public function activate(context:DiagramShellContext, model:Object):void {
			var renderer:DisplayObject = DisplayObject(context.diagramShell.getRendererForModel(model));
			var textField:TextInput = new TextInput();
						
			context.diagramShell.diagramRenderer.addElement(textField);
			
			var bounds:Rectangle = renderer.getBounds(DisplayObject(context.diagramShell.diagramRenderer));
			textField.x = bounds.x + 2;
			textField.y = bounds.y;
			textField.width = bounds.width;
			textField.height = bounds.height;
			textField.text = BasicSubModel(model).name;
			textField.callLater(textField.setFocus);
			
			context.diagramShell.modelToExtraInfoMap[model].inplaceEditor = textField;
		}
		
		public function commit(context:DiagramShellContext, model:Object):void {		
			var textField:TextInput = context.diagramShell.modelToExtraInfoMap[model].inplaceEditor;
			BasicSubModel(model).name = textField.text;
			
			context.diagramShell.mainToolFinishedItsJob();
		}
		
		public function abort(context:DiagramShellContext, model:Object):void {
			// here can be placed a warning
			context.diagramShell.mainToolFinishedItsJob();
		}
		
		public function deactivate(context:DiagramShellContext, model:Object):void {
			var textField:TextInput = context.diagramShell.modelToExtraInfoMap[model].inplaceEditor;
			context.diagramShell.diagramRenderer.removeElement(textField);
			
			delete context.diagramShell.modelToExtraInfoMap[model].inplaceEditor;			
		}		
	}
	
}