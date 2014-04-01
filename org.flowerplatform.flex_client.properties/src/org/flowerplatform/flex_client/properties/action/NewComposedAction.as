package org.flowerplatform.flex_client.properties.action {
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.action.ComposedAction;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class NewComposedAction extends ComposedAction {
		
		public static const ACTION_ID_NEW:String = "new";
		
		public function NewComposedAction() {
			label = Resources.getMessage("action.new");	
			icon = Resources.addIcon;
			orderIndex = 10;
			delegateIfSingleChild = true;
			id = ACTION_ID_NEW;
		}
		
	}
}