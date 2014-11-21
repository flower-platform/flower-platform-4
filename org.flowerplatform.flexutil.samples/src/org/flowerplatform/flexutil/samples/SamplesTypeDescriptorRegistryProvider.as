package org.flowerplatform.flexutil.samples {
	import org.flowerplatform.flexutil.controller.ITypeDescriptorRegistryProvider;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	
	public class SamplesTypeDescriptorRegistryProvider implements ITypeDescriptorRegistryProvider {
		
		private var registry:TypeDescriptorRegistry;
		
		public function SamplesTypeDescriptorRegistryProvider(registry:TypeDescriptorRegistry) {
			this.registry = registry;
		}
		
		public function getTypeDescriptorRegistry(model:Object):TypeDescriptorRegistry {
			return registry;
		}
	}
}