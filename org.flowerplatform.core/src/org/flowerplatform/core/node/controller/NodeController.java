package org.flowerplatform.core.node.controller;

/**
 * @author Cristian Spiescu
 */
public class NodeController implements Comparable<NodeController> {
	private int priority;

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public int compareTo(NodeController o) {
		return Integer.compare(getPriority(), o.getPriority());
	}
	
}
