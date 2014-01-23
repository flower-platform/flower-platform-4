/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.flexdiagram {
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.ui.Multitouch;
	import flash.ui.MultitouchInputMode;
	import flash.utils.Dictionary;
	
	import mx.collections.IList;
	import mx.core.IDataRenderer;
	import mx.core.IInvalidating;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.core.UIComponent;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	import mx.events.MoveEvent;
	import mx.events.PropertyChangeEvent;
	import mx.events.ResizeEvent;
	
	import org.flowerplatform.flexdiagram.controller.IControllerProvider;
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.renderer.IRendererController;
	import org.flowerplatform.flexdiagram.controller.selection.ISelectionController;
	import org.flowerplatform.flexdiagram.event.UpdateConnectionEndsEvent;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	import org.flowerplatform.flexdiagram.renderer.IVisualChildrenRefreshable;
	import org.flowerplatform.flexdiagram.tool.IWakeUpableTool;
	import org.flowerplatform.flexdiagram.tool.Tool;
	import org.flowerplatform.flexdiagram.tool.WakeUpTool;
	import org.flowerplatform.flexdiagram.util.ParentAwareArrayList;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class DiagramShell extends EventDispatcher {
		
		private var _modelToExtraInfoMap:Dictionary = new Dictionary();
		
		private var _rootModel:Object;
		
		private var _diagramRenderer:IVisualElementContainer;

		private var _mainSelectedItem:Object;
		private var _selectedItems:ParentAwareArrayList = new ParentAwareArrayList(null);
		
		private var _defaultTool:Tool;
		
		private var _mainTool:Tool;
		
		private var _tools:Dictionary = new Dictionary();
		
		private var _toolsActivated:Boolean = false;
		
		public function get modelToExtraInfoMap():Dictionary {
			return _modelToExtraInfoMap;
		}
		
		public function get rootModel():Object {
			return _rootModel;
		}
		
		public function set rootModel(value:Object):void {
			if (rootModel != null) {			
				unassociateModelFromRenderer(rootModel, IVisualElement(diagramRenderer), true);
			}
			_rootModel = value;
			if (rootModel != null) {
				addInModelMapIfNecesssary(rootModel);
				associateModelToRenderer(rootModel, IVisualElement(diagramRenderer), null);				
			}
		}
		
		public function get diagramRenderer():IVisualElementContainer {
			return _diagramRenderer;
		}
		
		public function set diagramRenderer(value:IVisualElementContainer):void {
			if (_diagramRenderer != null) {			
				deactivateTools();
			}
			_diagramRenderer = value;
			if (_diagramRenderer != null) {
				UIComponent(_diagramRenderer).callLater(activateTools);	
			}			
		}
		
		public function get selectedItems():ParentAwareArrayList {
			return _selectedItems;
		}
		
		public function get tools():Dictionary {
			return _tools;
		}
		
		public function set tools(array:Dictionary):void {
			_tools = array;
		}
		
		public function set mainSelectedItem(value:Object):void {
			if (_mainSelectedItem != value) {			
				if (_mainSelectedItem != null &&
					getControllerProvider(_mainSelectedItem).getSelectionController(_mainSelectedItem) != null) {				
					getControllerProvider(_mainSelectedItem).getSelectionController(_mainSelectedItem).
						setSelectedState(_mainSelectedItem, getRendererForModel(_mainSelectedItem), true, false);
				}
				// mark the new main selection 
				_mainSelectedItem = value;
							
				if (_mainSelectedItem != null &&
					getControllerProvider(_mainSelectedItem).getSelectionController(_mainSelectedItem) != null) {
					getControllerProvider(_mainSelectedItem).getSelectionController(_mainSelectedItem).
						setSelectedState(_mainSelectedItem, getRendererForModel(_mainSelectedItem), true, true);			
				}
			}
		}
		
		public function get mainSelectedItem():Object {			
			return _mainSelectedItem;
		}
				
		[Bindable]
		public function get mainTool():Tool {			
			return _mainTool;
		}
		
		public function set mainTool(value:Tool):void {
			if (_mainTool != value) {
				if (_mainTool != null) {
					_mainTool.deactivateAsMainTool();					
				}
				
				_mainTool = value;
				
				if (mainTool != null) {
					_mainTool.activateAsMainTool();					
				}
			}
		}
		
		public function mainToolFinishedItsJob():void {
			mainTool = _defaultTool;
		}
		
		public function DiagramShell() {
			selectedItems.addEventListener(CollectionEvent.COLLECTION_CHANGE, selectionChangeHandler);
			
			var wakeUpTool:WakeUpTool = new WakeUpTool(this);
			_defaultTool = wakeUpTool;
			tools[WakeUpTool] = wakeUpTool;
		
			Multitouch.inputMode = MultitouchInputMode.GESTURE;
		}
		
		public function registerTools(toolClasses:Array):void {
			for (var i:int=0; i < toolClasses.length; i++) {
				tools[toolClasses[i]] = new toolClasses[i](this);
			}
		}
		
		public function getControllerProvider(model:Object):IControllerProvider {
			throw new Error("Should be implemented by subclass");
		}
		
		public function addInModelMapIfNecesssary(model:Object, controllerProvider:IControllerProvider = null):Boolean {
			if (controllerProvider == null) {
				controllerProvider = getControllerProvider(model);
			}
			if (modelToExtraInfoMap[model] == null && controllerProvider.getModelExtraInfoController(model) != null) {
				var extraInfo:Object = controllerProvider.getModelExtraInfoController(model).createExtraInfo(model);
				modelToExtraInfoMap[model] = extraInfo;
				return true;
			}
			return false;
		}
		
		public function associateModelToRenderer(model:Object, renderer:IVisualElement, controllerProvider:IControllerProvider = null):void {
			if (controllerProvider == null) {
				controllerProvider = getControllerProvider(model);
			}
			
			// update the renderer in model map
			if (controllerProvider.getModelExtraInfoController(model) != null) {
				controllerProvider.getModelExtraInfoController(model).setRenderer(model, modelToExtraInfoMap[model], renderer);
			}
			
			if (renderer is IInvalidating) {
				var invalidating:IInvalidating = IInvalidating(renderer);
				invalidating.invalidateDisplayList();
				invalidating.invalidateProperties();
				invalidating.invalidateSize();
			}
			if (renderer is IVisualChildrenRefreshable) {
				IVisualChildrenRefreshable(renderer).shouldRefreshVisualChildren = true;
			}
			if (renderer is IDiagramShellAware) {
				IDiagramShellAware(renderer).diagramShell = this;
			}

			IDataRenderer(renderer).data = model;
			
			var rendererController:IRendererController = controllerProvider.getRendererController(model);
			if (rendererController != null) {
				// is null just for the diagram
				rendererController.associatedModelToRenderer(model, renderer);
			}
			
			var modelChildrenController:IModelChildrenController = controllerProvider.getModelChildrenController(model);
			if (modelChildrenController != null) {
				// "leaf" models don't have children, i.e. no provider
				modelChildrenController.beginListeningForChanges(model);
			}
			
			var selectionController:ISelectionController = controllerProvider.getSelectionController(model);
			if (selectionController != null) {
				selectionController.associatedModelToSelectionRenderer(model, renderer);
			}
			
			// connection related
			if (renderer != diagramRenderer) {
				if (model is IEventDispatcher) {
					IEventDispatcher(model).dispatchEvent(new UpdateConnectionEndsEvent());
				}
				renderer.addEventListener(ResizeEvent.RESIZE, moveResizeHandler);
				renderer.addEventListener(MoveEvent.MOVE, moveResizeHandler);
			}
		}
		
		public function unassociateModelFromRenderer(model:Object, renderer:IVisualElement, modelIsDisposed:Boolean, controllerProvider:IControllerProvider = null):void {
			if (model == null) {
				return;
			}
			
			if (controllerProvider == null) {
				controllerProvider = getControllerProvider(model);
			}
			
			var modelChildrenController:IModelChildrenController = controllerProvider.getModelChildrenController(model);
			if (modelChildrenController != null) {
				var children:IList = modelChildrenController.getChildren(model);
				for (var i:int = 0; i < children.length; i++) {
					var childModel:Object = children.getItemAt(i);
					unassociateModelFromRenderer(childModel, getRendererForModel(childModel), modelIsDisposed);
				}
				// "leaf" models don't have children, i.e. no provider
				modelChildrenController.endListeningForChanges(model);
			}
		
			// update the renderer in model map
			if (controllerProvider.getModelExtraInfoController(model) != null) {
				controllerProvider.getModelExtraInfoController(model).setRenderer(model, modelToExtraInfoMap[model], null);
			}
			
			if (renderer != null) {
				IDataRenderer(renderer).data = null;
				if (renderer is IDiagramShellAware) {
					IDiagramShellAware(renderer).diagramShell = null;
				}
				// connection related
				if (renderer != diagramRenderer) {
					renderer.removeEventListener(ResizeEvent.RESIZE, moveResizeHandler);
					renderer.removeEventListener(MoveEvent.MOVE, moveResizeHandler);
				}
			}

			var rendererController:IRendererController = controllerProvider.getRendererController(model);
			if (rendererController != null) {
				// is null just for the diagram
				rendererController.unassociatedModelFromRenderer(model, renderer, modelIsDisposed);
			}
						
			var selectionController:ISelectionController = controllerProvider.getSelectionController(model);
			if (selectionController != null) {
				selectionController.unassociatedModelFromSelectionRenderer(model, renderer);
			}
			
			if (modelIsDisposed) {
				delete modelToExtraInfoMap[model];
			}
		}
		
		protected function moveResizeHandler(event:Event, model:Object = null):void {
			if (model == null) {
				model = IEventDispatcher(event.target.data);
			}
			model.dispatchEvent(new UpdateConnectionEndsEvent());
			
			var controller:IModelChildrenController = getControllerProvider(model).getModelChildrenController(model);
			if (controller == null) {
				return;
			}
			// TODO CS/FP2 de vazut daca nu se trimit prea multe evenimente. de controlat cu trace. E.g. la inceput? Sa facem un sistem de flag, ca la updateDispList?
			// sigur e o problema! vad ca se intra de multe ori pe aici, si in CoReCo.getEstimate....: cam la orice operatiune. De asemenea, la display de diag initial,
			// linia e un pic decalata
			// UPDATE: am rezolvat un pic, caci se trimitea de multe ori pentru ca era inregitrat si pentru diagrama
			var children:IList = controller.getChildren(model);
			if (children != null) {
				for (var i:int = 0; i < children.length; i++) {
					moveResizeHandler(null, children.getItemAt(i));
				}
			}
		}
		
		public function getRendererForModel(model:Object):IVisualElement {
			if (getControllerProvider(model).getModelExtraInfoController(model) != null) {
				return getControllerProvider(model).getModelExtraInfoController(model).getRenderer(modelToExtraInfoMap[model]);
			}
			return null;
		}
		
		public function shouldRefreshVisualChildren(model:Object):void {
			var renderer:IVisualElement = getRendererForModel(model);
			if (renderer == null) {
				return;
			}
			
			if (renderer is IInvalidating) {
				var invalidating:IInvalidating = IInvalidating(renderer);
				invalidating.invalidateDisplayList();
				invalidating.invalidateProperties();
				invalidating.invalidateSize();
			}
			if (renderer is IVisualChildrenRefreshable) {
				IVisualChildrenRefreshable(renderer).shouldRefreshVisualChildren = true;
			}
		}
		
		/**
		 * @author Cristina Constantinescu
		 * @author Cristian Spiescu
		 */
		private function selectionChangeHandler(event:CollectionEvent):void {
			if (event.kind == CollectionEventKind.RESET) {
				// we don't need to react to this, because our overriden list (ParentAwareArrayList) dispatches
				// a removed event for all children. I.e. this method will be called shortly after, for each
				// element that was previously there
				return;
			}
			var model:Object = event.items[0];
			if (model is PropertyChangeEvent) {
				model = PropertyChangeEvent(model).source;
			}
			var selectionController:ISelectionController = getControllerProvider(model).getSelectionController(model);
			
			if (selectedItems.getItemIndex(mainSelectedItem) == -1) {
				// set main selected item to last item from list
				mainSelectedItem = selectedItems.length == 0 ? null : selectedItems.getItemAt(selectedItems.length  - 1);					
			}
		
			if (selectionController != null) {				
				if (event.kind == CollectionEventKind.ADD) {
					mainSelectedItem = model;
					//selectionController.setSelectedState(model, getRendererForModel(model), true, _mainSelectedItem == model);					
				} else if (event.kind == CollectionEventKind.REMOVE ||  event.kind == CollectionEventKind.REPLACE) {
					
					if (event.kind == CollectionEventKind.REPLACE) {
						model = PropertyChangeEvent(event.items[0]).oldValue;	
					}				
					selectionController.setSelectedState(model, getRendererForModel(model), false, false);
				}
			}
		}
		
		public function activateTools():void {
			// activate only if they weren't yet activated
			if (!_toolsActivated) {
				for (var key:Object in tools) {
					Tool(tools[key]).activateDozingMode();
				}	
				if (mainTool == null) {
					mainTool = _defaultTool;
				} else {
					mainTool.activateAsMainTool();
				}
				_toolsActivated = true;
			}
		}		
	
		public function deactivateTools():void {
			// deactivate only if they were activated
			if (_toolsActivated) {
				if (mainTool is IWakeUpableTool) { // return to default tool in case of a wakeupable tool
					mainTool = _defaultTool;					
				}
				mainTool.deactivateAsMainTool();
				for (var key:Object in tools) {
					Tool(tools[key]).deactivateDozingMode();
				}	
				_toolsActivated = false;
			}
		}
		
		/**
		 * @author Cristina Constantinescu
		 */  
		public function convertCoordinates(rectangle:Rectangle, fromComponent:UIComponent, toComponent:UIComponent):Rectangle {					
			var fromGlobalPoint:Point = fromComponent.contentToGlobal(rectangle.topLeft);
			
			var localPoint:Point = toComponent.globalToLocal(fromGlobalPoint);
			var contentPoint:Point = toComponent.localToContent(localPoint);
			
			return new Rectangle(
				contentPoint.x, contentPoint.y, 
				rectangle.width * toComponent.scaleX, rectangle.height * toComponent.scaleY);
		}
	}
}
