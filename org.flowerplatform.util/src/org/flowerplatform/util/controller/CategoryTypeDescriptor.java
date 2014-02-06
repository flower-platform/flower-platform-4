package org.flowerplatform.util.controller;

/**
 * Same as it's superclass, but disables {@link #addCategory(String)}, so that categories 
 * cannot belong to categories.
 * 
 * <p>
 * We do this, because it would increase the complexity of the code of these classes (e.g. check for
 * infinite cycles), and it would make room for structures that are not very easy to work with.
 * 
 * @author Cristian Spiescu
 */
public class CategoryTypeDescriptor extends TypeDescriptor {

	public CategoryTypeDescriptor(TypeDescriptorRegistry registry, String type) {
		super(registry, type);
	}

	@Override
	public TypeDescriptor addCategory(String category) {
		throw new UnsupportedOperationException("Categories cannot belong to other categories");
	}

}
