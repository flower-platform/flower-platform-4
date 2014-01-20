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
package org.flowerplatform.flexutil.layout.event
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;

	/**
	 * Dispatched when one or multiple views are removed from workbench.
	 * 
	 * @author Cristina
	 */ 
	public class ViewsRemovedEvent extends Event {
		
		public static const VIEWS_REMOVED:String = "views_removed";
		
		private var _removedViews:ArrayCollection; /* of UIComponent */
		
		public var dontRemoveViews:ArrayCollection; /* of UIComponent */
		
		public function ViewsRemovedEvent(removedViews:ArrayCollection) {
			super(VIEWS_REMOVED);
			
			_removedViews = removedViews;
			dontRemoveViews = new ArrayCollection();
		}
		
		/**
		 * The <code>ViewLayoutData</code> corresponding to the view
		 * that is being closed.
		 */ 
		public function get removedViews():ArrayCollection {
			return _removedViews;
		}
	}
	
}