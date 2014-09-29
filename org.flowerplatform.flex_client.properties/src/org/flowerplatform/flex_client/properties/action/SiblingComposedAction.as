package org.flowerplatform.flex_client.properties.action
{
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.action.ComposedAction;

	public class SiblingComposedAction extends ComposedAction {
		
		public static const ID:String = "org.flowerplatform.flex_client.properties.action.SiblingComposedAction";
		
		public function SiblingComposedAction() {
			label = Resources.getMessage("action.newSibling");	
			icon = Resources.addIcon;
			orderIndex = 15;
			delegateIfSingleChild = true;			
		}
	}
}