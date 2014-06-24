package org.flowerplatform.flex_client.properties.action {
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.properties.preferences.PreferencesViewProvider;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class ShowPreferencesAction extends ActionBase {
		
		public function ShowPreferencesAction(){
			super();
			label = Resources.getMessage("preferences.action.show");
			icon = Resources.preferencesIcon;
			parentId = CoreConstants.TOOLS_MENU_ID;
		}
		
		override public function get visible():Boolean {
			return true;
		}
		
		override public function run():void {	
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
				.setViewIdInWorkbench(PreferencesViewProvider.ID)
				.setWidth(800)
				.setHeight(500)
				.show();
		}
	}
}