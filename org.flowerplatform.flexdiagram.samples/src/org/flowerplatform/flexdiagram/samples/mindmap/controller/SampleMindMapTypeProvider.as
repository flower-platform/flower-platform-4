package org.flowerplatform.flexdiagram.samples.mindmap.controller {
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.ITypeProvider;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.SampleMindMapModel;
	
	public class SampleMindMapTypeProvider implements ITypeProvider {
		
		public function getType(context:DiagramShellContext, model:Object):String {
			if (model is SampleMindMapModel) {
				return "mindmap";
			}
			return "diagram";
		}
		
	}
}