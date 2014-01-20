package org.flowerplatform.flexutil.mobile.view_content_host.split {
	import org.flowerplatform.flexutil.FlexUtilAssets;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class ToggleOneViewModeAction extends ActionBase {
		
		[Embed(source="switch_view1.png")]
		public static var ICON_1:Class;
		
		[Embed(source="switch_view2.png")]
		public static var ICON_2:Class;

		public var splitWrapperView:MobileSplitViewHost;
		
		public function ToggleOneViewModeAction() {
			super();
		}
		
		override public function get icon():Object {
			if (splitWrapperView.oneViewMode) {
				return ICON_2;
			} else {
				return ICON_1;
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