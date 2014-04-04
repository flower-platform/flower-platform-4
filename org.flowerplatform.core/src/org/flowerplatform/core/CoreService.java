package org.flowerplatform.core;

/**
 * @author Cristina Constantinescu
 */
public class CoreService {

	public void helloServer(String clientAppVersion) throws Exception {
		if (!CoreConstants.VERSION.equals(clientAppVersion)) {
			throw new Exception(CoreConstants.VERSION);
		}
	}
	
}
