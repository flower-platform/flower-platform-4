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
package com.crispico.flower.util.tree.checkboxtree {
	
	import flash.display.GradientType;
	import flash.display.Graphics;
	
	import mx.skins.Border;
	import mx.skins.halo.HaloColors;
	import mx.styles.StyleManager;
	import mx.utils.ColorUtil;
	
	/**
	 *  The skin for all the states of the icon in a CheckBoxExtended.
	 */
	public class CheckBoxExtendedIcon extends Border {
		
		private static var cache:Object = {};

		/**
		 *  @private
		 *  Several colors used for drawing are calculated from the base colors
		 *  of the component (themeColor, borderColor and fillColors).
		 *  Since these calculations can be a bit expensive,
		 *  we calculate once per color set and cache the results.
		 */
		private static function calcDerivedStyles(themeColor:uint, borderColor:uint, fillColor0:uint, fillColor1:uint):Object {
			var key:String = HaloColors.getCacheKey(themeColor, borderColor, fillColor0, fillColor1);
			if (!cache[key])
			{
				var o:Object = cache[key] = {};
				HaloColors.addHaloColors(o, themeColor, fillColor0, fillColor1);
				o.borderColorDrk1 = ColorUtil.adjustBrightness2(borderColor, -50);
			}
			return cache[key];
		}
		
	
		/**
		 *  Constructor.
		 */
		public function CheckBoxExtendedIcon() {
			super();
		}

	    override public function get measuredWidth():Number {
	        return 14;
	    }

	    override public function get measuredHeight():Number {
	        return 14;
	    }

		override protected function updateDisplayList(w:Number, h:Number):void {
			super.updateDisplayList(w, h);
			// User-defined styles
			var borderColor:uint = getStyle("borderColor");
			var fillAlphas:Array = getStyle("fillAlphas");
			var fillColors:Array = getStyle("fillColors");
			StyleManager.getColorNames(fillColors);
			var highlightAlphas:Array = getStyle("highlightAlphas");		
			var themeColor:uint = getStyle("themeColor");
			
			// Placeholder styles stub	
			var checkColor:uint = 0x2B333C;// added style prop
			
			// Derived styles
			var derStyles:Object = calcDerivedStyles(themeColor, borderColor, fillColors[0], fillColors[1]);
			
			var borderColorDrk1:Number = ColorUtil.adjustBrightness2(borderColor, -50);
			
			var themeColorDrk1:Number = ColorUtil.adjustBrightness2(themeColor, -25);
			
			var bDrawCheck:Boolean = false;
			var bDrawMiddle:Boolean = false;
			
			var upFillColors:Array;
			var upFillAlphas:Array;
			
			var overFillColors:Array;
			var overFillAlphas:Array;
			
			var disFillColors:Array;
			var disFillAlphas:Array;
	
			var g:Graphics = graphics;
			
			g.clear();
			
			switch (name){
				case "upIcon":
				case "middleUpIcon":
				{
	   				upFillColors = [ fillColors[0], fillColors[1] ];
					upFillAlphas = [ fillAlphas[0], fillAlphas[1] ];
	
					// border
					drawRoundRect(
						0, 0, w, h, 0,
						[ borderColor, borderColorDrk1 ], 1,
						verticalGradientMatrix(0, 0, w, h ),
						GradientType.LINEAR, null, 
						{ x: 1, y: 1, w: w - 2, h: h - 2, r: 0 });
	 
	
					// box fill
					drawRoundRect(
						1, 1, w - 2, h - 2, 0,
						upFillColors, upFillAlphas,
						verticalGradientMatrix(1, 1, w - 2, h - 2)); 
	
					// top highlight
					drawRoundRect(
						1, 1, w - 2, (h - 2) / 2, 0,
						[ 0xFFFFFF, 0xFFFFFF ], highlightAlphas,
						verticalGradientMatrix(1, 1, w - 2, (h - 2) / 2)); 
	
					if(name == "middleUpIcon")
					{
						bDrawMiddle = true;
					}
					break;
				}
					
				case "overIcon":
				case "middleOverIcon":
				{
					if (fillColors.length > 2)
						overFillColors = [ fillColors[2], fillColors[3] ];
					else
						overFillColors = [ fillColors[0], fillColors[1] ];
	
					if (fillAlphas.length > 2)
						overFillAlphas = [ fillAlphas[2], fillAlphas[3] ];
	  				else
						overFillAlphas = [ fillAlphas[0], fillAlphas[1] ];
	
					// border
					drawRoundRect(
						0, 0, w, h, 0,
						[ themeColor, themeColorDrk1 ], 1,
						verticalGradientMatrix(0, 0, w, h),
						GradientType.LINEAR, null, 
						{ x: 1, y: 1, w: w - 2, h: h - 2, r: 0 }); 
					
					// box fill
					drawRoundRect(
						1, 1, w - 2, h - 2, 0,
						overFillColors, overFillAlphas,
						verticalGradientMatrix(1, 1, w - 2, h - 2));
	
					// top highlight
					drawRoundRect(
						1, 1, w - 2, (h - 2) / 2, 0,
						[ 0xFFFFFF, 0xFFFFFF ], highlightAlphas,
						verticalGradientMatrix(1, 1, w - 2, (h - 2) / 2)); 
	
					if(name == "middleOverIcon") {
						bDrawMiddle = true;
					}
					break;
				}
	
				case "downIcon":
				case "middleDownIcon":
				{				
					// border
					drawRoundRect(
						0, 0, w, h, 0,
						[ themeColor, themeColorDrk1 ], 1,
						verticalGradientMatrix(0, 0, w, h)); 
					
					// box fill
					drawRoundRect(
						1, 1, w - 2, h - 2, 0,
						[ derStyles.fillColorPress1,
						derStyles.fillColorPress2 ], 1,
						verticalGradientMatrix(1, 1, w - 2, h - 2)); 
								
					// top highlight
					drawRoundRect(
						1, 1, w - 2, (h - 2) / 2, 0,
						[ 0xFFFFFF, 0xFFFFFF ], highlightAlphas,
						verticalGradientMatrix(1, 1, w - 2, (h - 2) / 2));
					if(name == "middleDownIcon") {
						bDrawMiddle = true;
					}
					break;
				}
	
				case "disabledIcon":
				{
	   				disFillColors = [ fillColors[0], fillColors[1] ];
					disFillAlphas = [ Math.max(0, fillAlphas[0] - 15),
									  Math.max(0, fillAlphas[1] - 15) ];
	
					// border
					drawRoundRect(
						0, 0, w, h, 0,
						[ borderColor, borderColorDrk1 ], 0.5,
						verticalGradientMatrix(0, 0, w, h),
						GradientType.LINEAR, null, 
						{ x: 1, y: 1, w: w - 2, h: h - 2, r: 0 }); 
	
					// box fill
					drawRoundRect(
						1, 1, w - 2, h - 2, 0,
						disFillColors, disFillAlphas,
						verticalGradientMatrix(1, 1, w - 2, h - 2)); 
	
					break;
				}
							
				case "selectedUpIcon":
				{
					bDrawCheck = true;
					
	   				upFillColors = [ fillColors[0], fillColors[1] ];
					upFillAlphas = [ fillAlphas[0], fillAlphas[1] ];
	
					// border
					drawRoundRect(
						0, 0, w, h, 0,
						[ borderColor, borderColorDrk1 ], 1,
						verticalGradientMatrix(0, 0, w, h),
						GradientType.LINEAR, null, 
						{ x: 1, y: 1, w: w - 2, h: h - 2, r: 0 }); 
	
					// box fill
					drawRoundRect(
						1, 1, w - 2, h - 2, 0,
						upFillColors, upFillAlphas,
						verticalGradientMatrix(1, 1, w - 2, h - 2)); 
	
					// top highlight
					drawRoundRect(
						1, 1, w - 2, (h - 2) / 2, 0,
						[ 0xFFFFFF, 0xFFFFFF ], highlightAlphas,
						verticalGradientMatrix(1, 1, w - 2, (h - 2) / 2));
	
					break;
				}
	
				case "selectedOverIcon":
				{
					bDrawCheck = true;
					
					if (fillColors.length > 2)
						overFillColors = [ fillColors[2], fillColors[3] ];
					else
						overFillColors = [ fillColors[0], fillColors[1] ];
	
					if (fillAlphas.length > 2)
						overFillAlphas = [ fillAlphas[2], fillAlphas[3] ];
	  				else
						overFillAlphas = [ fillAlphas[0], fillAlphas[1] ];
	
					// border
					drawRoundRect(
						0, 0, w, h, 0,
						[ themeColor, themeColorDrk1 ], 1,
						verticalGradientMatrix(0, 0, w, h),
						GradientType.LINEAR, null,
						{ x: 1, y: 1, w: w - 2, h: h - 2, r: 0 }); 
	
					// box fill
					drawRoundRect(
						1, 1, w - 2, h - 2, 0,
						overFillColors, overFillAlphas,
						verticalGradientMatrix(1, 1, w - 2, h - 2)); 
	
					// top highlight
					drawRoundRect(
						1, 1, w - 2, (h - 2) / 2, 0,
						[ 0xFFFFFF, 0xFFFFFF ], highlightAlphas,
						verticalGradientMatrix(1, 1, w - 2, (h - 2) / 2)); 
					
					break;
				}
	
				case "selectedDownIcon":
				{
					bDrawCheck = true;
					
					// border
					drawRoundRect(
						0, 0, w, h, 0,
						[ themeColor, themeColorDrk1 ], 1,
						verticalGradientMatrix(0, 0, w, h)); 
					
					// box fill
					drawRoundRect(
						1, 1, w - 2, h - 2, 0,
						[ derStyles.fillColorPress1,
						derStyles.fillColorPress2 ], 1,
						verticalGradientMatrix(1, 1, w - 2, h - 2)); 
								
					// top highlight
					drawRoundRect(
						1, 1, w - 2, (h - 2) / 2, 0,
						[ 0xFFFFFF, 0xFFFFFF ], highlightAlphas,
						verticalGradientMatrix(1, 1, w - 2, (h - 2) / 2)); 
	
					break;
				}
	
				case "selectedDisabledIcon":
				{
					bDrawCheck = true;
					checkColor = 0x999999;
					
	   				disFillColors = [ fillColors[0], fillColors[1] ];
					disFillAlphas = [ Math.max( 0, fillAlphas[0] - 0.15),
									  Math.max( 0, fillAlphas[1] - 0.15) ];
	
					// border
					drawRoundRect(
						0, 0, w, h, 0,
						[ borderColor, borderColorDrk1 ], 0.5,
						verticalGradientMatrix(0, 0, w, h),
						GradientType.LINEAR, null, 
						{ x: 1, y: 1, w: w - 2, h: h - 2, r: 0 }); 
	
					// box fill
					drawRoundRect(
						1, 1, w - 2, h - 2, 0,
						disFillColors, disFillAlphas,
						verticalGradientMatrix(1, 1, w - 2, h - 2)); 
	
					break;
				}
			}
			
			// Draw the checkmark symbol.
			if (bDrawCheck) {
				g.beginFill(checkColor);
				g.moveTo(3, 5);
				g.lineTo(5, 10);
				g.lineTo(7, 10);
				g.lineTo(12, 2);
				g.lineTo(13, 1);
				g.lineTo(11, 1);
				g.lineTo(6.5, 7);
				g.lineTo(5, 5);
				g.lineTo(3, 5);
				g.endFill();
			} 

			if(bDrawMiddle)	{
				// middle box fill
				g.beginFill(0x666666);			
				g.drawRect(3,3,w-6,h-6);
				g.endFill()
			}
		}
	}

}