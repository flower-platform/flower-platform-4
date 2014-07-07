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
package org.flowerplatform.flex_client.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mariana Gheorghe
 */
public class IndexFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// nothing to do
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) req;
		
		// path is /
		String path = httpServletRequest.getPathInfo();
		if ("/".equals(path)) {
			String requestURL = httpServletRequest.getRequestURL().toString();
			if (requestURL.endsWith("/")) {
				// request is host:port/context/
				// forward to main.jsp
				req.getRequestDispatcher("/main.jsp").forward(req, res);
				return;
			} else {
				String contextPath = req.getServletContext().getContextPath();
				if (contextPath.length() > 0 && requestURL.endsWith(contextPath)) {
					// request is host:port/context
					// redirect to host:port/context/
					((HttpServletResponse) res).sendRedirect(contextPath + "/");
					return;
				}
			}
		}
		
		chain.doFilter(req, res);
	}

	@Override
	public void destroy() {
		// nothing to do
	}

}