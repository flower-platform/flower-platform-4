package org.flowerplatform.util.controller;

/**
 * Entry object for the {@link TypeDescriptor#singleControllers} and {@link TypeDescriptor#additiveControllers} maps.
 * Keeps the controller (or list of controllers) originally set for the type, the controller (or list of controllers)
 * processed from the categories of the type and a flag that specifies if the processed controllers have been cached.
 * 
 * @author Mariana Gheorghe
 */
public class ControllerEntry<T> {

	private T selfValue;
	private T cachedValue;
	private boolean wasCached;
	
	public T getSelfValue() {
		return selfValue;
	}
	
	public void setSelfValue(T selfValue) {
		this.selfValue = selfValue;
	}
	
	public T getCachedValue() {
		return cachedValue;
	}
	
	public void setCachedValue(T cachedValue) {
		this.cachedValue = cachedValue;
	}
	
	public boolean wasCached() {
		return wasCached;
	}
	
	public void setCached(boolean cached) {
		this.wasCached = cached;
	}
	
}
