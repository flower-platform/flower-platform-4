package org.flowerplatform.flexutil.properties.property_line_renderer {
	import flash.events.MouseEvent;
	
	import spark.components.Button;
	import spark.components.CheckBox;
	
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * @author Balutoiu Diana
	 * @author Cristina Constantinescu
	 */
	public class PreferenceUserPropertyLineRenderer extends PreferencePropertyLineRenderer {
		
		protected var usedCheckBox:CheckBox;
		
		protected var copyToGlobal:Button;
		
		override protected function createChildren():void {			
			usedCheckBox = new CheckBox();
			usedCheckBox.label = Resources.getMessage("used");
			usedCheckBox.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {				
				if (!usedCheckBox.selected) {					
					unsetPreference();
				} else {
					setPreference();
				}	
			});
			
			copyToGlobal = new Button();
			copyToGlobal.toolTip = Resources.getMessage("preference.copy.to.global");
			copyToGlobal.setStyle("icon", Resources.propertiesIcon);
			copyToGlobal.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {setPreference(globalPropertyName)});
			
			if (!FlexUtilGlobals.getInstance().isMobile) {
				copyToGlobal.width = 22;
				copyToGlobal.height = 22
			}
			
			super.createChildren();
			
			rendererArea.addElement(usedCheckBox);		
			rendererArea.addElement(copyToGlobal);					
		}		
		
		override public function nodeObjectUpdated():void {
			super.nodeObjectUpdated();
			
			//TODO: needs node property
		}
	}
}