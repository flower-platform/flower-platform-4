package org.flowerplatform.flexutil.controller {
	
	/**
	 * @author Mariana Gheorghe
	 */
	public interface ITypeDescriptorRegistryProvider {
		
		function getTypeDescriptorRegistry(model:Object):TypeDescriptorRegistry;
		
	}
}