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
package org.flowerplatform.flexdiagram.controller.visual_children {
	import mx.collections.IList;
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.renderer.RendererController;
	import org.flowerplatform.flexdiagram.renderer.IVisualChildrenRefreshable;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SequentialLayoutVisualChildrenController extends VisualChildrenController {
		
		public function SequentialLayoutVisualChildrenController(orderIndex:int = 0) {
			super(orderIndex);
		}
		
		protected function getVisualElementsToSkip(model:Object):int {
			return 0;
		}
		
		override public function refreshVisualChildren(context:DiagramShellContext, parentModel:Object):void {
			// log related
			var logSameModelAndRenderer:int = 0;
			var logRenderersReused:int = 0;
			var logRenderersRemoved:int = 0;
			var logRenderersAdded:int = 0;
			
			// I have preffixed the variables with "parent" and "child", to avoid making mistakes and
			// using one instead of the other. It helped!			
			var parentRenderer:IVisualElementContainer = IVisualElementContainer(ControllerUtils.getModelExtraInfoController(context, parentModel).getRenderer(context, context.diagramShell.modelToExtraInfoMap[parentModel]));
			
			if (!IVisualChildrenRefreshable(parentRenderer).shouldRefreshVisualChildren) {
				return;
			}
			
			var visualElementsToSkip:int = getVisualElementsToSkip(parentModel);
			var children:IList = ControllerUtils.getModelChildrenController(context, parentModel).getChildren(context, parentModel);
			
			for (var i:int = 0; i < children.length; i++) {
				var childRendererCandidate:IVisualElement = null;
				var childModel:Object = children.getItemAt(i);				
				var childRendererController:RendererController = ControllerUtils.getRendererController(context, childModel);
				
				context.diagramShell.addInModelMapIfNecesssary(context, childModel);
				
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
							context.diagramShell.unassociateModelFromRenderer(context, modelRendererCandidate, childRendererCandidate, true);
						}
						if (Object(childRendererCandidate).constructor != childRendererController.geUniqueKeyForRendererToRecycle(context, modelRendererCandidate)) {
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
					childRendererCandidate = childRendererController.createRenderer(context, childModel);
					parentRenderer.addElementAt(childRendererCandidate, i + visualElementsToSkip);
					logRenderersAdded++;
				}
				
				// TODO CC: temporary code
				// special case: the childModel already has a renderer associated
				// in while (line 115), the old renderer will be unassoc and the model deleted from diagramShell map
				// -> problems
				var renderer:IVisualElement = context.diagramShell.getRendererForModel(context, childModel);
				if (renderer != null) {
					context.diagramShell.unassociateModelFromRenderer(context, childModel, renderer, false);
				}
				context.diagramShell.associateModelToRenderer(context, childModel, childRendererCandidate);
			}
			
			// this loop happens if the number of models < number of renderers in the (probably recycled) parent renderer
			while (parentRenderer.numElements > children.length + visualElementsToSkip) {
				childRendererCandidate = parentRenderer.getElementAt(parentRenderer.numElements - 1);
				context.diagramShell.unassociateModelFromRenderer(context, IDataRenderer(childRendererCandidate).data, childRendererCandidate, true);
				parentRenderer.removeElementAt(parentRenderer.numElements - 1);
				logRenderersRemoved++;
			}

			IVisualChildrenRefreshable(parentRenderer).shouldRefreshVisualChildren = false;
//			trace("SeqLayout.refrVC(): sameModelAndRenderer=" + logSameModelAndRenderer + ",renderersReused=" + logRenderersReused + ",renderersRemoved=" + logRenderersRemoved + ",renderersAdded=" + logRenderersAdded);
		}
		
	}
}