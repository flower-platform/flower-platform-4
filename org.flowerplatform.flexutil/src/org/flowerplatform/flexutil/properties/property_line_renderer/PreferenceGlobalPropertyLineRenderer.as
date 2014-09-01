package org.flowerplatform.flexutil.properties.property_line_renderer {
	import flash.events.MouseEvent;
	
	import spark.components.Button;
	import spark.components.CheckBox;
	
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * @author Diana Balutoiu
	 * @author Cristina Constantinescu
	 */
	public class PreferenceGlobalPropertyLineRenderer extends PreferencePropertyLineRenderer {
		
		protected var usedCheckBox:CheckBox;
		protected var copyToUser:Button;
		
		override protected function createChildren():void {			
			usedCheckBox = new CheckBox();
			usedCheckBox.label = Resources.getMessage("used");
			usedCheckBox.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {		
				if (!usedCheckBox.selected) {
					unsetPreference();
				} else {
					unsetPreference(userPropertyName);
					setPreference();
				}			
				
			});
			
			copyToUser = new Button();
			copyToUser.toolTip = Resources.getMessage("preference.copy.to.user");
			copyToUser.setStyle("icon", Resources.propertiesIcon);
			copyToUser.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {setPreference(userPropertyName)});
			
			if (!FlexUtilGlobals.getInstance().isMobile) {
				copyToUser.width = 22;
				copyToUser.height = 22
			}
			
			super.createChildren();
			
			rendererArea.addElement(usedCheckBox);					
			rendererArea.addElement(copyToUser);			
		}		
		
		override public function nodeObjectUpdated():void {
			super.nodeObjectUpdated();
			//TODO: needs node properties
		}
	}
}