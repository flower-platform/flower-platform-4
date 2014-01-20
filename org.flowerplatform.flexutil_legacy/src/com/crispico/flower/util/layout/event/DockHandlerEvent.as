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
package com.crispico.flower.util.layout.event
{
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	import flash.events.Event;
	
	import mx.core.UIComponent;

	/**
	 * Dispatched by <code>Workbench</code> when the "Dock" button from
	 * <code>ViewPopupWindow</code> is pressed.
	 * 
	 * <p>
	 * Allows adding custom behavior for adding the view back to workbench. <br>
	 * It is cancelable, so other events can prevent default behavior to be executed.
	 * 
	 * @author Cristina
	 */ 
	public class DockHandlerEvent extends Event	{
		
		public static const CLICK:String = "DockClick";
				
		public var component:UIComponent;
		
		public var viewLayoutData:ViewLayoutData;
		
		public function DockHandlerEvent(type:String, viewLayoutData:ViewLayoutData, component:UIComponent) {
			super(type, false, true);
			this.viewLayoutData = viewLayoutData;
			this.component = component;
		}
	}
}