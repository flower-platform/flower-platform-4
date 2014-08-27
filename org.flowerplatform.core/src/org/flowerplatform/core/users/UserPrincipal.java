package org.flowerplatform.core.users;

import java.security.Principal;

public class UserPrincipal implements Principal { 
    private String username;
    
    public UserPrincipal(String username) { 
        this.username = username; 
    }
    
    public String getName() { 
        return username; 
    } 
} 