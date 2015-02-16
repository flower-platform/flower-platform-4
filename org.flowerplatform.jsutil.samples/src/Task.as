package
{
	import mx.collections.ArrayList;
	import mx.collections.IList;

	public class Task extends AbstractEntity {
		
		public var isRootEntity:Boolean = true;
		
		public var objectActionGroups:IList = new ArrayList();
		
	}
}