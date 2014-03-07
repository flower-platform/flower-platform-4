package org.flowerplatform.flex_client.core.mindmap.controller
{
	import mx.collections.ArrayList;
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexutil.controller.IDynamicCategoryProvider;
	import org.flowerplatform.flexutil.controller.TypeDescriptor;
	
	/**
	 * @see java doc
	 * @author Cristina Constantinescu
	 */ 
	public class ResourceTypeDynamicCategoryProvider implements IDynamicCategoryProvider {
		
		public static const CATEGORY_RESOURCE_PREFIX:String = TypeDescriptor.CATEGORY_PREFIX + "resource.";
		private static const RESOURCE_PATTERN:RegExp = new RegExp("(\\w+)://?");
						
		public function getDynamicCategories(object:Object):IList {
			if (object is Node) {
				var node:Node = Node(object);
				if (node.resource != null) {								
					var groups:Array = RESOURCE_PATTERN.exec(node.resource);
					if (groups != null) {
						return new ArrayList([CATEGORY_RESOURCE_PREFIX + groups[1]]);
					}
				}
			}
			return null;
		}
		
	}
}