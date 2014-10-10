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
	
	import spark.components.Label;
	import spark.components.RichText;
	
	import flashx.textLayout.conversion.TextConverter;
	
	import org.flowerplatform.flexdiagram.mindmap.MindMapRenderer;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.controller.ValuesProvider;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SampleMindMapNodeRendererWithDetails extends MindMapRenderer {
		
		protected var detailsLabel:RichText;
		
		protected static var instanceNumber:int = -1;
		
		public function set detailsText(value:String):void {
			if (value != null) {
				detailsLabel.textFlow = TextConverter.importToFlow(value , Utils.isHTMLText(value) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT);	
			} else {
				detailsLabel.textFlow = null;
			}			
		}
		
		public function SampleMindMapNodeRendererWithDetails() {
			featureForValuesProvider = "mindMapValuesProvider";
			canHaveChildren = true;
		}
		
		override protected function createChildren():void {
			super.createChildren();
			var label:Label = new Label();
			label.text = "Details: (instance no = " + ++instanceNumber + ")";
			addElement(label);
			detailsLabel = new RichText();
			addElement(detailsLabel);
		}
		
		override protected function modelChangedHandler(event:PropertyChangeEvent):void {
			super.modelChangedHandler(event);
			var valuesProvider:ValuesProvider = getRequiredValuesProvider();
			setFieldIfNeeded(valuesProvider, typeDescriptorRegistry, event, "detailsText", "mindMapNodeRenderer.detailsText", "");
		}	
		
	}
}