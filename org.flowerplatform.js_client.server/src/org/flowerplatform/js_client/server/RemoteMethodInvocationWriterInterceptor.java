package org.flowerplatform.js_client.server;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

/**
 * Overrides the {@link MediaType} for a response. Since the {@link RemoteMethodInvocationFilter filter}
 * wraps the returned entity with a map, the response will always be serialized as JSON, regardless of
 * the original return type.
 * 
 * @author Mariana Gheorghe
 */
public class RemoteMethodInvocationWriterInterceptor implements WriterInterceptor {

	@Override
	public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
		context.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		context.proceed();
	}

}
