package org.flowerplatform.flexutil.samples.context_menu {
	
	import org.flowerplatform.flexutil.action.ActionBase;

	/**
	 * @author Iulian-Catalin Burcea
	 */
	public class ToggleAction extends ActionBase {
		
		public function ToggleAction() {
			super();
			isToggleAction = true;
			label = "Test Toggle Action";
		}

	}
}