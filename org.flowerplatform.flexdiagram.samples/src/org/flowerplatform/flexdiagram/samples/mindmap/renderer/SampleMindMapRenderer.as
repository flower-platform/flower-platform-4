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
	import flash.events.Event;
	
	import mx.collections.IList;
	import mx.core.IVisualElement;
	import mx.core.mx_internal;
	import mx.events.FlexEvent;
	import mx.events.PropertyChangeEvent;
	
	import spark.components.Group;
	import spark.components.HGroup;
	import spark.components.Label;
	import spark.components.RichText;
	
	import flashx.textLayout.conversion.TextConverter;
	import flashx.textLayout.elements.BreakElement;
	import flashx.textLayout.elements.ParagraphElement;
	import flashx.textLayout.elements.SpanElement;
	import flashx.textLayout.elements.TextFlow;
	import flashx.textLayout.events.CompositionCompleteEvent;
	
	import org.flowerplatform.flexdiagram.mindmap.MindMapRenderer;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.SampleMindMapModel;
	import org.flowerplatform.flexdiagram.samples.properties.SamplePropertiesHelper;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.controller.ValuesProvider;
	import org.flowerplatform.flexutil.properties.PropertyEntry;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SampleMindMapRenderer extends MindMapRenderer {
		
		protected var detailsLabel:RichText;
		
		protected var propertiesGroup:Group;
		
		protected var propertiesNames:RichText;
		
		protected var propertiesValues:RichText;
		
		protected static var instanceNumber:int = -1;
		
		public function set detailsText(value:String):void {
			if (value != null && value != "") {
				if (detailsLabel == null) {
					canHaveChildren = true;
					detailsLabel = new RichText();
					insertNewChild(detailsLabel);
				}
				detailsLabel.textFlow = TextConverter.importToFlow(value , Utils.isHTMLText(value) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT);	
			} else {
				if (detailsLabel != null) {
					removeElement(detailsLabel);
					detailsLabel = null;
					if (!hasChildren()) {
						canHaveChildren = false;
					}
				}
			}			
		}

		public function set propertyEntries(entries:IList):void {
			if (entries != null) {
				if (propertiesGroup == null) {
					canHaveChildren = true;
					propertiesGroup = new HGroup();
					propertiesGroup.percentWidth = 100;
					insertNewChild(propertiesGroup);
					propertiesNames = new RichText();
					propertiesGroup.addElement(propertiesNames);
					propertiesValues = new RichText();
					propertiesValues.percentWidth = 100;
					propertiesGroup.addElement(propertiesValues);
				}
				var namesTextFlow:TextFlow = new TextFlow();
				var names:ParagraphElement = new ParagraphElement();
				namesTextFlow.addChild(names);

				var valuesTextFlow:TextFlow = new TextFlow();
				var values:ParagraphElement = new ParagraphElement();
				valuesTextFlow.addChild(values);
				
				for (var i:int = 0; i < entries.length; i++) {
					var entry:PropertyEntry = PropertyEntry(entries.getItemAt(i));
					var name:SpanElement = new SpanElement();
					var value:SpanElement = new SpanElement();
					if (entry.isGroup) {
						name.fontWeight = "bold";
						name.text = " " + entry.descriptor.name;
					} else {
						value.text = String(entry.value);
						name.text = "  " + entry.descriptor.name;
					}
					names.addChild(name);
					values.addChild(value);
					
					
					if (i < entries.length - 1) {
						names.addChild(new BreakElement());
						values.addChild(new BreakElement());
					}
				}
				propertiesNames.textFlow = namesTextFlow;
				propertiesValues.textFlow = valuesTextFlow;
//				propertiesValues.addEventListener(FlexEvent.UPDATE_COMPLETE, function (event:Event):void {
//					trace(propertiesValues.mx_internal::textLines.length);
//				});
			} else {
				if (propertiesGroup != null) {
					removeElement(propertiesGroup);
					propertiesGroup = null;
					propertiesNames = null;
					propertiesValues = null;
					if (!hasChildren()) {
						canHaveChildren = false;
					}
				}
			}
		}
		
		protected function getLogicalIndexForChild(element:IVisualElement):int {
			if (detailsLabel == element) {
				return 0;
			} else if (propertiesGroup == element) {
				return 1;
			} else {
				return -1;
			}
		}
		
		protected function hasChildren():Boolean {
			return detailsLabel != null || propertiesGroup != null;
		}
		
		protected function insertNewChild(newlyAddedElement:IVisualElement):void {
			var elementIndex:int = getLogicalIndexForChild(newlyAddedElement);
			for (var i:int = 0; i < numElements; i++) {
				if (elementIndex <= getLogicalIndexForChild(getElementAt(i))) {
					// e.g. look for element 3; currentResult = 4
					// equality shouldn't happen, as it's newly added element
					break;
				}
			}
			// not found, i.e. all elements have lower index; e.g. look for element 3, found 0 and 2
			// i.e. numElements; i.e. insert at the end
			addElementAt(newlyAddedElement, i);
		}
		
		public function SampleMindMapRenderer() {
			featureForValuesProvider = "mindMapValuesProvider";
//			canHaveChildren = true;
		}
		
		override protected function beginModelListen():void {
			super.beginModelListen();
		}
		
		override protected function endModelListen():void {
			SampleMindMapModel(data).properties.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, propertiesChangedHandler);
			super.endModelListen();
		}
		
		protected function createChildren1():void {
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
			propertyEntries = new SamplePropertiesHelper().getPropertyEntries(diagramShellContext.diagramShell.registry, data, true);
		}
		
	}
}