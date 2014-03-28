package org.flowerplatform.core.session;

/**
 * @author Cristina Constantinescu
 */
public interface ISessionListener {

	void sessionCreated(String sessionId);
	
	void sessionRemoved(String sessionId);	
		
}
