package org.flowerplatform.core;

import java.util.HashMap;

/**
 * @author Cristina Constantinescu
 */
public class ServiceContext extends HashMap<String, Object> {

	/**
	 * An additive controller may set this option, to stop the service from invoking any controllers
	 * with a higher order index.
	 */
	public static final String DONT_PROCESS_OTHER_CONTROLLERS = "dontProcessOtherControllers";
	
	/**
	 * All controllers except the updater controllers should check this option and not execute their logic
	 * if this option is set to true (e.g. persistence controllers should not set the <code>IS_DIRTY</code>
	 * property, but the updater controller must register the update).
	 */
	public static final String EXECUTE_ONLY_FOR_UPDATER = "executeOnlyForUpdater";
		
	// TODO CC: move to CoreConstants
	public static final String POPULATE_WITH_PROPERTIES = "populateWithProperties";
	
	private static final long serialVersionUID = 1L;

	public boolean getValue(String key) {
		return containsKey(key) && (boolean) get(key);
	}

	public ServiceContext add(String key, Object value) {
		put(key, value);
		return this;
	}	
	
}
