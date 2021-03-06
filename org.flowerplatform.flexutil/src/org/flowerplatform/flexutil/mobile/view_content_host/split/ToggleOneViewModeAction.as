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
package org.flowerplatform.flexutil.mobile.view_content_host.split {
	import org.flowerplatform.flexutil.FlexUtilAssets;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class ToggleOneViewModeAction extends ActionBase {
		
		public var splitWrapperView:MobileSplitViewHost;
		
		public function ToggleOneViewModeAction() {
			super();
		}
		
		override public function get icon():Object {
			if (splitWrapperView.oneViewMode) {
				return FlexUtilAssets.switch_two_views;
			} else {
				return FlexUtilAssets.switch_one_view;
			}
		}
		
		override public function get label():String {
			if (splitWrapperView.oneViewMode) {
				return FlexUtilAssets.INSTANCE.getMessage("SplitWrapperView.ToggleOneViewModeAction.modeTwo");
			} else {
				return FlexUtilAssets.INSTANCE.getMessage("SplitWrapperView.ToggleOneViewModeAction.modeOne");
			}
		}
		
		override public function run():void {
			splitWrapperView.oneViewMode = !splitWrapperView.oneViewMode;
		}
		
		override public function get visible():Boolean {
			return selection == null || selection.length == 0 || splitWrapperView.switchActionsVisibleOnNonEmptySelection;
		}		
		
	}
}