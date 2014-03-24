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
