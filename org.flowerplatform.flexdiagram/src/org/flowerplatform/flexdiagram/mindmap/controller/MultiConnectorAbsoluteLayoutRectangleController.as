package org.flowerplatform.flexdiagram.mindmap.controller {
	import flash.geom.Rectangle;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.AbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.mindmap.MultiConnectorModel;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class MultiConnectorAbsoluteLayoutRectangleController extends AbsoluteLayoutRectangleController {
		public function MultiConnectorAbsoluteLayoutRectangleController(orderIndex:int=0) {
			super(orderIndex);
		}
		
		override public function getBounds(context:DiagramShellContext, model:Object):Rectangle {
			var multiConnectorModel:MultiConnectorModel = MultiConnectorModel(model);
			return new Rectangle(multiConnectorModel.x, multiConnectorModel.y, multiConnectorModel.width, multiConnectorModel.height);
		}
		
	}
}