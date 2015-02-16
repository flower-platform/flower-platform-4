package
{
	public class AbstractEntity {

		public static var instanceIdCounter:int = 0;
		
		public var id:int;
	
		public var instanceId:int;
		
		public function setIdAs(id:int):AbstractEntity {
			this.id = id;
			this.instanceId = ++instanceIdCounter;
			return this;
		}
		
	}
}