package org.flowerplatform.flex_client.server.blazeds;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.servlet.ServletContext;

import flex.messaging.HttpFlexSession;
import flex.messaging.MessageBrokerServlet;

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
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = method.invoke(this.proxy, args);
		switch (method.getName()) {
		case "setAttribute":
		case "removeAttribute":
			// also invoke this method on the internal context
			method.invoke(context, args);
			break;
		}
		return result;
	}

}
