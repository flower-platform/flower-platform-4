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
package com.crispico.flower.util.layout {
	import flash.events.MouseEvent;
	
	import org.flowerplatform.flexutil.action.ActionUtil;
	import org.flowerplatform.flexutil.action.IAction;
	
	import spark.components.Button;
	
	/**		
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public class ActionButton extends Button {
		
		public var viewWrapper:WorkbenchViewHost;
		
		public var action:IAction;
				
		protected override function clickHandler(event:MouseEvent):void {
			super.clickHandler(event);
			if (ActionUtil.isComposedAction(action)) {
				viewWrapper.openMenu(event.stageX, event.stageY, viewWrapper.contextForActions, action.id);
			} else {
				try {
					action.selection = viewWrapper.selection;
					action.context = viewWrapper.contextForActions;
					action.run();
				} finally {
					action.selection = null;
					action.context = null;
				}
			}
		}
	}
}