package org.flowerplatform.flex_client.properties.ui {
	
	import flash.utils.Dictionary;
	
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.node.controller.NodeControllerUtils;
	import org.flowerplatform.flex_client.core.node.event.NodeUpdatedEvent;
	import org.flowerplatform.flex_client.properties.PropertiesConstants;
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flex_client.properties.property_line_renderer.IPropertyLineRenderer;
	import org.flowerplatform.flex_client.properties.property_line_renderer.PropertyLineRenderer;
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	
	import spark.components.Form;
	import spark.components.Group;
	import spark.components.Scroller;
	
	public class PropertiesComponent extends Scroller {
		
		protected var propertiesForm:Form;
		
		protected var currentNode:Node;
		
		public function PropertiesComponent() {
			super();
		}
	
		public function refreshForm(node:Node, includeRawProperties:Boolean = false):void {			
			// remove listeners from previous node				
			if (currentNode != null) {
				currentNode.removeEventListener(NodeUpdatedEvent.NODE_UPDATED, nodeUpdatedHandler);
			}
			
			currentNode = node;
			refreshRenderers(currentNode, includeRawProperties);
			
			// add listeners to current node				
			if (currentNode != null) {					
				currentNode.addEventListener(NodeUpdatedEvent.NODE_UPDATED, nodeUpdatedHandler);					
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
			var propertyDescriptor:PropertyDescriptor;
			
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
			properties.sort(function sortOnIndex(a:PropertyDescriptor, b:PropertyDescriptor):Number {				
				if (a.orderIndex > b.orderIndex) {
					return 1;
				} else if (a.orderIndex < b.orderIndex) {
					return -1;
				}
				return 0;
			});
			
			return properties;
		}
		
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
		
		private function nodeUpdatedHandler(event:NodeUpdatedEvent):void {
			var node:Node = event.node;
			
			for (var i:int = 0; i < propertiesForm.numElements; i++) {
				var obj:Object = propertiesForm.getElementAt(i);
				if (!(obj is PropertyLineRenderer)) {
					continue;
				}
				var propertyItemRenderer:PropertyLineRenderer = PropertyLineRenderer(obj);
				if (node.properties == null || !NodeControllerUtils.hasPropertyChanged(node, propertyItemRenderer.propertyDescriptor.name, event)) {
					continue;
				}
				propertyItemRenderer.node = node;																		
			}
		}		
		
	}
}