package org.flowerplatform.js_client.server.jackson;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * When an exception is thrown, return this response (contains also the exception message).
 * 
 * @see ObjectMapperProvider
 * 
 * @author Cristina Constantinescu
 */
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception ex) {		
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
	}

}
