package org.flowerplatform.js_client.server;
 
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.flowerplatform.core.users.UserPrincipal;
 
public class UserValidatorServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
    @Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String loginMethod = request.getParameter("loginMethod");
    	UserPrincipal user = null;    	
    	
    	switch(loginMethod){
    	// TODO VB: Login with Facebook
    	case "facebook": {
    		String email = request.getParameter("email");       	
            user = new UserPrincipal("facebookUser", email);
    		break;
    	}
    	// TODO VB: Usual login (username & password)
    	default:
    		// TODO VB: Get the user's info (username & password)
	    	String name = request.getParameter("name");
	        String password = request.getParameter("password");       
	        
	        // TODO VB: Validate the user who tries to login in.	       
	        user = validateLogin(name, password);       
    		break;
    	}
    	
    	// TODO VB: If valid, redirect the client to the desired page,
    	// TODO VB: else redirect to error page.
    	if (user == null) {
			response.sendRedirect("/org.flowerplatform.host.web_app/authenticate/loginError.html");
		} else {
		    HttpSession session = request.getSession();
		    session.setAttribute("userPrincipal", user);
		    String requestURL = (String)session.getAttribute("requestURL");
		    // TODO VB: If the user has accessed directly the login page
		    // TODO VB: the redirect him to a main page.
		    if (requestURL == null) {
		    	response.sendRedirect("/org.flowerplatform.host.web_app/authenticate/loginSuccess.html");
		    } else {
		        session.removeAttribute("requestURL");
		        response.sendRedirect(requestURL);
		    }
		}
    	
	}
 
    private UserPrincipal validateLogin(String name, String password) {
    	
    	// TODO VB: See if the principal is a valid one.
        if (name.equals("user") && password.equals("password")) {
        	return new UserPrincipal(name);
        }
        
        return null;
    }
}