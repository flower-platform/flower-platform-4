package org.flowerplatform.core;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;

/**
 * @author Cristina Constantinescu
 */
@Path("/coreService")
public class CoreService {

	public void helloServer(String clientAppVersion) throws Exception {
		if (!CoreConstants.VERSION.equals(clientAppVersion)) {
			throw new Exception(CoreConstants.VERSION);
		}
	}
	
	public String getSessionId() {
		return CorePlugin.getInstance().getRequestThreadLocal()
				.get().getSession().getId();
	}
	
	@PUT @Path("/login")
	public void login() {
		
	}
	
}
