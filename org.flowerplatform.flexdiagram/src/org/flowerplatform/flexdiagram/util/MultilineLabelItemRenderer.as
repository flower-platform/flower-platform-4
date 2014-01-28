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
package org.flowerplatform.flexdiagram.util {
	import flash.text.TextFieldAutoSize;
	
	import mx.core.DPIClassification;
	import mx.core.mx_internal;
	
	import spark.components.LabelItemRenderer;
	import spark.components.supportClasses.StyleableTextField;
	
	use namespace mx_internal;
	
	/**
	 * A subclass of LabelItemRenderer that allows for multi-line text rather
	 * than the default single line truncation behavior.
	 * 
	 * @author Cristina Constantinescu
	 */
	public class MultilineLabelItemRenderer extends LabelItemRenderer {
		
		/**
		 *  The width of the component on the previous layout manager 
		 *  pass.  This gets set in updateDisplayList() and used in measure() on 
		 *  the next layout pass.  This is so our "guessed width" in measure() 
		 *  will be as accurate as possible since labelDisplay is multiline and 
		 *  the labelDisplay height is dependent on the width.
		 */
		private var oldUnscaledWidth:Number = 0;
		
		/**
		 * Constructor
		 */
		public function MultilineLabelItemRenderer() {
			super();
			// provide an initial guess at the estimated component width based on DPI class
			if (applicationDPI == DPIClassification.DPI_320) {
				oldUnscaledWidth = 640;
			} else if (applicationDPI == DPIClassification.DPI_240) {
				oldUnscaledWidth = 480;
			} else { // 160 dpi
				oldUnscaledWidth = 320;
			}			
		}
		
		/**
		 * Turn on multiline and wordWrap properties of the labelDisplay
		 */
		override protected function createLabelDisplay():void {
			super.createLabelDisplay();
			labelDisplay.multiline = true;
			labelDisplay.wordWrap = true;
					
			invalidateSize();
		}
		
		/**
		 * Upgrade measure to handle text reflow.
		 */
		override protected function measure():void {
			super.measure();
			
			var horizontalPadding:Number = Number(getStyle("paddingLeft")) + Number(getStyle("paddingRight"));
			var verticalPadding:Number = Number(getStyle("paddingTop")) + Number(getStyle("paddingBottom"));
			
			// now we need to measure labelDisplay's height.  Unfortunately, this is tricky and 
			// is dependent on labelDisplay's width.  We use the old unscaledWidth as an 
			// estimate for the new one.  If this estimate is wrong then there is code in 
			// updateDisplayList() that will trigger a new measure pass to correct it.
			var labelDisplayEstimatedWidth:Number = oldUnscaledWidth - horizontalPadding;
	
			setElementSize(labelDisplay, labelDisplayEstimatedWidth, NaN);
			
			measuredWidth = getElementPreferredWidth(labelDisplay) + horizontalPadding;
			measuredHeight = getElementPreferredHeight(labelDisplay) + verticalPadding;
		}
		
		/**
		 * Upgrade layoutContents to handle text reflow.
		 */
		override protected function layoutContents(unscaledWidth:Number, unscaledHeight:Number):void {
			if (!labelDisplay) {
				return;
			}
			var paddingLeft:Number   = getStyle("paddingLeft"); 
			var paddingRight:Number  = getStyle("paddingRight");
			var paddingTop:Number    = getStyle("paddingTop");
			var paddingBottom:Number = getStyle("paddingBottom");
			var verticalAlign:String = getStyle("verticalAlign");
			
			var viewWidth:Number  = unscaledWidth  - paddingLeft - paddingRight;
			var viewHeight:Number = unscaledHeight - paddingTop  - paddingBottom;
			
			var vAlign:Number;
			if (verticalAlign == "top")
				vAlign = 0;
			else if (verticalAlign == "bottom")
				vAlign = 1;
			else // if (verticalAlign == "middle")
				vAlign = 0.5;
			
			if (label != "") {
				labelDisplay.commitStyles();
			}
			
			// Size the labelDisplay 
			
			// we want the labelWidth to be the viewWidth and then we'll calculate the height
			// of the text from that
			var labelWidth:Number = Math.max(viewWidth, 0);
			
			// keep track of the old label height
			var oldPreferredLabelHeight:Number = 0;
			
			// We get called with unscaledWidth = 0 a few times...
			// rather than deal with this case normally, 
			// we can just special-case it later to do something smarter
			if (labelWidth == 0) {
				// if unscaledWidth is 0, we want to make sure labelDisplay is invisible.
				// we could set labelDisplay's width to 0, but that would cause an extra 
				// layout pass because of the text reflow logic.  To avoid that we can 
				// just set its height to 0 instead of setting the width.
				setElementSize(labelDisplay, NaN, 0);
			} else {
				// grab old height before we resize the labelDisplay
				oldPreferredLabelHeight = getElementPreferredHeight(labelDisplay);
				
				// keep track of oldUnscaledWidth so we have a good guess as to the width 
				// of the labelDisplay on the next measure() pass
				oldUnscaledWidth = unscaledWidth;
				
				// set the width of labelDisplay to labelWidth.
				// set the height to old label height.  If the height's actually wrong, 
				// we'll invalidateSize() and go through this layout pass again anyways
				setElementSize(labelDisplay, labelWidth, oldPreferredLabelHeight);
				
				// grab new labelDisplay height after the labelDisplay has taken its final width
				var newPreferredLabelHeight:Number = getElementPreferredHeight(labelDisplay);
				
				// if the resize caused the labelDisplay's height to change (because of 
				// text reflow), then we need to re-measure ourselves with our new width
				if (oldPreferredLabelHeight != newPreferredLabelHeight) {
					invalidateSize();
				}
			}
			
			// Position the labelDisplay
			
			var labelY:Number = Math.round(vAlign * (viewHeight - oldPreferredLabelHeight))  + paddingTop;
			setElementPosition(labelDisplay, paddingLeft, labelY);			
		}
		
		override public function set maxWidth(value:Number):void {
			super.maxWidth = value;
			
			if (labelDisplay) {
				oldUnscaledWidth = value;;
			}
		}
	}	
}