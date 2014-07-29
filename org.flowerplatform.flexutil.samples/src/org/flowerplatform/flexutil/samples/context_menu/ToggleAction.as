package org.flowerplatform.flexutil.samples.context_menu {
	
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.action.ActionBase;

	
	/**
	 * @author Iulian-Catalin Burcea
	 */
	public class ToggleAction extends ActionBase {
		
		private var _isToggle:Boolean = false;
		
		public function ToggleAction() {
			super();
			icon = Resources.mindmap_uncheckedIcon;
		}
		
		public function get isToggle():Boolean {
			return _isToggle;
		}

		public function set isToggle(value:Boolean):void {
			_isToggle = value;
			icon = (value) ? Resources.mindmap_checkedIcon : Resources.mindmap_uncheckedIcon;
		}
		
		override public function run():void {
			isToggle=!isToggle;
		}
	}
}