package org.flowerplatform.flex_client.properties.property_line_renderer {
	
	import org.flowerplatform.flex_client.core.editor.remote.PreferencePropertyWrapper;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.Utils;
	
	import spark.components.Button;
	import spark.components.CheckBox;

	public class PreferenceUserPropertyLineRenderer extends PropertyLineRenderer {
		
		protected var usedCheckBox:CheckBox;
		
		protected var copyToGlobal:Button;
		
		override protected function createChildren():void {			
			usedCheckBox = new CheckBox();
			usedCheckBox.label = Resources.getMessage("used");
				
			copyToGlobal = new Button();
			copyToGlobal.toolTip = Resources.getMessage("preference.copy.to.global");
			copyToGlobal.label = "CG";
			
			super.createChildren();
			
			rendererArea.addElement(usedCheckBox);		
			rendererArea.addElement(copyToGlobal);					
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