package org.flowerplatform.js_client.server.remote;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.flowerplatform.core.CorePlugin;

/**
 * @author Cristina Constantinescu
 */
@Path("/javaClientMethodInvocationService")
public class JavaClientMethodInvocationService {

	private static final String SERVICE_DOT_METHOD_NAME = "serviceDotMethodName";
	private static final String PARAMETERS = "parameters";
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Object invoke(HashMap<?, ?> requestParams) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String serviceAndMethod = (String) requestParams.get(SERVICE_DOT_METHOD_NAME);
		Object[] parameters = (Object[]) requestParams.get(PARAMETERS);
		
		String[] tokens = serviceAndMethod.split("\\.");
		String serviceName = tokens[0];
		String methodName = tokens[1];
		
		Object service = CorePlugin.getInstance().getServiceRegistry().getService(serviceName);		
		if (service == null) {
			throw new RuntimeException(String.format("The service with id='%s' was not found in the service registry.", serviceName));
		}
		// find the method
		Method foundMethod = null;
		for (Method method : service.getClass().getMethods()) {
			if (method.getName().equals(methodName)) {
				foundMethod = method;
				break;
			}
		}
		if (foundMethod == null) {
			throw new RuntimeException(String.format("The service with id='%s' doesn't contain the method '%s'", serviceName, methodName));
		}
		// invoke the method
		return foundMethod.invoke(service, parameters);
	}
	
}
