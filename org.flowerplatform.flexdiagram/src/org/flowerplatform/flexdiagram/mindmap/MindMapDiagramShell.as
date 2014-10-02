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
package org.flowerplatform.flexdiagram.mindmap {
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.DPIClassification;
	import mx.core.FlexGlobals;
	import mx.core.IInvalidating;
	import mx.core.IVisualElement;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelController;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapDiagramShell extends DiagramShell {
		
		public static const POSITION_LEFT:int = -1;
		public static const POSITION_RIGHT:int = 1;
		public static const POSITION_CENTER:int = 0;
		
		public static const HORIZONTAL_PADDING_DEFAULT:int = 20;
		public static const VERTICAL_PADDING_DEFAULT:int = 5;
		
		public static const ADDITIONAL_PADDING_DEFAULT:int = 15;
		
		public static const COORDINATES_CHANGED_EVENT:String = "coordinatesChanged";
		
		public var horizontalPadding:int = HORIZONTAL_PADDING_DEFAULT;
		public var verticalPadding:int = VERTICAL_PADDING_DEFAULT;
		
		/**
		 * Additional padding value (left/right/top/bottom) for nodes.
		 * 
		 * <p>
		 * When set in model's dynamic object, it is used to calculate model children & siblings coordinates based on it.
		 * (e.g. mindmap cloud shape)
		 */ 
		public var additionalPadding:int = ADDITIONAL_PADDING_DEFAULT;
		
		/**
		 * If <code>true</code>, the rootModel can be added as first child in rootModel's list of children.
		 * @see addRootModelAsRootNode()
		 * @see set rootModel
		 */ 
		public var showRootModelAsRootNode:Boolean = true;
		
		/**
		 * Sets the horizontal padding depending on the platform.
		 * 
		 * @author Mariana Gheorghe
		 */
		public function MindMapDiagramShell() {
			if (FlexUtilGlobals.getInstance().isMobile) {
				switch (FlexGlobals.topLevelApplication.applicationDPI) {
					case DPIClassification.DPI_320:	{
						horizontalPadding = 80;
						break;
					}
					case DPIClassification.DPI_240:	{
						horizontalPadding = 60;
						break;
					}
					default: {
						// default PPI160
						horizontalPadding = 40;
						break;
					}
				}
			}
		}
		
		/**
		 * Structure:
		 * - rootModel -> a MindMapRootModelWrapper that keeps the model and the list of model children added directly on diagram renderer
		 * - root (model from where the mindmap structure begins) -> first child in MindMapRootModelWrapper.children, it has no parent
		 */ 
		override public function set rootModel(value:Object):void {
			super.rootModel = new MindMapRootModelWrapper(value);
			
			refreshRootModelChildren(getNewDiagramShellContext());			
		}
		
		public function getRoot(context:DiagramShellContext):Object {
			if (showRootModelAsRootNode) {
				return MindMapRootModelWrapper(rootModel).model;
			}
			if (MindMapRootModelWrapper(rootModel).children != null) {
				return MindMapRootModelWrapper(rootModel).children.getItemAt(0);
			}
			return null;
		}
		
		public function getRootNodeX(context:DiagramShellContext, rootNode:Object):Number {
			return 0;
		}
		
		public function getRootNodeY(context:DiagramShellContext, rootNode:Object):Number {
			return 0;
		}
		
		public function shouldRefreshModelPositions(context:DiagramShellContext, model:Object):void {
			var renderer:IVisualElement = getRendererForModel(context, model);
			if (renderer == null) {
				return;
			}
			
			if (renderer is IInvalidating) {
				var invalidating:IInvalidating = IInvalidating(renderer);
				invalidating.invalidateDisplayList();
				invalidating.invalidateProperties();
				invalidating.invalidateSize();
			}
			MindMapDiagramRenderer(renderer).shouldRefreshModelPositions = true;			
		}
		
		public function refreshRootModelChildren(context:DiagramShellContext):void {			
			var root:Object = getRoot(context);
			if (root != null) {			
				// clear old children
				MindMapRootModelWrapper(rootModel).children = new ArrayList();
				
				// add new children
				addModelInRootModelChildrenListRecursive(context, root, true, true);	
			}
			// refresh rootModel's visual children
			shouldRefreshModelPositions(context, rootModel);
			shouldRefreshVisualChildren(context, rootModel);
		}
		
		/**
		 * @author Cristina Constantinescu
		 * @author Cristian Spiescu
		 */
		protected function addModelInRootModelChildrenList(context:DiagramShellContext, model:Object, expanded:Boolean, asRoot:Boolean = false, depth:int = 1):void {
			// set depth in model's dynamic object -> it will be used further, in renderer
			setPropertyValue(context, model, "depth", depth);
			
			var wrapper:MindMapRootModelWrapper = MindMapRootModelWrapper(rootModel);
			if (wrapper.children == null) {
				wrapper.children = new ArrayList();
			}
			if (asRoot) {
				// TODO CS/MM: needed because MMDS.getRoot() with showRootModelAsRootNode = false
				// I think we should remove this flag (and this check)
				wrapper.children.addItemAt(model, 0);
			} else {
				wrapper.children.addItem(model);
			}
			
			var dynamicObject:Object = getDynamicObject(context, model);
			var multiConnectorModel:MultiConnectorModel = addOrRemoveMultiConnectorModel(context, wrapper, model, dynamicObject, "multiConnectorModel", expanded);
			if (asRoot) {
				if (multiConnectorModel != null) {
					// i.e. it was just created
					multiConnectorModel.isRight = true;
					multiConnectorModel.isForRoot = true;
				}
				// for root, we add 2 models
				multiConnectorModel = addOrRemoveMultiConnectorModel(context, wrapper, model, dynamicObject, "multiConnectorModelLeft", expanded);
				if (multiConnectorModel != null) {
					// i.e. it was just created
					multiConnectorModel.isRight = false;
					multiConnectorModel.isForRoot = true;
				}
			}
		}
		
		/**
		 * If expanded: creates (or recycles from dynamic object) the corresponding <code>MultiConnectorModel</code>, that corresponds to a <code>MultiConnectorRenderer</code>
		 * 
		 * <p>
		 * Otherwise, (i.e. collapsed) removes it (+ renderer). Actually it's not within the children list. But it's still in the dynamic object & on screen.
		 * 
		 * @author Cristian Spiescu
		 */
		protected function addOrRemoveMultiConnectorModel(context:DiagramShellContext, wrapper:MindMapRootModelWrapper, model:Object, dynamicObject:Object, dynamicObjectProperty:String, expanded:Boolean):MultiConnectorModel {
			var multiConnectorModel:MultiConnectorModel = null;
			var result:MultiConnectorModel = null;
			if (dynamicObject.hasOwnProperty(dynamicObjectProperty)) {
				multiConnectorModel = dynamicObject[dynamicObjectProperty];
			}
			
			if (expanded) {
				if (multiConnectorModel == null) {
					// i.e. not found in dynamic object => create
					multiConnectorModel = createMultiConnectorModel();
					if (multiConnectorModel != null) {
						// may be null if create... was overridden
						dynamicObject[dynamicObjectProperty] = multiConnectorModel;
						multiConnectorModel.diagramShellContext = context;
						multiConnectorModel.source = model;
						var side:int = getModelController(context, model).getSide(context, model);
						multiConnectorModel.isRight = side == POSITION_RIGHT;
						result = multiConnectorModel;
					}
				}
				if (multiConnectorModel != null) {
					// may be null if create... was overridden
					wrapper.children.addItem(multiConnectorModel);
				}
			} else {
				if (multiConnectorModel != null) {
					unassociateModelFromRenderer(context, multiConnectorModel, getRendererForModel(context, multiConnectorModel), true);
					// this will trigger listener removal
					multiConnectorModel.source = null;
					delete dynamicObject[dynamicObjectProperty];
				}
			}
			return result;
		}
		
		/**
		 * @author Cristian Spiescu
		 */
		protected function createMultiConnectorModel():MultiConnectorModel {
			return new MultiConnectorModel();
		}
		
		/**
		 * We don't use a type provider; otherwise the user would need to care
		 * about this, which is internal to the mind map diagram.
		 * 
		 * @author Cristian Spiescu
		 */
		override public function getType(context:DiagramShellContext, model:Object):String {
			if (model is MultiConnectorModel) {
				return MultiConnectorModel.TYPE;
			} else if (model is MindMapRootModelWrapper) {
				return MindMapRootModelWrapper.TYPE;
			} else {
				return super.getType(context, model);
			} 
		}
		
		/**
		 * @author Cristina Constantinescu
		 * @author Cristian Spiescu
		 */
		override public function unassociateModelFromRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement, modelIsDisposed:Boolean):void {
			if (modelIsDisposed && !(model is MultiConnectorModel)) {
				var dynamicObject:Object = getDynamicObject(context, model);
				if (dynamicObject.hasOwnProperty("multiConnectorModel")) {
					var multiConnectorModel:MultiConnectorModel = dynamicObject["multiConnectorModel"];
					unassociateModelFromRenderer(context, multiConnectorModel, getRendererForModel(context, multiConnectorModel), true);
				}
			}
			super.unassociateModelFromRenderer(context, model, renderer, modelIsDisposed);
		}
		
		
		protected function addModelInRootModelChildrenListRecursive(context:DiagramShellContext, model:Object, expanded:Boolean, asRoot:Boolean = false, depth:int = 1):void {			
			var expanded:Boolean = getModelController(context, model).getExpanded(context, model);
			if (expanded) {
				var children:IList = getModelController(context, model).getChildren(context, model);
				for (var i:int = 0; i < children.length; i++) {
					addModelInRootModelChildrenListRecursive(context, children.getItemAt(i), expanded, false, depth + 1);
				}
			}
			addModelInRootModelChildrenList(context, model, expanded, asRoot, depth);				
		}
		
		public function getModelController(context:DiagramShellContext, model:Object):MindMapModelController {
			return ControllerUtils.getMindMapModelController(context, model);
		}
		
		private function getInitialPropertyValue(context:DiagramShellContext, model:Object, property:String):Object {
			switch (property) {								
				case "width":
					return 10;	// minWidth
				case "height":
				case "oldHeight":
					if (FlexUtilGlobals.getInstance().isMobile) {
						switch (FlexGlobals.topLevelApplication.applicationDPI) {
							case DPIClassification.DPI_320:	{
								return 88;								
							}
							case DPIClassification.DPI_240:	{
								return 66;								
							}
							default: {
								// default PPI160
								return 44;								
							}
						}
					}
					return 22;	// minHeight							
				case "expandedHeight":
					return getPropertyValue(context, model, "height");
				case "expandedWidth":
					return getPropertyValue(context, model, "width");
				default:
					return 0;
			}
		}
		
		public function getPropertyValue(context:DiagramShellContext, model:Object, property:String, dynamicObject:Object = null):Number {
			if (dynamicObject == null) {
				dynamicObject = getDynamicObject(context, model);
			}
			if (isNaN(dynamicObject[property])) {				
				dynamicObject[property] = getInitialPropertyValue(context, model, property);
			}
			return dynamicObject[property];			
		}
		
		public function setPropertyValue(context:DiagramShellContext, model:Object, property:String, value:Object, dynamicObject:Object = null):void {
			if (dynamicObject == null) {
				dynamicObject = getDynamicObject(context, model);
			}
			var oldValue:Number = dynamicObject[property];
			
			dynamicObject[property] = value;
			
			// TODO CS/MM: we should check oldValue != newValue?
			model.dispatchEvent(PropertyChangeEvent.createUpdateEvent(model, property, oldValue, value));
		}
		
		public function getChildrenBasedOnSide(context:DiagramShellContext, model:Object, side:int = 0):Array {
			if (side == 0) {
				side = getModelController(context, model).getSide(context, model);
			}
			
			var list:Array = [];	
			var children:IList = getModelController(context, model).getChildren(context, model);
			if (children != null) {
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					if (side == 0 || side == getModelController(context, child).getSide(context, child)) {
						list.push(child);
					}
				}
			}
			return list;
		}
		
		public function getDeltaBetweenExpandedHeightAndHeight(context:DiagramShellContext, model:Object, preventNegativeValues:Boolean = false, addAdditionalPaddingIfNecessary:Boolean = true):Number {
			var dynamicObject:Object = getDynamicObject(context, model);
			
			var additionalPadding:Number = getPropertyValue(context, model, "additionalPadding", dynamicObject);			
			var expandedHeight:Number = getPropertyValue(context, model, "expandedHeight", dynamicObject);
			var height:Number = getPropertyValue(context, model, "height", dynamicObject);
			
			if ((preventNegativeValues && expandedHeight < height) || expandedHeight == 0) {
				// expandedHeight is smaller than height and we don't want negative values OR expandedHeght is 0 (model isn't expanded) -> return only the additional delta
				return addAdditionalPaddingIfNecessary ? additionalPadding : 0;
			}
			
			// expandedHeight exists (model is expanded) -> return delta between expandedHeight and height and, based on addAdditionalPaddingIfNecessary, remove additional padding
			// (used when we want to calculate the first child position)
			return expandedHeight - height - (!addAdditionalPaddingIfNecessary ? additionalPadding : 0);			
		}
	}
}