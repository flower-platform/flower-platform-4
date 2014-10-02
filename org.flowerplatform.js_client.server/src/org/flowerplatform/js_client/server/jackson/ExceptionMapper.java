package org.flowerplatform.js_client.server.jackson;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * When an exception is thrown, return this response (contains also the exception message).
 * 
 * @see ObjectMapperProvider
 * 
 * @author Cristina Constantinescu
 */
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionMapper.class);
	
	@Override
	public Response toResponse(Exception ex) {
		LOGGER.error("Internal server error", ex);
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
	}

}
