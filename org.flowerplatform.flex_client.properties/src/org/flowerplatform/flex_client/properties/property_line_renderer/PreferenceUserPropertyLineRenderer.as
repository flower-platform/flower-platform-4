package org.flowerplatform.flex_client.properties.property_line_renderer {
	
	import flash.events.MouseEvent;
	
	import mx.binding.utils.BindingUtils;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.remote.PreferencePropertyWrapper;
	import org.flowerplatform.flex_client.core.node.event.NodeUpdatedEvent;
	import org.flowerplatform.flex_client.properties.ui.PropertiesComponent;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	
	import spark.components.Button;
	import spark.components.CheckBox;

	/**
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
	
		override protected function nodeUpdated():void {
			super.nodeUpdated();
			if (usedCheckBox) {
				usedCheckBox.selected = PreferencePropertyWrapper(node.getPropertyValueOrWrapper(propertyDescriptor.name)).isUsed;
			}
		}
			
		override public function commit(callbackHandler:Function = null):void {			
			super.commit(refreshPreferences);
		}
		
	}
}