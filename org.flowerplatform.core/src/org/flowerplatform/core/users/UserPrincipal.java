package org.flowerplatform.core.users;

import java.security.Principal;
import java.util.Map;

/**
 * @author Valentina-Camelia Bojan
 * @author Mariana Gheorghe
 */
public class UserPrincipal implements Principal { 
    private String username;
    
    private Map<String, Object> info;
    
    public UserPrincipal() {
    	// nothing to do
    }
    
    public UserPrincipal(String username, Map<String, Object> info) {
        this.username = username;
        this.info = info;
    }
    
    public String getName() { 
        return username; 
    } 
    
    public Map<String, Object> getInfo() { 
        return info; 
    } 
} 