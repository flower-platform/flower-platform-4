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
////////////////////////////////////////////////////////////////////////////////
//
//  ADOBE SYSTEMS INCORPORATED
//  Copyright 2011 Adobe Systems Incorporated
//  All Rights Reserved.
//
//  NOTICE: Adobe permits you to use, modify, and distribute this file
//  in accordance with the terms of the license agreement accompanying it.
//
////////////////////////////////////////////////////////////////////////////////

package org.flowerplatform.flex_client.host_app.mobile.text
{
	import flash.system.Capabilities;
	
	import mx.core.DPIClassification;
	import mx.core.mx_internal;
	
	import spark.components.Scroller;
	import spark.components.TextArea;
	import spark.components.supportClasses.StyleableTextField;
	
	use namespace mx_internal;
	
	/**
	 *  ActionScript-based skin for TextArea controls in mobile applications that uses a
	 *  StyleableStageText class for the text display. 
	 * 
	 *  @see spark.components.TextArea
	 *  @see spark.components.supportClasses.StyleableStageText
	 * 
	 *  @langversion 3.0
	 *  @playerversion AIR 3.0 
	 *  @productversion Flex 4.6
	 */
	public class RichEditableTextAreaSkin extends RichEditableTextSkinBase
	{
		//--------------------------------------------------------------------------
		//
		//  Class variables
		//
		//--------------------------------------------------------------------------
		
		/**
		 *  The underlying native text control on iOS has internal margins of its
		 *  own. In order to remain faithful to the paddingTop and paddingBottom
		 *  style values that developers may specify, those internal margins need to
		 *  be compensated for. This variable contains size of that compensation in
		 *  pixels.
		 */
		mx_internal static var iOSVerticalPaddingAdjustment:Number = 5;
		
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
		 */
		public function RichEditableTextAreaSkin()
		{
			super();
			multiline = true;
			
			switch (applicationDPI)
			{
				case DPIClassification.DPI_320:
				{
					measuredDefaultHeight = 106;
					break;
				}
				case DPIClassification.DPI_240:
				{
					measuredDefaultHeight = 70;
					break;
				}
				default:
				{
					measuredDefaultHeight = 53;
					break;
				}
			}
		}
		
		//--------------------------------------------------------------------------
		//
		//  Variables
		//
		//--------------------------------------------------------------------------
		
		/**
		 *  Scroller skin part.
		 *
		 *  @langversion 3.0
		 *  @playerversion Flash 10
		 *  @playerversion AIR 2.5 
		 *  @productversion Flex 4.5
		 */ 
		public var scroller:Scroller;
		
		/** 
		 *  @copy spark.skins.spark.ApplicationSkin#hostComponent
		 */
		public var hostComponent:TextArea;
		
		// SkinnableComponent will populate
		
		//--------------------------------------------------------------------------
		//
		//  Overridden methods
		//
		//--------------------------------------------------------------------------
		
		override protected function createChildren():void
		{
			super.createChildren();
			
			if (!scroller)
			{
				scroller = new Scroller();
				scroller.minViewportInset = 0;
				scroller.measuredSizeIncludesScrollBars = false;
				scroller.ensureElementIsVisibleForSoftKeyboard = false;
				
				addChild(scroller);
			}
			
			if (!scroller.viewport)
				scroller.viewport = textDisplay;
		}
		
		/**
		 *  @private
		 */
		override protected function layoutContents(unscaledWidth:Number, 
												   unscaledHeight:Number):void
		{
			// base class handles border position & size
			super.layoutContents(unscaledWidth, unscaledHeight);
			
			// position & size border
			if (border)
			{
				setElementSize(border, unscaledWidth, unscaledHeight);
				setElementPosition(border, 0, 0);
			}
			
			setElementSize(scroller, unscaledWidth, unscaledHeight);
			setElementPosition(scroller, 0, 0);
			
			// position & size the text
			var paddingLeft:Number = getStyle("paddingLeft");
			var paddingRight:Number = getStyle("paddingRight");
			var paddingTop:Number = getStyle("paddingTop");
			var paddingBottom:Number = getStyle("paddingBottom");
			
			var unscaledTextWidth:Number = Math.max(0, unscaledWidth - paddingLeft - paddingRight);
			var unscaledTextHeight:Number = Math.max(0, unscaledHeight - paddingTop - paddingBottom);
			
			if (textDisplay)
			{
				var verticalPosAdjustment:Number = 0;
				var heightAdjustment:Number = 0;
				
				if (Capabilities.version.indexOf("IOS") == 0)
				{
					verticalPosAdjustment = Math.min(iOSVerticalPaddingAdjustment, paddingTop);
					heightAdjustment = verticalPosAdjustment + Math.min(iOSVerticalPaddingAdjustment, paddingBottom);
				}
				
				//				textDisplay.commitStyles();
				setElementSize(textDisplay, unscaledTextWidth, unscaledTextHeight + heightAdjustment);
				setElementPosition(textDisplay, paddingLeft, paddingTop - verticalPosAdjustment);
			}
			
			if (promptDisplay)
			{
				if (promptDisplay is StyleableTextField)
					StyleableTextField(promptDisplay).commitStyles();
				
				setElementSize(promptDisplay, unscaledTextWidth, unscaledTextHeight);
				setElementPosition(promptDisplay, paddingLeft, paddingTop);
			}
		}
		
		/**
		 *  @private
		 */
		override protected function measure():void
		{
			super.measure();
			measureTextComponent(hostComponent);
		}
	}
}
