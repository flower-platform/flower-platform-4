package org.flowerplatform.flexdiagram {
	import org.flowerplatform.flexdiagram.controller.AbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.controller.model_children.ModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.ModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.RendererController;
	import org.flowerplatform.flexdiagram.controller.selection.SelectionController;
	import org.flowerplatform.flexdiagram.controller.visual_children.VisualChildrenController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelController;
	import org.flowerplatform.flexdiagram.tool.controller.DragToCreateRelationController;
	import org.flowerplatform.flexdiagram.tool.controller.InplaceEditorController;
	import org.flowerplatform.flexdiagram.tool.controller.ResizeController;
	import org.flowerplatform.flexdiagram.tool.controller.SelectOrDragToCreateElementController;
	import org.flowerplatform.flexdiagram.tool.controller.drag.DragController;
	import org.flowerplatform.flexutil.controller.TypeDescriptor;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class ControllerUtils {
		
		public static function getVisualChildrenController(context:DiagramShellContext, model:Object):VisualChildrenController {
			var descriptor:TypeDescriptor = getTypeDescriptor(context, model);
			if (descriptor == null) {
				return null;
			}			
			return VisualChildrenController(descriptor.getSingleController(FlexDiagramConstants.VISUAL_CHILDREN_CONTROLLER, model));			
		}
		
		public static function getModelExtraInfoController(context:DiagramShellContext, model:Object):ModelExtraInfoController {
			var descriptor:TypeDescriptor = getTypeDescriptor(context, model);
			if (descriptor == null) {
				return null;
			}			
			return ModelExtraInfoController(descriptor.getSingleController(FlexDiagramConstants.MODEL_EXTRA_INFO_CONTROLLER, model));			
		}
		
		public static function getModelChildrenController(context:DiagramShellContext, model:Object):ModelChildrenController {
			var descriptor:TypeDescriptor = getTypeDescriptor(context, model);
			if (descriptor == null) {
				return null;
			}			
			return ModelChildrenController(descriptor.getSingleController(FlexDiagramConstants.MODEL_CHILDREN_CONTROLLER, model));			
		}
		
		public static function getAbsoluteLayoutRectangleController(context:DiagramShellContext, model:Object):AbsoluteLayoutRectangleController {
			var descriptor:TypeDescriptor = getTypeDescriptor(context, model);
			if (descriptor == null) {
				return null;
			}			
			return AbsoluteLayoutRectangleController(descriptor.getSingleController(FlexDiagramConstants.ABSOLUTE_LAYOUT_RECTANGLE_CONTROLLER, model));				
		}
		
		public static function getRendererController(context:DiagramShellContext, model:Object):RendererController {
			var descriptor:TypeDescriptor = getTypeDescriptor(context, model);
			if (descriptor == null) {
				return null;
			}			
			return RendererController(descriptor.getSingleController(FlexDiagramConstants.RENDERER_CONTROLLER, model));	
		}
		
		public static function getSelectionController(context:DiagramShellContext, model:Object):SelectionController {
			var descriptor:TypeDescriptor = getTypeDescriptor(context, model);
			if (descriptor == null) {
				return null;
			}			
			return SelectionController(descriptor.getSingleController(FlexDiagramConstants.SELECTION_CONTROLLER, model));	
		}
		
		public static function getInplaceEditorController(context:DiagramShellContext, model:Object):InplaceEditorController {
			var descriptor:TypeDescriptor = getTypeDescriptor(context, model);
			if (descriptor == null) {
				return null;
			}			
			return InplaceEditorController(descriptor.getSingleController(FlexDiagramConstants.INPLACE_EDITOR_CONTROLLER, model));	
		}
		
		public static function getResizeController(context:DiagramShellContext, model:Object):ResizeController {
			var descriptor:TypeDescriptor = getTypeDescriptor(context, model);
			if (descriptor == null) {
				return null;
			}			
			return ResizeController(descriptor.getSingleController(FlexDiagramConstants.RESIZE_CONTROLLER, model));	
		}
		
		public static function getDragToCreateRelationController(context:DiagramShellContext, model:Object):DragToCreateRelationController {
			var descriptor:TypeDescriptor = getTypeDescriptor(context, model);
			if (descriptor == null) {
				return null;
			}			
			return DragToCreateRelationController(descriptor.getSingleController(FlexDiagramConstants.DRAG_TO_CREATE_RELATION_CONTROLLER, model));	
		}
		
		public static function getDragController(context:DiagramShellContext, model:Object):DragController {
			var descriptor:TypeDescriptor = getTypeDescriptor(context, model);
			if (descriptor == null) {
				return null;
			}			
			return DragController(descriptor.getSingleController(FlexDiagramConstants.DRAG_CONTROLLER, model));	
		}
		
		public static function getSelectOrDragToCreateElementController(context:DiagramShellContext, model:Object):SelectOrDragToCreateElementController {
			var descriptor:TypeDescriptor = getTypeDescriptor(context, model);
			if (descriptor == null) {
				return null;
			}			
			return SelectOrDragToCreateElementController(descriptor.getSingleController(FlexDiagramConstants.SELECT_OR_DRAG_TO_CREATE_ELEMENT_CONTROLLER, model));	
		}
		
		public static function getMindMapModelController(context:DiagramShellContext, model:Object):MindMapModelController {
			var descriptor:TypeDescriptor = getTypeDescriptor(context, model);
			if (descriptor == null) {
				return null;
			}			
			return MindMapModelController(descriptor.getSingleController(FlexDiagramConstants.MINDMAP_MODEL_CONTROLLER, model));	
		}
		
		private static function getTypeDescriptor(context:DiagramShellContext, model:Object):TypeDescriptor {
			return context.diagramShell.registry.getExpectedTypeDescriptor(context.diagramShell.getType(context, model));
		}
		
	}
}