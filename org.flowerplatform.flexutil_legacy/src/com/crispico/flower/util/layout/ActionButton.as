/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package com.crispico.flower.util.layout {
	import flash.events.MouseEvent;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
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
			if (FlexUtilGlobals.getInstance().actionHelper.isComposedAction(action)) {
				viewWrapper.openMenu(event.stageX, event.stageY, viewWrapper.contextForActions, action.id);
			} else {
				FlexUtilGlobals.getInstance().actionHelper.runAction(action, viewWrapper.selection, viewWrapper.contextForActions);				
			}
		}
		
		/**
		 * Use the global image content cache for the icon component.
		 * 
		 * @author Mariana Gheorghe
		 */
		override protected function partAdded(partName:String, instance:Object):void {							 
			if (instance == iconDisplay) {
				iconDisplay.contentLoader = FlexUtilGlobals.getInstance().imageContentCache;								
			}
			super.partAdded(partName, instance);
		}
	}
}
