package org.flowerplatform.flexutil.properties.ui {
	import flash.utils.Dictionary;
	
	import mx.core.IVisualElement;
	import mx.events.PropertyChangeEvent;
	
	import spark.components.Form;
	import spark.components.Group;
	import spark.components.Scroller;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.controller.IPropertyModelAdapter;
	import org.flowerplatform.flexutil.properties.PropertiesConstants;
	import org.flowerplatform.flexutil.properties.PropertiesHelper;
	import org.flowerplatform.flexutil.properties.property_line_renderer.IPropertyLineRenderer;
	import org.flowerplatform.flexutil.properties.property_line_renderer.PropertyLineRenderer;
	import org.flowerplatform.flexutil.properties.remote.IPropertyDescriptor;
	import org.flowerplatform.flexutil.properties.remote.PropertyDescriptor;

	/**
	 * Component can be used to display a node's properties.
	 * 
	 * <p>
	 * Use <code>refreshForm</code> to populate with data and <code>clearForm</code> to clear data.
	 * 
	 * @author Balutoiu Diana
	 * @author Cristina Constantinescu
	 */ 
	public class PropertiesComponent extends Scroller {
		
		protected var propertiesForm:Form;
		public var propertyModelAdapter:IPropertyModelAdapter;
		
		protected var currentNodeObject:Object;
		
		public function PropertiesComponent() {
			super();
		}
		
		public function refreshForm(nodeObject:Object, includeRawProperties:Boolean = false):void {
			// remove listeners from previous node				
			if (currentNodeObject != null) {
				PropertiesHelper.getInstance().propertyModelAdapter.getProperties(currentNodeObject)
					.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, nodeUpdatedHandler);
			}
			
			currentNodeObject = nodeObject;
			refreshRenderers(currentNodeObject, includeRawProperties);
			
			// add listeners to current node				
			if (currentNodeObject != null) {					
				PropertiesHelper.getInstance().propertyModelAdapter.getProperties(currentNodeObject)
					.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, nodeUpdatedHandler);					
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
		
		private function getPropertiesToDisplay(nodeObject:Object, includeRawProperties:Boolean = false):Array {
			var properties:Array = new Array();
			var categories:Dictionary = new Dictionary();
			var propertyDescriptor:IPropertyDescriptor;
			var nodeProperties:Object = PropertiesHelper.getInstance().propertyModelAdapter.getProperties(nodeObject);
			for (var key:* in nodeProperties) {
				propertyDescriptor = PropertiesHelper.getInstance().getPropertyDescriptor(nodeObject, key, includeRawProperties);
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
		private function refreshRenderers(nodeObject:Object, includeRawProperties:Boolean = false):void {
			if (nodeObject == null) {
				return;
			}								
			var propertyDescriptor:PropertyDescriptor;				
			var properties:Array = getPropertiesToDisplay(nodeObject, includeRawProperties);
			
			var updatePropertyLineRendererData:Function = function(renderer:IPropertyLineRenderer, propertyDescriptor:PropertyDescriptor, nodeObject:Object):void {
				renderer.propertyDescriptor = propertyDescriptor;
				renderer.nodeObject = nodeObject;	
			};
			
			for (var i:int = 0; i < properties.length; i++) {
				var childRendererCandidate:IPropertyLineRenderer = null;
				propertyDescriptor = PropertyDescriptor(properties[i]);
				
				if (i < propertiesForm.numElements) {
					// we still have renderer candidates
					childRendererCandidate = IPropertyLineRenderer(propertiesForm.getElementAt(i)); 
					var childPropertyDescriptorCandidate:PropertyDescriptor = IPropertyLineRenderer(childRendererCandidate).propertyDescriptor;
					if (propertyDescriptor == childPropertyDescriptorCandidate) {
						updatePropertyLineRendererData(childRendererCandidate, propertyDescriptor, nodeObject);					
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
					childRendererCandidate = FlexUtilGlobals.getInstance().propertiesHelper.getNewPropertyLineRendererInstance(propertyDescriptor.propertyLineRenderer);	
					
					updatePropertyLineRendererData(childRendererCandidate, propertyDescriptor, nodeObject);	
					propertiesForm.addElementAt(IVisualElement(childRendererCandidate), i);						
				} else {
					updatePropertyLineRendererData(childRendererCandidate, propertyDescriptor, nodeObject);					
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
					PropertyLineRenderer(obj).nodeObjectUpdated();
				}
			}
		}		
	}
}