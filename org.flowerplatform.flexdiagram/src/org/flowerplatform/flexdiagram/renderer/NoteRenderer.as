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
package org.flowerplatform.flexdiagram.renderer {
	import flash.display.GradientType;
	import flash.display.Graphics;
	import flash.display.InterpolationMethod;
	import flash.display.SpreadMethod;
	import flash.geom.Matrix;
	
	import mx.managers.IFocusManagerComponent;
	
	import org.flowerplatform.flexdiagram.event.ZoomPerformedEvent;
	import org.flowerplatform.flexdiagram.util.MultilineLabelItemRenderer;
	
	/**
	 * <code>IFocusManagerComponent</code> needed to set focus on diagram
	 * when clicking on a note.
	 * 
	 * @author Cristina Constantinescu
	 */
	public class NoteRenderer extends MultilineLabelItemRenderer implements IFocusManagerComponent {
				
		public var gradientTx:Number = 0;
		public var gradientTy:Number = 0;
		public var gradientType:String = GradientType.LINEAR;
		
		public var gradientStartColor:String = "0xFFFF99";
		public var gradientEndColor:String = "0xFFFFFF";
		
		public var alphas:Array = [0.9, 0.5];
		public var ratios:Array = [0, 255];
		
		public var spreadMethod:String = SpreadMethod.PAD;
		public var interp:String = InterpolationMethod.LINEAR_RGB;
		public var focalPointRatio:Number = 0;
		
		public var lineThickness:Number = 1;
		public var lineColor:uint = 0x00000;
		
		public function NoteRenderer() {
			super();
			
			setStyle("verticalAlign", "top");	
			setStyle("horizontalAlign", "left");
			setStyle("paddingTop", "20");
			setStyle("paddingBottom", "5");
			setStyle("paddingLeft", "5");
			setStyle("paddingRight", "5");
			
			// default values
			minWidth = 100;
			minHeight = 15;
			
			addEventListener(ZoomPerformedEvent.ZOOM_PERFORMED, zoomPerformedHandler);
		}
						
		protected function zoomPerformedHandler(event:ZoomPerformedEvent):void {
			invalidateSize();
			// use calllater because the updateList must be done after recalculating size
			callLater(invalidateDisplayList);
		}
				
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			drawNoteBorder(graphics, unscaledWidth, unscaledHeight);
		}
		
		override protected function drawBorder(unscaledWidth:Number, unscaledHeight:Number):void {				
		}
		
		override protected function drawBackground(unscaledWidth:Number, unscaledHeight:Number):void {			
		}	
		
		private function drawNoteBorder(graphics: Graphics, width:Number, height:Number):void {
			var midX:int = width - 15; //15 pixels 
			var midY:int = 15;
			
			graphics.clear();
			graphics.lineStyle(lineThickness, lineColor);	
			
			var matrix:Matrix = new Matrix();
			matrix.createGradientBox(unscaledWidth, unscaledHeight, rotation, gradientTx, gradientTy);
			graphics.beginGradientFill(gradientType, [gradientStartColor, gradientEndColor], alphas, ratios, matrix, spreadMethod, interp, focalPointRatio);
						
			graphics.moveTo(0, 0);
			graphics.lineTo(midX, 0);
			graphics.lineTo(width, midY);
			graphics.lineTo(width, height);
			graphics.lineTo(0, height);
			graphics.lineTo(0, 0);
			graphics.endFill();
			
			graphics.beginFill(0, 0);
			graphics.moveTo(midX, 0);
			graphics.lineTo(midX, midY);
			graphics.lineTo(width, midY);
			graphics.endFill();
		}
				
	}
}