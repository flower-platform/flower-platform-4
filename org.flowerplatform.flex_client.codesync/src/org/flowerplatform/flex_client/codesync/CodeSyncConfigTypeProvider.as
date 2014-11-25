package org.flowerplatform.flex_client.codesync {
	
	import org.flowerplatform.flex_client.core.editor.NodeTypeProvider;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class CodeSyncConfigTypeProvider extends NodeTypeProvider {
		
		override public function getType(model:Object):String {
			if (model is Node) {
				var type:String = Node(model).properties.template; // TODO rename this prop?
				if (type != null) {
					return type;
				}
			}			
			return super.getType(model);
		}
	}
}