package org.flowerplatform.flexutil.controller {
	
	/**
	 * @author Diana Balutoiu
	 */
	public class ComposedTypeProvider implements ITypeProvider {
		
		public var typeProviders:Vector.<ITypeProvider> = new Vector.<ITypeProvider>();
		
		public function getType(model:Object):String {
			
			for each (var provider:ITypeProvider in typeProviders) {
				var result:String = getType(provider);
				if (result != null) {
					return result;
				}
			}
			return null;
		}
		
	}
}