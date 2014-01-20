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
package org.flowerplatform.flexutil.iframe {
	import flash.display.DisplayObjectContainer;
	import flash.events.Event;
	
	import mx.containers.TabNavigator;
	import mx.core.Container;
	import mx.core.UIComponent;
	import mx.core.mx_internal;
	import mx.events.FlexEvent;
	import mx.graphics.shaderClasses.ExclusionShader;
	
	/**
	 * <code>IFrame</code> code was copied from here:
	 * http://code.google.com/p/flex-iframe
	 * 
	 * <p>
	 * This class adds specific flower behavior and styles.
	 * @author Cristina Constantinescu
	 */
	public class FlowerIFrame extends IFrame {
		
		public function FlowerIFrame(id:String = null) {
			super(id);
			
			setStyle("paddingLeft", 0);
			setStyle("paddingRight", 0);
			setStyle("paddingBottom", 0);
			setStyle("paddingTop", 0);
			
			overlayDetection = true;			
		}
		
		override public function parentChanged(p:DisplayObjectContainer):void {
			super.parentChanged(p);
			updateFrameVisibility(true);
		}
		
		override protected function updateFrameVisibility(value:Boolean):Boolean {
			try {
				return super.updateFrameVisibility(value);
			} catch (e:Error) {
				// error thrown when this object changes its parent, but the new parent isn't set yet
				// to avoid it, catch it here and call it after, in parentChanged
			}
			return false;
		}
		
	}
}