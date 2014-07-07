package org.flowerplatform.flex_client.core.node_tree {

	import mx.core.UIComponent;
	
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.layout.AbstractViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @author Claudiu Matei
	 */
	public class GenericNodeTreeViewProvider extends AbstractViewProvider {
		
		public static const ID:String = "nodeTree";
		
		override public function getId():String {
			return ID;
		}
		
		override public function createView(viewLayoutData:ViewLayoutData):UIComponent {			
			return new NodeTree();
		}
		
		override public function getTitle(viewLayoutData:ViewLayoutData=null):String	{
			return Resources.getMessage("nodeTree.view");
		}
		
		override public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			return Resources.propertiesIcon;
		}
	
	}
}
