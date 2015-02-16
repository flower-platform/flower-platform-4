package
{
	import mx.collections.ArrayList;
	import mx.collections.IList;

	public class Mission extends AbstractEntity {

		public var isRootEntity:Boolean = true;
		
		public var objectActionGroups:IList = new ArrayList();

		public var resources:IList = new ArrayList();

	}

}