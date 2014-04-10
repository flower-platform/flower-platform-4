package org.flowerplatform.js_client.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flowerplatform.core.CorePlugin;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * @author Mariana Gheorghe
 */
public class FlowerServletContainer extends ServletContainer {

	private static final long serialVersionUID = 1555129714260240256L;

	@Override
	public void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			CorePlugin.getInstance().getRequestThreadLocal().set(request);
			super.service(request, response);
		} finally {
			CorePlugin.getInstance().getRequestThreadLocal().remove();
		}
	}
	
}
