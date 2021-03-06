/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.js_client.server.jackson;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.flowerplatform.js_client.server.ObjectMapperProvider;

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
