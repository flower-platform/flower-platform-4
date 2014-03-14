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
package org.flowerplatform.flex_client.host_app.mobile.text
{
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.FocusEvent;
	import flash.geom.Point;
	
	import mx.core.DPIClassification;
	import mx.core.mx_internal;
	
	import spark.components.RichEditableText;
	import spark.components.supportClasses.SkinnableTextBase;
	import spark.components.supportClasses.StyleableStageText;
	import spark.components.supportClasses.StyleableTextField;
	import spark.core.IDisplayText;
	import spark.skins.mobile.supportClasses.MobileSkin;
	import spark.skins.mobile160.assets.TextInput_border;
	import spark.skins.mobile240.assets.TextInput_border;
	import spark.skins.mobile320.assets.TextInput_border;
	
	use namespace mx_internal;
	
	/**
	 *  ActionScript-based skin for text input controls in mobile applications. 
	 * 
	 *  @langversion 3.0
	 *  @playerversion AIR 3.0
	 *  @productversion Flex 4.6
	 */
	public class RichEditableTextSkinBase extends MobileSkin
	{
		//--------------------------------------------------------------------------
		//
		//  Constructor
		//
		//--------------------------------------------------------------------------
		
		/**
		 *  Constructor.
		 * 
		 *  @langversion 3.0
		 *  @playerversion AIR 3.0 
		 *  @productversion Flex 4.6
		 * 
		 */
		public function RichEditableTextSkinBase()
		{
			super();
			
			switch (applicationDPI)
			{
				case DPIClassification.DPI_320:
				{
					borderClass = spark.skins.mobile320.assets.TextInput_border;
					layoutCornerEllipseSize = 24;
					measuredDefaultWidth = 600;
					measuredDefaultHeight = 66;
					layoutBorderSize = 2;
					
					break;
				}
				case DPIClassification.DPI_240:
				{
					borderClass = spark.skins.mobile240.assets.TextInput_border;
					layoutCornerEllipseSize = 12;
					measuredDefaultWidth = 440;
					measuredDefaultHeight = 50;
					layoutBorderSize = 1;
					
					break;
				}
				default:
				{
					borderClass = spark.skins.mobile160.assets.TextInput_border;
					layoutCornerEllipseSize = 12;
					measuredDefaultWidth = 300;
					measuredDefaultHeight = 33;
					layoutBorderSize = 1;
					
					break;
				}
			}
		}
		
		//--------------------------------------------------------------------------
		//
		//  Graphics variables
		//
		//--------------------------------------------------------------------------
		
		/**
		 *  Defines the border.
		 * 
		 *  @langversion 3.0
		 *  @playerversion AIR 3.0 
		 *  @productversion Flex 4.6
		 */  
		protected var borderClass:Class;
		
		//--------------------------------------------------------------------------
		//
		//  Layout variables
		//
		//--------------------------------------------------------------------------
		
		/**
		 *  Defines the corner radius.
		 * 
		 *  @langversion 3.0
		 *  @playerversion AIR 3.0 
		 *  @productversion Flex 4.6
		 */  
		protected var layoutCornerEllipseSize:uint;
		
		/**
		 *  Defines the border's thickness.
		 * 
		 *  @langversion 3.0
		 *  @playerversion AIR 3.0
		 *  @productversion Flex 4.6
		 */
		protected var layoutBorderSize:uint;
		
		//--------------------------------------------------------------------------
		//
		//  Variables
		//
		//--------------------------------------------------------------------------
		
		/**
		 *  @private
		 * 
		 *  Instance of the border graphics.
		 */
		protected var border:DisplayObject;
		
		private var borderVisibleChanged:Boolean = false;
		
		/**
		 *  @private
		 * 
		 *  Multiline flag.
		 */
		protected var multiline:Boolean = false;
		
		//--------------------------------------------------------------------------
		//
		//  Skin parts
		//
		//--------------------------------------------------------------------------
		
		/**
		 *  textDisplay skin part.
		 * 
		 *  @langversion 3.0
		 *  @playerversion AIR 3.0
		 *  @productversion Flex 4.6
		 */
		public var textDisplay: RichEditableText/* StyleableStageText */;
		
		[Bindable]
		/**
		 *  Bindable promptDisplay skin part. Bindings fire when promptDisplay is
		 *  removed and added for proper updating by the SkinnableTextBase.
		 * 
		 *  @langversion 3.0
		 *  @playerversion AIR 3.0
		 *  @productversion Flex 4.6
		 */
		public var promptDisplay:IDisplayText;
		
		//--------------------------------------------------------------------------
		//
		//  Overridden methods
		//
		//--------------------------------------------------------------------------
		
		/**
		 *  @private
		 */
		override protected function createChildren():void
		{
			super.createChildren();
			
			if (!textDisplay)
			{
				textDisplay = new RichEditableText();
				textDisplay.multiline = multiline;
				textDisplay.editable = true;
				
				textDisplay.styleName = this;
				this.addChild(textDisplay);
			}
			
			if (!border)
			{
				border = new borderClass();
				addChild(border);
			}
		}
		
		/**
		 *  @private 
		 */ 
		override protected function commitProperties():void
		{
			super.commitProperties();
			
			if (borderVisibleChanged)
			{
				borderVisibleChanged = false;
				
				var borderVisible:Boolean = getStyle("borderVisible");
				
				if (borderVisible && !border)
				{
					border = new borderClass();
					addChild(border);
				}
				else if (!borderVisible && border)
				{
					removeChild(border);
					border = null;
				}
			}
		}
		
		/**
		 *  @private
		 */
		override protected function drawBackground(unscaledWidth:Number, unscaledHeight:Number):void
		{
			super.drawBackground(unscaledWidth, unscaledHeight);
			
			var borderSize:uint = (border) ? layoutBorderSize : 0;
			var borderWidth:uint = borderSize * 2;
			
			var contentBackgroundColor:uint = getStyle("contentBackgroundColor");
			var contentBackgroundAlpha:Number = getStyle("contentBackgroundAlpha");
			
			if (isNaN(contentBackgroundAlpha))
				contentBackgroundAlpha = 1;
			
			// Draw the contentBackgroundColor
			graphics.beginFill(contentBackgroundColor, contentBackgroundAlpha);
			graphics.drawRoundRect(borderSize, borderSize, unscaledWidth - borderWidth, unscaledHeight - borderWidth, layoutCornerEllipseSize, layoutCornerEllipseSize);
			graphics.endFill();
		}
		
		/**
		 *  @private
		 */
		override public function styleChanged(styleProp:String):void
		{
			var allStyles:Boolean = !styleProp || styleProp == "styleName";
			
			if (allStyles || styleProp == "borderVisible")
			{
				borderVisibleChanged = true;
				invalidateProperties();
			}
			
			if (allStyles || styleProp.indexOf("padding") == 0)
			{
				invalidateDisplayList();
			}
			
			super.styleChanged(styleProp);
		}
		
		/**
		 *  @private
		 */
		override protected function commitCurrentState():void
		{
			super.commitCurrentState();
			
			alpha = currentState.indexOf("disabled") == -1 ? 1 : 0.5;
			
			var showPrompt:Boolean = currentState.indexOf("WithPrompt") != -1;
			
			if (showPrompt && !promptDisplay)
			{
				promptDisplay = createPromptDisplay();
				promptDisplay.addEventListener(FocusEvent.FOCUS_IN, promptDisplay_focusInHandler);
			}
			else if (!showPrompt && promptDisplay)
			{
				promptDisplay.removeEventListener(FocusEvent.FOCUS_IN, promptDisplay_focusInHandler);
				removeChild(promptDisplay as DisplayObject);
				promptDisplay = null;
			}
			
			invalidateDisplayList();
		}
		
		/**
		 *  @private
		 */
		override protected function layoutContents(unscaledWidth:Number, 
												   unscaledHeight:Number):void
		{
			super.layoutContents(unscaledWidth, unscaledHeight);
			
			// position & size border
			if (border)
			{
				setElementSize(border, unscaledWidth, unscaledHeight);
				setElementPosition(border, 0, 0);
			}
		}
		
		//--------------------------------------------------------------------------
		//
		//  Methods
		//
		//--------------------------------------------------------------------------
		
		/**
		 *  @private
		 *  Create a control appropriate for displaying the prompt text in a mobile
		 *  input field.
		 */
		protected function createPromptDisplay():IDisplayText
		{
			var prompt:StyleableTextField = StyleableTextField(createInFontContext(StyleableTextField));
			prompt.styleName = this;
			prompt.editable = false;
			prompt.mouseEnabled = false;
			prompt.useTightTextBounds = false;
			
			// StageText objects appear in their own layer on top of the display
			// list. So, even though this prompt may be created after the StageText
			// for textDisplay, textDisplay will still be on top.
			addChild(prompt);
			
			return prompt;
		}
		
		/**
		 *  @private
		 *  Utility function used by subclasses' measure functions to measure their
		 *  text host components.
		 */
		protected function measureTextComponent(hostComponent:SkinnableTextBase):void
		{
			var paddingLeft:Number = getStyle("paddingLeft");
			var paddingRight:Number = getStyle("paddingRight");
			var paddingTop:Number = getStyle("paddingTop");
			var paddingBottom:Number = getStyle("paddingBottom");
			var textHeight:Number = getStyle("fontSize");
			
			if (textDisplay)
				textHeight = getElementPreferredHeight(textDisplay);
			
			// width is based on maxChars (if set)
			if (hostComponent && hostComponent.maxChars)
			{
				// Grab the fontSize and subtract 2 as the pixel value for each character.
				// This is just an approximation, but it appears to be a reasonable one
				// for most input and most font.
				var characterWidth:int = Math.max(1, (textHeight - 2));
				measuredWidth =  (characterWidth * hostComponent.maxChars) + 
					paddingLeft + paddingRight;
			}
			
			measuredHeight = paddingTop + textHeight + paddingBottom;
		}
		
		//--------------------------------------------------------------------------
		//
		//  Event handlers
		//
		//--------------------------------------------------------------------------
		
		/**
		 *  If the prompt is focused, we need to move focus to the textDisplay
		 *  StageText. This needs to happen outside of the process of setting focus
		 *  to the prompt, so we use callLater to do that.
		 */
		private function focusTextDisplay():void
		{
			textDisplay.setFocus();
		}
		
		private function promptDisplay_focusInHandler(event:FocusEvent):void
		{
			callLater(focusTextDisplay);
		}
	}
}
