package org.flowerplatform.util.type_descriptor;

/**
 * @author Cristian Spiescu
 */
public class OrderedElement implements Comparable<OrderedElement> {
	
	private int orderIndex;

	public int getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}

	@Override
	public int compareTo(OrderedElement o) {
		return Integer.compare(getOrderIndex(), o.getOrderIndex());
	}
	
}
