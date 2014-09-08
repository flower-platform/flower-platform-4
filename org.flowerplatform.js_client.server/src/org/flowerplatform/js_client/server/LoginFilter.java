package org.flowerplatform.js_client.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.flowerplatform.core.users.UserPrincipal;

public class LoginFilter implements Filter {
    
    public void init(FilterConfig fConfig) {}
 
    public void destroy() {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	
    	// TODO VB: Filter that verifies if there is any user already authenticated in the
    	// TODO VB: application. If there is not, the filter redirects the client to a login form.
    	HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        
        UserPrincipal user = (UserPrincipal) session.getAttribute("userPrincipal");
        if (user == null){
        	session.setAttribute("requestURL", req.getRequestURI());
        	((HttpServletResponse)response).sendRedirect("/org.flowerplatform.host.web_app/authenticate/loginUser.html");        
            return;
        }
    	
        chain.doFilter(request, response);
    }
}