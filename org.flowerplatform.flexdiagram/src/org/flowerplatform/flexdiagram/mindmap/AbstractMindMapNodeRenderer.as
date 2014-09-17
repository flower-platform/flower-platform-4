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
package org.flowerplatform.flexdiagram.mindmap
{
	import mx.events.PropertyChangeEvent;
	import mx.events.ResizeEvent;
	
	import spark.components.DataRenderer;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.IDiagramShellContextAware;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.focusable_component.FocusableRichText;
	
	/**
	 * @author Alexandra Topoloaga
	 */
	public class AbstractMindMapNodeRenderer extends DataRenderer implements IDiagramShellContextAware {
		
		protected var _label:FocusableRichText;
		
		protected static const BACKGROUND_COLOR_DEFAULT:uint = 0xFFFFFFFF;
		
		protected var backgroundColor:uint = BACKGROUND_COLOR_DEFAULT;
		
		protected var _context:DiagramShellContext;
		
		/**************************************************************************
		 * Graphic properties supported by this renderer.
		 *************************************************************************/
		
		public function set fontFamily(value:String):void {
			_label.setStyle("fontFamily", Utils.getSupportedFontFamily(value));
		}
		
		public function set fontSize(value:Number):void {
			_label.setStyle("fontSize", value);
		}
		
		public function set fontWeight(value:Boolean):void {
			_label.setStyle("fontWeight", value == true ? "bold" : "normal");
		}
		
		public function set fontStyle(value:Boolean):void {
			_label.setStyle("fontStyle", value == true ? "italic" : "normal");
		}
		
		public function set text(value:String):void {
			_label.text = value;
		}
		
		/**************************************************************************
		 * Other functions.
		 *************************************************************************/
		
		public function get diagramShellContext():DiagramShellContext {			
			return _context;
		}
		
		public function set diagramShellContext(value:DiagramShellContext):void {
			this._context = value;
			if (!hasEventListener(ResizeEvent.RESIZE)) {
				addEventListener(ResizeEvent.RESIZE, resizeHandler);	
			}
		}
		
		protected function get mindMapDiagramShell():MindMapDiagramShell {
			return diagramShellContext != null ? MindMapDiagramShell(diagramShellContext.diagramShell) : null;
		}
		
		/**
		 * We need to inform the system about the real dimensions of the renderer. The system will
		 * process the nodes again, with the real dimensions.
		 */
		protected function resizeHandler(event:ResizeEvent):void {
			if (height == 0 || width == 0) {
				// don't change values if first resize, wait until component fully initialized
				return;
			}
			var refresh:Boolean = false;
			if (mindMapDiagramShell.getPropertyValue(diagramShellContext, data, "width") != width) {
				mindMapDiagramShell.setPropertyValue(diagramShellContext, data, "width", width);
				refresh = true;
			}
			if (mindMapDiagramShell.getPropertyValue(diagramShellContext, data, "height") != height) {			
				mindMapDiagramShell.setPropertyValue(diagramShellContext, data, "height", height);
				refresh = true;
			}
			
			if (refresh) {					
				mindMapDiagramShell.shouldRefreshModelPositions(diagramShellContext, mindMapDiagramShell.rootModel);
				mindMapDiagramShell.shouldRefreshVisualChildren(diagramShellContext, mindMapDiagramShell.rootModel);
			}
		}
		
		override public function set data(value:Object):void {
			if (data != null) {
				endModelListen();
			}
			super.data = value;
			if (data != null) {
				beginModelListen();
			}
		}
		
		protected function beginModelListen():void {
			data.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);	
			modelChangedHandler(null);
		}
		
		protected function endModelListen():void {
			data.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
		}

		/**
		 * Invoked when <code>data</code> changes or when the mind map system
		 * recalculates data in the dynamic object.
		 */
		protected function modelChangedHandler(event:PropertyChangeEvent):void {
		}
		
		override protected function createChildren():void {
			super.createChildren();
			_label = new FocusableRichText();
			_label.percentHeight = 100;
			_label.percentWidth = 100;			
			_label.setStyle("verticalAlign" , "middle");
			addElement(_label);
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {			
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			graphics.clear();
			graphics.lineStyle(1, 0x808080);
			graphics.beginFill(backgroundColor, 1);
			graphics.drawRoundRect(0, 0, unscaledWidth, unscaledHeight, 10, 10);		
			graphics.endFill();
		}	
	}
}