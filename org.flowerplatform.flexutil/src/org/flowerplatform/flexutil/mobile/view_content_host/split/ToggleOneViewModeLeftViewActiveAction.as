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
package org.flowerplatform.flexutil.mobile.view_content_host.split {
	import org.flowerplatform.flexutil.FlexUtilAssets;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class ToggleOneViewModeLeftViewActiveAction extends ActionBase {
		
		public var splitWrapperView:MobileSplitViewHost;
		
		public function ToggleOneViewModeLeftViewActiveAction() {
			super();
			icon = FlexUtilAssets.switch_view;
		}
		
		override public function get label():String {
			if (splitWrapperView.oneViewModeLeftViewActive) {
				return FlexUtilAssets.INSTANCE.getMessage("SplitWrapperView.ToggleOneViewModeActionLeftViewActive.right");
			} else {
				return FlexUtilAssets.INSTANCE.getMessage("SplitWrapperView.ToggleOneViewModeActionLeftViewActive.left");
			}
		}
		
		override public function run():void {
			splitWrapperView.oneViewModeLeftViewActive = !splitWrapperView.oneViewModeLeftViewActive;
		}
		
		override public function get visible():Boolean {
			return splitWrapperView.oneViewMode && (selection == null || selection.length == 0 || splitWrapperView.switchActionsVisibleOnNonEmptySelection);
		}		
		
	}
}