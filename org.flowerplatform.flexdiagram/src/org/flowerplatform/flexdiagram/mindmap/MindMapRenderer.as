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
package org.flowerplatform.flexdiagram.mindmap {
	import flash.events.IEventDispatcher;
	
	import mx.events.PropertyChangeEvent;
	import mx.events.ResizeEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.FlexDiagramConstants;
	import org.flowerplatform.flexdiagram.renderer.BaseRenderer;
	import org.flowerplatform.flexutil.controller.ValuesProvider;
	
	/**
	 * Renderer for a mind map node. As in the case of the parent class, it can be used within a mind map diagram, or in any other container (in which
	 * case some properties that are specific to FlexDiagram / mind map diagram are disabled, such as cloud). 
	 * 
	 * <p>
	 * When the renderer is used inside a mind map (i.e. <code>mindMapDiagramShell</code> is not null), this class listens
	 * to the resize event. When it happens, it orders a global refresh of the positions (i.e. by setting a flag). So basically,
	 * when an item appears on the screen, it is rendered twice: first with a default size, and second with a modified position.
	 * 
	 * @author Cristina Constantinescu
	 * @author Alexandra Topoloaga
	 * @author Cristian Spiescu
	 */
	public class MindMapRenderer extends BaseRenderer {

		/**************************************************************************
		 * Constants.
		 *************************************************************************/
		
		public static const CLOUD_TYPE_RECTANGLE:String = "RECT";
		
		public static const CLOUD_TYPE_ROUNDED_RECTANGLE:String = "ROUND_RECT";
		
		public static const CLOUD_COLOR_DEFAULT:uint = 0xFFFBBF;
				
		/**************************************************************************
		 * Attributes.
		 *************************************************************************/

		protected var _cloudColor:uint;
		
		protected var _cloudType:String;
		
		/**************************************************************************
		 * Graphic properties supported by this renderer.
		 *************************************************************************/
		
		public function get cloudColor():uint {
			return _cloudColor;
		}
		
		public function set cloudColor(value:uint):void {
			_cloudColor = value;
			invalidateDisplayList();
		}
		
		public function get cloudType():String {
			return _cloudType;
		}
		
		public function set cloudType(value:String):void {
			_cloudType = value;
			invalidateDisplayList();
		}
		
		/**************************************************************************
		 * Other functions.
		 *************************************************************************/
		
		override public function set diagramShellContext(value:DiagramShellContext):void {
			super.diagramShellContext = value;
			if (value != null) {
				if (!hasEventListener(ResizeEvent.RESIZE)) {
					addEventListener(ResizeEvent.RESIZE, resizeHandler);	
				}
			} else {
				removeEventListener(ResizeEvent.RESIZE, resizeHandler);
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
		
		override protected function beginModelListen():void {
			super.beginModelListen();
			// we invoke this for the case when a recycled renderer has the exact same width; in
			// this case, the resizeHandler is not called. We need to invoke it here, to inform the
			// system of the real width. E.g. visible problem: without this, some nodes on the left 
			// would be translated
			resizeHandler(null);
		}
		
		/**
		 * Besides the invocation scenarios of the initial function, invoked as well
		 * when the mind map system recalculates data in the dynamic object.
		 */
		override protected function modelChangedHandler(event:PropertyChangeEvent):void {
			super.modelChangedHandler(event);
			var valuesProvider:ValuesProvider = getRequiredValuesProvider();
			var mindMapDiagramShell:MindMapDiagramShell = this.mindMapDiagramShell;
			
			if (mindMapDiagramShell != null) {
				// i.e. used as a renderer in a mind map diagram
				if (event == null || event.property == "x") {
					x = mindMapDiagramShell.getPropertyValue(diagramShellContext, data, "x");	
				}
				if (event == null || event.property == "y") {
					y = mindMapDiagramShell.getPropertyValue(diagramShellContext, data, "y");	
				}
				if (event == null || event.property == "depth") {
					depth = mindMapDiagramShell.getPropertyValue(diagramShellContext, data, "depth");	
				}
				if (event == null || event.property == "expandedWidth") {
					invalidateDisplayList();
				}
				setFieldIfNeeded(valuesProvider, typeDescriptorRegistry, event, "cloudColor", FlexDiagramConstants.MIND_MAP_RENDERER_CLOUD_COLOR, CLOUD_COLOR_DEFAULT);
				setFieldIfNeeded(valuesProvider, typeDescriptorRegistry, event, "cloudType", FlexDiagramConstants.MIND_MAP_RENDERER_CLOUD_TYPE, null);
			}
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {			
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			if (shouldDrawCircle()) {
				drawLittleCircle();
			}
		}
			
		override protected function drawCloud(unscaledWidth:Number, unscaledHeight:Number):void {			
			new CloudDrawer().drawCloud(this, unscaledWidth, unscaledHeight);
		}
		
		protected function shouldDrawCircle():Boolean {			
			if (mindMapDiagramShell == null || mindMapDiagramShell.getModelController(diagramShellContext, data).getExpanded(diagramShellContext, data)) {
				return false;
			}
			var value:Object = getRequiredValuesProvider().getValue(typeDescriptorRegistry, IEventDispatcher(data), FlexDiagramConstants.MIND_MAP_RENDERER_HAS_CHILDREN);
			if (value != null) {
				return Boolean(value);
			}
			return false;
		}
		
		protected function drawLittleCircle(circleY:Number=NaN):void {
			if (isNaN(circleY)) {
				circleY = height/2;
			}
			graphics.beginFill(BACKGROUND_COLOR_DEFAULT, 1);
			var side:int = MindMapDiagramShell(diagramShellContext.diagramShell).getModelController(diagramShellContext, data).getSide(diagramShellContext, data);
			if (side == MindMapDiagramShell.POSITION_LEFT) {
				graphics.drawCircle(-CIRCLE_RADIUS, circleY, CIRCLE_RADIUS);
			} else if (side == MindMapDiagramShell.POSITION_RIGHT) {						
				graphics.drawCircle(width + CIRCLE_RADIUS, circleY, CIRCLE_RADIUS);
			}
		}
		
	}
}