package org.flowerplatform.flexutil.controller {
	import mx.collections.ArrayCollection;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ComposedTypeDescriptorRegistryProvider implements ITypeDescriptorRegistryProvider {
		
		private var providers:ArrayCollection = new ArrayCollection();
		
		private var masterRegistry:TypeDescriptorRegistry;
		
		public function ComposedTypeDescriptorRegistryProvider(masterRegistry:TypeDescriptorRegistry) {
			this.masterRegistry = masterRegistry;
		}
		
		public function addProvider(provider:ITypeDescriptorRegistryProvider):void {
			providers.addItem(provider);
		}
		
		public function getTypeDescriptorRegistry(model:Object):TypeDescriptorRegistry {
			for each (var provider:ITypeDescriptorRegistryProvider in providers) {
				var registry:TypeDescriptorRegistry = provider.getTypeDescriptorRegistry(model);
				if (registry != null) {
					registry.masterRegistry = masterRegistry;
					return registry;
				}
			}
			return masterRegistry;
		}
	}
}