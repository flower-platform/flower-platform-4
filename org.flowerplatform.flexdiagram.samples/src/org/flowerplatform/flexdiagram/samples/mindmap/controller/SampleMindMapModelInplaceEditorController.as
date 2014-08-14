/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flexdiagram.samples.mindmap.controller
{
	import flash.display.DisplayObject;
	import flash.geom.Rectangle;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.SampleMindMapModel;
	import org.flowerplatform.flexdiagram.tool.controller.InplaceEditorController;
	
	import spark.components.TextInput;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class SampleMindMapModelInplaceEditorController extends InplaceEditorController {
		
		override public function canActivate(context:DiagramShellContext, model:Object):Boolean	{		
			return true;
		}
		
		override public function activate(context:DiagramShellContext, model:Object):void {
			var renderer:DisplayObject = DisplayObject(context.diagramShell.getRendererForModel(context, model));
			var textField:TextInput = new TextInput();
			
			context.diagramShell.diagramRenderer.addElement(textField);
			
			var bounds:Rectangle = renderer.getBounds(DisplayObject(context.diagramShell.diagramRenderer));
			textField.x = bounds.x + 2;
			textField.y = bounds.y;
			textField.width = bounds.width;
			textField.height = bounds.height;
			textField.text = SampleMindMapModel(model).text;
			textField.callLater(textField.setFocus);
			
			context.diagramShell.modelToExtraInfoMap[model].inplaceEditor = textField;
		}
		
		override public function commit(context:DiagramShellContext, model:Object):void {		
			var textField:TextInput = context.diagramShell.modelToExtraInfoMap[model].inplaceEditor;
			SampleMindMapModel(model).text = textField.text;
			
			context.diagramShell.mainToolFinishedItsJob();
		}
				
		override public function deactivate(context:DiagramShellContext, model:Object):void {
			var textField:TextInput = context.diagramShell.modelToExtraInfoMap[model].inplaceEditor;
			context.diagramShell.diagramRenderer.removeElement(textField);
			
			delete context.diagramShell.modelToExtraInfoMap[model].inplaceEditor;			
		}
	}
	
}