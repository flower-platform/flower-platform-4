package org.flowerplatform.js_client.server.remote;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.flowerplatform.core.CorePlugin;

/**
 * All java client methods invoked from js will be handled by this service.
 * 
 * <p>
 * <code>requestParams</code> contains:
 * <ul>
 * 	<li> {@value #SERVICE_DOT_METHOD_NAME} -> serviceId.methodName
 * 	<li> {@value #PARAMETERS} -> Object[]
 * </ul>
 * 
 * We use this solution because:
 * <ul>
 * 	<li> Jersey server doesn't support multiple normal parameters for get/post/put...
 * 	<li> with {@link QueryParam}s we must put annotations to all additional service method's parameters
 * 	<li> it is possible to have different resource paths for service or method (not the exact id or method name). 
 * </ul>
 * 
 * @author Cristina Constantinescu
 */
@Path("/javaClientMethodInvocationService")
public class JavaClientMethodInvocationService {

	private static final String SERVICE_DOT_METHOD_NAME = "serviceDotMethodName";
	private static final String PARAMETERS = "parameters";
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Object invoke(HashMap<String, Object> requestParams) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {		
		String serviceAndMethod = (String) requestParams.get(SERVICE_DOT_METHOD_NAME);
		ArrayList<?> parameters = (ArrayList<?>) requestParams.get(PARAMETERS);
		
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
		return foundMethod.invoke(service, parameters != null ? parameters.toArray() : null);
	}
	
}
