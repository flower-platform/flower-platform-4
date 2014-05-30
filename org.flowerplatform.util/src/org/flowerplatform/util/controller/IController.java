package org.flowerplatform.util.controller;

public interface IController extends Comparable<IController> {

	/**
	 * For additive controllers, specifies the order. Lower values are invoked first.
	 * 
	 * <p>
	 * It's recommended to use big values e.g. 50 000, -100 000. So that new indexes can be
	 * added in the future.
	 */
	int getOrderIndex();

	/**
	 * @see #getOrderIndex()
	 */
	void setOrderIndex(int orderIndex);

}