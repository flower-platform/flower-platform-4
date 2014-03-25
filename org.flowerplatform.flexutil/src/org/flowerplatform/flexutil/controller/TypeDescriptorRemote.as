package org.flowerplatform.flexutil.controller {
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	
	/**
	 * @author Mariana Gheorghe
	 */
	[RemoteClass(alias="org.flowerplatform.util.controller.TypeDescriptorRemote")]
	public class TypeDescriptorRemote {
		
		public var type:String;
		
		public var categories:ArrayCollection;
		
		public var singleControllers:Object;	// Map<String, IDescriptor>
		
		public var additiveControllers:Object;	// Map<String, List<IDescriptor>>
		
	}
}