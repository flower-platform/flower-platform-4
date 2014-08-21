package org.flowerplatform.flex_client.mindmap.action {
	import org.flowerplatform.flex_client.core.CoreConstants;
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
		
		override public function get visible():Boolean{
			if(!(selection.getItemAt(0) is Node)){
				return false;
			}
			return Node(selection.getItemAt(0)).getPropertyValue(CoreConstants.HAS_CHILDREN);
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			context.diagramShell.getModelController(context, node).setExpanded(
				context, 
				node, 
				!context.diagramShell.getModelController(context, node).getExpanded(context, node));
		}	
		
	}
}