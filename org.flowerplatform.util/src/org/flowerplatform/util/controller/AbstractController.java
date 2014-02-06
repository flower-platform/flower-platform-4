package org.flowerplatform.util.controller;

/**
 * Base class for controllers. A controller, is a class that knows how to handle a specific operation
 * for a type (or several types) of objects (or nodes).
 * 
 * <p>
 * We can divide controllers into 2 categories:
 * <ul>
 * 	<li>an actual controller, that has one or several methods. It takes as parameter (at least) one
 * 		object (of type = the type for which the controller was registered in {@link TypeDescriptorRegistry}. E.g.
 * 		<code>getChildren(node)</code> provides logic to get children for a node of given type.
 * 	</li>
 * 	<li>a descriptor, that has no methods. It has some attributes that provide information that applies
 * 		to all nodes of a certain type (of type = the type for which the controller was registered in {@link TypeDescriptorRegistry}. E.g.
 * 		some <code>PropertyDescriptor</code> specify what are the valid properties for a certain node.
 * 	</li> 
 * </ul>
 * 
 * Controllers are registered in the {@link TypeDescriptorRegistry}, for a node type(s) and/or node category(es). 
 * 
 * <p>
 * Controllers can be:
 * <ul>
 * 	<li>single. I.e. only one controller is allowed for a type.</li>
 * 	<li>additive. I.e. several controllers can be used for a type. Each one will be invoked when needed.</li>
 * </ul> 
 * 
 * @see TypeDescriptorRegistry
 * @see TypeDescriptor
 * 
 * @author Cristian Spiescu
 */
public abstract class AbstractController implements Comparable<AbstractController> {
	
	/**
	 * @see #getOrderIndex()
	 */
	private int orderIndex;

	/**
	 * For additive controllers, specifies the order. Lower values are invoked first.
	 * 
	 * <p>
	 * It's recommended to use big values e.g. 50 000, -100 000. So that new indexes can be
	 * added in the future.
	 */
	public int getOrderIndex() {
		return orderIndex;
	}
	
	/**
	 * @see #getOrderIndex()
	 */
	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}

	/**
	 * Needed to know how to sort the list of controllers. For additive controllers.
	 */
	@Override
	public int compareTo(AbstractController o) {
		return Integer.compare(getOrderIndex(), o.getOrderIndex());
	}
	
}
