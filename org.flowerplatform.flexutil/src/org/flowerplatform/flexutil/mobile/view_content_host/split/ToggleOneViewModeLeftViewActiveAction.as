package org.flowerplatform.flexutil.mobile.view_content_host.split {
	import org.flowerplatform.flexutil.FlexUtilAssets;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class ToggleOneViewModeLeftViewActiveAction extends ActionBase {
		
		[Embed(source="switch_view.png")]
		public static var ICON:Class;
		
		public var splitWrapperView:MobileSplitViewHost;
		
		public function ToggleOneViewModeLeftViewActiveAction() {
			super();
			icon = ICON;
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