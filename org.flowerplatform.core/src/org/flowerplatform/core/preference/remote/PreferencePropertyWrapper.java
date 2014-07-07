package org.flowerplatform.core.preference.remote;

import org.flowerplatform.core.node.remote.PropertyWrapper;
import org.flowerplatform.core.preference.PreferencePropertiesProvider;

/**
 * @see PreferencePropertiesProvider
 * @author Cristina Constantinescu
 */
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
	
	public PreferencePropertyWrapper() {
		super();
	}	
	
}
