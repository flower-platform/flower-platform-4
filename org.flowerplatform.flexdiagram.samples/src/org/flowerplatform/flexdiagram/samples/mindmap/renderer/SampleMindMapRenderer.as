/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.flexdiagram.samples.mindmap.renderer {
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flexdiagram.mindmap.MindMapRenderer;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.SampleMindMapModel;
	import org.flowerplatform.flexutil.controller.ValuesProvider;
	import org.flowerplatform.flexutil.samples.properties.SamplePropertiesHelper;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SampleMindMapRenderer extends MindMapRenderer {

		public function SampleMindMapRenderer() {
			featureForValuesProvider = "mindMapValuesProvider";
		}
		
		override protected function beginModelListen():void {
			super.beginModelListen();
		}
		
		override protected function endModelListen():void {
			SampleMindMapModel(data).properties.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, propertiesChangedHandler);
			super.endModelListen();
		}
		
		override protected function modelChangedHandler(event:PropertyChangeEvent):void {
			super.modelChangedHandler(event);
			var valuesProvider:ValuesProvider = getRequiredValuesProvider();
			if (event == null || event.property == "showProperties") {
				if (SampleMindMapModel(data).showProperties) {
					SampleMindMapModel(data).properties.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, propertiesChangedHandler);
					propertiesChangedHandler(null);
				} else {
					propertyEntries = null;
					SampleMindMapModel(data).properties.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, propertiesChangedHandler);
				}
			}
		}	

		protected function propertiesChangedHandler(event:PropertyChangeEvent):void {
			propertyEntries = new SamplePropertiesHelper().getPropertyEntries(null, diagramShellContext.diagramShell.registry, data);
		}
		
	}
}