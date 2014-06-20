package org.flowerplatform.flex_client.properties.property_line_renderer {
	
	import org.flowerplatform.flex_client.core.editor.remote.PreferencePropertyWrapper;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.Utils;
	
	import spark.components.Button;
	import spark.components.CheckBox;

	public class PreferenceDefaultPropertyLineRenderer extends PropertyLineRenderer {
				
		protected var copyToUser:Button;
		protected var copyToGlobal:Button;
		
		override protected function createChildren():void {			
			copyToGlobal = new Button();
			copyToGlobal.toolTip = Resources.getMessage("preference.copy.to.global");
			copyToGlobal.label = "CG";
			
			copyToUser = new Button();
			copyToUser.toolTip = Resources.getMessage("preference.copy.to.user");
			copyToUser.label = "CU";
			
			super.createChildren();
			
			rendererArea.addElement(copyToGlobal);			
			rendererArea.addElement(copyToUser);			
		}		
		
		override protected function propertyDescriptorUpdated():void {			
			super.propertyDescriptorUpdated();
		
		}
			
		override protected function nodeUpdated():void {
			super.nodeUpdated();
		
		}
		
		override protected function prepareCommit(propertyValueOrWrapper:Object, newPropertyValue:Object):void {
			super.prepareCommit(propertyValueOrWrapper, newPropertyValue);
			
//			PreferencePropertyWrapper(propertyValueOrWrapper).isUsed = usedCheckBox.selected;
		}
		
	}
}