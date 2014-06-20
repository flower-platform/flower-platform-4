package org.flowerplatform.core.node.remote;

public class PreferencePropertyWrapper extends PropertyWrapper {

	private boolean isUsed;

	public boolean getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
	
	public PreferencePropertyWrapper setIsUsedAs(boolean isUsed) {
		setIsUsed(isUsed);
		return this;
	}
	
}
