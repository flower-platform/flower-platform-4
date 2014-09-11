/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flex_client.server.blazeds;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.servlet.ServletContext;

/**
 * Maintains a reference to the actual {@link ServletContext} from the servlet container, wrapped in 
 * a ServletContextAdaptor by OSGi, in order to set and remove attributes from its attributes map.
 * 
 * <p>
 * Because the OSGi adaptor keeps a separate attributes map, the {@link HttpFlexSession#SESSION_MAP} 
 * attribute set when the {@link MessageBrokerServlet} is initialized will not be set in the actual 
 * attributes map from the servlet container. This means that the flex session map will not be available
 * from the servlet context attributes, for example, when a session expires. In this case, BlazeDS is
 * not able to unregister any flex clients associated with that session, which leads to duplicate session
 * errors that will prevent future requests from those clients.
 * 
 * @author Mariana Gheorghe
 */
public class ServletContextAdaptorInvocationHandler implements InvocationHandler {

	/**
	 * The proxy returned by the ServletContextAdaptor.
	 */
	private ServletContext proxy;
	
	/**
	 * The real {@link ServletContext} from the servlet container.
	 */
	private ServletContext context;
	
	/**
	 *@author see class
	 **/
	public ServletContextAdaptorInvocationHandler(ServletContext proxy) {
		super();
		this.proxy = proxy;
		
		InvocationHandler h = Proxy.getInvocationHandler(proxy);
		try {
			// the invocation handler references the adaptor via ServletContextAdaptor.this
			Field this0 = h.getClass().getDeclaredField("this$0");
			this0.setAccessible(true);
			Object adaptor = this0.get(h);
			
			// get the servlet context wrapped by the adaptor
			Field result = adaptor.getClass().getDeclaredField("servletContext");
			result.setAccessible(true);
			context = (ServletContext) result.get(adaptor);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Invoke the method on the proxy. Methods that modify the attributes map are also
	 * invoked on the context from the servlet container.
	 */
	@Override
	public Object invoke(Object proxyObject, Method method, Object[] args) throws Throwable {
		Object result = method.invoke(this.proxy, args);
		switch (method.getName()) {
		case "setAttribute":
		case "removeAttribute":
			// also invoke this method on the internal context
			method.invoke(context, args);
			break;
		default:
			break;
		}
		return result;
	}

}