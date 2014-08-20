package org.flowerplatform.flex_client.core.editor.action
{
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IActionProvider;

	public class NodeTypeActionProvider implements IActionProvider {
		
		var actionFactories:Vector.<IAction> ;
		
		public function NodeTypeActionProvider():void {
		}
		
		public function getActions(selection:IList):Vector.<IAction> {
			var nodeType:String;
			actionFactories = new Vector.<IAction>();
			
			// verify if nodes have same type; if not, we're not displaying actions
			if (selection != null && selection.length > 0 && selection.getItemAt(0) is Node) {
				for (var i:int = 0; i < selection.length - 1 ; i++) {
					if (selection.getItemAt(i).type != selection.getItemAt(i+1)) {
						return null;
					}
				}
				nodeType = selection.getItemAt(0).type;
				
				// get list of action descriptors for the type of node the selection has
				
				var descriptors:IList = CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(nodeType)
					.getAdditiveControllers(CoreConstants.ACTION_DESCRIPTOR,selection);
				
				// create list of action factories using action descriptors and action registry
				for each (var a:ActionDescriptor in descriptors) {
					actionFactories.push(FlexUtilGlobals.getInstance().actionRegistry[a.actionId.toString()].newInstance());
				}
				return actionFactories;
			}
			return null;
		}
	}
}