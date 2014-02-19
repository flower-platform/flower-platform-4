package org.flowerplatform.util.controller;

import java.util.List;

/**
 * {@link TypeDescriptor} relies on implementations of this interface (registered cf. {@link TypeDescriptorRegistry#getDynamicCategoryProviders()}, 
 * {@link TypeDescriptorRegistry#addDynamicCategoryProvider(IDynamicCategoryProvider)}) to get a list of dynamic categories for an {@link Object object} type. 
 * 
 * <p>
 * A static category is a category which is specified per node/object type (cf. {@link TypeDescriptor#getCategories()}). <br>
 * A dynamic category is a category which is generated programmatically (by implementations of this interface) for an object <b>type</b> (not for an object, cf. remark below).
 * 
 * <p>
 * Examples:
 * <ul>
 * 	<li>{@link AllDynamicCategoryProvider} provides "category.all" for all node types.</li>
 *  <li>a provider that returns, depending on some object's constant/immutable attribute/property, the corresponding category 
 *  	 (object is File -> "category.file"; object is MindMapNode -> "category.mm")</li>
 * </ul>
 * 
 * <b>REMARKS:</b>
 * <ul>
 * 	<li>This is not a {@link AbstractController controller}.</li>
 * 	<li>The "dynamicity" is per node type, not per node. E.g. The first time when a controller is retrieved for "node1" of type "myNodeType",
 *  {@link IDynamicCategoryProvider}s are interrogated. The second time when a controller is retrieved for another node ("node2") of same type
 *  ("myNodeType"), {@link IDynamicCategoryProvider}s are no longer interrogated, because the first value(s) that were returned are already cached.
 *  We have this behavior considering performance. In the future, if "dynamicity" at node level is needed, we may find a solution.
 * 
 * @see TypeDescriptor
 * @see TypeDescriptorRegistry#getDynamicCategoryProviders()
 * 
 * @author Cristina Constantinescu
 * @author Cristian Spiescu
 */
public interface IDynamicCategoryProvider {

	List<String> getDynamicCategories(Object object);
	
}
