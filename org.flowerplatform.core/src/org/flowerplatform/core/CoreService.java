package org.flowerplatform.core;

/**
 * @author Cristina Constantinescu
 */
public class CoreService {

	public String[] getVersions() {
		return new String[] {CoreConstants.APP_VERSION, CoreConstants.API_VERSION};
	}
	
}
