package org.flowerplatform.util.controller;

/**
 * @author Mariana Gheorghe
 */
public interface ITypeDescriptorRegistryProvider {

	/**
	 * Return the {@link TypeDescriptorRegistry} for the <code>model</code>.
	 */
	TypeDescriptorRegistry getTypeDescriptorRegistry(Object model);

}
