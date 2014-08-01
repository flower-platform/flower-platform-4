package org.flowerplatform.flex_client.mindmap.action {
	import org.flowerplatform.flex_client.core.editor.action.DiagramShellAwareActionBase;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	
	/**
	 * @author Diana Balutoiu
	 */
	public class ExpandCollapseAction extends DiagramShellAwareActionBase {
		
		public static const ID:String = "ExpandCollapseAction";
		
		public function ExpandCollapseAction() {
			super();
		}
		
		override public function run():void {
			try{
				var node:Node = Node(selection.getItemAt(0));
				context.diagramShell.getModelController(context, node).
				setExpanded(context, node, !context.diagramShell.getModelController(context, node).getExpanded(context, node));
			} catch (castError:Error){ 
				//If click outside a node -> do nothing
				return;
			}
		}
	}
}