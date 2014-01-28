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
package org.flowerplatform.flexdiagram.event {
	import flash.events.Event;

	/**
	 * Dispatched by EditParts when the associated connections
	 * should update. If the EditPart has a figure, UI events (of
	 * the figure and/or parent figure(s)) should
	 * trigger this event (e.g. move, resize). If not, this event
	 * should be dispatched as an effect of some modifications of
	 * the model (and/or parent model). Received by ConnectionEditParts.
	 * 
	 * @see EditPart.dispatchUpdateConnectionEndsEvent()
  	 * @see ConnectionEditPart.updateConnectionsHandler()
	 * @author Cristi
	 */
	public class UpdateConnectionEndsEvent extends Event {
	
		public static const UPDATE_CONNECTION_ENDS:String = "UpdateConnectionEndsEvent";
		
		public function UpdateConnectionEndsEvent():void {
			super(UPDATE_CONNECTION_ENDS);
		}

	}
}