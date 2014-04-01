package org.flowerplatform.flex_client.properties.action {
	
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flex_client.properties.PropertiesViewProvider;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class ShowPropertiesAction extends ActionBase {
		
		public function ShowPropertiesAction(){
			super();
			label = Resources.getMessage("properties.action.show");
			icon = Resources.propertiesIcon;
			orderIndex = 500;
		}
			
		override public function get visible():Boolean {
			return true;
		}
		
		override public function run():void {	
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
				.setViewIdInWorkbench(PropertiesViewProvider.ID)
				.setWidth(500)
				.setHeight(350)
				.show();
		}
	}
}