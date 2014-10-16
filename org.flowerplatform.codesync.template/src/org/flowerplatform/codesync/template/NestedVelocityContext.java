package org.flowerplatform.codesync.template;

import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.VelocityContext;

/**
 * @author Mariana Gheorghe
 */
public class NestedVelocityContext extends VelocityContext {

	private NestedVelocityContext parent;
	
	private List<NestedVelocityContext> children = new ArrayList<NestedVelocityContext>();

	public NestedVelocityContext getParent() {
		return parent;
	}

	/**
	 * @param parent
	 */
	public void setParent(NestedVelocityContext parent) {
		this.parent = parent;
		parent.getChildren().add(this);
	}

	public List<NestedVelocityContext> getChildren() {
		return children;
	}

	public void setChildren(List<NestedVelocityContext> children) {
		this.children = children;
	}

}
