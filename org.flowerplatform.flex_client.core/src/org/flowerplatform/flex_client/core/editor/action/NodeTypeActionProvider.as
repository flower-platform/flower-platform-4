/**
 * Its method <code>getActions(selection:Ilist)</code> returns all the actions registered 
 * for the selection type. 
 * 
 * @author Alina Bratu
 */
package org.flowerplatform.flex_client.core.editor.action
{
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRootModelWrapper;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IActionProvider;

	public class NodeTypeActionProvider implements IActionProvider {
		
		public function NodeTypeActionProvider():void {
		}
		
		/**
		 * If all nodes in <code>selection</code> have the same type, it returns a list of the actions 
		 * that can be done on that particular type of node. If not, returns null, so no actions are displayed.
		 * It uses the <code>actionRegistry</code> in <code>FlexUtilGlobals</code>.
		 */
		public function getActions(selection:IList):Vector.<IAction> {
			var nodeType:String;
			var actions:Vector.<IAction> = new Vector.<IAction>();
			
			// verify if selection is not null and not empty
			if (selection != null && selection.length > 0) {
				
				// if selection is a mind map node, get the node from the wrapper
				var obj:Object = selection.getItemAt(0);
				if (obj is MindMapRootModelWrapper) {
					obj = MindMapRootModelWrapper(obj).model;
				}
				nodeType =  Node(obj).type;
				
				for (var i:int = 1; i < selection.length; i++) {
					obj = selection.getItemAt(i);
					if (obj is MindMapRootModelWrapper) {
						obj = MindMapRootModelWrapper(obj).model;
					}
					var nextNode:Node = Node(obj);
					
					if (nodeType != nextNode.type) {
						return null;
					}
				}
				
				// get list of action descriptors for the type of node the selection has
				var descriptors:IList = CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(nodeType)
					.getAdditiveControllers(CoreConstants.ACTION_DESCRIPTOR,selection.getItemAt(0));
				
				// create list of action factories using action descriptors and action registry
				for each (var a:ActionDescriptor in descriptors) {
					actions.push(FlexUtilGlobals.getInstance().actionRegistry[a.actionId].newInstance());
				}
				return actions;
			}
			return null;
		}
	}
}