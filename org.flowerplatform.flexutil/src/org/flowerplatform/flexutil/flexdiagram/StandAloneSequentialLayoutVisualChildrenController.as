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
package org.flowerplatform.flexutil.flexdiagram {
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	
	/**
	 * The reason for which these classes exist in this project, is that this class is used by the properties
	 * mechansim, i.e. <code>PropertiesForm</code>. We didn't want a) to force people to have the flexdiagram lib
	 * if they only wanted the properties and b) to spread the properties implementation into 2 libs. 
	 * 
	 * <p>
	 * The <code>createOrHideContainer()</code> logic was done for nodes that would hold an embeded property editor.
	 * In this case, the container would be a subclass of <code>Form</code>. However, this is very slow. For one element
	 * it's OK; but starting for 5-6, there is a visible lag due to component instatiation/add. That's why, we don't use 
	 * this in production, and the mechanism may not be 100% finished (e.g. we would need another mechanism to the 
	 * "visualElementsToSkip", because the number of icons in a MM node can vary).
	 * However, in the Mind Map Samples, we illustrate this.
	 * 
	 * @author Cristian Spiescu
	 */
	public class StandAloneSequentialLayoutVisualChildrenController extends VisualChildrenController {
		
		protected static const EMPTY_LIST:IList = new ArrayList([]);
		
		protected static const CHILDREN_DISABLED:int = 0;
		protected static const CHILDREN_HIDE:int = 1;
		protected static const CHILDREN_SHOW:int = 2;
		
		protected var visualElementsToSkip:int = 0;
		
		protected var requiredContainerClass:Class = null;
		
		public function StandAloneSequentialLayoutVisualChildrenController(visualElementsToSkip:int = 0, requiredContainerClass:Class = null, orderIndex:int = 0) {
			super(orderIndex);
			this.visualElementsToSkip = visualElementsToSkip;
			this.requiredContainerClass = requiredContainerClass;
		}
		
		protected function showChildren(context:Object, parentRenderer:IVisualElementContainer, parentModel:Object):int {
			return CHILDREN_SHOW;
		}
		
		protected function createOrHideContainer(context:Object, parentRenderer:IVisualElementContainer, parentModel:Object, showChildren:int):IVisualElementContainer {
			if (requiredContainerClass == null) {
				if (CHILDREN_HIDE == showChildren) {
					return null;
				} else {
					return parentRenderer;
				}
			} else {
				var container:IVisualElementContainer = null;
				if (visualElementsToSkip < parentRenderer.numElements) {
					// i.e. container exists
					container = IVisualElementContainer(parentRenderer.getElementAt(visualElementsToSkip));
					if (CHILDREN_HIDE == showChildren|| Object(container).constructor != requiredContainerClass) {
						// different classes; probably due to recyclation
						parentRenderer.removeElementAt(visualElementsToSkip);
						container = null;
					}
				}
				// at this point, either null or == requriedContainerClass
				if (container == null && CHILDREN_SHOW == showChildren) {
					container = new requiredContainerClass();
					parentRenderer.addElementAt(IVisualElement(container), visualElementsToSkip);
				}
				return container;
			}
		}
		
		protected function getRendererController(typeDescriptorRegistry:TypeDescriptorRegistry, childModel:Object):RendererController {
			throw new Error("This method should be implemented");
		}
		
		protected function delegateToDiagramShell_addInModelMapIfNecesssary(untypedContext:Object, childModel:Object):void {
		}
		
		protected function delegateToDiagramShell_getRendererForModel(untypedContext:Object, childModel:Object):IVisualElement {
			return null;
		}
		
		protected function delegateToDiagramShell_associateModelToRenderer(untypedContext:Object, childModel:Object, childRendererCandidate:IVisualElement):void {
		}
		
		protected function delegateToDiagramShell_unassociateModelFromRenderer(untypedContext:Object, childModel:Object, childRendererCandidate:IVisualElement, modelIsDisposed:Boolean):void {
		}
		
		public function refreshVisualChildrenDiagramOrStandAlone(context:Object, typeDescriptorRegistry:TypeDescriptorRegistry, parentRenderer:IVisualElementContainer, parentModel:Object, children:IList):void {
			var showChildren:int = showChildren(context, parentRenderer, parentModel);
			if (CHILDREN_DISABLED == showChildren) {
				return;
			}
			
			// log related
			var logTsStart:Number = new Date().time;
			var logSameModelAndRenderer:int = 0;
			var logRenderersReused:int = 0;
			var logRenderersRemoved:int = 0;
			var logRenderersAdded:int = 0;
			var logTotalTimeForCreation:Number = 0;
			var logTotalTimeForAdd:Number = 0;
			
			// I have preffixed the variables with "parent" and "child", to avoid making mistakes and
			// using one instead of the other. It helped!			
			
			var parentContainer:IVisualElementContainer = createOrHideContainer(context, parentRenderer, parentModel, showChildren);
			if (parentContainer == null) {
				if (requiredContainerClass == null) {
					// run the algorithm until the end; there may be children (but the controller doesn't want to show them, i.e. show = false)
					children = EMPTY_LIST;
					parentContainer = parentRenderer;
				} else {
					return;
				}
			}
			
			// if there is a container, this value has already been taken into account
			var visualElementsToSkip:int = requiredContainerClass == null ? this.visualElementsToSkip : 0;
			
			for (var i:int = 0; i < children.length; i++) {
				var childRendererCandidate:IVisualElement = null;
				var childModel:Object = children.getItemAt(i);				
				var childRendererController:RendererController = getRendererController(typeDescriptorRegistry, childModel);
				
				if (context != null) {
					delegateToDiagramShell_addInModelMapIfNecesssary(context, childModel);
				}
				
				if (i + visualElementsToSkip < parentContainer.numElements) {
					// we still have renderer candidates
					childRendererCandidate = parentContainer.getElementAt(i + visualElementsToSkip); 
					var modelRendererCandidate:Object = IDataRenderer(childRendererCandidate).data;
					if (modelRendererCandidate == childModel) {
						// nothing to do, so skip
						logSameModelAndRenderer++;
						continue;
					} else {
						if (modelRendererCandidate != null && context != null) {
							// the model is null if the entire parent renderer is reused. So it has several child renderers
							// that don't have any model
							delegateToDiagramShell_unassociateModelFromRenderer(context, modelRendererCandidate, childRendererCandidate, true);
						}
						if (Object(childRendererCandidate).constructor != childRendererController.getUniqueKeyForRendererToRecycle(context, modelRendererCandidate)) {
							// the candidate renderer are not compatible => remove it
							// Remark: we don't have the renderer controller of the renderer candidate, because we don't know it's model.
							// Hence, the constraint that geUniqueKeyForRendererToRecycle SHOULD return the renderer class as key
							parentContainer.removeElementAt(i + visualElementsToSkip);
							childRendererCandidate = null; // i.e. instruct the code below to create
							logRenderersRemoved++;
						} else {
							// a valid candidate
							logRenderersReused++;
						}
					}
				} 
				
				if (childRendererCandidate == null) {
					// i.e. either we have reached numElements,
					// or the candidate was not compatible/recyclable so it was removed
					var tsStart:Number = new Date().time;
					childRendererCandidate = childRendererController.createRenderer(context, childModel);
					var tsNow:Number = new Date().time;
					logTotalTimeForCreation += tsNow - tsStart;
					
					tsStart = tsNow;
					parentContainer.addElementAt(childRendererCandidate, i + visualElementsToSkip);
					tsNow = new Date().time;
					logTotalTimeForAdd += tsNow - tsStart;
					
					logRenderersAdded++;
				}
				
				if (context != null) {
					// TODO CC: temporary code
					// special case: the childModel already has a renderer associated
					// in while (line 115), the old renderer will be unassoc and the model deleted from diagramShell map
					// -> problems
					var renderer:IVisualElement = delegateToDiagramShell_getRendererForModel(context, childModel);
					if (renderer != null) {
						delegateToDiagramShell_unassociateModelFromRenderer(context, childModel, renderer, false);
					}
					delegateToDiagramShell_associateModelToRenderer(context, childModel, childRendererCandidate);
				} else {
					// i.e. not within diagram; stand alone mode
					IDataRenderer(childRendererCandidate).data = childModel;
				}
			}
			
			// this loop happens if the number of models < number of renderers in the (probably recycled) parent renderer
			while (parentContainer.numElements > children.length + visualElementsToSkip) {
				childRendererCandidate = parentContainer.getElementAt(parentContainer.numElements - 1);
				if (context != null) {
					delegateToDiagramShell_unassociateModelFromRenderer(context, IDataRenderer(childRendererCandidate).data, childRendererCandidate, true);
				} else {
					// i.e. not within diagram; stand alone mode
					IDataRenderer(childRendererCandidate).data = null;
				}
				parentContainer.removeElementAt(parentContainer.numElements - 1);
				logRenderersRemoved++;
			}

//			trace("SeqLayout.refrVC(): " + (new Date().time - logTsStart) + " ms, + sameModelAndRenderer=" + logSameModelAndRenderer + ",renderersReused=" + logRenderersReused + ",renderersRemoved=" + logRenderersRemoved + ",renderersAdded=" + logRenderersAdded
//				+ ",totalTimeForCreation=" + logTotalTimeForCreation + ",totalTimeForAdd=" + logTotalTimeForAdd);
		}
		
	}
}