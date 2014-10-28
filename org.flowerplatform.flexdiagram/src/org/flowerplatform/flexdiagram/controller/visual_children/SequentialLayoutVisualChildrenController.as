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
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.FlexDiagramConstants;
	import org.flowerplatform.flexdiagram.controller.model_children.ModelChildrenController;
	import org.flowerplatform.flexdiagram.renderer.IVisualChildrenRefreshable;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SequentialLayoutVisualChildrenController extends StandAloneSequentialLayoutVisualChildrenController {
		
		public function SequentialLayoutVisualChildrenController(visualElementsToSkip:int = 0, requiredContainerClass:Class = null, orderIndex:int = 0) {
			super(visualElementsToSkip, requiredContainerClass, orderIndex);
		}
		
		override public function refreshVisualChildren(untypedContext:Object, parentRenderer:IVisualElementContainer, parentModel:Object):void {
			if (!IVisualChildrenRefreshable(parentRenderer).shouldRefreshVisualChildren) {
				return;
			}

			var context:DiagramShellContext = DiagramShellContext(untypedContext);
			var children:IList;
			if (parentModel == null) {
				children = StandAloneSequentialLayoutVisualChildrenController.EMPTY_LIST;
			} else {
				var logTsStart:Number = new Date().time;
				children = ModelChildrenController(context.diagramShell.registry.getSingleController(FlexDiagramConstants.MODEL_CHILDREN_CONTROLLER, parentModel)).getChildren(context, parentModel);
//				trace("SeqLayout.refrVC().getChildren(): " + (new Date().time - logTsStart) + " ms");
			}
			refreshVisualChildrenDiagramOrStandAlone(context, context.diagramShell.registry, parentRenderer, parentModel, children);
		
			IVisualChildrenRefreshable(parentRenderer).shouldRefreshVisualChildren = false;
		}
		
		override protected function delegateToDiagramShell_addInModelMapIfNecesssary(untypedContext:Object, childModel:Object):void {
			var context:DiagramShellContext = DiagramShellContext(untypedContext);
			context.diagramShell.addInModelMapIfNecesssary(context, childModel);
		}
		
		override protected function delegateToDiagramShell_getRendererForModel(untypedContext:Object, childModel:Object):IVisualElement {
			var context:DiagramShellContext = DiagramShellContext(untypedContext);
			return context.diagramShell.getRendererForModel(context, childModel);
		}
		
		override protected function delegateToDiagramShell_associateModelToRenderer(untypedContext:Object, childModel:Object, childRendererCandidate:IVisualElement):void {
			var context:DiagramShellContext = DiagramShellContext(untypedContext);
			context.diagramShell.associateModelToRenderer(context, childModel, childRendererCandidate);
		}
		
		override protected function delegateToDiagramShell_unassociateModelFromRenderer(untypedContext:Object, childModel:Object, childRendererCandidate:IVisualElement, modelIsDisposed:Boolean):void {
			var context:DiagramShellContext = DiagramShellContext(untypedContext);
			context.diagramShell.unassociateModelFromRenderer(context, childModel, childRendererCandidate, modelIsDisposed);
		}
		
	}
}