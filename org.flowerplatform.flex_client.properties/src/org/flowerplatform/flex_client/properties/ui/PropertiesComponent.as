/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flex_client.properties.ui {
	
	import flash.utils.Dictionary;
	
	import mx.core.IVisualElement;
	import mx.events.PropertyChangeEvent;
	
	import spark.components.Form;
	import spark.components.Group;
	import spark.components.Scroller;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.properties.PropertiesConstants;
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flex_client.properties.property_line_renderer.IPropertyLineRenderer;
	import org.flowerplatform.flex_client.properties.property_line_renderer.PropertyLineRenderer;
	import org.flowerplatform.flex_client.properties.remote.IPropertyDescriptor;
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	
	/**
	 * Component can be used to display a node's properties.
	 * 
	 * <p>
	 * Use <code>refreshForm</code> to populate with data and <code>clearForm</code> to clear data.
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class PropertiesComponent extends Scroller {
		
		protected var propertiesForm:Form;
		
		protected var currentNode:Node;
		
		public function PropertiesComponent() {
			super();
		}
	
		public function refreshForm(node:Node, includeRawProperties:Boolean = false):void {
			// remove listeners from previous node				
			if (currentNode != null) {
				currentNode.properties.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, nodeUpdatedHandler);
			}
			
			currentNode = node;
			refreshRenderers(currentNode, includeRawProperties);
			
			// add listeners to current node				
			if (currentNode != null) {					
				currentNode.properties.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, nodeUpdatedHandler);					
			}
		}
		
		public function clearForm():void {
			propertiesForm.removeAllElements();
		}
		
		override protected function createChildren():void {
			super.createChildren();
			
			var group:Group = new Group();
			group.percentHeight = 100;
			group.percentWidth = 100;
			viewport = group;
			
			propertiesForm = new Form();
			propertiesForm.percentHeight = 100;
			propertiesForm.percentWidth = 100;
			group.addElement(propertiesForm);
		}
					
		private function getPropertiesToDisplay(node:Node, includeRawProperties:Boolean = false):Array {
			var properties:Array = new Array();
			var categories:Dictionary = new Dictionary();
			var propertyDescriptor:IPropertyDescriptor;
			
			node.properties.nodeUri = node.nodeUri;

			for (var key:* in node.properties) {
				propertyDescriptor = PropertiesPlugin.getInstance().getPropertyDescriptor(node, key, includeRawProperties);
				if (propertyDescriptor == null) { 
					// if no property descriptor found -> don't display it
					continue;
				}
				
				// verify if there is already created a dummy property descriptor for this category
				if (categories.hasOwnProperty(propertyDescriptor.category)) {
					// added -> make sure that it has the lowest index 
					categories[propertyDescriptor.category].orderIndex = Math.min(categories[propertyDescriptor.category].orderIndex, propertyDescriptor.orderIndex - 1);
				} else {
					// new category -> create a dummy property descriptor for it
					var dummyPropertyDescriptor:PropertyDescriptor = new PropertyDescriptor();						
					dummyPropertyDescriptor.propertyLineRenderer = PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_CATEGORY;
					dummyPropertyDescriptor.name = propertyDescriptor.category;
					// category must be displayed first, so it must have the lowest index
					dummyPropertyDescriptor.orderIndex = propertyDescriptor.orderIndex - 1; 
					
					categories[propertyDescriptor.category] = dummyPropertyDescriptor;
					properties.push(dummyPropertyDescriptor);
				}						
				properties.push(propertyDescriptor);
			}
			
			// sort property descriptors based on their orderIndex -> this will represent the display order for visual renderers
			properties.sort(function sortOnIndex(a:IPropertyDescriptor, b:IPropertyDescriptor):Number {				
				if (a.orderIndex > b.orderIndex) {
					return 1;
				} else if (a.orderIndex < b.orderIndex) {
					return -1;
				}
				return 0;
			});
	
			return properties;
		}
		
		/**
		 * Note: code copied and adapted from SequentialLayoutVisualChildrenController (same mechanism, but different data, hard to create common code).
		 */ 
		private function refreshRenderers(node:Node, includeRawProperties:Boolean = false):void {
			if (node == null) {
				return;
			}								
			var propertyDescriptor:PropertyDescriptor;				
			var properties:Array = getPropertiesToDisplay(node, includeRawProperties);
			
			var updatePropertyLineRendererData:Function = function(renderer:IPropertyLineRenderer, propertyDescriptor:PropertyDescriptor, node:Node):void {
				renderer.propertyDescriptor = propertyDescriptor;
				renderer.node = node;	
			};
			
			for (var i:int = 0; i < properties.length; i++) {
				var childRendererCandidate:IPropertyLineRenderer = null;
				propertyDescriptor = PropertyDescriptor(properties[i]);
				
				if (i < propertiesForm.numElements) {
					// we still have renderer candidates
					childRendererCandidate = IPropertyLineRenderer(propertiesForm.getElementAt(i)); 
					var childPropertyDescriptorCandidate:PropertyDescriptor = IPropertyLineRenderer(childRendererCandidate).propertyDescriptor;
					if (propertyDescriptor == childPropertyDescriptorCandidate) {
						updatePropertyLineRendererData(childRendererCandidate, propertyDescriptor, node);					
						// nothing to do, so skip							
						continue;
					} else {
						if (childPropertyDescriptorCandidate.type != propertyDescriptor.type || 
							childPropertyDescriptorCandidate.propertyLineRenderer != propertyDescriptor.propertyLineRenderer) {
							// the candidate renderer are not compatible => remove it
							propertiesForm.removeElementAt(i);
							childRendererCandidate = null; // i.e. instruct the code below to create									
						} else {
							// a valid candidate								
						}
					}
				} 
				
				if (childRendererCandidate == null) {
					// i.e. either we have reached numElements,
					// or the candidate was not compatible/recyclable so it was removed					
					childRendererCandidate = PropertiesPlugin.getInstance().getNewPropertyLineRendererInstance(propertyDescriptor.propertyLineRenderer);	
					
					updatePropertyLineRendererData(childRendererCandidate, propertyDescriptor, node);	
					propertiesForm.addElementAt(IVisualElement(childRendererCandidate), i);						
				} else {
					updatePropertyLineRendererData(childRendererCandidate, propertyDescriptor, node);					
				}					
			}
			
			// this loop happens if the number of models < number of renderers in the (probably recycled) parent renderer
			while (propertiesForm.numElements > properties.length) {
				childRendererCandidate = IPropertyLineRenderer(propertiesForm.getElementAt(propertiesForm.numElements - 1));				
				propertiesForm.removeElementAt(propertiesForm.numElements - 1);					
			}
		}
		
		private function nodeUpdatedHandler(event:PropertyChangeEvent):void {
			for (var i:int = 0; i < propertiesForm.numElements; i++) {
				var obj:Object = propertiesForm.getElementAt(i);
				if (!(obj is PropertyLineRenderer)) {
					continue;
				}				
				if (PropertyLineRenderer(obj).propertyDescriptor.name == event.property) {
					PropertyLineRenderer(obj).nodeUpdated();
				}
			}
		}		
		
	}
}