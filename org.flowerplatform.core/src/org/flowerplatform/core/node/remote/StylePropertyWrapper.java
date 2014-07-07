package org.flowerplatform.core.node.remote;

/**
 * @author Cristina Constantinescu
 */
public class StylePropertyWrapper extends PropertyWrapper {

	private boolean isDefault;

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	public StylePropertyWrapper setIsDefaultAs(boolean isDefault) {
		setIsDefault(isDefault);
		return this;
	}
	
}
