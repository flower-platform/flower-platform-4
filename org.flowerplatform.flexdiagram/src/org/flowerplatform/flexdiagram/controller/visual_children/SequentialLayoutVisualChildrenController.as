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
package org.flowerplatform.flexdiagram.controller.visual_children {
	import mx.collections.IList;
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.IControllerProvider;
	import org.flowerplatform.flexdiagram.controller.renderer.IRendererController;
	import org.flowerplatform.flexdiagram.renderer.IVisualChildrenRefreshable;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SequentialLayoutVisualChildrenController extends ControllerBase implements IVisualChildrenController {
		public function SequentialLayoutVisualChildrenController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		protected function getVisualElementsToSkip(model:Object):int {
			return 0;
		}
		
		public function refreshVisualChildren(parentModel:Object):void {
			// log related
			var logSameModelAndRenderer:int = 0;
			var logRenderersReused:int = 0;
			var logRenderersRemoved:int = 0;
			var logRenderersAdded:int = 0;
			
			// I have preffixed the variables with "parent" and "child", to avoid making mistakes and
			// using one instead of the other. It helped!
			var parentControllerProvider:IControllerProvider = diagramShell.getControllerProvider(parentModel);
			var parentRenderer:IVisualElementContainer = IVisualElementContainer(parentControllerProvider.getModelExtraInfoController(parentModel).getRenderer(diagramShell.modelToExtraInfoMap[parentModel]));
			
			if (!IVisualChildrenRefreshable(parentRenderer).shouldRefreshVisualChildren) {
				return;
			}
			
			var visualElementsToSkip:int = getVisualElementsToSkip(parentModel);
			var children:IList = parentControllerProvider.getModelChildrenController(parentModel).getChildren(parentModel);
			
			for (var i:int = 0; i < children.length; i++) {
				var childRendererCandidate:IVisualElement = null;
				var childModel:Object = children.getItemAt(i);
				var childControllerProvider:IControllerProvider = diagramShell.getControllerProvider(childModel);
				var childRendererController:IRendererController = childControllerProvider.getRendererController(childModel);
				
				diagramShell.addInModelMapIfNecesssary(childModel, childControllerProvider);
				
				if (i + visualElementsToSkip < parentRenderer.numElements) {
					// we still have renderer candidates
					childRendererCandidate = parentRenderer.getElementAt(i + visualElementsToSkip); 
					var modelRendererCandidate:Object = IDataRenderer(childRendererCandidate).data;
					if (modelRendererCandidate == childModel) {
						// nothing to do, so skip
						logSameModelAndRenderer++;
						continue;
					} else {
						if (modelRendererCandidate != null) {
							// the model is null if the entire parent renderer is reused. So it has several child renderers
							// that don't have any model
							diagramShell.unassociateModelFromRenderer(modelRendererCandidate, childRendererCandidate, true);
						}
						if (Object(childRendererCandidate).constructor != childRendererController.geUniqueKeyForRendererToRecycle(modelRendererCandidate)) {
							// the candidate renderer are not compatible => remove it
							// Remark: we don't have the renderer controller of the renderer candidate, because we don't know it's model.
							// Hence, the constraint that geUniqueKeyForRendererToRecycle SHOULD return the renderer class as key
							parentRenderer.removeElementAt(i + visualElementsToSkip);
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
					childRendererCandidate = childRendererController.createRenderer(childModel);
					parentRenderer.addElementAt(childRendererCandidate, i + visualElementsToSkip);
					logRenderersAdded++;
				}
				
				// TODO CC: temporary code
				// special case: the childModel already has a renderer associated
				// in while (line 115), the old renderer will be unassoc and the model deleted from diagramShell map
				// -> problems
				if (diagramShell.getRendererForModel(childModel) != null) {
					diagramShell.unassociateModelFromRenderer(childModel, diagramShell.getRendererForModel(childModel), false);
				}
				diagramShell.associateModelToRenderer(childModel, childRendererCandidate, childControllerProvider);
			}
			
			// this loop happens if the number of models < number of renderers in the (probably recycled) parent renderer
			while (parentRenderer.numElements > children.length + visualElementsToSkip) {
				childRendererCandidate = parentRenderer.getElementAt(parentRenderer.numElements - 1);
				diagramShell.unassociateModelFromRenderer(IDataRenderer(childRendererCandidate).data, childRendererCandidate, true);
				parentRenderer.removeElementAt(parentRenderer.numElements - 1);
				logRenderersRemoved++;
			}

			IVisualChildrenRefreshable(parentRenderer).shouldRefreshVisualChildren = false;
			trace("SeqLayout.refrVC(): sameModelAndRenderer=" + logSameModelAndRenderer + ",renderersReused=" + logRenderersReused + ",renderersRemoved=" + logRenderersRemoved + ",renderersAdded=" + logRenderersAdded);
		}
		
	}
}