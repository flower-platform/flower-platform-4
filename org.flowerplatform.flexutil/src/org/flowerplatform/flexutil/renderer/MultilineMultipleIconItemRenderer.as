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
package org.flowerplatform.flexutil.renderer {
	import mx.core.DPIClassification;
	
	import spark.primitives.BitmapImage;

	/**
	 * Code from http://flexponential.com/2011/08/21/adding-multiline-text-support-to-labelitemrenderer/.
	 * Adapted to work when using <code>MultipleIconItemRenderer</code>.
	 * @author Cristina Constantinescu
	 */ 
	public class MultilineMultipleIconItemRenderer extends MultipleIconItemRenderer	{
		
		/**
		 *  The width of the component on the previous layout manager 
		 *  pass.  This gets set in updateDisplayList() and used in measure() on 
		 *  the next layout pass.  This is so our "guessed width" in measure() 
		 *  will be as accurate as possible since labelDisplay is multiline and 
		 *  the labelDisplay height is dependent on the width.
		 */
		private var oldUnscaledWidth:Number;
		
		public function MultilineMultipleIconItemRenderer() {
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
		}
		
		/**
		 * Upgrade measure to handle text reflow.
		 */
		override protected function measure():void {
			// don't call super.measure() because there's no need to do the work that's
			// in there--we do it all in here.
			//super.measure();
			
			// start them at 0, then go through icon, label, and decorator
			// and add to these
			var myMeasuredWidth:Number = 0;
			var myMeasuredHeight:Number = 0;
			var myMeasuredMinWidth:Number = 0;
			var myMeasuredMinHeight:Number = 0;
			
			// calculate padding and horizontal gap
			// verticalGap is already handled above when there's a label
			// and a message since that's the only place verticalGap matters.
			// if we handled verticalGap here, it might add it to the icon if 
			// the icon was the tallest item.
			var numHorizontalSections:int = 0;
			var numHorizontalSectionBetweenIcons:int = 0;
			if (iconDisplays && iconDisplays.length > 0) {
				numHorizontalSections++;
				numHorizontalSectionBetweenIcons += iconDisplays.length;
			}
			
			if (labelDisplay) {
				numHorizontalSections++;
			}
			var paddingAndGapWidth:Number = Number(getStyle("paddingLeft")) + Number(getStyle("paddingRight"));
			if (numHorizontalSections > 0)
				paddingAndGapWidth += (getStyle("horizontalGap") * (numHorizontalSections - 1));
			if (numHorizontalSectionBetweenIcons > 0) {
				paddingAndGapWidth += (getStyle("iconsGap") * (numHorizontalSectionBetweenIcons - 1));
			}
			
			var hasLabel:Boolean = labelDisplay && labelDisplay.text != "";
			
			var verticalGap:Number = (hasLabel) ? getStyle("verticalGap") : 0;
			var paddingHeight:Number = getStyle("paddingTop") + getStyle("paddingBottom");
			
			// Icon is on left
			var myIconWidth:Number = 0;
			var myIconHeight:Number = 0;
			if (iconDisplays && iconDisplays.length > 0) {
				for (var i:int=0; i < iconDisplays.length; i++) {
					var iconDisplay:BitmapImage = BitmapImage(iconDisplays.getItemAt(i));
					
					myIconWidth += (isNaN(iconWidth) ? getElementPreferredWidth(iconDisplay) : iconWidth);
					myIconHeight = (isNaN(iconHeight) ? getElementPreferredHeight(iconDisplay) : iconHeight);
				}
				myMeasuredWidth += myIconWidth;
				myMeasuredMinWidth += myIconWidth;
				myMeasuredHeight = Math.max(myMeasuredHeight, myIconHeight);
				myMeasuredMinHeight = Math.max(myMeasuredMinHeight, myIconHeight);				
			}			

			myMeasuredWidth += paddingAndGapWidth;
			myMeasuredMinWidth += paddingAndGapWidth;
			
			// verticalGap handled in label and message
			myMeasuredHeight += paddingHeight;
			myMeasuredMinHeight += paddingHeight;
			
			// now set the local variables to the member variables.
			measuredWidth = myMeasuredWidth;
			measuredHeight = myMeasuredHeight;
			
			// now we need to measure labelDisplay's height.  Unfortunately, this is tricky and 
			// is dependent on labelDisplay's width.  We use the old unscaledWidth as an 
			// estimate for the new one.  If this estimate is wrong then there is code in 
			// updateDisplayList() that will trigger a new measure pass to correct it.
			var labelDisplayEstimatedWidth:Number = oldUnscaledWidth - measuredWidth;
			setElementSize(labelDisplay, labelDisplayEstimatedWidth, NaN);
			
			measuredWidth += getElementPreferredWidth(labelDisplay);
			measuredHeight += getElementPreferredHeight(labelDisplay); 
			
			measuredMinWidth = myMeasuredMinWidth;
			measuredMinHeight = myMeasuredMinHeight;
		}
		
		/**
		 * Upgrade layoutContents to handle text reflow.
		 */
		override protected function layoutContents(unscaledWidth:Number, unscaledHeight:Number):void {			
			// no need to call super.layoutContents() since we're changing how it happens here
			
			// start laying out our children now
			var iconWidth:Number = 0;
			var iconHeight:Number = 0;
			
			var hasLabel:Boolean = labelDisplay && labelDisplay.text != "";
			
			var paddingLeft:Number   = getStyle("paddingLeft");
			var paddingRight:Number  = getStyle("paddingRight");
			var paddingTop:Number    = getStyle("paddingTop");
			var paddingBottom:Number = getStyle("paddingBottom");
			var horizontalGap:Number = getStyle("horizontalGap");
			var iconsGap:Number = getStyle("iconsGap");
			var verticalAlign:String = getStyle("verticalAlign");
			var verticalGap:Number   = (hasLabel) ? getStyle("verticalGap") : 0;
			
			var vAlign:Number;
			if (verticalAlign == "top")
				vAlign = 0;
			else if (verticalAlign == "bottom")
				vAlign = 1;
			else // if (verticalAlign == "middle")
				vAlign = 0.5;
			// made "middle" last even though it's most likely so it is the default and if someone 
			// types "center", then it will still vertically center itself.
			
			var viewWidth:Number  = unscaledWidth  - paddingLeft - paddingRight;
			var viewHeight:Number = unscaledHeight - paddingTop  - paddingBottom;
			
			// icon is on the left
			if (iconDisplays && iconDisplays.length > 0) {
				var iconX:Number = paddingLeft;
				for (var i:int=0; i < iconDisplays.length; i++) {
					var iconDisplay:BitmapImage = BitmapImage(iconDisplays.getItemAt(i));
					// set the icon's position and size
					setElementSize(iconDisplay, this.iconWidth, this.iconHeight);
					
					iconWidth = iconDisplay.getLayoutBoundsWidth();
					iconHeight = iconDisplay.getLayoutBoundsHeight();
					
					// use vAlign to position the icon.
					var iconDisplayY:Number = Math.round(vAlign * (viewHeight - iconHeight)) + paddingTop;
					setElementPosition(iconDisplay, iconX, iconDisplayY);
					iconX += iconWidth + iconsGap;
				}
				iconWidth = iconX - iconsGap;
			}			
	
			var labelComponentsViewWidth:Number = viewWidth - iconWidth;
			
			// don't forget the extra gap padding if these elements exist
			if (iconDisplays && iconDisplays.length > 0) 
				labelComponentsViewWidth -= horizontalGap;
			
			var labelComponentsX:Number = paddingLeft;
			if (iconDisplays && iconDisplays.length > 0) 
				labelComponentsX += iconWidth + horizontalGap;
			
			if (hasLabel) {				
				// commit styles to make sure it uses updated look
				labelDisplay.commitStyles();				
			}
			
			// Size the labelDisplay 
			
			// we want the labelWidth to be the viewWidth and then we'll calculate the height
			// of the text from that
			var labelWidth:Number = Math.max(labelComponentsViewWidth, 0);
			
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
				
				if (measuredWidth < maxWidth) {
					setElementSize(labelDisplay, NaN, oldPreferredLabelHeight);
				} else {
					// set the width of labelDisplay to labelWidth.
					// set the height to old label height.  If the height's actually wrong, 
					// we'll invalidateSize() and go through this layout pass again anyways
					setElementSize(labelDisplay, labelWidth, oldPreferredLabelHeight);				
				}
				// grab new labelDisplay height after the labelDisplay has taken its final width
				var newPreferredLabelHeight:Number = getElementPreferredHeight(labelDisplay);
				
				// if the resize caused the labelDisplay's height to change (because of 
				// text reflow), then we need to re-measure ourselves with our new width
				if (oldPreferredLabelHeight != newPreferredLabelHeight || measuredWidth < maxWidth) {
					invalidateSize();
				}
			}
			// Position the labelDisplay
			
			var labelY:Number = Math.round(vAlign * (viewHeight - oldPreferredLabelHeight))  + paddingTop;
			setElementPosition(labelDisplay, labelComponentsX, labelY);
		}
		
		override public function set maxWidth(value:Number):void {
			super.maxWidth = value;
			
			// this is needed to recalculate renderers width
			if (labelDisplay) {
				oldUnscaledWidth = value;
			}
		}
	}
}