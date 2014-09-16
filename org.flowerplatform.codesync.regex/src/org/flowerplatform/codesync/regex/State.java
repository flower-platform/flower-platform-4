package org.flowerplatform.codesync.regex;

/**
 * @author Elena Posea
 *
 */
public class State {
	
	public int level;
	
	public Object node;
/**
 * @param level nesting level
 * @param node currentNode for stack (it is needed to know where to attach info as children)
 */
	public State(int level, Object node) {
		this.level = level;
		this.node = node;
	}
}
