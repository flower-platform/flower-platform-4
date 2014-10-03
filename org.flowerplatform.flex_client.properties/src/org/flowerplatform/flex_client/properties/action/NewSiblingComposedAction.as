package org.flowerplatform.flex_client.properties.action
{
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.action.ComposedAction;

	/**
	 * @author Vlad Bogdan Manica
	 */ 
	public class NewSiblingComposedAction extends ComposedAction {
		
		public static const ID:String = "org.flowerplatform.flex_client.properties.action.NewSiblingComposedAction";
		
		public function NewSiblingComposedAction() {
			label = Resources.getMessage("action.newSibling");	
			icon = Resources.addSiblingIcon;
			orderIndex = 15;
			delegateIfSingleChild = true;			
		}
	}
}