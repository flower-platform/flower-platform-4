package org.flowerplatform.flex_client.core.editor.action {
	
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.action.ComposedAction;
	
	/**	
	 * @author Cristina Constantinescu
	 */
	public class OpenWithEditorComposedAction extends ComposedAction {
		
		public static const ACTION_ID_OPEN_WITH:String = "openWith";
		
		public function OpenWithEditorComposedAction() {
			label = Resources.getMessage("action.open.with");
			icon = Resources.openIcon;
			orderIndex = 30;
			id = ACTION_ID_OPEN_WITH;
		}
		
	}
}