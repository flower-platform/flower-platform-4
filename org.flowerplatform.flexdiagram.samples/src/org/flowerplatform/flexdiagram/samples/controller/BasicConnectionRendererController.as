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
package org.flowerplatform.flexdiagram.samples.controller {
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.renderer.ConnectionRendererController;
	import org.flowerplatform.flexdiagram.renderer.connection.ConnectionRenderer;
	import org.flowerplatform.flexdiagram.samples.model.BasicConnection;
	
	public class BasicConnectionRendererController extends ConnectionRendererController {
		public function BasicConnectionRendererController(diagramShell:DiagramShell, rendererClass:Class=null) {
			super(diagramShell, rendererClass);
		}
		
		override public function associatedModelToRenderer(model:Object, renderer:IVisualElement):void {
			super.associatedModelToRenderer(model, renderer);
			ConnectionRenderer(renderer).middleConnectionLabel.text = "Connection Label";
		}
		
		override public function unassociatedModelFromRenderer(model:Object, renderer:IVisualElement, isModelDisposed:Boolean):void {
			super.unassociatedModelFromRenderer(model, renderer, isModelDisposed);
			if (isModelDisposed && renderer != null) {
					IVisualElementContainer(renderer.parent).removeElement(renderer);
			}
		}
		
		override public function getSourceModel(connectionModel:Object):Object {
			return BasicConnection(connectionModel).source;
		}
		
		override public function getTargetModel(connectionModel:Object):Object {
			return BasicConnection(connectionModel).target;
		}
		
		override public function hasMiddleLabel(connectionModel:Object):Boolean {
			return true;
		}
		
	}
}