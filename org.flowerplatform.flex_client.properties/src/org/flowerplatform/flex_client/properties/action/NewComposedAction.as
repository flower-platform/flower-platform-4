package org.flowerplatform.flex_client.properties.action {
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flexutil.action.ComposedAction;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class NewComposedAction extends ComposedAction {
		
		public static const ACTION_ID_NEW:String = "new";
		
		public function NewComposedAction() {
			label = PropertiesPlugin.getInstance().getMessage("action.new");	
			icon = CorePlugin.getInstance().getResourceUrl("images/add.png");
			orderIndex = 10;
			delegateIfSingleChild = true;
			id = ACTION_ID_NEW;
		}
		
	}
}