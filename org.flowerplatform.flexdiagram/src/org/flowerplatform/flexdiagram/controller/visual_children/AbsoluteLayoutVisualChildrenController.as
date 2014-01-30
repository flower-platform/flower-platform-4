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
	import flash.geom.Rectangle;
	import flash.utils.Dictionary;
	
	import mx.collections.IList;
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.IAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.controller.IControllerProvider;
	import org.flowerplatform.flexdiagram.controller.renderer.IRendererController;
	import org.flowerplatform.flexdiagram.event.ZoomPerformedEvent;
	import org.flowerplatform.flexdiagram.renderer.IAbsoluteLayoutRenderer;
	import org.flowerplatform.flexdiagram.renderer.IVisualChildrenRefreshable;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class AbsoluteLayoutVisualChildrenController extends ControllerBase implements IVisualChildrenController {
		
		public var preferredMaxNumberOfRenderers:int = 150;
		
		public function AbsoluteLayoutVisualChildrenController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		// TODO the commented code was code that handled the order (i.e. depth), in the previous FD implementation/Flex 3
		public function refreshVisualChildren(parentModel:Object):void {			
			// log related
			var logTsStart:Number = new Date().time;
			var logNewModels:int = 0;
			var logRenderersReused:int = 0;
			var logReusableRenderersCreated:int = 0;
			var logNonReusableRenderersCreated:int = 0;
			var logReusableRenderersRemoved:int = 0;
			
			// I have preffixed the variables with "parent" and "child", to avoid making mistakes and
			// using one instead of the other. It helped!
			var parentControllerProvider:IControllerProvider = diagramShell.getControllerProvider(parentModel);
			var parentRenderer:IVisualElementContainer = IVisualElementContainer(parentControllerProvider.getModelExtraInfoController(parentModel).getRenderer(diagramShell.modelToExtraInfoMap[parentModel]));

			var scrollRect:Rectangle = IAbsoluteLayoutRenderer(parentRenderer).getViewportRect();
			var noNeedToRefreshRect:Rectangle = IAbsoluteLayoutRenderer(parentRenderer).noNeedToRefreshRect;
			if (!IVisualChildrenRefreshable(parentRenderer).shouldRefreshVisualChildren
				&& noNeedToRefreshRect != null && noNeedToRefreshRect.containsRect(scrollRect)) {
				return;
			}
			
			// holds the renderers to reuse
			// key: provided by the controller, usually figure class; value: a Vector of renderers (IVisualElement)
			var renderersToReuse:Dictionary = new Dictionary();
			var modelsToAdd:Vector.<Object> = new Vector.<Object>();
			var visibleModelsCounter:int = 0;
			
			// These values are computed based on the dimensions of all children 
			// (including those that are not displayed, because out of viewable area)
			var horizontalScrollPositionMin:Number = int.MAX_VALUE;
			var horizontalScrollPositionMax:Number = int.MIN_VALUE;
			var verticalScrollPositionMin:Number = int.MAX_VALUE;
			var verticalScrollPositionMax:Number = int.MIN_VALUE;
			
			// These values are computed based on the children that are not visible
			var horizontalNoNeedToRefreshLeft:int = int.MIN_VALUE;
			var horizontalNoNeedToRefreshRight:int = int.MAX_VALUE;
			var verticalNoNeedToRefreshTop:int = int.MIN_VALUE;
			var verticalNoNeedToRefreshBottom:int = int.MAX_VALUE;
			
//			var figuresToAdd:int = 0;
//			var visualIndex:int = 0;
			var children:IList = parentControllerProvider.getModelChildrenController(parentModel).getChildren(parentModel);
			
			for (var i:int = 0; i < children.length; i++) {
				var childModel:Object = children.getItemAt(i);
				var childControllerProvider:IControllerProvider = diagramShell.getControllerProvider(childModel);
				var childAbsoluteLayoutRectangleController:IAbsoluteLayoutRectangleController = childControllerProvider.getAbsoluteLayoutRectangleController(childModel);
				var childRendererController:IRendererController = childControllerProvider.getRendererController(childModel);
				var childRenderer:IVisualElement = childControllerProvider.getModelExtraInfoController(childModel).getRenderer(diagramShell.modelToExtraInfoMap[childModel]);

				var childModelIsNew:Boolean = diagramShell.addInModelMapIfNecesssary(childModel, childControllerProvider);
				if (childModelIsNew) {
					logNewModels++;
				}
				
				if (childAbsoluteLayoutRectangleController != null) {
					// a child that participates to renderer recycling logic
					var crtRect:Rectangle = childAbsoluteLayoutRectangleController.getBounds(childModel);
					
					// updates the new scroll bounds, based on the dimensions of the current child
					if (crtRect.x + crtRect.width > horizontalScrollPositionMax) {
						horizontalScrollPositionMax = crtRect.x + crtRect.width;
					}
					if (crtRect.x < horizontalScrollPositionMin) {
						horizontalScrollPositionMin = crtRect.x;
					}
					if (crtRect.y + crtRect.height > verticalScrollPositionMax) {
						verticalScrollPositionMax = crtRect.y + crtRect.height;
					}
					if (crtRect.y < verticalScrollPositionMin) {
						verticalScrollPositionMin = crtRect.y;
					}

					if (scrollRect.intersects(crtRect)) {
						// the model should be visible
						visibleModelsCounter++;
						
						if (childRenderer == null) { 
							// the model should be visible and it is not
							modelsToAdd.push(childModel);
//							var entry:EditPartToAddEntry = new EditPartToAddEntry();
//							entry.editPart = ep;
//							entry.visualIndex = visualIndex;
//							entry.correctionStartingWithMe = 0;
//							figuresToAdd++;
						} else {
							// TODO la ce o trebui asta?
							// CC: am observat ca erau folosite cand se facea zoom (figurile cu label prea mare erau trunchiate)
							// model is visible and still has a figure => position the figure correctly
							notifyVisualChildren(UIComponent(childRenderer));
//							
//							AbsolutePositionEditPartUtils.setChildFigureIndex(IVisualElementContainer(getFigure()), IVisualElement(ep.getFigure()), visualIndex - figuresToAdd);
						}
//						visualIndex ++;
					} else {
						// the model is not in the viewable area;
						
						// update the "no scroll rect"
						if (crtRect.right <= scrollRect.left && crtRect.right > horizontalNoNeedToRefreshLeft) {
							horizontalNoNeedToRefreshLeft = crtRect.right;
						}
						if (crtRect.left >= scrollRect.right && crtRect.left < horizontalNoNeedToRefreshRight) {
							horizontalNoNeedToRefreshRight = crtRect.left;
						}
						if (crtRect.bottom <= scrollRect.top && crtRect.bottom > verticalNoNeedToRefreshTop) {
							verticalNoNeedToRefreshTop = crtRect.bottom;
						}
						if (crtRect.top >= scrollRect.bottom && crtRect.top < verticalNoNeedToRefreshBottom) {
							verticalNoNeedToRefreshBottom = crtRect.top;
						}
						
						if (childRenderer != null) {
							// the model may not be visible (and it is currently) => the renderer is reusable
							var renderersToRemove:Vector.<IVisualElement> = renderersToReuse[childRendererController.geUniqueKeyForRendererToRecycle(childModel)];
							// lazy init the collection
							if (renderersToRemove == null) {
								renderersToRemove = new Vector.<IVisualElement>();
								renderersToReuse[childRendererController.geUniqueKeyForRendererToRecycle(childModel)] = renderersToRemove;
							}
							renderersToRemove.push(childRenderer);
						
//							var figuretoReuseEntry:FigureToReuseEntry = new FigureToReuseEntry();
//						
//							figuretoReuseEntry.figure = ep.getFigure();
//							figuretoReuseEntry.visualIndex = visualIndex;
//							figuretoReuseEntry.nextEditPartToAddIndex = figuresToAdd;
//							if (childrenChangedFlag) {
//								AbsolutePositionEditPartUtils.setChildFigureIndex(IVisualElementContainer(getFigure()), IVisualElement(ep.getFigure()), visualIndex - figuresToAdd);
//							}
//							visualIndex ++;	
						} else if (childModelIsNew) {
							// we are dealing with a freshly added model, which is not visible. Give a chance to
							// the controller to start listening for position change
							diagramShell.unassociateModelFromRenderer(childModel, null, false, childControllerProvider);
						}
					}
				} else {
					// no IAbsoluteLayoutRectangleController available =>
					// it doesn't participate to renderer recycling logic
					if (childRenderer == null && childRendererController != null) {
						childRenderer = childRendererController.createRenderer(childModel);
						parentRenderer.addElement(childRenderer);
						diagramShell.associateModelToRenderer(childModel, childRenderer, childControllerProvider);
						logNonReusableRenderersCreated++;
					}
//						AbsolutePositionEditPartUtils.addChildFigureAtIndex(IVisualElementContainer(getFigure()), IVisualElement(currentFigure), visualIndex - figuresToAdd);	
						
//					} else if (childrenChangedFlag) {
//						AbsolutePositionEditPartUtils.setChildFigureIndex(IVisualElementContainer(getFigure()), IVisualElement(ep.getFigure()), visualIndex - figuresToAdd);	
//					}
//					
//					visualIndex ++;
				}
			}

			if (children.length == 0) {
				horizontalScrollPositionMin = 0;
				horizontalScrollPositionMax = 0;
				verticalScrollPositionMin = 0;
				verticalScrollPositionMax = 0;
			}
			IAbsoluteLayoutRenderer(parentRenderer).setContentRect(new Rectangle(horizontalScrollPositionMin, verticalScrollPositionMin, horizontalScrollPositionMax - horizontalScrollPositionMin, verticalScrollPositionMax - verticalScrollPositionMin));
			
			var logTsModelIterationDone:Number = new Date().time;
			
//			var currentCorrection:int = 0;
			// at this step we have renderersToReuse and modelsToDisplay populated => process the additions : 
			// for each model to add, try to reuse a renderer that is not visible any more if possible, or create a new one
			for (i = 0; i < modelsToAdd.length; i++) {				
				childModel = modelsToAdd[i];
				childControllerProvider = diagramShell.getControllerProvider(childModel);
				childRendererController = childControllerProvider.getRendererController(childModel);

				renderersToRemove = renderersToReuse[childRendererController.geUniqueKeyForRendererToRecycle(childModel)];
//				currentCorrection += entry.correctionStartingWithMe;
				
				if (renderersToRemove != null && renderersToRemove.length > 0) {
					// recycle a renderer that is ready for recycling
					childRenderer = renderersToRemove.pop();
					diagramShell.unassociateModelFromRenderer(IDataRenderer(childRenderer).data, childRenderer, false);
					logRenderersReused++;
				} else {
					// no recycling possible => create a renderer
					childRenderer = childRendererController.createRenderer(childModel);
					parentRenderer.addElement(childRenderer);
					logReusableRenderersCreated++;
//					AbsolutePositionEditPartUtils.addChildFigureAtIndex(IVisualElementContainer(getFigure()), IVisualElement(currentFigure), entry.visualIndex + currentCorrection);
				}
				diagramShell.associateModelToRenderer(childModel, childRenderer, childControllerProvider);

				
//				if (figToReuseEntry != null) { 
//					// if an existing figure has been reused
//					
//					// because the reused figure was also numbered when computing the visualIndex:
//					// if I reused a figure from my left then all the EditParts to add starting with me must correct their index with -1
//					if (figToReuseEntry.visualIndex < entry.visualIndex) {
//						currentCorrection--;
//					} else if (figToReuseEntry.nextEditPartToAddIndex < editPartsToAdd.length) {
//						// I reused a figure from my right update the correction starting with the next EditPart that
//						// follows the reused Figure 
//						EditPartToAddEntry(editPartsToAdd[figToReuseEntry.nextEditPartToAddIndex]).correctionStartingWithMe--;			
//					}
//					
//					// position the figure to the correct index 
//					AbsolutePositionEditPartUtils.setChildFigureIndex(IVisualElementContainer(getFigure()), IVisualElement(currentFigure), entry.visualIndex + currentCorrection);
//					figToReuseEntry = null;
//				}
				
				// TODO la ce servesc astea?
				// CC: am observat ca erau folosite cand se facea zoom (figurile cu label prea mare erau trunchiate)
				notifyVisualChildren(UIComponent(childRenderer));
			}
			
//			childrenChangedFlag = false;
			
			// process the removals: remove from the container if preferredMaxNumberOfRenderers is reached
			for (var key:Object in renderersToReuse) {
				renderersToRemove = renderersToReuse[key];

				if (visibleModelsCounter + renderersToRemove.length <= preferredMaxNumberOfRenderers) {
					// leave on screen; no need to iterate the list
					visibleModelsCounter += renderersToRemove.length;
				} else {
					// some (or all) renderers will be removed
					for (i = 0; i < renderersToRemove.length; i++) {
						visibleModelsCounter++;
						if (visibleModelsCounter > preferredMaxNumberOfRenderers) {
							childRenderer = renderersToRemove[i];
							childModel = IDataRenderer(childRenderer).data;
							diagramShell.unassociateModelFromRenderer(childModel, childRenderer, false);
							
							parentRenderer.removeElement(childRenderer);
							logReusableRenderersRemoved++;
						}
					}
				}
				
			}
			
			IVisualChildrenRefreshable(parentRenderer).shouldRefreshVisualChildren = false;
			IAbsoluteLayoutRenderer(parentRenderer).noNeedToRefreshRect = new Rectangle(horizontalNoNeedToRefreshLeft, verticalNoNeedToRefreshTop, horizontalNoNeedToRefreshRight - horizontalNoNeedToRefreshLeft, verticalNoNeedToRefreshBottom - verticalNoNeedToRefreshTop);
			
			trace("AbsLayout.refrVC(): " + (logTsModelIterationDone - logTsStart) + "ms/" + (new Date().time - logTsModelIterationDone) + "ms visibleReusableRenderers=" + visibleModelsCounter + 
				",newModels=" + logNewModels + ",renderersReused=" + logRenderersReused + ",reusableRenderersCreated=" + logReusableRenderersCreated + 
				",nonReusableRenderersCreated=" + logNonReusableRenderersCreated + ",reusableRenderersRemoved=" + logReusableRenderersRemoved);
		}
		
		/**
		 * Recursive method used to notify all the graphical children.		
		 */
		private function notifyVisualChildren(child:UIComponent):void {
			child.dispatchEvent(new ZoomPerformedEvent());
			for (var i:int = 0; i < child.numChildren; i++) {
				if (child.getChildAt(i) is UIComponent) {
					notifyVisualChildren(UIComponent(child.getChildAt(i)));		
				}
			}
		}
	
	}
	
}