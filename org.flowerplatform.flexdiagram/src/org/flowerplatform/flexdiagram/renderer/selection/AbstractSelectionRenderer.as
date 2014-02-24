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
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	
	/**	
	 * @author Cristina Constantinescu
	 */
	public class AbstractSelectionRenderer extends UIComponent implements IDiagramShellAware {
						
		/**
		 * The figure where the anchors will be shown.
		 */
		protected var target:IVisualElement;
				
		protected var _model:Object;
		
		public function get model():Object {
			return _model;
		}
		
		protected var _diagramShell:DiagramShell;
		
		public function get diagramShell():DiagramShell {
			return _diagramShell;
		}
		
		public function set diagramShell(value:DiagramShell):void {
			_diagramShell = value;
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
		
		public function activate(model:Object):void {
			this._model = model;			
			this.target = diagramShell.getRendererForModel(model);
			
			diagramShell.diagramRenderer.addElement(this);
		}
		
		public function deactivate(model:Object):void {
			diagramShell.diagramRenderer.removeElement(this);
		}
		
		/**
		 * This can be implemented by subclasses to update anchors display.
		 */ 
		protected function invalidateActiveAnchors():void {			
		}
		
	}	
}