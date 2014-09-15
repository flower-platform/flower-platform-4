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
package org.flowerplatform.flexdiagram.renderer.selection {
	import mx.core.IVisualElement;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	
	/**	
	 * @author Cristina Constantinescu
	 */
	public class AbstractSelectionRenderer extends UIComponent {
						
		/**
		 * The figure where the anchors will be shown.
		 */
		protected var target:IVisualElement;
				
		protected var _model:Object;
		
		protected var context:DiagramShellContext;
		
		public function get model():Object {
			return _model;
		}
		
		/**
		 * Main selection influences the aspect of the anchors.
		 */
		protected var _isMainSelection:Boolean;
		
		public function get isMainSelection():Boolean {
			return _isMainSelection;
		}
		
		public function set isMainSelection(value:Boolean):void {			
			var oldValue:Boolean = _isMainSelection;
			
			_isMainSelection = value;
			
			if (oldValue != value) {
				// announce ResizeAnchors that the value has been modified
				invalidateActiveAnchors();
			}
		}		
		
		public function activate(context:DiagramShellContext, model:Object):void {
			this._model = model;	
			this.context = context;
			this.target = context.diagramShell.getRendererForModel(context, model);
			
			context.diagramShell.diagramRenderer.addElement(this);
		}
		
		public function deactivate(context:DiagramShellContext, model:Object):void {
			context.diagramShell.diagramRenderer.removeElement(this);
		}
		
		/**
		 * This can be implemented by subclasses to update anchors display.
		 */ 
		protected function invalidateActiveAnchors():void {			
		}
		
	}	
}