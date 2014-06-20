package org.flowerplatform.flex_client.properties.preferences {
	
	import mx.core.UIComponent;
	
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.layout.AbstractViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	public class PreferencesViewProvider extends AbstractViewProvider {
		
		public static const ID:String = "preferences";
		
		override public function getId():String {
			return ID;
		}
		
		override public function createView(viewLayoutData:ViewLayoutData):UIComponent {			
			return new PreferencesView();
		}
		
		override public function getTitle(viewLayoutData:ViewLayoutData=null):String	{
			return Resources.getMessage("preferences.view");
		}
		
		override public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			return Resources.propertiesIcon;
		}
		
	}
}