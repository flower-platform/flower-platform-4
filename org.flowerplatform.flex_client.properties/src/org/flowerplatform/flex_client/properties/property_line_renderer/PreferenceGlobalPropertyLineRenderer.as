package org.flowerplatform.flex_client.properties.property_line_renderer {
	
	import org.flowerplatform.flex_client.core.editor.remote.PreferencePropertyWrapper;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.Utils;
	
	import spark.components.Button;
	import spark.components.CheckBox;

	public class PreferenceGlobalPropertyLineRenderer extends PropertyLineRenderer {
		
		protected var usedCheckBox:CheckBox;
		
		protected var copyToUser:Button;
		
		override protected function createChildren():void {			
			usedCheckBox = new CheckBox();
			usedCheckBox.label = Resources.getMessage("used");
			
			copyToUser = new Button();
			copyToUser.toolTip = Resources.getMessage("preference.copy.to.user");
			copyToUser.label = "CU";
					
			super.createChildren();
			
			rendererArea.addElement(usedCheckBox);					
			rendererArea.addElement(copyToUser);			
		}		
		
		override protected function propertyDescriptorUpdated():void {			
			super.propertyDescriptorUpdated();
								
		}
			
		override protected function nodeUpdated():void {
			super.nodeUpdated();
			
			if (usedCheckBox != null) {				
//				usedCheckBox.selected = PreferencePropertyWrapper(node.getPropertyValueOrWrapper(propertyDescriptor.name)).isUsed;
			}
		}
		
		override protected function prepareCommit(propertyValueOrWrapper:Object, newPropertyValue:Object):void {
			super.prepareCommit(propertyValueOrWrapper, newPropertyValue);
			
//			PreferencePropertyWrapper(propertyValueOrWrapper).isUsed = usedCheckBox.selected;
		}
		
	}
}