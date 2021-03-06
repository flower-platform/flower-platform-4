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
package com.crispico.flower.util.layout.event {
	import flash.events.Event;
	
	import mx.core.UIComponent;
	
	/**
	 * @author Cristi
	 */
	public class ViewAddedEvent extends Event {
		
		public static const VIEW_ADDED:String = "view_added";
		
		private var _view:UIComponent;
		
		public function ViewAddedEvent(view:UIComponent) {
			super(VIEW_ADDED);
			_view = view;
		}

		public function get view():UIComponent {
			return _view;
		}

	}
}