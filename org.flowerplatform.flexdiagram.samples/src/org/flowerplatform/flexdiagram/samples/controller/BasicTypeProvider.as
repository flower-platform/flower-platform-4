package org.flowerplatform.flexdiagram.samples.controller {
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.ITypeProvider;
	import org.flowerplatform.flexdiagram.samples.model.BasicConnection;
	import org.flowerplatform.flexdiagram.samples.model.BasicModel;
	import org.flowerplatform.flexdiagram.samples.model.BasicSubModel;
	
	public class BasicTypeProvider implements ITypeProvider {
				
		public function getType(context:DiagramShellContext, model:Object):String {
			if (model is BasicModel) {
				return "basicModel";
			} else if (model is BasicSubModel) {
				return "basicSubModel";
			} else if (model is BasicConnection) {
				return "basicConnection";
			}
			return "diagram";
		}
	}
}